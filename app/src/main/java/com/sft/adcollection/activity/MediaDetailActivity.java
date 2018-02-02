package com.sft.adcollection.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADAroundDetailBean;
import com.sft.adcollection.bean.ADBean;
import com.sft.adcollection.bean.ADSuperviseDetailBean;
import com.sft.adcollection.bean.ADSuperviseDetailPageBean;
import com.sft.adcollection.common.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sft.baseactivity.util.JSONUtil;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/26.
 */
public class MediaDetailActivity extends BaseActivity {

    //
    private ImageButton returnBtn;
    //媒体编号
    private TextView mediaCodeTV;
    //媒体形式
    private TextView mediaTypeTV;
    //区域属性
    private TextView areaAttributeTV;
    //媒体位置
    private TextView mediaAddressTV;
    //行政区域
    private TextView mediaregionTV;
    //机构名称
    private TextView organizeNameTV;
    //机构电话
    private TextView organizePhoneTV;
    //交通编号
    private TextView trafficNumberTV;
    //城管编号
    private TextView urbanManagerNumberTV;
    // 照片
    private ImageView mediaPhotoIM;
    // 版面布局
    private LinearLayout pageLayout;

    // 媒体信息
    private ADBean adBean;
    // 广告媒体详情
    private ADAroundDetailBean adAroundDetailBean;
    // 检测媒体详情
    private ADSuperviseDetailBean adSuperviseDetailBean;
    // 检测媒体版面详情
    private ArrayList<ADSuperviseDetailPageBean> adSuperviseDetailPageList;
    //
    private String imageUrl;

    // 页面id
    private int pageIndex;

    private static final String MEDIA_AROUND_DETAIL_TYPE = "mediaarounddetail";
    private static final String MEDIA_SUPERVISE_DETAIL_IMG_TYPE = "mediasupervisedetailimg";
    private static final String MEDIA_SUPERVISE_DETAIL_TYPE = "mediasupervisedetail";
    private static final String MEDIA_SUPERVISE_DETAIL_PAGE_TYPE = "mediasupervisepagedetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);
        initView();
        initData();
        initListener();
        resizeLayout();
        if (savedInstanceState == null) {
            obtainMediaDetail();
        }
    }

    private void initView() {
        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        mediaCodeTV = (TextView) findViewById(R.id.mediadetail_codetv);
        mediaTypeTV = (TextView) findViewById(R.id.mediadetail_typetv);
        areaAttributeTV = (TextView) findViewById(R.id.mediadetail_attributetv);
        mediaAddressTV = (TextView) findViewById(R.id.mediadetail_addresstv);
        mediaregionTV = (TextView) findViewById(R.id.mediadetail_regiontv);
        organizeNameTV = (TextView) findViewById(R.id.mediadetail_nametv);
        organizePhoneTV = (TextView) findViewById(R.id.mediadetail_phonetv);
        trafficNumberTV = (TextView) findViewById(R.id.mediadetail_trafficnumbertv);
        urbanManagerNumberTV = (TextView) findViewById(R.id.mediadetail_urbanmanagernumbertv);
        mediaPhotoIM = (ImageView) findViewById(R.id.mediadetail_picim);
        pageLayout = (LinearLayout) findViewById(R.id.mediadetail_pagelayout);
    }

    private void initData() {
        adBean = (ADBean) getIntent().getSerializableExtra("adBean");
        pageIndex = getIntent().getIntExtra("pageIndex", 0);
    }

    private void initListener() {
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mediaPhotoIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageIndex) {
                    case R.id.map_adbtn:
                        if (adAroundDetailBean != null && !TextUtils.isEmpty(adAroundDetailBean.getImage())) {
                            Intent intent = new Intent(MediaDetailActivity.this, ShowBigImageActivity.class);
                            intent.putExtra("url", Constant.IPPORT + adAroundDetailBean.getImage());
                            intent.putExtra("type", "net");
                            startActivity(intent);
                        }
                        break;
                    case R.id.map_surveybtn:
                        if (adSuperviseDetailBean != null && !TextUtils.isEmpty(adSuperviseDetailBean.getImageUrl())) {
                            Intent intent = new Intent(MediaDetailActivity.this, ShowBigImageActivity.class);
                            intent.putExtra("url", Constant.IPPORT + adSuperviseDetailBean.getImageUrl());
                            intent.putExtra("type", "net");
                            startActivity(intent);
                        }
                        break;
                }

            }
        });
    }

    private void resizeLayout() {
        ViewTreeObserver vto = mediaPhotoIM.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mediaPhotoIM.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mediaPhotoIM.getMeasuredWidth();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mediaPhotoIM.getLayoutParams();
                params.width = width;
                params.height = width * 3 / 4;
                mediaPhotoIM.setLayoutParams(params);
            }
        });
    }

    private void obtainMediaDetail() {
        RequestParams requestParams = new RequestParams();
        switch (pageIndex) {
            case R.id.map_adbtn:
                requestParams.put("id", adBean.getId());
                httpPost(MEDIA_AROUND_DETAIL_TYPE, requestParams, Constant.HTTPUrl.MEDIA_AROUND_DETAIL_URL);
                break;
            case R.id.map_surveybtn:
                requestParams.put("id", adBean.getId());
                httpPost(MEDIA_SUPERVISE_DETAIL_TYPE, requestParams, Constant.HTTPUrl.MEDIA_SUPERVISE_DETAIL_URL);
                requestParams.remove("id");
                requestParams.put("jcbgId", adBean.getId());
                httpPost(MEDIA_SUPERVISE_DETAIL_PAGE_TYPE, requestParams, Constant.HTTPUrl.MEDIA_SUPERVISE_DETAIL_PAGE_URL);
                break;
        }

    }

    @Override
    protected void onHttpFailure(String type, String json, String Err) {
        if (MEDIA_SUPERVISE_DETAIL_IMG_TYPE.equals(type)) {
            toast.setText(Err);
        } else if (MEDIA_SUPERVISE_DETAIL_TYPE.equals(type)) {
            toast.setText(Err);
        } else if (MEDIA_SUPERVISE_DETAIL_PAGE_TYPE.equals(type)) {
            toast.setText(Err);
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        try {
            if (MEDIA_SUPERVISE_DETAIL_PAGE_TYPE.equals(type)) {
                adSuperviseDetailPageList = JSONUtil.toJavaBeanList(ADSuperviseDetailPageBean.class, jsonArray);
                fillData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONObject jsonObject) {
        try {
            if (MEDIA_AROUND_DETAIL_TYPE.equals(type)) {
                adAroundDetailBean = JSONUtil.toJavaBean(ADAroundDetailBean.class, jsonObject);
                fillData();
            } else if (MEDIA_SUPERVISE_DETAIL_TYPE.equals(type)) {
                adSuperviseDetailBean = JSONUtil.toJavaBean(ADSuperviseDetailBean.class, jsonObject);
                fillData();

                RequestParams requestParams = new RequestParams();
                requestParams.put("id", adSuperviseDetailBean.getMtzlId());
                httpPost(MEDIA_SUPERVISE_DETAIL_IMG_TYPE, requestParams, Constant.HTTPUrl.MEDIA_AROUND_DETAIL_URL);
            } else if (MEDIA_SUPERVISE_DETAIL_IMG_TYPE.equals(type)) {
                imageUrl = jsonObject.getString("image");
                if (!TextUtils.isEmpty(imageUrl)) {
                    Picasso.with(this).load(Constant.IPPORT + imageUrl).into(mediaPhotoIM);
                } else {
                    Picasso.with(this).load(R.drawable.nophoto).into(mediaPhotoIM);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData() {
        fillAroundData();
        fillSuperviseData();
        fillSupervisePageData();
    }

    private void fillAroundData() {
        if (adAroundDetailBean == null) return;
        mediaCodeTV.setText(getString(R.string.mediacode) + adAroundDetailBean.getMtbh());
        mediaTypeTV.setText(getString(R.string.mediatype) + adAroundDetailBean.getMtxs());
        areaAttributeTV.setText(getString(R.string.areaattribute) + adAroundDetailBean.getQysx());
        mediaAddressTV.setText(getString(R.string.mediaaddress) + adAroundDetailBean.getMtdz());
        mediaregionTV.setText(getString(R.string.mediaregion) + adAroundDetailBean.getSheng() + adAroundDetailBean.getShi() + adAroundDetailBean.getQu());
        organizeNameTV.setText(getString(R.string.organizename) + adAroundDetailBean.getJgmc());
        organizePhoneTV.setText(getString(R.string.organizephone) + adAroundDetailBean.getJgTel());
        trafficNumberTV.setText(getString(R.string.trafficnumber) + adAroundDetailBean.getSzbh());
        urbanManagerNumberTV.setText(getString(R.string.urbanmanagernumber) + adAroundDetailBean.getCgbh());
        if (!TextUtils.isEmpty(adAroundDetailBean.getImage())) {
            Picasso.with(this).load(Constant.IPPORT + adAroundDetailBean.getImage()).into(mediaPhotoIM);
        } else {
            Picasso.with(this).load(R.drawable.nophoto).into(mediaPhotoIM);
        }
    }

    private void fillSuperviseData() {
        if (adSuperviseDetailBean == null) return;
        mediaCodeTV.setText(getString(R.string.mediacode) + adSuperviseDetailBean.getMtzlBianhao());
        mediaTypeTV.setText(getString(R.string.mediatype) + adSuperviseDetailBean.getMtxs());
        areaAttributeTV.setText(getString(R.string.areaattribute) + adSuperviseDetailBean.getQysx());
        mediaAddressTV.setText(getString(R.string.mediaaddress) + adSuperviseDetailBean.getXxdz());
        mediaregionTV.setText(getString(R.string.mediaregion) + adSuperviseDetailBean.getSheng() + adSuperviseDetailBean.getShi() + adSuperviseDetailBean.getQu());
        organizeNameTV.setText(getString(R.string.organizename) + adSuperviseDetailBean.getJgName());
        organizePhoneTV.setText(getString(R.string.organizephone) + adSuperviseDetailBean.getJgTel());
        trafficNumberTV.setText(getString(R.string.trafficnumber) + adSuperviseDetailBean.getSzbh());
        urbanManagerNumberTV.setText(getString(R.string.urbanmanagernumber) + adSuperviseDetailBean.getCgbh());
    }

    private void fillSupervisePageData() {
        pageLayout.removeAllViews();
        if (adSuperviseDetailPageList != null) {
            int length = adSuperviseDetailPageList.size();
            if (length == 0) {
                final Button button = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, util.dp2px(this, 40));
                params.gravity = Gravity.CENTER;
                button.setLayoutParams(params);
                button.setText("暂无版面信息");
                button.setTextColor(Color.BLACK);
                button.setBackgroundResource(R.color.uicolor_lightblue);
                pageLayout.addView(button);
            }
            for (int i = 0; i < length; i++) {
                final ADSuperviseDetailPageBean pageBean = adSuperviseDetailPageList.get(i);
                final Button button = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, util.dp2px(this, 40));
                params.gravity = Gravity.CENTER;
                button.setLayoutParams(params);
                button.setTag(pageBean.getId());
                button.setText("版面" + pageBean.getBm());
                button.setTextColor(Color.BLACK);
                button.setBackgroundResource(R.color.uicolor_lightblue);
                pageLayout.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaDetailActivity.this, ADSuperviseCheckActivity.class);
                        intent.putExtra("adSuperviseDetailBean", adSuperviseDetailBean);
                        intent.putExtra("adBean", adBean);
                        intent.putExtra("pageBean", pageBean);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void forReceiverResult(Intent intent) {
        int length = pageLayout.getChildCount();
        ADSuperviseDetailPageBean pageBean = (ADSuperviseDetailPageBean) intent.getSerializableExtra("pageBean");
        if (length == 1) {
            sendBroadcast(new Intent(MapActivity.class.getName()).putExtra("isRefreshSuperviseAD", true));
            finish();
            return;
        }
        for (int i = 0; i < pageLayout.getChildCount(); i++) {
            View view = pageLayout.getChildAt(i);
            if (view.getTag().equals(pageBean.getId())) {
                pageLayout.removeViewAt(i);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // 恢复保存的数据
            pageIndex = savedInstanceState.getInt("pageIndex");
            adBean = (ADBean) savedInstanceState.getSerializable("adBean");
            adAroundDetailBean = (ADAroundDetailBean) savedInstanceState.getSerializable("adAroundDetailBean");
            adSuperviseDetailBean = (ADSuperviseDetailBean) savedInstanceState.getSerializable("adSuperviseDetailBean");
            adSuperviseDetailPageList = (ArrayList<ADSuperviseDetailPageBean>) savedInstanceState.getSerializable("adSuperviseDetailPageList");
            mediaCodeTV.setText(savedInstanceState.getString("mediaCodeTV"));
            mediaTypeTV.setText(savedInstanceState.getString("mediaTypeTV"));
            areaAttributeTV.setText(savedInstanceState.getString("areaAttributeTV"));
            mediaAddressTV.setText(savedInstanceState.getString("mediaAddressTV"));
            mediaregionTV.setText(savedInstanceState.getString("mediaregionTV"));
            organizeNameTV.setText(savedInstanceState.getString("organizeNameTV"));
            organizePhoneTV.setText(savedInstanceState.getString("organizePhoneTV"));
            trafficNumberTV.setText(savedInstanceState.getString("trafficNumberTV"));
            urbanManagerNumberTV.setText(savedInstanceState.getString("urbanManagerNumberTV"));
            imageUrl = savedInstanceState.getString("imageUrl");
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.with(this).load(Constant.IPPORT + imageUrl).into(mediaPhotoIM);
                mediaPhotoIM.setVisibility(View.VISIBLE);
            }
            fillData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("adBean", adBean);
        outState.putInt("pageIndex", pageIndex);
        outState.putSerializable("adAroundDetailBean", adAroundDetailBean);
        outState.putSerializable("adSuperviseDetailBean", adSuperviseDetailBean);
        outState.putSerializable("adSuperviseDetailPageList", adSuperviseDetailPageList);
        outState.putString("mediaCodeTV", mediaCodeTV.getText().toString());
        outState.putString("mediaTypeTV", mediaTypeTV.getText().toString());
        outState.putString("areaAttributeTV", areaAttributeTV.getText().toString());
        outState.putString("mediaAddressTV", mediaAddressTV.getText().toString());
        outState.putString("mediaregionTV", mediaregionTV.getText().toString());
        outState.putString("organizeNameTV", organizeNameTV.getText().toString());
        outState.putString("organizePhoneTV", organizePhoneTV.getText().toString());
        outState.putString("trafficNumberTV", trafficNumberTV.getText().toString());
        outState.putString("urbanManagerNumberTV", urbanManagerNumberTV.getText().toString());
        outState.putString("imageUrl", imageUrl);
        super.onSaveInstanceState(outState);
    }

}
