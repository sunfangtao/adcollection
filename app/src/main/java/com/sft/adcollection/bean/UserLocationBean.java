package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2016/8/21.
 * 用户定位的信息
 */
public class UserLocationBean extends DBVO {

    private double lat;
    private double lng;
    private String address;
    private String province;
    private String city;
    private String district;
    private String region;

    public UserLocationBean() {

    }

    public UserLocationBean(UserLocationBean userLocationBean) {
        this.lat = userLocationBean.getLat();
        this.lng = userLocationBean.getLng();
        this.address = userLocationBean.getAddress();
        this.province = userLocationBean.getProvince();
        this.city = userLocationBean.getCity();
        this.district = userLocationBean.getDistrict();
        this.region = userLocationBean.getRegion();
    }

    @Override
    public String toString() {
        return "UserLocationBean{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    public String getSimpleAdd() {
        return address.replace(region, "");
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
