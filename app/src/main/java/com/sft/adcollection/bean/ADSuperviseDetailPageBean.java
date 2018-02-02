package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 */
public class ADSuperviseDetailPageBean extends DBVO {

    // 检测报告版面编号
    private String id;
    // 版面
    private String bm;
    // 状态
    private String status;
    // 检测报告编号
    private String jcbgId;
    // 图片地址
    private String src;
    // 媒体规格
    private String mtgg;
    // 广告内容
    private String ggnr;
    // 广告类别
    private String gglb;
    // 广告类别 ID
    private String gglbId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJcbgId() {
        return jcbgId;
    }

    public void setJcbgId(String jcbgId) {
        this.jcbgId = jcbgId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMtgg() {
        return mtgg;
    }

    public void setMtgg(String mtgg) {
        this.mtgg = mtgg;
    }

    public String getGgnr() {
        return ggnr;
    }

    public void setGgnr(String ggnr) {
        this.ggnr = ggnr;
    }

    public String getGglb() {
        return gglb;
    }

    public void setGglb(String gglb) {
        this.gglb = gglb;
    }

    public String getGglbId() {
        return gglbId;
    }

    public void setGglbId(String gglbId) {
        this.gglbId = gglbId;
    }
}
