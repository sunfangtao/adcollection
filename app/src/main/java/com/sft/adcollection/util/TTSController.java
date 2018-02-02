package com.sft.adcollection.util;

/**
 * Created by Administrator on 2016/8/21.
 */

import android.content.Context;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class TTSController implements SynthesizerListener, AMapNaviListener {
    public static TTSController ttsManager;
    boolean isfinish = true;
    private Context mContext;
    private SpeechSynthesizer mSpeechSynthesizer;

    TTSController(Context paramContext) {
        this.mContext = paramContext;
    }

    public static TTSController getInstance(Context paramContext) {
        if (ttsManager == null)
            ttsManager = new TTSController(paramContext);
        return ttsManager;
    }

    private void initSpeechSynthesizer() {
        this.mSpeechSynthesizer.setParameter("voice_name", "xiaoyan");
        this.mSpeechSynthesizer.setParameter("speed", "50");
        this.mSpeechSynthesizer.setParameter("volume", "50");
        this.mSpeechSynthesizer.setParameter("pitch", "50");
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo paramAMapNaviTrafficFacilityInfo) {
    }

    public void OnUpdateTrafficFacility(TrafficFacilityInfo paramTrafficFacilityInfo) {
    }

    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] paramArrayOfAMapNaviTrafficFacilityInfo) {
    }

    public void destroy() {
        if (this.mSpeechSynthesizer != null)
            this.mSpeechSynthesizer.stopSpeaking();
    }

    public void hideCross() {
    }

    public void hideLaneInfo() {
    }

    public void init() {
        SpeechUtility.createUtility(this.mContext, "appid=57b91e21");
        this.mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this.mContext, null);
        initSpeechSynthesizer();
    }

    public void notifyParallelRoad(int paramInt) {
    }

    public void onArriveDestination() {
        playText("到达目的地");
    }

    public void onArrivedWayPoint(int paramInt) {
    }

    public void onBufferProgress(int paramInt1, int paramInt2, int paramInt3, String paramString) {
    }

    public void onCalculateMultipleRoutesSuccess(int[] paramArrayOfInt) {
    }

    public void onCalculateRouteFailure(int paramInt) {
        playText("路径计算失败，请检查网络或输入参数");
    }

    public void onCalculateRouteSuccess() {
        playText("路径计算就绪");
    }

    public void onCompleted(SpeechError paramSpeechError) {
        this.isfinish = true;
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    public void onEndEmulatorNavi() {
        playText("导航结束");
    }

    public void onGetNavigationText(int paramInt, String paramString) {
        playText(paramString);
    }

    public void onGpsOpenStatus(boolean paramBoolean) {
    }

    public void onInitNaviFailure() {
    }

    public void onInitNaviSuccess() {
    }

    public void onLocationChange(AMapNaviLocation paramAMapNaviLocation) {
    }

    public void onNaviInfoUpdate(NaviInfo paramNaviInfo) {
    }

    public void onNaviInfoUpdated(AMapNaviInfo paramAMapNaviInfo) {
    }

    public void onReCalculateRouteForTrafficJam() {
        playText("前方路线拥堵，路线重新规划");
    }

    public void onReCalculateRouteForYaw() {
        playText("您已偏航");
    }

    public void onSpeakBegin() {
        this.isfinish = false;
    }

    public void onSpeakPaused() {
    }

    public void onSpeakProgress(int paramInt1, int paramInt2, int paramInt3) {
    }

    public void onSpeakResumed() {
    }

    public void onStartNavi(int paramInt) {
    }

    public void onTrafficStatusUpdate() {
    }

    public void playText(String paramString) {
        if (!this.isfinish)
            return;
        if (this.mSpeechSynthesizer == null) {
            this.mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this.mContext, null);
            initSpeechSynthesizer();
        }
        this.mSpeechSynthesizer.startSpeaking(paramString, this);
    }

    public void showCross(AMapNaviCross paramAMapNaviCross) {
    }

    public void showLaneInfo(AMapLaneInfo[] paramArrayOfAMapLaneInfo, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
    }

    public void startSpeaking() {
        this.isfinish = true;
    }

    public void stopSpeaking() {
        if (this.mSpeechSynthesizer != null)
            this.mSpeechSynthesizer.stopSpeaking();
    }

    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo paramAimLessModeCongestionInfo) {
    }

    public void updateAimlessModeStatistics(AimLessModeStat paramAimLessModeStat) {
    }
}
