package com.sft.adcollection.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.UserLocationBean;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/23.
 */
public class ChangeAddressActivity extends BaseActivity implements AMap.OnMarkerDragListener, View.OnClickListener, AMap.OnCameraChangeListener {

    // 地图控件
    private MapView mapView;
    // 高德地图对象
    private AMap aMap;
    // 定位Marker
    private Marker locationMarker;
    // 用户的位置信息
    private UserLocationBean userLocationBean;
    // 地图界面中心点
    private LatLng centerLatLng;
    // 地图的缩放级别
    private float mapScale = 0f;

    // 返回按钮
    private ImageButton returnBtn;
    // 保存文字
    private TextView saveTV;

    private LinearLayout paramLayout;
    private TextView titleTV;
    private TextView latTV;
    private TextView lngTV;
    private TextView addressTV;
    private TextView addressErrorTV;
    private ProgressBar progressBar;

    private LinearLayout searchLayout;
    private ProgressBar searchProgressBar;
    private TextView searchTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        initView();
        initData(savedInstanceState);
        initListener();
        showInitMarker();
    }

    private void initView() {
        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        saveTV = (TextView) findViewById(R.id.titlebar_right_tv);
        mapView = (MapView) findViewById(R.id.changeaddress_map);

        paramLayout = (LinearLayout) findViewById(R.id.changeaddress_paramlayout);
        titleTV = (TextView) findViewById(R.id.changeaddress_titletext);
        latTV = (TextView) findViewById(R.id.changeaddress_lattext);
        lngTV = (TextView) findViewById(R.id.changeaddress_lngtext);
        addressTV = (TextView) findViewById(R.id.changeaddress_addresstext);
        addressErrorTV = (TextView) findViewById(R.id.changeaddress_errortext);
        progressBar = (ProgressBar) findViewById(R.id.changeaddress_progressbar);

        searchLayout = (LinearLayout) findViewById(R.id.changeaddress_searchlayout);
        searchProgressBar = (ProgressBar) findViewById(R.id.changeaddress_waitprogressbar);
        searchTV = (TextView) findViewById(R.id.changeaddress_search);
    }

    private void initData(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.titlebar_title_tv)).setText("修改位置");
        saveTV.setText("保存");
        saveTV.setVisibility(View.VISIBLE);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        aMap.getUiSettings().setScaleControlsEnabled(true);
        // 设置缩放级别
        mapScale = 16f;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(mapScale));

        if (getIntent().getBooleanExtra("isNeedLocation", false)) {
            userLocationBean = (UserLocationBean) getIntent().getSerializableExtra("userLocationBean");
        } else {
            userLocationBean = new UserLocationBean();
            userLocationBean.setLat(getIntent().getDoubleExtra("lat", 0));
            userLocationBean.setLng(getIntent().getDoubleExtra("lng", 0));
            userLocationBean.setAddress(getIntent().getStringExtra("address"));
        }
    }

    private void initListener() {
        returnBtn.setOnClickListener(this);
        saveTV.setOnClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnCameraChangeListener(this);
    }

    private void showInitMarker() {
        if (userLocationBean == null) return;
        MarkerOptions locationMarkerOptions = new MarkerOptions();
        locationMarkerOptions.draggable(true);
        locationMarkerOptions.position(new LatLng(userLocationBean.getLat(), userLocationBean.getLng()));
        locationMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.mylocation)));
        locationMarkerOptions.anchor(0.5f, 0.734375f);
        locationMarker = this.aMap.addMarker(locationMarkerOptions);

        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(userLocationBean.getLat(), userLocationBean.getLng())));
        centerLatLng = new LatLng(userLocationBean.getLat(), userLocationBean.getLng());

        latTV.setText(getString(R.string.changeaddress_lat) + "( " + userLocationBean.getLat() + " )");
        lngTV.setText(getString(R.string.changeaddress_lng) + "( " + userLocationBean.getLng() + " )");
        addressTV.setText(getString(R.string.changeaddress_address) + userLocationBean.getAddress());
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        latTV.setText(getString(R.string.changeaddress_lat) + "( " + marker.getPosition().latitude + " )");
        lngTV.setText(getString(R.string.changeaddress_lng) + "( " + marker.getPosition().longitude + " )");
        addressTV.setVisibility(View.GONE);
        addressErrorTV.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        latTV.setText(getString(R.string.changeaddress_lat) + "( " + marker.getPosition().latitude + " )");
        lngTV.setText(getString(R.string.changeaddress_lng) + "( " + marker.getPosition().longitude + " )");
        addressTV.setVisibility(View.GONE);
        addressErrorTV.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latTV.setText(getString(R.string.changeaddress_lat) + "( " + marker.getPosition().latitude + " )");
        lngTV.setText(getString(R.string.changeaddress_lng) + "( " + marker.getPosition().longitude + " )");
        addressTV.setVisibility(View.VISIBLE);
        addressErrorTV.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        LatLonPoint latLonPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 3000, GeocodeSearch.AMAP);
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new MyOnGeocodeSearchListener(marker));
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);

        httpProgressDialog.setMessage("正在解析当前位置...");
    }

    @Override
    public void onClick(View v) {
        if (!onClickSingleView()) {
            return;
        }
        switch (v.getId()) {
            case R.id.titlebar_left_imgbtn:
                finish();
                break;
            case R.id.titlebar_right_tv:
                if (userLocationBean.getLat() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("userLocationBean", userLocationBean);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    toast.setText(getString(R.string.changeaddress_location));
                }
                break;
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

    private class MyOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {

        private Marker marker;

        public MyOnGeocodeSearchListener(Marker marker) {
            this.marker = marker;
        }

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            userLocationBean.setLat(marker.getPosition().latitude);
            userLocationBean.setLng(marker.getPosition().longitude);
            userLocationBean.setAddress(regeocodeAddress.getFormatAddress());
            userLocationBean.setProvince(regeocodeAddress.getProvince());
            userLocationBean.setCity(regeocodeAddress.getCity());
            userLocationBean.setDistrict(regeocodeAddress.getDistrict());
            userLocationBean.setRegion(regeocodeAddress.getProvince() + regeocodeAddress.getCity() + regeocodeAddress.getDistrict());

            addressTV.setText(getString(R.string.changeaddress_address) + regeocodeAddress.getFormatAddress());
            progressBar.setVisibility(View.GONE);

            httpProgressDialog.dismissWithSuccess("地理位置解析成功");
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // 恢复保存的数据
            userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");

            latTV.setText(getString(R.string.changeaddress_lat) + "( " + userLocationBean.getLat() + " )");
            lngTV.setText(getString(R.string.changeaddress_lng) + "( " + userLocationBean.getLng() + " )");
            addressTV.setText(getString(R.string.changeaddress_address) + userLocationBean.getAddress());

            mapScale = savedInstanceState.getFloat("mapScale");
            aMap.moveCamera(CameraUpdateFactory.zoomTo(mapScale));
            centerLatLng = savedInstanceState.getParcelable("centerLatLng");
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(centerLatLng));

            showInitMarker();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("userLocationBean", userLocationBean);
        outState.putParcelable("centerLatLng", centerLatLng);
        outState.putFloat("mapScale", mapScale);
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
