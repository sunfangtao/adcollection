package com.sft.adcollection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.UserAreaLevelBean;
import com.sft.adcollection.bean.UserBean;
import com.sft.adcollection.common.Constant;

import org.json.JSONObject;

import cn.sft.baseactivity.util.CrashHandler;
import cn.sft.baseactivity.util.JSONUtil;
import cn.sft.baseactivity.util.MyHandler;

/**
 * Created by Administrator on 2016/8/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    //用户头像
    private ImageView headImageView;
    // 用户名
    private EditText accountET;
    // 密码
    private EditText passwordET;
    // 登录按钮
    private Button loginBtn;
    // 忘记密码按钮
    private Button forgetBtn;
    // 注册按钮
    private Button registerBtn;

    private final static String LOGIN_TYPE = "login";
    private static final String USER_LEVEL_TYPE = "userleveltype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CrashHandler.init(this, true);
        super.onCreate(savedInstanceState);
        appInit();

        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        headImageView = (ImageView) findViewById(R.id.login_headimg);
        accountET = (EditText) findViewById(R.id.login_accountet);
        passwordET = (EditText) findViewById(R.id.login_passwordet);
        loginBtn = (Button) findViewById(R.id.login_loginbtn);
        forgetBtn = (Button) findViewById(R.id.login_forgetbtn);
        registerBtn = (Button) findViewById(R.id.login_registerbtn);
    }

    private void initData() {
        app.setUserBean(null);
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        persistentCookieStore.clear();

        accountET.setText(util.readStrParam("userAccount"));
        passwordET.setText(util.readStrParam("userPassword"));
    }

    private void initListener() {
        loginBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!onClickSingleView()) {
            //防止多点触控，导致问题
            return;
        }
        switch (v.getId()) {
            case R.id.login_loginbtn:
                String result = checkLoginInfo();
                if (result != null) {
                    toast.setText(result);
                } else {
                    login();
                }
                break;
            case R.id.login_forgetbtn:
                break;
            case R.id.login_registerbtn:
                break;
        }
    }

    private String checkLoginInfo() {
        String account = accountET.getText().toString();
        if (TextUtils.isEmpty(account)) {
            return getString(R.string.logininputaccount);
        }
        String password = passwordET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            return getString(R.string.logininputpassword);
        }
        return null;
    }

    private void login() {
        String account = accountET.getText().toString();
        String password = passwordET.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.put("username", account);
        requestParams.put("password", password);
        requestParams.put("validateCode", "");
        requestParams.put("mobileLogin", "true");

        httpPost(LOGIN_TYPE, requestParams, Constant.HTTPUrl.LOGIN_URL);
        httpProgressDialog.setMessage(getString(R.string.logining)).show();
    }

    // 获取用户权限等级
    private void obtainUserLevel() {
        httpPost(USER_LEVEL_TYPE, new RequestParams(), Constant.HTTPUrl.USER_PERMISSION_LEVEL_URL);
    }

    @Override
    protected void onHttpSuccess(String type, JSONObject json) {
        if (LOGIN_TYPE.equals(type)) {
            try {
                if (json.has("isValidateCodeLogin")) {
                    httpProgressDialog.dismissWithFailure(json.getString("message"));
                    return;
                }
                UserBean userBean = JSONUtil.toJavaBean(UserBean.class, json);
                app.setUserBean(userBean);

                if (app.getUserBean().getFenLei().equals("1")) {
                    obtainUserLevel();
                } else {
                    loginSuccess();
                }
            } catch (Exception e) {
                httpProgressDialog.dismissWithFailure(getString(R.string.loginfail));
            }
        } else if (type.equals(USER_LEVEL_TYPE)) {
            if (USER_LEVEL_TYPE.equals(type)) {
                UserAreaLevelBean userAreaLevelBean = JSONUtil.toJavaBean(UserAreaLevelBean.class, json);
                app.setUserGrade(userAreaLevelBean);
                loginSuccess();
            }
        }
    }

    private void loginSuccess(){
        httpProgressDialog.dismissWithSuccess(getString(R.string.loginsuccess));
        util.saveParam("userAccount", accountET.getText().toString());
        util.saveParam("userPassword", passwordET.getText().toString());
        new MyHandler(1000) {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    protected void onHttpFailure(String type, String json, String Err) {
        if (LOGIN_TYPE.equals(type)) {
            if (!TextUtils.isEmpty(json)) {
                httpProgressDialog.dismissWithFailure(Err);
            } else {
                httpProgressDialog.dismissWithFailure(getString(R.string.loginfail));
            }
        } else if ("test".equals(type)) {
            util.print("Err=" + Err);
        }
    }
}
