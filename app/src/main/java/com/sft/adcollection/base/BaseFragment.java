package com.sft.adcollection.base;

import android.os.Bundle;

import com.loopj.android.http.RequestParams;
import com.sft.adcollection.common.CollectionApplication;

import org.json.JSONObject;

import cn.sft.baseactivity.base.MyAsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

/**
 * Created by SunFangtao on 2016/8/3.
 */
public class BaseFragment extends cn.sft.baseactivity.base.BaseFragment {

    protected CollectionApplication app;
    private static final String PARSE_ERR_MESSAGE = "dataParseError";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (app == null) {
            app = CollectionApplication.getInstance();
        }
    }

    /**
     * http请求成功返回后调用，与asyncHttpClient.get("http://www.baidu.com", params, asyncHttpResponseHandler)进行匹配；
     * 多个http请求可能同时返回，在此进行同步
     *
     * @param type    http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType("")方法中的参数一致
     * @param code    http请求的返回码，200表示成功，404,500等等
     * @param headers http协议头
     * @param bytes   http请求返回的字节数据，统一使用UTF-8进行编码
     */
    @Override
    public synchronized boolean onSuccess(String type, int code, Header[] headers, byte[] bytes) {
        //对返回的数据进行统一的处理，比如数据是否可用，是否有效等等，如果可用交由子类处理
        try {
            String result = new String(bytes);
            util.print("返回数据  type=" + type + " json=" + result);
            JSONObject jsonObject = new JSONObject(result);
            if ("success".equals(jsonObject.getString("result"))) {
                onHttpSuccess(type, jsonObject);
            } else {
                onHttpFailure(type, result, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onHttpFailure(type, "", PARSE_ERR_MESSAGE);
        }
        return true;
    }

    /**
     * http请求失败返回后调用，与asyncHttpClient.get("http://www.baidu.com", params, asyncHttpResponseHandler)进行匹配；
     * 多个http请求可能同时返回，在此进行同步
     *
     * @param type    http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType("")方法中的参数一致
     * @param code    http请求的返回码，200表示成功，404,500等等
     * @param headers http协议头
     * @param bytes   http请求返回的字节数据，统一使用UTF-8进行编码
     */
    @Override
    public synchronized boolean onFailure(String type, int code, Header[] headers, byte[] bytes, Throwable throwable) {
        // 对返回的数据进行统一的处理，比如数据是否可用，是否有效等等，如果可用交由子类处理
        // 一般情况下，请求错误不再需要子类去处理。不如BaseActivity直接给出错误提示
        try {
            String result = new String(bytes);
            util.print("返回数据  type=" + type + " code=" + code);
            onHttpFailure(type, result, new JSONObject(result).getString("message"));
        } catch (Exception e) {
            onHttpFailure(type, "", PARSE_ERR_MESSAGE);
        }
        return true;
    }

    /**
     * 当数据有效时，交由相应的子activity去处理，方法体的具体内容由子activity实现
     *
     * @param type http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType()方法中的参数一致
     * @param json http返回的数据
     */
    protected void onHttpSuccess(String type, JSONObject json) {

    }

    /**
     * 当数据无效时，交由相应的子activity去处理，方法体的具体内容由子activity实现
     *
     * @param type http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType()方法中的参数一致
     * @param json http返回的数据
     * @param Err  错误信息
     */
    protected void onHttpFailure(String type, String json, String Err) {

    }

    protected void httpGet(String type, RequestParams requestParams, String url) {
        util.print("type=" + type + " :" + requestParams.toString().replace("&", "  "));
        asyncHttpResponseHandler = new MyAsyncHttpResponseHandler(type, this);
        asyncHttpClient.get(url, requestParams, asyncHttpResponseHandler);
    }

    protected void httpPost(String type, RequestParams requestParams, String url) {
        util.print("type=" + type + " :" + requestParams.toString().replace("&", "  "));
        asyncHttpResponseHandler = new MyAsyncHttpResponseHandler(type, this);
        asyncHttpClient.post(url, requestParams, asyncHttpResponseHandler);
    }
}
