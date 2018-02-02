package com.sft.adcollection.bean;

import android.text.TextUtils;

import cn.sft.sqlhelper.DBVO;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/9/6.
 */
public class UserAreaLevelBean extends DBVO {

    private String parentIds;
    private String grade;

    public String getParentIds() {
//        String[] parents = parentIds.split(",");
//        for (int i = parents.length - 1; i >= 0; i--) {
//            if (!TextUtils.isEmpty(parents[i]))
//                return parents[i];
//        }
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
