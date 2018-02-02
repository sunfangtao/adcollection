package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/27.
 */
public class ADSuperviseDetailBean extends DBVO {

    // 检测报告ID
    private String id;
    // 媒体资料
    private String mtzlId;
    // 媒体资料编号
    private String mtzlBianhao;
    // 设置编号
    private String szbh;
    // 媒体类别
    private String mtlb;
    // 媒体属性
    private String mtsx;
    // 媒体形式
    private String mtxs;
    // 区域属性
    private String qysx;
    // 省ID
    private int shengId;
    // 市ID
    private int shiId;
    // 区ID
    private int quId;
    // 省名称
    private String sheng;
    // 市名称
    private String shi;
    // 区名称
    private String qu;
    // 详细地址
    private String xxdz;
    // 违法依据
    private String wfyj;
    // 经度
    private double jingDu;
    // 维度
    private double weiDu;
    // 版面
    private String bm;
    // 媒体规格
    private String mtgg;
    // 图片地址
    private String imageUrl;
    // 画面分类
    private String hmfl;
    // 监测状态
    private String jczt;
    // 初审人员
    private String chuShenRen;
    // 复审人
    private String fuShenRen;
    // 复审结果
    private String fuShenJieGuo;
    // 终审结果
    private String zhongShenJieGuo;
    // 检测限定日期
    private String jcxdrq;
    // 机构ID
    private String jgId;
    // 机构名称
    private String jgName;
    // 机构电话
    private String jgTel;
    //审核状态0初审1复审2终审
    private String shzt;
    // 城管编号
    private String cgbh;
    // 客户名称
    private String kehuName;
    // 客户电话
    private String kehuTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMtzlId() {
        return mtzlId;
    }

    public void setMtzlId(String mtzlId) {
        this.mtzlId = mtzlId;
    }

    public String getMtzlBianhao() {
        return mtzlBianhao;
    }

    public void setMtzlBianhao(String mtzlBianhao) {
        this.mtzlBianhao = mtzlBianhao;
    }

    public String getSzbh() {
        return szbh;
    }

    public void setSzbh(String szbh) {
        this.szbh = szbh;
    }

    public String getMtlb() {
        return mtlb;
    }

    public void setMtlb(String mtlb) {
        this.mtlb = mtlb;
    }

    public String getMtsx() {
        return mtsx;
    }

    public void setMtsx(String mtsx) {
        this.mtsx = mtsx;
    }

    public String getMtxs() {
        return mtxs;
    }

    public void setMtxs(String mtxs) {
        this.mtxs = mtxs;
    }

    public String getQysx() {
        return qysx;
    }

    public void setQysx(String qysx) {
        this.qysx = qysx;
    }

    public int getShengId() {
        return shengId;
    }

    public void setShengId(int shengId) {
        this.shengId = shengId;
    }

    public int getShiId() {
        return shiId;
    }

    public void setShiId(int shiId) {
        this.shiId = shiId;
    }

    public int getQuId() {
        return quId;
    }

    public void setQuId(int quId) {
        this.quId = quId;
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

    public String getXxdz() {
        return xxdz;
    }

    public void setXxdz(String xxdz) {
        this.xxdz = xxdz;
    }

    public String getWfyj() {
        return wfyj;
    }

    public void setWfyj(String wfyj) {
        this.wfyj = wfyj;
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

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getMtgg() {
        return mtgg;
    }

    public void setMtgg(String mtgg) {
        this.mtgg = mtgg;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHmfl() {
        return hmfl;
    }

    public void setHmfl(String hmfl) {
        this.hmfl = hmfl;
    }

    public String getJczt() {
        return jczt;
    }

    public void setJczt(String jczt) {
        this.jczt = jczt;
    }

    public String getChuShenRen() {
        return chuShenRen;
    }

    public void setChuShenRen(String chuShenRen) {
        this.chuShenRen = chuShenRen;
    }

    public String getFuShenRen() {
        return fuShenRen;
    }

    public void setFuShenRen(String fuShenRen) {
        this.fuShenRen = fuShenRen;
    }

    public String getFuShenJieGuo() {
        return fuShenJieGuo;
    }

    public void setFuShenJieGuo(String fuShenJieGuo) {
        this.fuShenJieGuo = fuShenJieGuo;
    }

    public String getZhongShenJieGuo() {
        return zhongShenJieGuo;
    }

    public void setZhongShenJieGuo(String zhongShenJieGuo) {
        this.zhongShenJieGuo = zhongShenJieGuo;
    }

    public String getJcxdrq() {
        return jcxdrq;
    }

    public void setJcxdrq(String jcxdrq) {
        this.jcxdrq = jcxdrq;
    }

    public String getJgId() {
        return jgId;
    }

    public void setJgId(String jgId) {
        this.jgId = jgId;
    }

    public String getJgName() {
        return jgName;
    }

    public void setJgName(String jgName) {
        this.jgName = jgName;
    }

    public String getJgTel() {
        return jgTel;
    }

    public void setJgTel(String jgTel) {
        this.jgTel = jgTel;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getCgbh() {
        return cgbh;
    }

    public void setCgbh(String cgbh) {
        this.cgbh = cgbh;
    }

    public String getKehuName() {
        return kehuName;
    }

    public void setKehuName(String kehuName) {
        this.kehuName = kehuName;
    }

    public String getKehuTel() {
        return kehuTel;
    }

    public void setKehuTel(String kehuTel) {
        this.kehuTel = kehuTel;
    }
}
