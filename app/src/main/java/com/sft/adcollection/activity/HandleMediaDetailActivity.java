package com.sft.adcollection.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.adapter.HandleMediaDetailAdapter;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADBean;
import com.sft.adcollection.bean.ADHandleDetailBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.common.Constant;
import com.sft.adcollection.util.TakePhotoUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import cn.sft.baseactivity.util.JSONUtil;

/**
 * Created by Administrator on 2016/9/5.
 */
public class HandleMediaDetailActivity extends BaseActivity implements View.OnClickListener, TakePhoto.TakeResultListener {

    // 修改地址请求码
    private static final int CHANGE_ADDRESS_CODE = 0;

    // 获取案件详情
    private static final String HANDLE_DETAIL_TYPE = "handledetailtype";
    // 上传图片
    private static final String UPLOAD_PHOTO_TYPE = "uploadphototype";
    //
    private ImageButton returnBtn;
    //
    private TextView changeAddressTV;

    // 拍照工具
    private TakePhotoUtil takePhotoUtil;
    // 文件路径
    private String photoFilePath;
    // 文件名称
    private String photoFileName;

    //
    private TextView noPageTV;
    //
    private ListView pageListView;
    //
    private HandleMediaDetailAdapter handleMediaDetailAdapter;
    //
    private int index;
    //
    private ADBean adBean;
    //
    private ArrayList<ADHandleDetailBean> handleDetailList;
    //
    private UserLocationBean userLocationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_media_detail);
        initView();
        initData(savedInstanceState);
        initListener();
        if (savedInstanceState == null) {
            handleDetail();
        }
    }

    private void initView() {
        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        changeAddressTV = (TextView) findViewById(R.id.titlebar_right_tv);
        noPageTV = (TextView) findViewById(R.id.handlemedia_nopagetv);
        pageListView = (ListView) findViewById(R.id.handlemedia_pagelv);
    }

    private void initData(Bundle savedInstanceState) {
        index = -1;
        userLocationBean = (UserLocationBean) getIntent().getSerializableExtra("userLocationBean");

        ((TextView) findViewById(R.id.titlebar_title_tv)).setText("案件详情");
        takePhotoUtil = new TakePhotoUtil(this, this, savedInstanceState);
        photoFilePath = app.getBasePath() + Constant.FilePath.COLLECTION_HANDLE_PHOTO_PATH;

        adBean = (ADBean) getIntent().getSerializableExtra("adBean");
        handleDetailList = new ArrayList<>();
        handleMediaDetailAdapter = new HandleMediaDetailAdapter(this, handleDetailList);
        pageListView.setAdapter(handleMediaDetailAdapter);

        if (!app.getUserGrade().getGrade().equals("4")) {
            changeAddressTV.setVisibility(View.GONE);
        } else {
            changeAddressTV.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        returnBtn.setOnClickListener(this);
        changeAddressTV.setOnClickListener(this);
    }

    @Override
    public void forReceiverResult(Intent intent) {
        if (intent.getBooleanExtra("isTakePhoto", false)) {
            index = intent.getIntExtra("position", 0);
            takePhoto();
        } else if (intent.getBooleanExtra("isShowBigIm", false)) {
            Intent intent2 = new Intent(this, ShowBigImageActivity.class);
            intent2.putExtra("type", "net");
            intent2.putExtra("url", Constant.IPPORT + handleDetailList.get(intent.getIntExtra("position", 0)).getTbAjlbZj().getSrc());
            startActivity(intent2);
        }
        super.forReceiverResult(intent);
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
                intent.putExtra("lat", adBean.getWeiDu());
                intent.putExtra("lng", adBean.getJingDu());
                intent.putExtra("address", "无");
                startActivityForResult(intent, CHANGE_ADDRESS_CODE);
                break;
        }
    }

    private void takePhoto() {
        // 生成图片文件名
        photoFileName = System.currentTimeMillis() + ".png";
        takePhotoUtil.setFilePath(photoFilePath, photoFileName);
        takePhotoUtil.startTakePhoto();
    }

    private void uploadPhoto() {
        if (TextUtils.isEmpty(photoFileName)) {
            return;
        }
        if (index < 0 || index > handleDetailList.size() - 1) {
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", handleDetailList.get(index).getId());
        requestParams.put("file", new File(photoFilePath, photoFileName));
        httpPost(UPLOAD_PHOTO_TYPE, requestParams, Constant.HTTPUrl.UPLOAD_HANDLE_PHOTO_URL);
        httpProgressDialog.setMessage("正在上传照片...").show();
    }

    private void handleDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("mtzlBianhao", adBean.getMtzlBianhao());
        httpPost(HANDLE_DETAIL_TYPE, requestParams, Constant.HTTPUrl.HANDLE_DETAIL_URL);
    }

    @Override
    protected void onHttpSuccess(String type, String message) {
        if(UPLOAD_PHOTO_TYPE.equals(type)) {
            httpProgressDialog.dismissWithSuccess(message);
//            handleDetailList.remove(index);
//            handleMediaDetailAdapter.notifyDataSetChanged();
            index = -1;
            showView();
        }
    }

    @Override
    protected void onHttpFailure(String type, String json, String Err) {
        httpProgressDialog.dismissWithSuccess(Err);
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        if (HANDLE_DETAIL_TYPE.equals(type)) {
            handleDetailList.clear();
            handleDetailList.addAll(JSONUtil.toJavaBeanList(ADHandleDetailBean.class, jsonArray));

            for(int i = 0;i<handleDetailList.size();i++){
                if(TextUtils.isEmpty(handleDetailList.get(i).getMtzlBianhao())){
                    handleDetailList.get(i).setMtzlBianhao(adBean.getMtzlBianhao());
                }
            }
//            ArrayList<ADHandleDetailBean> tempList = new ArrayList<>();
//            for (int i = 0; i < handleDetailList.size(); i++) {
//                if (!handleDetailList.get(i).getJcqk().equals("已检测")) {
//                    tempList.add(handleDetailList.get(i));
//                }
//            }
//            handleDetailList.clear();
//            handleDetailList.addAll(tempList);

            showView();

            handleMediaDetailAdapter.notifyDataSetChanged();
        }
        super.onHttpSuccess(type, jsonArray);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CHANGE_ADDRESS_CODE) {
                userLocationBean = (UserLocationBean) data.getSerializableExtra("userLocationBean");
            }
        }
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showView() {
        if (handleDetailList.size() > 0) {
            pageListView.setVisibility(View.VISIBLE);
            noPageTV.setVisibility(View.GONE);
            if (app.getUserGrade().getGrade().equals("4")) {
                changeAddressTV.setVisibility(View.VISIBLE);
            } else {
                changeAddressTV.setVisibility(View.GONE);
            }
        } else {
            pageListView.setVisibility(View.GONE);
            noPageTV.setVisibility(View.VISIBLE);
            changeAddressTV.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        adBean = (ADBean) savedInstanceState.getSerializable("adBean");
        handleDetailList = (ArrayList<ADHandleDetailBean>) savedInstanceState.getSerializable("handleDetailList");
        userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");

        showView();

        index = savedInstanceState.getInt("index");

        photoFileName = savedInstanceState.getString("photoFileName");

        takePhotoUtil.handlePhoto(photoFilePath + photoFileName);
        if (index >= 0) {
            uploadPhoto();
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("photoFileName", photoFileName);
        bundle.putSerializable("adBean", adBean);
        bundle.putSerializable("userLocationBean", userLocationBean);
        bundle.putSerializable("handleDetailList", handleDetailList);
        bundle.putInt("index", index);

        takePhotoUtil.onSaveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
    }

    @Override
    public void takeSuccess(String imagePath) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 200, 150);
        uploadPhoto();
    }

    @Override
    public void takeFail(String msg) {
        index = -1;
    }

    @Override
    public void takeCancel() {
        index = -1;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
