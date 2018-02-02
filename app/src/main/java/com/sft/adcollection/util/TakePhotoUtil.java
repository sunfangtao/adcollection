package com.sft.adcollection.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.compress.CompressImage;
import com.jph.takephoto.compress.CompressImageImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.uitl.TUtils;

import java.io.File;

import cn.sft.baseactivity.util.FileUtils;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/23.
 */
public class TakePhotoUtil {

    private Activity activity;
    private TakePhoto takePhotoUtil;
    private String filePath = "";
    private String fileName = "";
    private CompressConfig compressConfig;
    private CropOptions cropOptions;
    private ProgressDialog wailLoadDialog;

    public static final int NO_FILE_PATH = 1;
    public static final int NO_FILE_NAME = 2;

    public TakePhotoUtil(Activity activity, TakePhoto.TakeResultListener listener, Bundle savedInstanceState) {
        this.activity = activity;
        takePhotoUtil = new TakePhotoImpl(activity, listener);
        takePhotoUtil.onCreate(savedInstanceState);
    }

    public void setFilePath(@NonNull String filePath, @NonNull String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public int startTakePhoto() {
        if (TextUtils.isEmpty(filePath)) {
            return NO_FILE_PATH;
        }
        if (TextUtils.isEmpty(fileName)) {
            return NO_FILE_NAME;
        }
        File pathFile = new File(filePath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        File file = new File(filePath, fileName); // 创建一个文件
        Uri imageFilePath = Uri.fromFile(file);
        cropOptions = new CropOptions.Builder().setAspectX(4).setAspectY(3).setWithOwnCrop(true).create();
        compressConfig = new CompressConfig.Builder().setMaxSize(100 * 1024).setMaxPixel(1000).create();
        takePhotoUtil.onEnableCompress(compressConfig, true).onPickFromCaptureWithCrop(imageFilePath, cropOptions);
        return 0;
    }

    public boolean handlePhoto(String file) {
        // 对图片进行剪裁
        if (FileUtils.getFileSize(file) > 100 * 1024) {
            // 如果图片大小大于1024，没有被压缩
            wailLoadDialog = TUtils.showProgressDialog(activity, "正在压缩照片...");
            new CompressImageImpl(compressConfig).compress(file, new CompressImage.CompressListener() {
                @Override
                public void onCompressSuccess(String imgPath) {
                    if (wailLoadDialog != null)
                        wailLoadDialog.dismiss();
                }

                @Override
                public void onCompressFailed(String imagePath, String msg) {
                    if (wailLoadDialog != null)
                        wailLoadDialog.dismiss();
                }
            });
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
    }

    public void onSaveInstanceState(Bundle bundle){
        takePhotoUtil.onSaveInstanceState(bundle);
    }
}
