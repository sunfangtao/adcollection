package com.sft.adcollection.bean;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 */
public class LawTypeDetailBean extends BaseSpinnerVO {
    // 编号
    private String bianHao;
    // 分类名称
    private String fenlei;
    // 分类Id
    private String fenleiId;
    // ID
    private String id;
    // 认定依据
    private String rdyj;
    // 违法程度
    private String wfcd;
    // 行为表现
    private String xwbx;

    public String getBianHao() {
        return bianHao;
    }

    public void setBianHao(String bianHao) {
        this.bianHao = bianHao;
    }

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    public String getFenleiId() {
        return fenleiId;
    }

    public void setFenleiId(String fenleiId) {
        this.fenleiId = fenleiId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRdyj() {
        return rdyj;
    }

    public void setRdyj(String rdyj) {
        this.rdyj = rdyj;
    }

    public String getWfcd() {
        return wfcd;
    }

    public void setWfcd(String wfcd) {
        this.wfcd = wfcd;
    }

    public String getXwbx() {
        return xwbx;
    }

    public void setXwbx(String xwbx) {
        this.xwbx = xwbx;
    }

    @Override
    public String findSpinnerShow() {
        return bianHao;
    }
}
