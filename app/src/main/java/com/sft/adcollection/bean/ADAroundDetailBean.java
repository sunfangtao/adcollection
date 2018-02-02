package com.sft.adcollection.bean;

import java.util.Arrays;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ADAroundDetailBean extends DBVO {
    // 媒体ID
    private String id;
    // 媒体编号
    private String mtbh;
    //
    private String image;
    // 区域属性
    private String qysxId;
    // 媒体属性
    private String mtsxId;
    // 经度
    private double jingDu;
    // 维度
    private double weiDu;
    // 经度度
    private double jdDu;
    // 经度分
    private double jdFen;
    // 经度秒
    private double jdMiao;
    // 维度度
    private double wdDu;
    // 维度分
    private double wdFen;
    // 维度秒
    private double wdMiao;
    //
    private String createid;
    //
    private String wanshanid;
    //
    private String uuuuid;
    // 照明状态（0无照明，1有照明）
    private String zmzt;
    // 媒体分类
    private String mtfl;
    // 媒体地址
    private String mtdz;
    // 省
    private String shengId;
    // 市
    private String shiId;
    // 区
    private String quId;
    // 用户ID
    private String userId;
    // 用户名称
    private String userName;
    // 媒体分类ID
    private String mtflId;
    // 区域属性
    private String qysx;
    // 媒体规格
    private String mtgg;
    // 媒体属性
    private String mtsx;
    // 媒体状态
    private String mtzt;
    // 省
    private String sheng;
    // 市
    private String shi;
    // 区
    private String qu;
    // 客户名字
    private String kehuName;
    // 媒体形式ID
    private String mtxsId;
    // 城管编号
    private String cgbh;
    // 媒体形式
    private String mtxs;
    // 签约状态(0空置，1签约)
    private String qyzt;
    // 交通编号
    private String szbh;
    // 机构名称
    private String jgmc;
    // 视频
    private String shiPin;
    // 机构电话
    private String jgTel;
    // 版面违法id
    private List<String> bmIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMtbh() {
        return mtbh;
    }

    public void setMtbh(String mtbh) {
        this.mtbh = mtbh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQysxId() {
        return qysxId;
    }

    public void setQysxId(String qysxId) {
        this.qysxId = qysxId;
    }

    public String getMtsxId() {
        return mtsxId;
    }

    public void setMtsxId(String mtsxId) {
        this.mtsxId = mtsxId;
    }

    public double getJingDu() {
        return jingDu;
    }

    public void setJingDu(double jingDu) {
        this.jingDu = jingDu;
    }

    public double getWeiDu() {
        return weiDu;
    }

    public void setWeiDu(double weiDu) {
        this.weiDu = weiDu;
    }

    public double getJdDu() {
        return jdDu;
    }

    public void setJdDu(double jdDu) {
        this.jdDu = jdDu;
    }

    public double getJdFen() {
        return jdFen;
    }

    public void setJdFen(double jdFen) {
        this.jdFen = jdFen;
    }

    public double getJdMiao() {
        return jdMiao;
    }

    public void setJdMiao(double jdMiao) {
        this.jdMiao = jdMiao;
    }

    public double getWdDu() {
        return wdDu;
    }

    public void setWdDu(double wdDu) {
        this.wdDu = wdDu;
    }

    public double getWdFen() {
        return wdFen;
    }

    public void setWdFen(double wdFen) {
        this.wdFen = wdFen;
    }

    public double getWdMiao() {
        return wdMiao;
    }

    public void setWdMiao(double wdMiao) {
        this.wdMiao = wdMiao;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getWanshanid() {
        return wanshanid;
    }

    public void setWanshanid(String wanshanid) {
        this.wanshanid = wanshanid;
    }

    public String getUuuuid() {
        return uuuuid;
    }

    public void setUuuuid(String uuuuid) {
        this.uuuuid = uuuuid;
    }

    public String getZmzt() {
        return zmzt;
    }

    public void setZmzt(String zmzt) {
        this.zmzt = zmzt;
    }

    public String getMtfl() {
        return mtfl;
    }

    public void setMtfl(String mtfl) {
        this.mtfl = mtfl;
    }

    public String getMtdz() {
        return mtdz;
    }

    public void setMtdz(String mtdz) {
        this.mtdz = mtdz;
    }

    public String getShengId() {
        return shengId;
    }

    public void setShengId(String shengId) {
        this.shengId = shengId;
    }

    public String getShiId() {
        return shiId;
    }

    public void setShiId(String shiId) {
        this.shiId = shiId;
    }

    public String getQuId() {
        return quId;
    }

    public void setQuId(String quId) {
        this.quId = quId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMtflId() {
        return mtflId;
    }

    public void setMtflId(String mtflId) {
        this.mtflId = mtflId;
    }

    public String getQysx() {
        return qysx;
    }

    public void setQysx(String qysx) {
        this.qysx = qysx;
    }

    public String getMtgg() {
        return mtgg;
    }

    public void setMtgg(String mtgg) {
        this.mtgg = mtgg;
    }

    public String getMtsx() {
        return mtsx;
    }

    public void setMtsx(String mtsx) {
        this.mtsx = mtsx;
    }

    public String getMtzt() {
        return mtzt;
    }

    public void setMtzt(String mtzt) {
        this.mtzt = mtzt;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getKehuName() {
        return kehuName;
    }

    public void setKehuName(String kehuName) {
        this.kehuName = kehuName;
    }

    public String getMtxsId() {
        return mtxsId;
    }

    public void setMtxsId(String mtxsId) {
        this.mtxsId = mtxsId;
    }

    public String getCgbh() {
        return cgbh;
    }

    public void setCgbh(String cgbh) {
        this.cgbh = cgbh;
    }

    public String getMtxs() {
        return mtxs;
    }

    public void setMtxs(String mtxs) {
        this.mtxs = mtxs;
    }

    public String getQyzt() {
        return qyzt;
    }

    public void setQyzt(String qyzt) {
        this.qyzt = qyzt;
    }

    public String getSzbh() {
        return szbh;
    }

    public void setSzbh(String szbh) {
        this.szbh = szbh;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getShiPin() {
        return shiPin;
    }

    public void setShiPin(String shiPin) {
        this.shiPin = shiPin;
    }

    public String getJgTel() {
        return jgTel;
    }

    public void setJgTel(String jgTel) {
        this.jgTel = jgTel;
    }

    public List<String> getBmIds() {
        return bmIds;
    }

    public void setBmIds(String bmIds) {
        this.bmIds = Arrays.asList(bmIds.replace("[", "").replace("]", "").split(","));
    }
}
