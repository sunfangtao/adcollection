package com.sft.adcollection.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;

import java.io.File;

import cn.sft.baseactivity.util.MyHandler;

/**
 * 项目 adcollection
 * Created by SunFangtao on 2016/8/20.
 */
public class VideoRecordingActivity extends BaseActivity implements View.OnClickListener {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button recordbutton;
    private Button stopbutton;
    private MediaRecorder mediaRecorder;
    private Camera camera;
    private String fileName = System.currentTimeMillis() + ".mp4";
    private String filePath;
    private static final int MAX_TIME = 15 * 1000;
    private MyHandler recordStopHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_videorecording);
        initView();
        setListener();
    }

    private void initView() {
        recordbutton = (Button) this.findViewById(R.id.videorecording_recordbutton);
        stopbutton = (Button) this.findViewById(R.id.videorecording_stopbutton);
        surfaceView = (SurfaceView) findViewById(R.id.videorecording_surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceCallback());

        fileName = getIntent().getStringExtra("fileName");
        filePath = getIntent().getStringExtra("filePath");
    }

    private void setListener() {
        recordbutton.setOnClickListener(this);
        stopbutton.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videorecording_recordbutton:
                try {
                    if (TextUtils.isEmpty(util.getSDPath())) {
                        toast.setText(getString(R.string.videorecording_nosdcard));
                        return;
                    }
                    if (camera == null)
                        camera = Camera.open();
                    if (mediaRecorder == null) {
                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.setOnErrorListener(null);
                    }

                    camera.unlock();
                    mediaRecorder.setCamera(camera);

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                    mediaRecorder.setVideoSize(640, 480);// 设置分辨率：
                    mediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);

                    //多级目录没有创建可能会有问题
                    File file = new File(filePath);
                    if (!file.exists())
                        file.mkdirs();
                    File videoFile = new File(filePath, fileName);
                    if (!videoFile.exists()) {
                        videoFile.createNewFile();
                    }
                    mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();

                    if (recordStopHandler != null) {
                        recordStopHandler.cancle();
                        recordStopHandler = null;
                    }
                    //MAX_TIME时间后，自动停止录像
                    recordStopHandler = new MyHandler(MAX_TIME) {
                        @Override
                        public void run() {
                            stopbutton.performClick();
                        }
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recordbutton.setEnabled(false);
                new MyHandler(1000) {
                    @Override
                    public void run() {
                        stopbutton.setEnabled(true);
                    }
                };
                break;
            case R.id.videorecording_stopbutton:
                if (mediaRecorder != null) {
                    //设置后不会崩
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                recordbutton.setEnabled(true);
                stopbutton.setEnabled(false);
                Intent intent = new Intent();
                intent.putExtra("filePath", filePath);
                intent.putExtra("fileName", fileName);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }


    private final class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceHolder = holder;
            openCamera(holder);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            surfaceHolder = holder;
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
    }

    //打开Camera
    private void openCamera(SurfaceHolder holder) {
        try {
            if (camera == null)
                camera = Camera.open();//打开摄像头
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureFormat(256);
            parameters.setPreviewFrameRate(20);
            parameters.setJpegQuality(80);
            camera.setParameters(parameters);
            parameters.setPreviewSize(640, 480);
            parameters.setPictureSize(640, 480);
            camera.startPreview();//开始预览
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
