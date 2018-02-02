package com.sft.adcollection.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.ADBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.util.TTSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class GPSNaviActivity extends BaseActivity implements AMapNaviListener, AMapNaviViewListener {

    protected final List<NaviLatLng> eList = new ArrayList();
    protected AMapNavi mAMapNavi;
    protected AMapNaviView mAMapNaviView;
    protected NaviLatLng mEndLatlng = new NaviLatLng(39.925846D, 116.432765D);
    protected NaviLatLng mStartLatlng = new NaviLatLng(39.925041D, 116.437901D);
    protected TTSController mTtsManager;
    protected List<NaviLatLng> mWayPointList;
    protected final List<NaviLatLng> sList = new ArrayList();

    private UserLocationBean userLocationBean;
    private ADBean adBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_navi);
        this.mAMapNaviView = ((AMapNaviView) findViewById(R.id.navi_view));
        this.mAMapNaviView.onCreate(savedInstanceState);

        this.mTtsManager = TTSController.getInstance(getApplicationContext());
        this.mTtsManager.init();
        this.mTtsManager.startSpeaking();
        this.mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        this.mAMapNavi.addAMapNaviListener(this);
        this.mAMapNavi.addAMapNaviListener(this.mTtsManager);

        if (savedInstanceState == null) {
            userLocationBean = app.getUserLocationBean();
            adBean = (ADBean) getIntent().getSerializableExtra("adBean");
        } else {
            userLocationBean = (UserLocationBean) savedInstanceState.getSerializable("userLocationBean");
            adBean = (ADBean) savedInstanceState.getSerializable("adBean");
        }

        this.mStartLatlng = new NaviLatLng(userLocationBean.getLat(), userLocationBean.getLng());
        this.mEndLatlng = new NaviLatLng(adBean.getWeiDu(), adBean.getJingDu());

        this.sList.add(this.mStartLatlng);
        this.eList.add(this.mEndLatlng);

        this.mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("userLocationBean", userLocationBean);
        outState.putSerializable("adBean", adBean);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mAMapNaviView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mAMapNaviView.onDestroy();
        this.mAMapNavi.stopNavi();
        this.mAMapNavi.destroy();
        this.mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
        try {
            int j = this.mAMapNavi.strategyConvert(true, false, false, false, false);
            this.mAMapNavi.calculateDriveRoute(this.sList, this.eList, this.mWayPointList, j);
        } catch (Exception localException) {
        }
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        this.mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {
        util.print("导航页面加载成功");
        util.print("请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }
}
