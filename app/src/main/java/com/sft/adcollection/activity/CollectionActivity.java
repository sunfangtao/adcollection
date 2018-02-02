package com.sft.adcollection.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.adapter.AutoCompleteTextViewAdapter;
import com.sft.adcollection.adapter.MySpinnerAdapter;
import com.sft.adcollection.adapter.MyStringSpinnerAdapter;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADStyleBean;
import com.sft.adcollection.bean.ADTypeBean;
import com.sft.adcollection.bean.CollectionBean;
import com.sft.adcollection.bean.SupplierBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.common.Constant;
import com.sft.adcollection.util.PermissionRequestUtil;
import com.sft.adcollection.util.TakePhotoUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.sft.baseactivity.util.JSONUtil;
import cn.sft.baseactivity.util.MyHandler;

/**
 * Created by Administrator on 2016/8/21.
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener, TakePhoto.TakeResultListener, TextWatcher, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String MEDIA_STYLE_TYPE = "mediastyletype";
    private static final String MEDIA_TYPE_TYPE = "mediatypetype";
    private static final String SUPPLIER_TYPE = "suppliertype";
    private static final String UPLOAD_MEDIA_TYPE = "uploadmediatype";
    private static final String UPLOAD_PAGE_TYPE = "uploadpagetype";


    // 视频请求码
    private static final int VIDEO_CODE = 0;
    // 修改位置请求码
    private static final int CHANGE_ADDRESS_CODE = 1;
    // 添加版面请求码
    private static final int ADD_PAGE_CODE = 2;

    //
    private ImageButton returnBtn;
    // 修改地址
    private TextView changeAddressTV;
    // 形式
    private Spinner styleSpinner;
    // 类别
    private Spinner typeSpinner;
    // 机构名称
    private AutoCompleteTextView supplierNameET;
    // 机构电话
    private EditText supplierPhoneET;
    // 行政区域
    private EditText areaET;
    // 行政区域(省市区)
    private Spinner provinceSpinner, citySpinner, districtSpinner;
    // 详细地址
    private EditText addressET;
    // 视频录制按钮
    private ImageButton videoBtn;
    // 拍照按钮
    private ImageButton takePhotoBtn;
    // 视频缩略图
    private ImageView videoIV;
    // 拍照缩略图
    private ImageView takePhotoIV;
    // 添加按钮
    private Button addBtn;
    // 添加版面数目
    private TextView pageNumTV;
    // 上传按钮
    private Button uploadBtn;

    // 机构名称列表
    private ArrayList<SupplierBean> supplierList;
    // 机构名称Adapter
    private AutoCompleteTextViewAdapter supplierNameAdapter;

    // 形式列表
    private ArrayList<ADStyleBean> styleList;
    // 类别列表
    private ArrayList<ADTypeBean> typeList;
    // 形式Adapter
    private MySpinnerAdapter styleAdapter;
    // 类别Adapter
    private MySpinnerAdapter typeAdapter;

    // 省列表
    private ArrayList<String> provinceList;
    // 市列表
    private ArrayList<String> cityList;
    // 区列表
    private ArrayList<String> districtList;
    // 省Adapter
    private MyStringSpinnerAdapter provinceAdapter;
    // 市Adapter
    private MyStringSpinnerAdapter cityAdapter;
    // 区Adapter
    private MyStringSpinnerAdapter districtAdapter;

    // 视频文件名称
    private String videoFileName;
    // 视频文件完整路径
    private String videoFilePath;
    // 图片文件名称
    private String photoFileName;
    // 图片文件完整路径
    private String photoFilePath;

    // 拍照工具
    private TakePhotoUtil takePhotoUtil;
    // 用户位置
    private UserLocationBean userLocationBean;
    // 记录用户请求Camera权限的操作：拍照0或录像1
    private int operateType = 0;

    // 媒体参数
    private RequestParams mediaParams;
    // 版面参数
    private ArrayList<RequestParams> pageParamsList;
    //
    private CollectionBean collectionBean;
    // 当前上传的版面个数;0:上传媒体，>0 上传版面
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData(savedInstanceState);
        initListener();
        if (savedInstanceState == null) {
            obtainStyle();
            obtainType();
        }
    }

    private void initView() {
        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        styleSpinner = (Spinner) findViewById(R.id.collection_stylespinner);
        typeSpinner = (Spinner) findViewById(R.id.collection_typespinner);
        supplierNameET = (AutoCompleteTextView) findViewById(R.id.collection_suppliernameet);
        supplierPhoneET = (EditText) findViewById(R.id.collection_supplierphoneet);
        areaET = (EditText) findViewById(R.id.collection_areaet);
        addressET = (EditText) findViewById(R.id.collection_addresset);
        changeAddressTV = (TextView) findViewById(R.id.titlebar_right_tv);
        videoBtn = (ImageButton) findViewById(R.id.collection_videoimg);
        takePhotoBtn = (ImageButton) findViewById(R.id.collection_takepicimg);
        videoIV = (ImageView) findViewById(R.id.collection_videothumbimg);
        takePhotoIV = (ImageView) findViewById(R.id.collection_takepicthumbimg);
        addBtn = (Button) findViewById(R.id.collection_addbtn);
        pageNumTV = (TextView) findViewById(R.id.collection_pagenumtv);
        uploadBtn = (Button) findViewById(R.id.collection_updatebtn);

        provinceSpinner = (Spinner) findViewById(R.id.collection_provincespinner);
        citySpinner = (Spinner) findViewById(R.id.collection_cityspinner);
        districtSpinner = (Spinner) findViewById(R.id.collection_districtspinner);
    }

    private void initData(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.titlebar_title_tv)).setText(getString(R.string.collection));
        changeAddressTV.setVisibility(View.VISIBLE);

        collectionBean = new CollectionBean();
        pageParamsList = new ArrayList<>();
        collectionBean.setPageParamsList(pageParamsList);

        supplierList = new ArrayList<>();
        supplierNameAdapter = new AutoCompleteTextViewAdapter(this, supplierList);
        supplierNameET.setAdapter(supplierNameAdapter);

        styleList = new ArrayList<>();
        ADStyleBean adStyleBean = new ADStyleBean();
        adStyleBean.setId("1");
        adStyleBean.setName("媒体形式");
        styleList.add(adStyleBean);
        styleAdapter = new MySpinnerAdapter(this, styleList);
        styleSpinner.setAdapter(styleAdapter);

        typeList = new ArrayList<>();
        ADTypeBean adTypeBean = new ADTypeBean();
        adTypeBean.setId("1");
        adTypeBean.setName("媒体类型");
        typeList.add(adTypeBean);
        typeAdapter = new MySpinnerAdapter(this, typeList);
        typeSpinner.setAdapter(typeAdapter);

        provinceList = new ArrayList<>();
        provinceAdapter = new MyStringSpinnerAdapter(this, provinceList);
        provinceSpinner.setAdapter(provinceAdapter);

        cityList = new ArrayList<>();
        cityAdapter = new MyStringSpinnerAdapter(this, cityList);
        citySpinner.setAdapter(cityAdapter);

        districtList = new ArrayList<>();
        districtAdapter = new MyStringSpinnerAdapter(this, districtList);
        districtSpinner.setAdapter(districtAdapter);


        videoFilePath = app.getBasePath() + Constant.FilePath.COLLECTION_VIDEO_PATH;
        photoFilePath = app.getBasePath() + Constant.FilePath.COLLECTION_PHOTO_PATH;

        takePhotoUtil = new TakePhotoUtil(this, this, savedInstanceState);

        if (savedInstanceState == null) {
            // 页面创建时，初始化行政区域和地址的默认值为当前定位的位置
            userLocationBean = app.getUserLocationBean();
            addressET.setText(userLocationBean.getSimpleAdd());
            areaET.setText(userLocationBean.getRegion());
            obtainProvinceList();
            setRegionSpnnerValue(userLocationBean.getProvince(), userLocationBean.getCity(), userLocationBean.getDistrict());
        }
    }

    private void initListener() {
        returnBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        changeAddressTV.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        takePhotoBtn.setOnClickListener(this);
        videoIV.setOnClickListener(this);
        takePhotoIV.setOnClickListener(this);
        supplierNameET.addTextChangedListener(this);
        supplierNameET.setOnItemClickListener(this);
    }

    // 获取媒体形式
    private void obtainStyle() {
        httpPost(MEDIA_STYLE_TYPE, new RequestParams(), Constant.HTTPUrl.MEDIA_STYLE_URL);
    }

    // 获取媒体类别
    private void obtainType() {
        httpPost(MEDIA_TYPE_TYPE, new RequestParams(), Constant.HTTPUrl.MEDIA_TYPE_URL);
    }

    // 获取机构名称
    private void obtainSupplierName(String curStr) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", curStr);
        requestParams.put("leiBieId", 2);
        httpPost(SUPPLIER_TYPE, requestParams, Constant.HTTPUrl.CONSUMER_URL);
    }

    private String creatMediaRequestParams() {

        if (TextUtils.isEmpty(photoFileName)) {
            // 图片为空
            return "请拍摄照片";
        }
        if (TextUtils.isEmpty(videoFileName)) {
            // 视频为空
            return "请录制视频";
        }
        File photoFile = new File(photoFilePath, photoFileName);
        if (!photoFile.exists()) {
            return "请拍摄照片";
        }
        File videoFile = new File(videoFilePath, videoFileName);
        if (!photoFile.exists()) {
            return "请录制视频";
        }

        try {
            mediaParams = new RequestParams();
            mediaParams.put("jingDu", userLocationBean.getLng());
            mediaParams.put("weiDu", userLocationBean.getLat());
            mediaParams.put("mtdz", addressET.getText().toString());
            mediaParams.put("mtxsId", styleList.get(styleAdapter.getSelectIndex()).getId());
            mediaParams.put("mtxs", styleAdapter.getSelectValue());
            mediaParams.put("mtflId", typeList.get(typeAdapter.getSelectIndex()).getId());
            mediaParams.put("mtfl", typeAdapter.getSelectValue());
            mediaParams.put("sheng", provinceAdapter.getSelectValue());
            mediaParams.put("shi", cityAdapter.getSelectValue());
            mediaParams.put("qu", districtAdapter.getSelectValue());
            mediaParams.put("jgmc", supplierNameET.getText().toString());
            mediaParams.put("jgTel", supplierPhoneET.getText().toString());
            mediaParams.put("file1", new File(photoFilePath, photoFileName));
            mediaParams.put("file", new File(videoFilePath, videoFileName));

            collectionBean.setMediaParams(mediaParams);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return null;
    }

    private void addMediaPage() {
        Intent intent = new Intent(this, CollectionPageActivity.class);
        startActivityForResult(intent, ADD_PAGE_CODE);
    }

    private void uploadMediaAndPage() {
        String result = null;
        if ((result = creatMediaRequestParams()) != null) {
            toast.setText(result);
            return;
        }
        count = 0;
        httpProgressDialog.setMessage("正在上传媒体资料...").show();
        httpPost(UPLOAD_MEDIA_TYPE, this.mediaParams, Constant.HTTPUrl.UPLOAD_NEW_MEDIA_URL);
    }

    private boolean uploadPage(int count) {
        if (count > pageParamsList.size()) {
            return false;
        }
        RequestParams requestParams = pageParamsList.get(count - 1);
        requestParams.put("count", count - 1);
        requestParams.put("mtzlId", collectionBean.getId());
        requestParams.put("jcbgId", collectionBean.getJcbgId());
        httpPost(UPLOAD_PAGE_TYPE, requestParams, Constant.HTTPUrl.UPLOAD_NEW_PAGE_URL);

        return true;
    }

    @Override
    protected void onHttpSuccess(String type, String message) {
        if (SUPPLIER_TYPE.equals(type)) {
            supplierList.clear();
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONObject jsonObject) {
        if (UPLOAD_MEDIA_TYPE.equals(type)) {
            count++;
            collectionBean = JSONUtil.toJavaBean(CollectionBean.class, jsonObject);
            uploadPage(count);
            httpProgressDialog.setMessage("开始上传第 1 个版面");
        } else {
            count++;
            if (uploadPage(count)) {
                httpProgressDialog.setMessage("开始上传第 " + count + " 个版面");
            } else {
                httpProgressDialog.dismissWithSuccess("上传成功");
                new MyHandler(1000) {
                    @Override
                    public void run() {
                        finish();
                    }
                };
            }
        }
    }

    @Override
    protected void onHttpFailure(String type, String json, String Err) {
        if (UPLOAD_MEDIA_TYPE.equals(type)) {
            httpProgressDialog.dismissWithFailure(Err);
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        try {
            if (MEDIA_STYLE_TYPE.equals(type)) {
                styleList.clear();
                styleList.addAll(JSONUtil.toJavaBeanList(ADStyleBean.class, jsonArray));
                styleAdapter.notifyDataSetChanged();
            } else if (MEDIA_TYPE_TYPE.equals(type)) {
                typeList.clear();
                typeList.addAll(JSONUtil.toJavaBeanList(ADTypeBean.class, jsonArray));
                typeAdapter.notifyDataSetChanged();
            } else if (SUPPLIER_TYPE.equals(type)) {
                supplierList.clear();
                supplierList.addAll(JSONUtil.toJavaBeanList(SupplierBean.class, jsonArray));
                supplierNameAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (!onClickSingleView()) {
            return;
        }
        Intent intent = null;
        switch (v.getId()) {
            case R.id.titlebar_left_imgbtn:
                finish();
                break;
            case R.id.titlebar_right_tv:
                intent = new Intent(this, ChangeAddressActivity.class);
                intent.putExtra("isNeedLocation", true);
                intent.putExtra("userLocationBean", app.getUserLocationBean());
                startActivityForResult(intent, CHANGE_ADDRESS_CODE);
                break;
            case R.id.collection_addbtn:
                addMediaPage();
                break;
            case R.id.collection_updatebtn:
                uploadMediaAndPage();
                break;
            case R.id.collection_videoimg:
                operateType = 1;
                if (PermissionRequestUtil.getInstance().requestPermission(this, PermissionRequestUtil.CAMERA, PermissionRequestUtil.RECORD_AUDIO, PermissionRequestUtil.READ_EXTERNAL_STORAGE)) {
                    recordVideo();
                }
                break;
            case R.id.collection_takepicimg:
                operateType = 0;
                if (PermissionRequestUtil.getInstance().requestPermission(this, PermissionRequestUtil.CAMERA, PermissionRequestUtil.READ_EXTERNAL_STORAGE)) {
                    takePhoto();
                }
                break;
            case R.id.collection_takepicthumbimg:
                if (!TextUtils.isEmpty(photoFileName)) {
                    intent = new Intent(this, ShowBigImageActivity.class);
                    intent.putExtra("url", photoFilePath + photoFileName);
                    startActivity(intent);
                }
                break;
            case R.id.collection_videothumbimg:
                if (!TextUtils.isEmpty(videoFileName)) {
                    intent = new Intent(android.content.Intent.ACTION_VIEW);
                    Uri data = Uri.parse("file://" + videoFilePath + videoFileName);
                    intent.setDataAndType(data, "video/mp4");
                    startActivity(intent);
                }
                break;
        }
    }

    private void takePhoto() {
        // 生成图片文件名
        photoFileName = System.currentTimeMillis() + ".png";
        takePhotoUtil.setFilePath(photoFilePath, photoFileName);
        takePhotoUtil.startTakePhoto();
    }

    private void recordVideo() {
        // 生成视频文件名
        videoFileName = System.currentTimeMillis() + ".mp4";
        Intent intent = new Intent(this, VideoRecordingActivity.class);
        intent.putExtra("fileName", videoFileName);
        intent.putExtra("filePath", videoFilePath);
        startActivityForResult(intent, VIDEO_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestUtil.CAMERA) {
            // 获取摄像头权限结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (operateType == 0) {
                    takePhoto();
                } else if (operateType == 1) {
                    recordVideo();
                }
            } else {
                // 拒绝权限
                toast.setText(getString(R.string.cancel_camerapermission));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_CODE) {
                //录像成功返回
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoFilePath + videoFileName, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoIV.setImageBitmap(bitmap);
            } else if (requestCode == CHANGE_ADDRESS_CODE) {
                userLocationBean = (UserLocationBean) data.getSerializableExtra("userLocationBean");
                if (userLocationBean.getAddress() != null)
                    addressET.setText(userLocationBean.getSimpleAdd());
                if (userLocationBean.getRegion() != null) {
                    areaET.setText(userLocationBean.getRegion());
                    setRegionSpnnerValue(userLocationBean.getProvince(), userLocationBean.getCity(), userLocationBean.getDistrict());
                }
            } else if (requestCode == ADD_PAGE_CODE) {
                pageParamsList.add((RequestParams) data.getSerializableExtra("pageRequestParams"));
                pageNumTV.setVisibility(View.VISIBLE);
                uploadBtn.setVisibility(View.VISIBLE);

                pageNumTV.setText("已经添加了" + pageParamsList.size() + "个版面");
            }
        }
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void takeSuccess(String imagePath) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 200, 150);
        takePhotoIV.setImageBitmap(bitmap);
    }

    @Override
    public void takeFail(String msg) {

    }

    @Override
    public void takeCancel() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        util.print("afterTextChanged");
        if (!TextUtils.isEmpty(s.toString()))
            obtainSupplierName(s.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SupplierBean supplierBean = supplierList.get(position);
        if (supplierBean != null) {
            String phone = null;
            if (!TextUtils.isEmpty(supplierBean.getMobile())) {
                phone = supplierBean.getMobile();
            } else if (!TextUtils.isEmpty(supplierBean.getTel())) {
                phone = supplierBean.getTel();
            } else if (!TextUtils.isEmpty(supplierBean.getMobile1())) {
                phone = supplierBean.getMobile1();
            } else if (!TextUtils.isEmpty(supplierBean.getTel1())) {
                phone = supplierBean.getTel1();
            }
            supplierPhoneET.setText(phone);
        }
    }

    private void setRegionSpnnerValue(String provinceName, String cityName, String districtName) {
        int index = provinceList.indexOf(provinceName);

        try {
            provinceSpinner.setOnItemSelectedListener(null);
            citySpinner.setOnItemSelectedListener(null);
            districtSpinner.setOnItemSelectedListener(null);
            if (index >= 0) {
                provinceAdapter.notifyDataSetChanged();
                provinceSpinner.setSelection(index);
                obtainCityList(provinceName);
                index = cityList.indexOf(cityName);
                if (index >= 0) {
                    cityAdapter.notifyDataSetChanged();
                    citySpinner.setSelection(index);
                    obtainDistrictList(cityName);
                    index = districtList.indexOf(districtName);
                    if (index >= 0) {
                        districtAdapter.notifyDataSetChanged();
                        districtSpinner.setSelection(index);
                    }
                }
            }
            new MyHandler(1000) {
                @Override
                public void run() {
                    provinceSpinner.setOnItemSelectedListener(CollectionActivity.this);
                    citySpinner.setOnItemSelectedListener(CollectionActivity.this);
                    districtSpinner.setOnItemSelectedListener(CollectionActivity.this);
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void obtainProvinceList() {
        provinceList.clear();
        provinceList.addAll(app.getProvinceNameList());
//        provinceAdapter.notifyDataSetChanged();
    }

    private void obtainCityList(String provinceName) {
        cityList.clear();
        cityList.addAll(app.getCitisDataMap().get(provinceName));
    }

    private void obtainDistrictList(String cityName) {
        districtList.clear();
        districtList.addAll(app.getDistrictDataMap().get(cityName));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        provinceSpinner.setOnItemSelectedListener(null);
        citySpinner.setOnItemSelectedListener(null);
        districtSpinner.setOnItemSelectedListener(null);
        switch (parent.getId()) {
            case R.id.collection_provincespinner:
                obtainCityList(provinceAdapter.getSelectValue());
                citySpinner.setSelection(0);
                cityAdapter.notifyDataSetChanged();
                obtainDistrictList(cityList.get(0));
                districtSpinner.setSelection(0);
                districtAdapter.notifyDataSetChanged();
                break;
            case R.id.collection_cityspinner:
                obtainDistrictList(cityAdapter.getSelectValue());
                districtSpinner.setSelection(0);
                districtAdapter.notifyDataSetChanged();
                break;
            case R.id.collection_districtspinner:
                break;
        }
        new MyHandler(1000) {
            @Override
            public void run() {
                provinceSpinner.setOnItemSelectedListener(CollectionActivity.this);
                citySpinner.setOnItemSelectedListener(CollectionActivity.this);
                districtSpinner.setOnItemSelectedListener(CollectionActivity.this);
            }
        };
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // 恢复保存的数据
            photoFileName = savedInstanceState.getString("photoFileName");
            videoFileName = savedInstanceState.getString("videoFileName");

            collectionBean = (CollectionBean) savedInstanceState.getSerializable("putSerializable");

            if (collectionBean != null) {
                pageParamsList = collectionBean.getPageParamsList();
                mediaParams = collectionBean.getMediaParams();

                if (pageParamsList.size() > 0) {
                    pageNumTV.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);

                    pageNumTV.setText("已经添加了" + pageParamsList.size() + "个版面");
                }
            }

            // 设置视频缩略图
            Bitmap videoBitmap = ThumbnailUtils.createVideoThumbnail(videoFilePath + videoFileName, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            videoIV.setImageBitmap(videoBitmap);
            // 设置图片缩略图
            Bitmap photoBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoFilePath + photoFileName), 200, 150);
            takePhotoIV.setImageBitmap(photoBitmap);

            supplierNameET.removeTextChangedListener(this);
            supplierNameET.setText(savedInstanceState.getString("supplierNameET"));
            supplierNameET.addTextChangedListener(this);
            supplierPhoneET.setText(savedInstanceState.getString("supplierPhoneET"));
            areaET.setText(savedInstanceState.getString("areaET"));
            addressET.setText(savedInstanceState.getString("addressET"));

            styleList.clear();
            typeList.clear();
            provinceList.clear();
            cityList.clear();
            districtList.clear();

            styleList.addAll((ArrayList<ADStyleBean>) savedInstanceState.getSerializable("styleList"));
            styleAdapter.notifyDataSetChanged();
            typeList.addAll((ArrayList<ADTypeBean>) savedInstanceState.getSerializable("typeList"));
            typeAdapter.notifyDataSetChanged();
            provinceList.addAll((ArrayList<String>) savedInstanceState.getSerializable("provinceList"));
            provinceAdapter.notifyDataSetChanged();
            cityList.addAll((ArrayList<String>) savedInstanceState.getSerializable("cityList"));
            cityAdapter.notifyDataSetChanged();
            districtList.addAll((ArrayList<String>) savedInstanceState.getSerializable("districtList"));
            districtAdapter.notifyDataSetChanged();
            // 修改下拉列表的选中项
            styleSpinner.setSelection(savedInstanceState.getInt("styleIndex"));
            typeSpinner.setSelection(savedInstanceState.getInt("typeIndex"));

            provinceSpinner.setOnItemSelectedListener(null);
            citySpinner.setOnItemSelectedListener(null);
            districtSpinner.setOnItemSelectedListener(null);

            provinceSpinner.setSelection(savedInstanceState.getInt("provinceIndex"));
            citySpinner.setSelection(savedInstanceState.getInt("cityIndex"));
            districtSpinner.setSelection(savedInstanceState.getInt("districtIndex"));

            new MyHandler(1000) {
                @Override
                public void run() {
                    provinceSpinner.setOnItemSelectedListener(CollectionActivity.this);
                    citySpinner.setOnItemSelectedListener(CollectionActivity.this);
                    districtSpinner.setOnItemSelectedListener(CollectionActivity.this);
                }
            };

            userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");

            operateType = savedInstanceState.getInt("operateType");

            takePhotoUtil.handlePhoto(photoFilePath + photoFileName);

            count = savedInstanceState.getInt("count");

            if (count > 0) {
                // 是否恢复上传
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("photoFileName", photoFileName);
        outState.putString("videoFileName", videoFileName);
        outState.putString("supplierNameET", supplierNameET.getText().toString());
        outState.putString("supplierPhoneET", supplierPhoneET.getText().toString());
        outState.putString("areaET", areaET.getText().toString());
        outState.putString("addressET", addressET.getText().toString());
        outState.putSerializable("styleList", styleList);
        outState.putSerializable("typeList", typeList);
        outState.putInt("styleIndex", styleAdapter.getSelectIndex());
        outState.putInt("typeIndex", typeAdapter.getSelectIndex());
        outState.putSerializable("provinceList", provinceList);
        outState.putInt("provinceIndex", provinceAdapter.getSelectIndex());
        outState.putSerializable("cityList", cityList);
        outState.putInt("cityIndex", cityAdapter.getSelectIndex());
        outState.putSerializable("districtList", districtList);
        outState.putInt("districtIndex", districtAdapter.getSelectIndex());
        outState.putSerializable("userLocationBean", userLocationBean);
        outState.putInt("operateType", operateType);
        outState.putSerializable("collectionBean", collectionBean);
        outState.putInt("count", count);
        takePhotoUtil.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
