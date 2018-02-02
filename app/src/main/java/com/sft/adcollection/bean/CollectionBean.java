package com.sft.adcollection.bean;

import com.loopj.android.http.RequestParams;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/9/5.
 */
public class CollectionBean extends DBVO {

    // 媒体资料id
    private String id;
    // 检测报告ID
    private String jcbgId;
    // 媒体参数
    private RequestParams mediaParams;
    // 版面参数
    private ArrayList<RequestParams> pageParamsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJcbgId() {
        return jcbgId;
    }

    public void setJcbgId(String jcbgId) {
        this.jcbgId = jcbgId;
    }

    public RequestParams getMediaParams() {
        return mediaParams;
    }

    public void setMediaParams(RequestParams mediaParams) {
        this.mediaParams = mediaParams;
    }

    public ArrayList<RequestParams> getPageParamsList() {
        return pageParamsList;
    }

    public void setPageParamsList(ArrayList<RequestParams> pageParamsList) {
        this.pageParamsList = pageParamsList;
    }
}
