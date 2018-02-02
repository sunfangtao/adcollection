package com.sft.adcollection.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunFangtao on 2016/8/9.
 */
public class LocationUtil {
    private Context context;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;

    private List<LocationListener> locationListeners = new ArrayList<>();

    public interface LocationListener {
        void onLocationChanged(AMapLocation aMapLocation);
    }

    public LocationUtil(Context context) {
        this.context = context;
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //初始化定位回调监听
        mLocationListener = new MyAMapLocationListener();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 配置定位参数
        initLocation();
    }

    private void initLocation() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private class MyOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {

        private AMapLocation aMapLocation;

        public MyOnGeocodeSearchListener(AMapLocation aMapLocation) {
            this.aMapLocation = aMapLocation;
        }

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            aMapLocation.setAddress(regeocodeAddress.getFormatAddress());
            aMapLocation.setProvince(regeocodeAddress.getProvince());
            aMapLocation.setCity(regeocodeAddress.getCity());
            aMapLocation.setDistrict(regeocodeAddress.getDistrict());

            for (LocationListener locationListener : locationListeners) {
                locationListener.onLocationChanged(aMapLocation);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    }

    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (aMapLocation.getLocationType() == 1) {
                        //GPS定位
                        LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 3000, GeocodeSearch.AMAP);
                        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
                        geocodeSearch.setOnGeocodeSearchListener(new MyOnGeocodeSearchListener(aMapLocation));
                        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
                    } else {
                        for (LocationListener locationListener : locationListeners) {
                            locationListener.onLocationChanged(aMapLocation);
                        }
                    }
                } else {
                    //定位失败
                    for (LocationListener locationListener : locationListeners) {
                        locationListener.onLocationChanged(null);
                    }
                }
            }
        }
    }

    public void addLocationListener(LocationListener locationListener) {
        locationListeners.add(locationListener);
    }

    public void removeLocationListener(LocationListener locationListener) {
        locationListeners.remove(locationListener);
    }

    public void startLocation() {
        //启动定位
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        //停止定位
        mLocationClient.stopLocation();
    }

    public void onDestory() {
        mLocationClient.onDestroy();
    }

}
