package com.sft.adcollection.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADBean;
import com.sft.adcollection.bean.UserAreaLevelBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.common.Constant;
import com.sft.adcollection.util.LocationUtil;
import com.sft.adcollection.util.PermissionRequestUtil;
import com.sft.adcollection.viewutil.RadioGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.sft.baseactivity.util.JSONUtil;
import cn.sft.baseactivity.util.MyHandler;

/**
 *
 */
public class MapActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, LocationUtil.LocationListener, AMap.InfoWindowAdapter, AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {

    //
    private TextView titleTV;
    // 地图控件
    private MapView mapView;
    // 高德地图对象
    private AMap aMap;
    // 定位工具
    private LocationUtil locationUtil;
    // 定位Marker
    private Marker locationMarker;
    // 底部菜单group
    private RadioGroup radioGroup;
    // RadioButton
    private RadioButton adBtn, superviseBtn, handlerBtn;
    //
    private RelativeLayout superviseLayout, handleLayout;
    // 采集按钮
    private Button collectionBtn;
    // 选择大区
    private TextView regionSelectTV;

    private static final String AROUND_AD_TYPE = "aroundAD";
    private static final String SUPERVISE_AD_TYPE = "superviseAD";
    private static final String HANDLE_AD_TYPE = "handleAD";

    private static final int SELECT_AREA_CODE = 0;

    // 附近自动刷新时间
    private static final int AROUND_AUTO_REFRESH_TIME = 2000;
    // 监测自动刷新时间
    private static final int SUPERVISE_AUTO_REFRESH_TIME = 5000;

    // 自动刷新附近的广告牌
    private MyHandler refreshAroundADHandler;
    // 自动刷新检测的广告牌
    private MyHandler refreshSuperviseADHandler;

    // 需要单独保存的对象onSaveInstanceState-------------------------------

    // 用户的位置信息
    private UserLocationBean userLocationBean;
    // 用户当前所在页面Index(RadioButton的ID)
    private int pageIndex = 0;
    // 用户周围的广告信息
    private ArrayList<ADBean> aroundADList;
    // 用户周围的检测广告信息
    private ArrayList<ADBean> superviseADList;
    // 用户周围的处理广告信息
    private ArrayList<ADBean> handleADList;
    // 地图缩放级别
    private float mapScale = 0f;
    // 当前地图的中心点
    private LatLng centerLatLng = null;
    // 是否自动刷新附近的广告牌
    private boolean isAutoRefreshAroundAD = false;
    // 是否自动刷新附近的广告牌
    private boolean isAutoRefreshSuperviseAD = false;
    // 用户权限等级
    private UserAreaLevelBean userAreaLevelBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initData(savedInstanceState);
        initListener();
        initPermission();
    }

    private void initView() {
        titleTV = (TextView) findViewById(R.id.titlebar_title_tv);
        mapView = (MapView) findViewById(R.id.map_map);
        radioGroup = (RadioGroup) findViewById(R.id.map_radiogroup);
        collectionBtn = (Button) findViewById(R.id.map_collectionbtn);
        adBtn = (RadioButton) findViewById(R.id.map_adbtn);
        superviseBtn = (RadioButton) findViewById(R.id.map_surveybtn);
        handlerBtn = (RadioButton) findViewById(R.id.map_handlebtn);

        superviseLayout = (RelativeLayout) findViewById(R.id.map_surveylayout);
        handleLayout = (RelativeLayout) findViewById(R.id.map_handlelayout);

        findViewById(R.id.titlebar_left_imgbtn).setVisibility(View.GONE);
        regionSelectTV = (TextView) findViewById(R.id.titlebar_right_tv);
        regionSelectTV.setText(getString(R.string.region_select));
        regionSelectTV.setVisibility(View.GONE);
    }

    private void initData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.setInfoWindowAdapter(this);
        // 设置缩放级别
        mapScale = 14f;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(mapScale));
        locationUtil = new LocationUtil(this);

        aroundADList = new ArrayList<>();
        superviseADList = new ArrayList<>();
        handleADList = new ArrayList<>();

        setRadioButtonEnable(false);

        if (app.getUserBean().getFenLei().equals("0")) {
            handleLayout.setVisibility(View.GONE);
        } else if (app.getUserBean().getFenLei().equals("1")) {
            userAreaLevelBean = app.getUserGrade();
            collectionBtn.setVisibility(View.GONE);
            superviseLayout.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        regionSelectTV.setOnClickListener(this);
        locationUtil.addLocationListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        collectionBtn.setOnClickListener(this);
        aMap.setOnCameraChangeListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * 有权限直接定位，没有权限就申请
     */
    private void initPermission() {
        boolean hasPermission = PermissionRequestUtil.getInstance().requestPermission(this, PermissionRequestUtil.ACCESS_COARSE_LOCATION);
        if (hasPermission) {
            //开始定位
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestUtil.ACCESS_COARSE_LOCATION) {
            // 获取定位权限结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 得到权限，开始定位
                startLocation();
            } else {
                // 拒绝权限
                toast.setText(getString(R.string.cancel_locationpermission) + "\n" + getString(R.string.appexit));
                new MyHandler(1000) {
                    @Override
                    public void run() {
                        finish();
                    }
                };
            }
        }
    }

    private void startLocation() {
        locationUtil.startLocation();
    }

    private void setRadioButtonEnable(boolean isEnable) {
        adBtn.setEnabled(isEnable);
        superviseBtn.setEnabled(isEnable);
        handlerBtn.setEnabled(isEnable);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            // 定位失败
            util.print("定位失败");
            return;
        }
//        util.print("定位信息：" + aMapLocation);
//        locationUtil.stopLocation();
        // 记录用户最后定位的经纬度信息
        if (userLocationBean == null)
            userLocationBean = new UserLocationBean();
//        userLocationBean.setLat(aMapLocation.getLatitude());
//        userLocationBean.setLng(aMapLocation.getLongitude());
        userLocationBean.setLat(37.4603125);
        userLocationBean.setLng(121.374484);
        userLocationBean.setAddress(aMapLocation.getAddress());
        userLocationBean.setProvince(aMapLocation.getProvince());
        userLocationBean.setCity(aMapLocation.getCity());
        userLocationBean.setDistrict(aMapLocation.getDistrict());
        userLocationBean.setRegion(aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict());

        app.setUserLocationBean(userLocationBean);

        //首次定位成功后，默认加载附近的广告栏
        setRadioButtonEnable(true);
        if (pageIndex == 0)
            radioGroup.check(R.id.map_adbtn);

        if (locationMarker == null) {
            MarkerOptions locationMarkerOptions = new MarkerOptions();
            locationMarkerOptions.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            locationMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.mylocation)));
            locationMarkerOptions.anchor(0.5f, 0.734375f);
            locationMarker = this.aMap.addMarker(locationMarkerOptions);
            centerLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));
        } else {
            locationMarker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (userLocationBean == null) {
            return;
        }
        pageIndex = checkedId;
        clearMarkers(false);
        toast.cancel();
//        asyncHttpClient.cancelRequests(this, true);
        switch (checkedId) {
            case R.id.map_adbtn:
                if (aroundADList.size() > 0) {
                    autoRefreshAD(true, AROUND_AUTO_REFRESH_TIME);
                    addAroundADMarker();
                } else {
                    autoRefreshAD(true);
                }
                titleTV.setText(getString(R.string.ad));
                regionSelectTV.setVisibility(View.GONE);
                break;
            case R.id.map_surveybtn:
                if (superviseADList.size() > 0) {
                    autoRefreshAD(true, SUPERVISE_AUTO_REFRESH_TIME);
                    addSuperviseADMarker();
                } else {
                    autoRefreshAD(true);
                }
                titleTV.setText(getString(R.string.survey));
                regionSelectTV.setVisibility(View.GONE);
                break;
            case R.id.map_handlebtn:
                autoRefreshAD(false);
                titleTV.setText(getString(R.string.handle));
                if (userAreaLevelBean != null && userAreaLevelBean.getGrade().equals("4")) {
                    regionSelectTV.setVisibility(View.GONE);
                    String[] areas = userAreaLevelBean.getParentIds().split(",");
                    String provinceId = "";
                    String cityId = "";
                    for (int i = areas.length - 1; i >= 0; i--) {
                        if (!TextUtils.isEmpty(areas[i])) {
                            if (TextUtils.isEmpty(cityId)) {
                                cityId = areas[i];
                            } else {
                                provinceId = areas[i];
                                break;
                            }
                        }
                    }
                    obtainHandleAD(provinceId, cityId, app.getUserBean().getId());
                } else {
                    regionSelectTV.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void obtainAroundAD() {
        util.print("obtainAroundAD");
        RequestParams requestParams = new RequestParams();
        requestParams.put("jingDu", userLocationBean.getLng());
        requestParams.put("weiDu", userLocationBean.getLat());
        requestParams.put("jczt", "0");
        httpPost(AROUND_AD_TYPE, requestParams, Constant.HTTPUrl.MEDIA_AROUND_URL);
    }

    private void obtainSuperviseAD() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jingDu", userLocationBean.getLng());
        requestParams.put("weiDu", userLocationBean.getLat());
        requestParams.put("jczt", "0");
        httpPost(SUPERVISE_AD_TYPE, requestParams, Constant.HTTPUrl.MEDIA_SUPERVISE_URL);
    }

    private void obtainHandleAD(String provinceId, String cityId, String districId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("shengId", provinceId);
        requestParams.put("shiId", cityId);
        requestParams.put("quId", districId);
        httpPost(HANDLE_AD_TYPE, requestParams, Constant.HTTPUrl.MEDIA_HANDLE_URL);
    }

    private ADBean findADBeanByID(String id) {
        ADBean bean = new ADBean();
        bean.setId(id);
        List<ADBean> list = null;
        switch (pageIndex) {
            case R.id.map_adbtn:
                list = aroundADList;
                break;
            case R.id.map_surveybtn:
                list = superviseADList;
                break;
            case R.id.map_handlebtn:
                list = handleADList;
                break;
        }
        int index = list.indexOf(bean);
        return index == -1 ? null : list.get(index);
    }

    @Override
    public void onClick(View v) {
        if (!onClickSingleView()) {
            return;
        }
        Intent intent = null;
        switch (v.getId()) {
            case R.id.map_collectionbtn:
                if (userLocationBean == null) {
                    return;
                }
                intent = new Intent(this, CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.titlebar_right_tv:
                if (pageIndex == R.id.map_handlebtn) {
//                    new RegionSelectDialog(this, RegionSelectDialog.PROVINCE_STYLE, "烟台市").show();
//                    obtainUserLevel();
                    intent = new Intent(this, AreaSelectActivity.class);
                    startActivityForResult(intent, SELECT_AREA_CODE);
                }
                break;
        }
    }

    private void addAroundADMarker() {
        util.print("pageIndex="+pageIndex);
        addADMarker(aroundADList);
    }

    private void addSuperviseADMarker() {
        addADMarker(superviseADList);
    }

    private void addHandleADMarker() {
        addADMarker(handleADList);
    }

    private void addADMarker(List<ADBean> list) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            ADBean adBean = list.get(i);
            MarkerOptions locationMarkerOptions = new MarkerOptions();
            locationMarkerOptions.position(new LatLng(adBean.getWeiDu(), adBean.getJingDu()));
            locationMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.admarker)));
            locationMarkerOptions.title(adBean.getId());
            this.aMap.addMarker(locationMarkerOptions);
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        try {
            if (AROUND_AD_TYPE.equals(type)) {
                aroundADList.clear();
                aroundADList = JSONUtil.toJavaBeanList(ADBean.class, jsonArray);
                util.print("ssssssssssssssss===" + (pageIndex == R.id.map_adbtn));
                if (pageIndex == R.id.map_adbtn) {
                    clearMarkers(false);
                    addAroundADMarker();
                }
            } else if (SUPERVISE_AD_TYPE.equals(type)) {
                superviseADList.clear();
                superviseADList = JSONUtil.toJavaBeanList(ADBean.class, jsonArray);
                if (pageIndex == R.id.map_surveybtn) {
                    clearMarkers(false);
                    addSuperviseADMarker();
                }
            } else if (HANDLE_AD_TYPE.equals(type)) {
                handleADList.clear();
                handleADList = JSONUtil.toJavaBeanList(ADBean.class, jsonArray);
                if (pageIndex == R.id.map_handlebtn) {
                    clearMarkers(false);
                    addHandleADMarker();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onHttpFailure(String type, String json, String Err) {
        if (AROUND_AD_TYPE.equals(type)) {
            if (pageIndex == R.id.map_adbtn) {
                clearMarkers(false);
            }
            Toast.makeText(this, Err, Toast.LENGTH_SHORT).show();
//            toast.setText(Err);
        } else if (SUPERVISE_AD_TYPE.equals(type)) {
            if (pageIndex == R.id.map_surveybtn) {
                clearMarkers(false);
            }
            Toast.makeText(this, Err, Toast.LENGTH_SHORT).show();
//            toast.setText(Err);
        } else if (HANDLE_AD_TYPE.equals(type)) {
            if (pageIndex == R.id.map_handlebtn) {
                clearMarkers(false);
            }
            Toast.makeText(this, Err, Toast.LENGTH_SHORT).show();
//            toast.setText(Err);
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
        Button detailBtn = (Button) view.findViewById(R.id.infowindow_detailbtn);
        if (pageIndex == R.id.map_surveybtn) {
            detailBtn.setText("监测信息");
        } else if (pageIndex == R.id.map_handlebtn) {
            detailBtn.setText("版面详情");
        }
        render(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
        Button detailBtn = (Button) view.findViewById(R.id.infowindow_detailbtn);
        if (pageIndex == R.id.map_surveybtn) {
            detailBtn.setText("监测信息");
        } else if (pageIndex == R.id.map_handlebtn) {
            detailBtn.setText("版面详情");
        }
        render(marker, view);
        return view;
    }

    private void render(final Marker marker, View view) {
        view.findViewById(R.id.infowindow_navibtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADBean adBean = findADBeanByID(marker.getTitle());
                if (adBean != null) {
                    Intent intent = new Intent(MapActivity.this, GPSNaviActivity.class);
                    intent.putExtra("userLocationBean", userLocationBean);
                    intent.putExtra("adBean", adBean);
                    startActivity(intent);
                }
            }
        });
        view.findViewById(R.id.infowindow_detailbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADBean adBean = findADBeanByID(marker.getTitle());
                Intent intent = null;
                if (adBean != null) {
                    switch (pageIndex) {
                        case R.id.map_adbtn:
                        case R.id.map_surveybtn:
                            intent = new Intent(MapActivity.this, MediaDetailActivity.class);
                            intent.putExtra("adBean", adBean);
                            intent.putExtra("pageIndex", pageIndex);
                            startActivity(intent);
                            break;
                        case R.id.map_handlebtn:
                            intent = new Intent(MapActivity.this, HandleMediaDetailActivity.class);
                            intent.putExtra("adBean", adBean);
                            startActivity(intent);
                            break;
                    }
//                    if (!TextUtils.isEmpty(adBean.getMtbh())) {
//                        Intent intent = new Intent(MapActivity.this, MediaDetailActivity.class);
//                        intent.putExtra("adBean", adBean);
//                        intent.putExtra("pageIndex", pageIndex);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(MapActivity.this, HandleMediaDetailActivity.class);
//                        intent.putExtra("adBean", adBean);
//                        startActivity(intent);
//                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_AREA_CODE) {
                String provinecId = data.getStringExtra("provinceId");
                String cityId = data.getStringExtra("cityId");
                String districId = data.getStringExtra("districId");
                obtainHandleAD(provinecId, cityId, districId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra("isRefreshSuperviseAD", false)) {
            clearMarkers(false);
        } else if (intent.getBooleanExtra("isObtainHandleAD", false)) {
            String province = intent.getStringExtra("province");
            String city = intent.getStringExtra("city");
            String district = intent.getStringExtra("district");
            obtainHandleAD(province, city, district);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (!isShowInfoWindowForMarkers()) {
            switch (pageIndex) {
                case R.id.map_adbtn:
                    autoRefreshAD(true, AROUND_AUTO_REFRESH_TIME);
                    break;
                case R.id.map_surveybtn:
                    autoRefreshAD(true, SUPERVISE_AUTO_REFRESH_TIME);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        autoRefreshAD(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtil.onDestory();
        mapView.onDestroy();
        if (refreshAroundADHandler != null) {
            refreshAroundADHandler.cancle();
            refreshAroundADHandler = null;
        }
        if (refreshSuperviseADHandler != null) {
            refreshSuperviseADHandler.cancle();
            refreshSuperviseADHandler = null;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        centerLatLng = cameraPosition.target;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        centerLatLng = cameraPosition.target;
        mapScale = cameraPosition.zoom;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // 恢复保存的数据
            isAutoRefreshAroundAD = false;
            isAutoRefreshSuperviseAD = false;

            userAreaLevelBean = (UserAreaLevelBean) savedInstanceState.getSerializable("userAreaLevelBean");

            userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");

            setRadioButtonEnable(true);

            aroundADList = (ArrayList<ADBean>) savedInstanceState.getSerializable("aroundADList");
            superviseADList = (ArrayList<ADBean>) savedInstanceState.getSerializable("superviseADList");
            handleADList = (ArrayList<ADBean>) savedInstanceState.getSerializable("handleADList");

            // 恢复地图
            mapScale = savedInstanceState.getFloat("mapScale");
            aMap.moveCamera(CameraUpdateFactory.zoomTo(mapScale));
            centerLatLng = savedInstanceState.getParcelable("centerLatLng");
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));

            pageIndex = savedInstanceState.getInt("pageIndex");
            radioGroup.check(pageIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("pageIndex", pageIndex);
        outState.putSerializable("userLocationBean", userLocationBean);
        outState.putSerializable("aroundADList", aroundADList);
        outState.putSerializable("superviseADList", superviseADList);
        outState.putSerializable("handleADList", handleADList);
        outState.putFloat("mapScale", mapScale);
        outState.putParcelable("centerLatLng", centerLatLng);
        outState.putSerializable("userAreaLevelBean", userAreaLevelBean);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(locationMarker)) {
            return true;
        }
        marker.showInfoWindow();
        this.aMap.animateCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
        autoRefreshAD(false);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        List<Marker> markerList = aMap.getMapScreenMarkers();
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).hideInfoWindow();
        }
        switch (pageIndex) {
            case R.id.map_adbtn:
                autoRefreshAD(true, AROUND_AUTO_REFRESH_TIME);
                break;
            case R.id.map_surveybtn:
                autoRefreshAD(true, SUPERVISE_AUTO_REFRESH_TIME);
                break;
        }
    }

    private void autoRefreshAD(boolean isRefresh, int... times) {
        int delay = 100;
        if (times.length > 0) {
            delay = times[0];
        }
        switch (pageIndex) {
            case R.id.map_adbtn:
                isAutoRefreshSuperviseAD = false;
                if (refreshSuperviseADHandler != null) {
                    refreshSuperviseADHandler.cancle();
                    refreshSuperviseADHandler = null;
                }
                if (isRefresh) {
                    if (isAutoRefreshAroundAD) {
                        // 已经在自动刷新中
                        return;
                    }
                    isAutoRefreshAroundAD = true;
                    if (refreshAroundADHandler != null) refreshAroundADHandler.cancle();
                    refreshAroundADHandler = new MyHandler(delay, true, AROUND_AUTO_REFRESH_TIME) {
                        @Override
                        public void run() {
                            obtainAroundAD();
                        }
                    };
                } else {
                    isAutoRefreshAroundAD = false;
                    if (refreshAroundADHandler != null) {
                        refreshAroundADHandler.cancle();
                        refreshAroundADHandler = null;
                    }
                }
                break;
            case R.id.map_surveybtn:
                isAutoRefreshAroundAD = false;
                if (refreshAroundADHandler != null) {
                    refreshAroundADHandler.cancle();
                    refreshAroundADHandler = null;
                }
                if (isRefresh) {
                    if (isAutoRefreshSuperviseAD) {
                        // 已经在自动刷新中
                        return;
                    }
                    isAutoRefreshSuperviseAD = true;
                    if (refreshSuperviseADHandler != null) refreshSuperviseADHandler.cancle();
                    refreshSuperviseADHandler = new MyHandler(delay, true, SUPERVISE_AUTO_REFRESH_TIME) {
                        @Override
                        public void run() {
                            obtainSuperviseAD();
                        }
                    };
                } else {
                    isAutoRefreshSuperviseAD = false;
                    if (refreshSuperviseADHandler != null) {
                        refreshSuperviseADHandler.cancle();
                        refreshSuperviseADHandler = null;
                    }
                }
                break;
            case R.id.map_handlebtn:
                isAutoRefreshSuperviseAD = false;
                isAutoRefreshAroundAD = false;
                if (refreshSuperviseADHandler != null) {
                    refreshSuperviseADHandler.cancle();
                    refreshSuperviseADHandler = null;
                }
                if (refreshAroundADHandler != null) {
                    refreshAroundADHandler.cancle();
                    refreshAroundADHandler = null;
                }
                break;
        }
    }


    private void clearMarkers(boolean clearLocationMarker) {
        List<Marker> markerList = aMap.getMapScreenMarkers();
        for (int i = 0; i < markerList.size(); i++) {
            if (clearLocationMarker) {
                markerList.get(i).remove();
                continue;
            }
            if (!markerList.get(i).equals(locationMarker)) {
                if (markerList.get(i).isInfoWindowShown())
                    markerList.get(i).hideInfoWindow();
                markerList.get(i).remove();
            }
        }
    }

    private boolean isShowInfoWindowForMarkers() {
        try {
            List<Marker> markerList = aMap.getMapScreenMarkers();
            for (int i = 0; i < markerList.size(); i++) {
                if (markerList.get(i).isInfoWindowShown()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
