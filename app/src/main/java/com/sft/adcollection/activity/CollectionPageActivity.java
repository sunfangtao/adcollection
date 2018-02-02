package com.sft.adcollection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.common.Constant;

import java.io.File;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/9/3.
 */
public class CollectionPageActivity extends ADSuperviseCheckActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        regionSelectTV.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.titlebar_title_tv)).setText("添加版面");
        photoFilePath = app.getBasePath() + Constant.FilePath.COLLECTION_PAGE_PHOTO_PATH;
    }

    @Override
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
                requestParams.put("kehuName", consumerNameET.getText().toString());
            if (!TextUtils.isEmpty(consumerPhoneET.getText().toString()))
                requestParams.put("kehuTel", consumerPhoneET.getText().toString());

            requestParams.put("ggnr", adContentET.getText().toString());
            requestParams.put("csjg", checkAdapter.getSelectIndex());
            requestParams.put("gglb", adStyleAdapter.getSelectValue());
            requestParams.put("gglbId", adStyleList.get(adStyleAdapter.getSelectIndex()).getId());
            if (userLocationBean != null) {
                requestParams.put("jingDu", userLocationBean.getLng());
                requestParams.put("weiDu", userLocationBean.getLat());
                requestParams.put("xxdz", userLocationBean.getSimpleAdd());
            }
            requestParams.put("hmfl", damageConAdapter.getSelectValue());
            requestParams.put("hmflId", damageConList.get(damageConAdapter.getSelectIndex()).getId());
            requestParams.put("bmStatus", stateAdapter.getSelectIndex() > 0 ? 0 : 1);
            if (checkAdapter.getSelectIndex() > 0)
                requestParams.put("ids", addResultTV.getText().toString().replace(" ", "="));
            requestParams.put("file", new File(photoFilePath, photoFileName));

            Intent intent = new Intent();
            intent.putExtra("pageRequestParams", requestParams);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
