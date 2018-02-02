package com.sft.adcollection.activity;

import android.os.Bundle;
import android.util.Base64;

import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.util.TakePhotoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/24.
 */
public class VideoToStringActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TakePhotoUtil takePhotoUtil = new TakePhotoUtil(this, null, null);
        takePhotoUtil.handlePhoto(util.getSDPath() + File.separator + "1" + File.separator + "1.jpg");
        takePhotoUtil.handlePhoto(util.getSDPath() + File.separator + "1" + File.separator + "2.jpg");
    }

    public String encodeFile(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    public void decodeFile(String base64Code, String savePath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }
}
