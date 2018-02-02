package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/25.
 */
public class ADBean extends DBVO {

    // 媒体ID
    private String id;
    // 媒体编号
    private String mtbh;
    // 纬度
    private double weiDu;
    // 经度
    private double jingDu;
    // 地址
    private String mtdz;
    // 媒体资料编号
    private String mtzlBianhao;

    private String cljgId;

    private String mtzlId;

    @Override
    public boolean equals(Object o) {
        if (o instanceof ADBean) {
            ADBean bean = (ADBean) o;
            if (id.equals(bean.id)) {
                return true;
            }
        }
        return false;
    }

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

    public double getWeiDu() {
        return weiDu;
    }

    public void setWeiDu(double weiDu) {
        this.weiDu = weiDu;
    }

    public double getJingDu() {
        return jingDu;
    }

    public void setJingDu(double jingDu) {
        this.jingDu = jingDu;
    }

    public String getMtdz() {
        return mtdz;
    }

    public void setMtdz(String mtdz) {
        this.mtdz = mtdz;
    }

    public String getMtzlBianhao() {
        return mtzlBianhao;
    }

    public void setMtzlBianhao(String mtzlBianhao) {
        this.mtzlBianhao = mtzlBianhao;
    }

    public String getCljgId() {
        return cljgId;
    }

    public void setCljgId(String cljgId) {
        this.cljgId = cljgId;
    }

    public String getMtzlId() {
        return mtzlId;
    }

    public void setMtzlId(String mtzlId) {
        this.mtzlId = mtzlId;
    }
}
