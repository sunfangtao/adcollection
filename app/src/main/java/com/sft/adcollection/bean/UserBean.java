package com.sft.adcollection.bean;

import cn.sft.sqlhelper.DBVO;

/**
 * Created by Administrator on 2016/8/21.
 * 用户信息
 */
public class UserBean extends DBVO {
    // 登录名
    private String loginName;
    // 密码
    private String password;
    // 用户名称
    private String name;
    // 用户的id
    private String id;
    // 用户分类
    private String fenLei;
    // SessionId
    private String sessionid;
    //
    private int zipnamecount;

    @Override
    public String toString() {
        return "UserBean{" +
                "loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", fenLei='" + fenLei + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", zipnamecount=" + zipnamecount +
                '}';
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFenLei() {
        return fenLei;
    }

    public void setFenLei(String fenLei) {
        this.fenLei = fenLei;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getZipnamecount() {
        return zipnamecount;
    }

    public void setZipnamecount(int zipnamecount) {
        this.zipnamecount = zipnamecount;
    }
}
