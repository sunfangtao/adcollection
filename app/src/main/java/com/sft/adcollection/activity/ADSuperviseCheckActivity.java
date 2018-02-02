package com.sft.adcollection.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.adapter.AutoCompleteTextViewAdapter;
import com.sft.adcollection.adapter.MySpinnerAdapter;
import com.sft.adcollection.adapter.MyStringSpinnerAdapter;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADBean;
import com.sft.adcollection.bean.ADSuperviseDetailBean;
import com.sft.adcollection.bean.ADSuperviseDetailPageBean;
import com.sft.adcollection.bean.ADTypeBean;
import com.sft.adcollection.bean.ConsumerBean;
import com.sft.adcollection.bean.DamageConBean;
import com.sft.adcollection.bean.LawTypeBean;
import com.sft.adcollection.bean.LawTypeDetailBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.common.Constant;
import com.sft.adcollection.util.PermissionRequestUtil;
import com.sft.adcollection.util.TakePhotoUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import cn.sft.baseactivity.util.JSONUtil;
import cn.sft.baseactivity.util.MyHandler;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 */
public class ADSuperviseCheckActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, TakePhoto.TakeResultListener, TextWatcher {

    protected static final String CONSUMER_TYPE = "consumer";
    protected static final String LAWTYPE_TYPE = "lawtype";
    protected static final String LAWTYPEDETAIL_TYPE = "lawtypedetailtype";
    protected static final String ADTYPE_TYPE = "adtype";
    protected static final String DAMAGECON_TYPE = "damagetype";
    protected static final String SUPERVISE_PAGE_TYPE = "supervisepagetype";

    protected static final int CHANGE_ADDRESS = 0;

    //
    protected ImageButton returnBtn;
    //
    protected TextView regionSelectTV;
    // 采购商名称
    protected AutoCompleteTextView consumerNameET;
    // 采购商电话
    protected EditText consumerPhoneET;
    // 签约状态
    protected Spinner stateSpinner;
    // 广告类别
    protected Spinner adStyleSpinner;
    // 画面分类
    protected Spinner damagedConSpinner;
    // 审核结果
    protected Spinner checkSpinner;
    // 违法信息布局
    protected LinearLayout lawLayout;
    // 法规分类
    protected Spinner lawTypeSpinner;
    // 法规编号
    protected Spinner lawCodeSpinner;
    // 法规内容
    protected TextView lawContentTV;
    // 广告内容
    protected EditText adContentET;
    // 拍照按钮
    protected ImageButton takePhotoBtn;
    // 拍照缩略图
    protected ImageView takePhotoIV;
    // 增加违法按钮
    protected Button addBtn;
    // 增加违法信息结果
    protected TextView addResultTV;
    // 确认
    protected Button confirmBtn;
    // 行政区域布局
    private LinearLayout regionLayout;
    // 行政区域(省市区)
    private Spinner provinceSpinner, citySpinner, districtSpinner;
    //
    protected TakePhotoUtil takePhotoUtil;

    //需要保存的-------------------------------------
    protected ADSuperviseDetailBean adSuperviseDetailBean;
    protected ADSuperviseDetailPageBean pageBean;

    // 采购商列表
    protected ArrayList<ConsumerBean> consumerList;
    // 采购商Adapter
    protected AutoCompleteTextViewAdapter consumerNameAdapter;

    // 签约状态列表
    protected ArrayList<String> stateList;
    // 签约状态Adapter
    protected MyStringSpinnerAdapter stateAdapter;

    // 广告类别列表
    protected ArrayList<DamageConBean> damageConList;
    // 广告类别Adapter
    protected MySpinnerAdapter damageConAdapter;

    // 画面分类列表
    protected ArrayList<ADTypeBean> adStyleList;
    // 画面分类Adapter
    protected MySpinnerAdapter adStyleAdapter;

    // 审核结果列表
    protected ArrayList<String> checkList;
    // 审核结果Adapter
    protected MyStringSpinnerAdapter checkAdapter;

    // 法规分类列表
    protected ArrayList<LawTypeBean> lawTypeList;
    // 法规分类Adapter
    protected MySpinnerAdapter lawTypeAdapter;

    // 法规编号列表
    protected ArrayList<LawTypeDetailBean> lawTypeDetailList;
    // 法规编号Adapter
    protected MySpinnerAdapter lawCodeAdapter;

    // 图片文件名称
    protected String photoFileName;
    // 图片文件完整路径
    protected String photoFilePath;

    // 用户修改的广告位置信息
    protected UserLocationBean userLocationBean;
    //
    protected ADBean adBean;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_supervise_check);
        initView();
        initData(savedInstanceState);
        initListener();

        if (savedInstanceState == null) {
            obtainADType();
            obtainLawType();
            obtainDamageCon();
        }
    }

    private void initView() {

        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        regionSelectTV = (TextView) findViewById(R.id.titlebar_right_tv);
        consumerNameET = (AutoCompleteTextView) findViewById(R.id.supervisecheck_customernameet);
        consumerPhoneET = (EditText) findViewById(R.id.supervisecheck_customerphoneet);
        stateSpinner = (Spinner) findViewById(R.id.supervisecheck_statespinner);
        adStyleSpinner = (Spinner) findViewById(R.id.supervisecheck_adtypespinner);
        damagedConSpinner = (Spinner) findViewById(R.id.supervisecheck_damagedconditionspinner);
        checkSpinner = (Spinner) findViewById(R.id.supervisecheck_checkspinner);
        lawLayout = (LinearLayout) findViewById(R.id.supervisecheck_lawlayout);
        lawTypeSpinner = (Spinner) findViewById(R.id.supervisecheck_lawtypespinner);
        lawCodeSpinner = (Spinner) findViewById(R.id.supervisecheck_lawcodespinner);
        lawContentTV = (TextView) findViewById(R.id.supervisecheck_lawcontenttv);
        adContentET = (EditText) findViewById(R.id.supervisecheck_adcontentet);
        takePhotoBtn = (ImageButton) findViewById(R.id.supervisecheck_takepicimg);
        takePhotoIV = (ImageView) findViewById(R.id.supervisecheck_takepicthumbimg);
        addBtn = (Button) findViewById(R.id.supervisecheck_addbtn);
        addResultTV = (TextView) findViewById(R.id.supervisecheck_addresulttv);
        confirmBtn = (Button) findViewById(R.id.supervisecheck_confirmbtn);

        provinceSpinner = (Spinner) findViewById(R.id.supervisecheck_provincespinner);
        citySpinner = (Spinner) findViewById(R.id.supervisecheck_cityspinner);
        districtSpinner = (Spinner) findViewById(R.id.supervisecheck_districtspinner);
        regionLayout = (LinearLayout) findViewById(R.id.supervisecheck_regionlayout);
    }

    private void initData(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.titlebar_title_tv)).setText("版面监测");
        regionSelectTV.setVisibility(View.VISIBLE);
        userLocationBean = null;

        lawLayout.setVisibility(View.GONE);
        photoFilePath = app.getBasePath() + Constant.FilePath.COLLECTION_SUPERVISE_PHOTO_PATH;
        takePhotoUtil = new TakePhotoUtil(this, this, savedInstanceState);

        adSuperviseDetailBean = (ADSuperviseDetailBean) getIntent().getSerializableExtra("adSuperviseDetailBean");
        pageBean = (ADSuperviseDetailPageBean) getIntent().getSerializableExtra("pageBean");
        adBean = (ADBean) getIntent().getSerializableExtra("adBean");

        adStyleList = new ArrayList<>();
        lawTypeList = new ArrayList<>();

        // 采购商列表
        consumerList = new ArrayList<>();
        consumerNameAdapter = new AutoCompleteTextViewAdapter(this, consumerList);
        consumerNameET.setAdapter(consumerNameAdapter);

        // 签约下拉列表
        stateList = new ArrayList<>();
        stateList.add("已签约");
        stateList.add("未签约");
        stateAdapter = new MyStringSpinnerAdapter(this, stateList);
        stateSpinner.setAdapter(stateAdapter);

        // 广告类别下拉列表
        adStyleList = new ArrayList<>();
        adStyleAdapter = new MySpinnerAdapter(this, adStyleList);
        adStyleSpinner.setAdapter(adStyleAdapter);

        // 画面分类下拉列表
        damageConList = new ArrayList<>();
        damageConAdapter = new MySpinnerAdapter(this, damageConList);
        damagedConSpinner.setAdapter(damageConAdapter);

        // 审核结果下拉列表
        checkList = new ArrayList<>();
        checkList.add("合法");
        checkList.add("不合法");
        checkAdapter = new MyStringSpinnerAdapter(this, checkList);
        checkSpinner.setAdapter(checkAdapter);

        // 法规分类下拉列表
        lawTypeList = new ArrayList<>();
        lawTypeAdapter = new MySpinnerAdapter(this, lawTypeList);
        lawTypeSpinner.setAdapter(lawTypeAdapter);

        // 法规编号下拉列表
        lawTypeDetailList = new ArrayList<>();
        lawCodeAdapter = new MySpinnerAdapter(this, lawTypeDetailList);
        lawCodeSpinner.setAdapter(lawCodeAdapter);

        provinceList = new ArrayList<>();
        provinceAdapter = new MyStringSpinnerAdapter(this, provinceList);
        provinceSpinner.setAdapter(provinceAdapter);

        cityList = new ArrayList<>();
        cityAdapter = new MyStringSpinnerAdapter(this, cityList);
        citySpinner.setAdapter(cityAdapter);

        districtList = new ArrayList<>();
        districtAdapter = new MyStringSpinnerAdapter(this, districtList);
        districtSpinner.setAdapter(districtAdapter);

//        regionLayout.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            obtainProvinceList();
            setRegionSpnnerValue(app.getUserLocationBean().getProvince(), app.getUserLocationBean().getCity(), app.getUserLocationBean().getDistrict());
        }
    }

    private void initListener() {
        returnBtn.setOnClickListener(this);
        regionSelectTV.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        takePhotoBtn.setOnClickListener(this);
        checkSpinner.setOnItemSelectedListener(this);
        lawTypeSpinner.setOnItemSelectedListener(this);
        lawCodeSpinner.setOnItemSelectedListener(this);
        consumerNameET.addTextChangedListener(this);
        consumerNameET.setOnItemClickListener(this);
    }

    // 获取广告类别
    private void obtainADType() {
        httpPost(ADTYPE_TYPE, new RequestParams(), Constant.HTTPUrl.AD_TYPE_URL);
    }

    // 获取法律法规分类
    private void obtainLawType() {
        httpPost(LAWTYPE_TYPE, new RequestParams(), Constant.HTTPUrl.LAW_TYPE_URL);
    }

    // 获取法律法规分类详情
    private void obtainLawTypeDetail(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("flfgflId", id);
        httpPost(LAWTYPEDETAIL_TYPE, requestParams, Constant.HTTPUrl.LAW_TYPE_DETAIL_URL);
    }

    // 获取画面分类
    private void obtainDamageCon() {
        httpPost(DAMAGECON_TYPE, new RequestParams(), Constant.HTTPUrl.DAMAGED_CON_RUL);
    }

    private void obtainConsumerName(String name) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", name);
        requestParams.put("leiBieId", 1);
        httpPost(CONSUMER_TYPE, requestParams, Constant.HTTPUrl.CONSUMER_URL);
    }

    @Override
    protected void onHttpFailure(String type, String json, String Err) {
        if (SUPERVISE_PAGE_TYPE.equals(type)) {
            httpProgressDialog.dismissWithFailure(Err);
        }
    }

    @Override
    protected void onHttpSuccess(String type, String message) {
        if (SUPERVISE_PAGE_TYPE.equals(type)) {
            sendBroadcast(new Intent(MediaDetailActivity.class.getName()).putExtra("pageBean", pageBean));
            httpProgressDialog.dismissWithSuccess(message);
            new MyHandler(1000) {
                @Override
                public void run() {
                    finish();
                }
            };
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        try {
            if (CONSUMER_TYPE.equals(type)) {
                consumerList.clear();
                consumerList.addAll(JSONUtil.toJavaBeanList(ConsumerBean.class, jsonArray));
                consumerNameAdapter.notifyDataSetChanged();
            } else if (ADTYPE_TYPE.equals(type)) {
                adStyleList.clear();
                adStyleList.addAll(JSONUtil.toJavaBeanList(ADTypeBean.class, jsonArray));
                adStyleAdapter.notifyDataSetChanged();
            } else if (LAWTYPE_TYPE.equals(type)) {
                lawTypeList.clear();
                lawTypeList.addAll(JSONUtil.toJavaBeanList(LawTypeBean.class, jsonArray));
                lawTypeAdapter.notifyDataSetChanged();
                if (lawTypeList.size() > 0)
                    obtainLawTypeDetail(lawTypeList.get(0).getId());
            } else if (LAWTYPEDETAIL_TYPE.equals(type)) {
                lawTypeDetailList.clear();
                lawTypeDetailList.addAll(JSONUtil.toJavaBeanList(LawTypeDetailBean.class, jsonArray));
                lawCodeAdapter.notifyDataSetChanged();
                if (lawTypeDetailList.size() > 0)
                    lawContentTV.setText(lawTypeDetailList.get(0).getXwbx());
            } else if (DAMAGECON_TYPE.equals(type)) {
                damageConList.clear();
                damageConList.addAll(JSONUtil.toJavaBeanList(DamageConBean.class, jsonArray));
                damageConAdapter.notifyDataSetChanged();
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
        switch (v.getId()) {
            case R.id.titlebar_left_imgbtn:
                finish();
                break;
            case R.id.titlebar_right_tv:
                Intent intent = new Intent(this, ChangeAddressActivity.class);
                if (userLocationBean == null) {
                    intent.putExtra("lat", adBean.getWeiDu());
                    intent.putExtra("lng", adBean.getJingDu());
                    intent.putExtra("address", adBean.getMtdz());
                } else {
                    intent.putExtra("lat", userLocationBean.getLat());
                    intent.putExtra("lng", userLocationBean.getLng());
                    intent.putExtra("address", userLocationBean.getAddress());
                }
                startActivityForResult(intent, CHANGE_ADDRESS);
                break;
            case R.id.supervisecheck_addbtn:
                String curContent = addResultTV.getText().toString();
                String bianHao = lawTypeDetailList.get(lawCodeAdapter.getSelectIndex()).getId();
                if (curContent.contains(bianHao)) {
                    toast.setText("此分类已经添加");
                } else {
                    addResultTV.setText(TextUtils.isEmpty(curContent) ? bianHao : (curContent + " " + bianHao));
                }
                break;
            case R.id.supervisecheck_confirmbtn:
                updatePage();
                break;
            case R.id.supervisecheck_takepicimg:
                if (PermissionRequestUtil.getInstance().requestPermission(this, PermissionRequestUtil.CAMERA, PermissionRequestUtil.READ_EXTERNAL_STORAGE)) {
                    takePhoto();
                }
                break;
        }
    }

    protected void updatePage() {
        try {
            RequestParams requestParams = new RequestParams();

            if (TextUtils.isEmpty(adContentET.getText())) {
                toast.setText("广告内容为空");
                return;
            }
            if (TextUtils.isEmpty(photoFileName)) {
                toast.setText("请先拍照");
                return;
            }
            File file = new File(photoFilePath, photoFileName);
            if (!file.exists()) {
                toast.setText("请先拍照");
                return;
            }

            if (!TextUtils.isEmpty(consumerNameET.getText().toString()))
                requestParams.put("kehuname", consumerNameET.getText().toString());
            if (!TextUtils.isEmpty(consumerPhoneET.getText().toString()))
                requestParams.put("kehuTel", consumerPhoneET.getText().toString());

            requestParams.put("id", pageBean.getId());
            requestParams.put("jcbgId", adSuperviseDetailBean.getId());
            requestParams.put("ggnr", adContentET.getText().toString());
            requestParams.put("status", checkAdapter.getSelectIndex());
            requestParams.put("gglb", adStyleAdapter.getSelectValue());
            requestParams.put("gglbId", adStyleList.get(adStyleAdapter.getSelectIndex()).getId());
            if (userLocationBean != null) {
                requestParams.put("jingDu", userLocationBean.getLng());
                requestParams.put("weiDu", userLocationBean.getLat());
                requestParams.put("xxdz", userLocationBean.getSimpleAdd());
            } else {
                requestParams.put("xxdz", adSuperviseDetailBean.getXxdz());
            }
            requestParams.put("sheng", provinceAdapter.getSelectValue());
            requestParams.put("shi", cityAdapter.getSelectValue());
            requestParams.put("qu", districtAdapter.getSelectValue());

            requestParams.put("hmfl", damageConAdapter.getSelectValue());
            requestParams.put("hmflId", damageConList.get(damageConAdapter.getSelectIndex()).getId());
            requestParams.put("bmStatus", stateAdapter.getSelectIndex() > 0 ? 0 : 1);
            if (checkAdapter.getSelectIndex() > 0)
                requestParams.put("jcbgFlfgId", addResultTV.getText().toString().replace(" ", "="));
            requestParams.put("file", new File(photoFilePath, photoFileName));

            httpPost(SUPERVISE_PAGE_TYPE, requestParams, Constant.HTTPUrl.UPDATE_SUPERVISE_PAGE_URL);
            httpProgressDialog.setMessage("正在保存监测报告...").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePhoto() {
        // 生成图片文件名
        photoFileName = System.currentTimeMillis() + ".png";
        takePhotoUtil.setFilePath(photoFilePath, photoFileName);
        takePhotoUtil.startTakePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                userLocationBean = (UserLocationBean) data.getSerializableExtra("userLocationBean");
                regionLayout.setVisibility(View.VISIBLE);
                setRegionSpnnerValue(userLocationBean.getProvince(), userLocationBean.getCity(), userLocationBean.getDistrict());
            }
        }
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestUtil.CAMERA) {
            // 获取摄像头权限结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
        } else {
            // 拒绝权限
            toast.setText(getString(R.string.cancel_camerapermission));
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
                    provinceSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                    citySpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                    districtSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
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
        switch (parent.getId()) {
            case R.id.supervisecheck_checkspinner:
                if (position == 0) {
                    lawLayout.setVisibility(View.GONE);
                    addBtn.setVisibility(View.GONE);
                } else {
                    lawLayout.setVisibility(View.VISIBLE);
                    addBtn.setVisibility(View.VISIBLE);
                }
                return;
            case R.id.supervisecheck_lawtypespinner:
                obtainLawTypeDetail(lawTypeList.get(lawTypeAdapter.getSelectIndex()).getId());
                return;
            case R.id.supervisecheck_lawcodespinner:
                lawContentTV.setText(lawTypeDetailList.get(lawCodeAdapter.getSelectIndex()).getXwbx());
                return;
        }

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
                provinceSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                citySpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                districtSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
            }
        };
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        if (!TextUtils.isEmpty(s.toString()))
            obtainConsumerName(s.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");
//        if (userLocationBean != null) {
//            regionLayout.setVisibility(View.VISIBLE);
//        } else {
//            regionLayout.setVisibility(View.GONE);
//        }
        adBean = (ADBean) savedInstanceState.getSerializable("adBean");

        photoFileName = savedInstanceState.getString("photoFileName");
        photoFilePath = savedInstanceState.getString("photoFilePath");

        adSuperviseDetailBean = (ADSuperviseDetailBean) savedInstanceState.getSerializable("adSuperviseDetailBean");
        pageBean = (ADSuperviseDetailPageBean) savedInstanceState.getSerializable("pageBean");

        consumerNameET.removeTextChangedListener(this);
        consumerNameET.setText(savedInstanceState.getString("consumerNameET"));
        consumerNameET.addTextChangedListener(this);

        lawContentTV.setText(savedInstanceState.getString("lawContentTV"));
        adContentET.setText(savedInstanceState.getString("adContentET"));
        addResultTV.setText(savedInstanceState.getString("addResultTV"));

        stateList.clear();
        adStyleList.clear();
        checkList.clear();
        lawTypeList.clear();
        lawTypeDetailList.clear();
        damageConList.clear();
        provinceList.clear();
        cityList.clear();
        districtList.clear();

        stateList.addAll(savedInstanceState.getStringArrayList("stateList"));
        adStyleList.addAll((ArrayList<ADTypeBean>) savedInstanceState.getSerializable("adStyleList"));
        checkList.addAll(savedInstanceState.getStringArrayList("checkList"));
        lawTypeList.addAll((ArrayList<LawTypeBean>) savedInstanceState.getSerializable("lawTypeList"));
        lawTypeDetailList.addAll((ArrayList<LawTypeDetailBean>) savedInstanceState.getSerializable("lawTypeDetailList"));
        damageConList.addAll((ArrayList<DamageConBean>) savedInstanceState.getSerializable("damageConList"));

        provinceList.addAll((ArrayList<String>) savedInstanceState.getSerializable("provinceList"));
        cityList.addAll((ArrayList<String>) savedInstanceState.getSerializable("cityList"));
        districtList.addAll((ArrayList<String>) savedInstanceState.getSerializable("districtList"));

        provinceAdapter.notifyDataSetChanged();
        cityAdapter.notifyDataSetChanged();
        districtAdapter.notifyDataSetChanged();

        stateAdapter.notifyDataSetChanged();
        adStyleAdapter.notifyDataSetChanged();
        checkAdapter.notifyDataSetChanged();
        lawTypeAdapter.notifyDataSetChanged();
        lawCodeAdapter.notifyDataSetChanged();
        damageConAdapter.notifyDataSetChanged();

        // 修改下拉列表的选中项
        stateSpinner.setSelection(savedInstanceState.getInt("stateSpinner"));
        adStyleSpinner.setSelection(savedInstanceState.getInt("adStyleSpinner"));
        provinceSpinner.setSelection(savedInstanceState.getInt("provinceIndex"));
        citySpinner.setSelection(savedInstanceState.getInt("cityIndex"));
        districtSpinner.setSelection(savedInstanceState.getInt("districtIndex"));

        checkSpinner.setOnItemSelectedListener(null);
        lawTypeSpinner.setOnItemSelectedListener(null);
        lawCodeSpinner.setOnItemSelectedListener(null);

        checkSpinner.setSelection(savedInstanceState.getInt("checkSpinner"));
        lawTypeSpinner.setSelection(savedInstanceState.getInt("lawTypeSpinner"));
        lawCodeSpinner.setSelection(savedInstanceState.getInt("lawCodeSpinner"));

        checkSpinner.setOnItemSelectedListener(this);
        lawTypeSpinner.setOnItemSelectedListener(this);
        lawCodeSpinner.setOnItemSelectedListener(this);

        provinceSpinner.setSelection(savedInstanceState.getInt("provinceIndex"));
        citySpinner.setSelection(savedInstanceState.getInt("cityIndex"));
        districtSpinner.setSelection(savedInstanceState.getInt("districtIndex"));

        new MyHandler(1000) {
            @Override
            public void run() {
                provinceSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                citySpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
                districtSpinner.setOnItemSelectedListener(ADSuperviseCheckActivity.this);
            }
        };

        if (savedInstanceState.getInt("checkSpinner") > 0) {
            lawLayout.setVisibility(View.VISIBLE);
        } else {
            lawLayout.setVisibility(View.GONE);
        }

        takePhotoUtil.handlePhoto(photoFilePath + photoFileName);

        // 文件不存在返回null
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoFilePath + photoFileName), 200, 150);
        if (bitmap != null)
            takePhotoIV.setImageBitmap(bitmap);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        bundle.putSerializable("userLocationBean", userLocationBean);
        bundle.putSerializable("adBean", adBean);

        bundle.putString("photoFileName", photoFileName);
        bundle.putString("photoFilePath", photoFilePath);
        bundle.putSerializable("adSuperviseDetailBean", adSuperviseDetailBean);
        bundle.putSerializable("pageBean", pageBean);
        bundle.putString("consumerNameET", consumerNameET.getText().toString());
        bundle.putString("lawContentTV", lawContentTV.getText().toString());
        bundle.putString("adContentET", adContentET.getText().toString());
        bundle.putString("addResultTV", addResultTV.getText().toString());

        bundle.putInt("stateSpinner", stateAdapter.getSelectIndex());
        bundle.putInt("adStyleSpinner", adStyleAdapter.getSelectIndex());
        bundle.putInt("checkSpinner", checkAdapter.getSelectIndex());
        bundle.putInt("lawTypeAdapter", lawTypeAdapter.getSelectIndex());
        bundle.putInt("lawCodeAdapter", lawCodeAdapter.getSelectIndex());
        bundle.putInt("damageConAdapter", damageConAdapter.getSelectIndex());

        bundle.putSerializable("provinceList", provinceList);
        bundle.putInt("provinceIndex", provinceAdapter.getSelectIndex());
        bundle.putSerializable("cityList", cityList);
        bundle.putInt("cityIndex", cityAdapter.getSelectIndex());
        bundle.putSerializable("districtList", districtList);
        bundle.putInt("districtIndex", districtAdapter.getSelectIndex());

        bundle.putStringArrayList("stateList", stateList);
        bundle.putSerializable("adStyleList", adStyleList);
        bundle.putStringArrayList("checkList", checkList);
        bundle.putSerializable("lawTypeList", lawTypeList);
        bundle.putSerializable("lawTypeDetailList", lawTypeDetailList);
        bundle.putSerializable("damageConList", damageConList);

        takePhotoUtil.onSaveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConsumerBean consumerBean = consumerList.get(position);
        if (consumerBean != null) {
            String phone = null;
            if (!TextUtils.isEmpty(consumerBean.getMobile())) {
                phone = consumerBean.getMobile();
            } else if (!TextUtils.isEmpty(consumerBean.getTel())) {
                phone = consumerBean.getTel();
            } else if (!TextUtils.isEmpty(consumerBean.getMobile1())) {
                phone = consumerBean.getMobile1();
            } else if (!TextUtils.isEmpty(consumerBean.getTel1())) {
                phone = consumerBean.getTel1();
            }
            consumerPhoneET.setText(phone);
        }
    }
}
