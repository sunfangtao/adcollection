package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ADHandleDetailBean extends DBVO {
    private String id;
    private String mtzlId;
    private String mtzlBianhao;
    private String jcbgTpId;
    private String gggs;
    private String ggnr;
    private String jcqkId;
    private String jcqk;
    private String wfbhId;
    private String wfyj;
    private String gglbId;
    private String gglb;
    private String status;
    private String shzt;
    private String shrq;
    private String fbdw;
    private String shengId;
    private String shiId;
    private String quId;
    private String sheng;
    private String shi;
    private String qu;
    private String cljg;
    private String cljgId;
    private String mtfl;
    private String bm;
    private String jgname;
    private String filesrc;
    private String wenjianID;
    private TbAjlbZj tbAjlbZj;

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

    public String getJcbgTpId() {
        return jcbgTpId;
    }

    public void setJcbgTpId(String jcbgTpId) {
        this.jcbgTpId = jcbgTpId;
    }

    public String getGggs() {
        return gggs;
    }

    public void setGggs(String gggs) {
        this.gggs = gggs;
    }

    public String getGgnr() {
        return ggnr;
    }

    public void setGgnr(String ggnr) {
        this.ggnr = ggnr;
    }

    public String getJcqkId() {
        return jcqkId;
    }

    public void setJcqkId(String jcqkId) {
        this.jcqkId = jcqkId;
    }

    public String getJcqk() {
        return jcqk;
    }

    public void setJcqk(String jcqk) {
        this.jcqk = jcqk;
    }

    public String getWfbhId() {
        return wfbhId;
    }

    public void setWfbhId(String wfbhId) {
        this.wfbhId = wfbhId;
    }

    public String getWfyj() {
        return wfyj;
    }

    public void setWfyj(String wfyj) {
        this.wfyj = wfyj;
    }

    public String getGglbId() {
        return gglbId;
    }

    public void setGglbId(String gglbId) {
        this.gglbId = gglbId;
    }

    public String getGglb() {
        return gglb;
    }

    public void setGglb(String gglb) {
        this.gglb = gglb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getShrq() {
        return shrq;
    }

    public void setShrq(String shrq) {
        this.shrq = shrq;
    }

    public String getFbdw() {
        return fbdw;
    }

    public void setFbdw(String fbdw) {
        this.fbdw = fbdw;
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

    public String getCljg() {
        return cljg;
    }

    public void setCljg(String cljg) {
        this.cljg = cljg;
    }

    public String getCljgId() {
        return cljgId;
    }

    public void setCljgId(String cljgId) {
        this.cljgId = cljgId;
    }

    public String getMtfl() {
        return mtfl;
    }

    public void setMtfl(String mtfl) {
        this.mtfl = mtfl;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getJgname() {
        return jgname;
    }

    public void setJgname(String jgname) {
        this.jgname = jgname;
    }

    public String getFilesrc() {
        return filesrc;
    }

    public void setFilesrc(String filesrc) {
        this.filesrc = filesrc;
    }

    public String getWenjianID() {
        return wenjianID;
    }

    public void setWenjianID(String wenjianID) {
        this.wenjianID = wenjianID;
    }

    public TbAjlbZj getTbAjlbZj() {
        return tbAjlbZj;
    }

    public void setTbAjlbZj(TbAjlbZj tbAjlbZj) {
        this.tbAjlbZj = tbAjlbZj;
    }

    public class TbAjlbZj extends DBVO {
        private String src;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
