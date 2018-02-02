package com.sft.adcollection.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.aioute.customview.HttpProgressDialog;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.activity.LoginActivity;
import com.sft.adcollection.common.CollectionApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.sft.baseactivity.base.MyAsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

/**
 * Created by SunFangtao on 2016/8/3.
 */
public class BaseActivity extends cn.sft.baseactivity.base.BaseActivity {

    protected CollectionApplication app;
    protected HttpProgressDialog httpProgressDialog;
    private static final String PARSE_ERR_MESSAGE = "数据格式错误";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (app == null) {
            app = CollectionApplication.getInstance();
        }
        if (httpProgressDialog == null) {
            httpProgressDialog = new HttpProgressDialog(this);
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
            if (jsonObject.has("code")) {
                if (jsonObject.getString("code").equals("success")) {
                    // 获取成功
                    if (jsonObject.has("message")) {
                        try {
                            JSONObject object = jsonObject.getJSONObject("message");
                            onHttpSuccess(type, object);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                JSONArray array = jsonObject.getJSONArray("message");
                                onHttpSuccess(type, array);
                            } catch (Exception ee) {
                                ee.printStackTrace();
                                onHttpSuccess(type, jsonObject.getString("message"));
                            }
                        }
                    } else {
                        // 格式错误
                        onHttpFailure(type, "", PARSE_ERR_MESSAGE);
                    }
                } else {
                    // 获取失败
                    if (jsonObject.has("message")) {
                        onHttpFailure(type, jsonObject.toString(), jsonObject.getString("message"));
                    } else {
                        onHttpFailure(type, "", PARSE_ERR_MESSAGE);
                    }
                }
            } else {
                if (jsonObject.has("sessionid") || jsonObject.has("isValidateCodeLogin")) {
                    // 登录包单独处理
                    onHttpSuccess(type, jsonObject);
                } else {
                    // 格式错误
                    onHttpFailure(type, "", PARSE_ERR_MESSAGE);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            String result = new String(bytes);
            if (result.contains("<html")) {
                toast.setText("登录失效，请重新登录");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
            // 格式错误
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
        util.print("返回数据  type=" + type + " code=" + code);
        if (code == 500) {
            onHttpFailure(type, "", "type=" + type + " 服务器异常");
        } else if (code == 0) {
            onHttpFailure(type, "", "暂无网络");
        } else {
            onHttpFailure(type, "", "错误代码：" + code);
        }
        return true;
    }

    /**
     * 当数据有效时，交由相应的子activity去处理，方法体的具体内容由子activity实现
     *
     * @param type       http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType()方法中的参数一致
     * @param jsonObject http返回的数据
     */
    protected void onHttpSuccess(String type, JSONObject jsonObject) {

    }

    /**
     * 当数据有效时，交由相应的子activity去处理，方法体的具体内容由子activity实现
     *
     * @param type      http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType()方法中的参数一致
     * @param jsonArray http返回的数据
     */
    protected void onHttpSuccess(String type, JSONArray jsonArray) {

    }

    /**
     * 当数据有效时，交由相应的子activity去处理，方法体的具体内容由子activity实现
     *
     * @param type    http请求的唯一标示，用于区分不同的http请求;与asyncHttpResponseHandler.setType()方法中的参数一致
     * @param message http返回的数据
     */
    protected void onHttpSuccess(String type, String message) {

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
        asyncHttpClient.setCookieStore(new PersistentCookieStore(this));
        asyncHttpClient.setResponseTimeout(60 * 1000);
        util.print("type=" + type + " :" + requestParams.toString().replace("&", "  "));
        asyncHttpResponseHandler = new MyAsyncHttpResponseHandler(type, this);
        asyncHttpClient.setEnableRedirects(true);
        asyncHttpClient.get(url, requestParams, asyncHttpResponseHandler);
    }

    protected void httpPost(String type, RequestParams requestParams, String url) {
        asyncHttpClient.setCookieStore(new PersistentCookieStore(this));
        util.print("type=" + type + " :" + requestParams.toString().replace("&", "  "));
        asyncHttpResponseHandler = new MyAsyncHttpResponseHandler(type, this);
        asyncHttpClient.setEnableRedirects(true);
        if (requestParams.toString().contains("FILE")) {
            asyncHttpClient.setTimeout(60 * 1000);
            asyncHttpClient.addHeader("Content-Disposition", "video/mp4;image/png;charset=UTF-8");
        } else {
            asyncHttpClient.setTimeout(15 * 1000);
            asyncHttpClient.removeHeader("Content-Disposition");
        }
        asyncHttpClient.post(url, requestParams, asyncHttpResponseHandler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (httpProgressDialog.isShowing()) {
                httpProgressDialog.dismiss();
                httpProgressDialog.reset();
                asyncHttpClient.cancelRequests(this, true);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
