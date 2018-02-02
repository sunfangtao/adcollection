package com.sft.adcollection.bean;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 */
public class ConsumerBean extends BaseAutoCompleteTextViewVO {
    // 公司名称
    private String companyName;
    // 公司地址
    private String companyAddress;
    // 邮箱
    private String email;
    // 传真
    private String fax;
    // 编号
    private String id;
    // 类别
    private String leiBie;
    // 类别编号
    private String leiBieId;
    // 手机号码
    private String mobile;
    // 手机号码
    private String mobile1;
    // 机构名称或客户名称
    private String name;
    // 电话
    private String tel;
    // 电话号码
    private String tel1;
    // 网址
    private String wangZhi;

    @Override
    public String toString() {
        return companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeiBie() {
        return leiBie;
    }

    public void setLeiBie(String leiBie) {
        this.leiBie = leiBie;
    }

    public String getLeiBieId() {
        return leiBieId;
    }

    public void setLeiBieId(String leiBieId) {
        this.leiBieId = leiBieId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getWangZhi() {
        return wangZhi;
    }

    public void setWangZhi(String wangZhi) {
        this.wangZhi = wangZhi;
    }

    @Override
    public String findTextShow() {
        return companyName;
    }
}
