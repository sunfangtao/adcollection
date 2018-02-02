package com.sft.adcollection.bean;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 * 画面分类
 */
public class DamageConBean extends BaseSpinnerVO {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String findSpinnerShow() {
        return name;
    }
}