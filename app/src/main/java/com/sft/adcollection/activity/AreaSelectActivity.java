package com.sft.adcollection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.sft.adcollection.R;
import com.sft.adcollection.adapter.AreaSelectAdapter;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.bean.CitySelectBean;
import com.sft.adcollection.bean.DistricSelectBean;
import com.sft.adcollection.bean.ProvinceSelectBean;
import com.sft.adcollection.bean.UserAreaLevelBean;
import com.sft.adcollection.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sft.baseactivity.util.JSONUtil;

/**
 * Created by Administrator on 2016/9/6.
 */
public class AreaSelectActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String USER_LEVEL_TYPE = "userleveltype";
    private static final String USER_AREA_TYPE = "userareatype";

    //
    private ImageButton returnBtn;

    // 省
    private ListView provinceListView;
    // 市
    private ListView cityListView;
    // 区
    private ListView districtListView;

    // 省Adapter
    private AreaSelectAdapter provinceAdapter;
    // 市Adapter
    private AreaSelectAdapter cityAdapter;
    // 区Adapter
    private AreaSelectAdapter districtAdapter;

    private ArrayList<ProvinceSelectBean> provinceList;
    private ArrayList<CitySelectBean> cityList;
    private ArrayList<DistricSelectBean> districtList;

    private int index = 0;

    // 用户权限等级
    private UserAreaLevelBean userAreaLevelBean;
    // 选择的区
    private DistricSelectBean districSelectBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_select);
        initView();
        initData();
        initListener();
        if (savedInstanceState == null) {
            obtainUserLevel();
        }
    }

    private void initView() {
        returnBtn = (ImageButton) findViewById(R.id.titlebar_left_imgbtn);
        provinceListView = (ListView) findViewById(R.id.areaselect_provincelv);
        cityListView = (ListView) findViewById(R.id.areaselect_citylv);
        districtListView = (ListView) findViewById(R.id.areaselect_districtlv);
    }

    private void initData() {
        setListViewVisible();

        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        districtList = new ArrayList<>();

        provinceAdapter = new AreaSelectAdapter(this, provinceList);
        cityAdapter = new AreaSelectAdapter(this, cityList);
        districtAdapter = new AreaSelectAdapter(this, districtList);

        districtListView.setAdapter(districtAdapter);
        cityListView.setAdapter(cityAdapter);
        provinceListView.setAdapter(provinceAdapter);

    }

    private void initListener() {
        returnBtn.setOnClickListener(this);
        provinceListView.setOnItemClickListener(this);
        cityListView.setOnItemClickListener(this);
        districtListView.setOnItemClickListener(this);
    }

    private void setListViewVisible() {
        provinceListView.setVisibility(View.GONE);
        cityListView.setVisibility(View.GONE);
        districtListView.setVisibility(View.GONE);

        switch (index) {
            case 1:
                provinceListView.setVisibility(View.VISIBLE);
                provinceAdapter.notifyDataSetChanged();
                break;
            case 2:
                cityListView.setVisibility(View.VISIBLE);
                cityAdapter.notifyDataSetChanged();
                break;
            case 3:
                districtListView.setVisibility(View.VISIBLE);
                districtAdapter.notifyDataSetChanged();
                break;
        }
    }

    // 获取用户权限等级
    private void obtainUserLevel() {
        httpPost(USER_LEVEL_TYPE, new RequestParams(), Constant.HTTPUrl.USER_PERMISSION_LEVEL_URL);
    }

    // 获取用户大区
    private void obtainUserArea(String parentId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("parent", parentId);
        httpPost(USER_AREA_TYPE, requestParams, Constant.HTTPUrl.USER_PERMISSION_AREA_URL);
    }

    @Override
    public void onClick(View v) {
        if (!onClickSingleView()) {
            return;
        }
        switch (v.getId()) {
            case R.id.titlebar_left_imgbtn:
                back();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.areaselect_provincelv:
                obtainUserArea(provinceList.get(position).getId());
                break;
            case R.id.areaselect_citylv:
                obtainUserArea(cityList.get(position).getId());
                break;
            case R.id.areaselect_districtlv:
                // 返回
                districSelectBean = districtList.get(position);

                Intent intent = new Intent();
                intent.putExtra("districId", districSelectBean.getId());
                intent.putExtra("cityId", districSelectBean.getParentId());
                String[] parents = districSelectBean.getParentIds().split(",");
                intent.putExtra("provinceId", parents[2]);

                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void back() {
        switch (index) {
            case 3:
                if (cityList.size() > 0) {
                    index = 2;
                    setListViewVisible();
                } else {
                    finish();
                }
                break;
            case 2:
                if (provinceList.size() > 0) {
                    index = 1;
                    setListViewVisible();
                } else {
                    finish();
                }
                break;
            case 1:
                finish();
                break;
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONObject jsonObject) {
        if (USER_LEVEL_TYPE.equals(type)) {
            userAreaLevelBean = JSONUtil.toJavaBean(UserAreaLevelBean.class, jsonObject);
            obtainUserArea(app.getUserBean().getId());
        }
    }

    @Override
    protected void onHttpSuccess(String type, JSONArray jsonArray) {
        if (USER_AREA_TYPE.equals(type)) {
            if (index > 0) {
                switch (index) {
                    case 1:
                        cityList.clear();
                        cityList.addAll(JSONUtil.toJavaBeanList(CitySelectBean.class, jsonArray));
                        index = 2;
                        break;
                    case 2:
                        districtList.clear();
                        districtList.addAll(JSONUtil.toJavaBeanList(DistricSelectBean.class, jsonArray));
                        index = 3;
                        break;
                }
            } else {
                if (userAreaLevelBean.getGrade().equals("1")) {
                    provinceList.clear();
                    provinceList.addAll(JSONUtil.toJavaBeanList(ProvinceSelectBean.class, jsonArray));
                    index = 1;
                } else if (userAreaLevelBean.getGrade().equals("2")) {
                    cityList.clear();
                    cityList.addAll(JSONUtil.toJavaBeanList(CitySelectBean.class, jsonArray));
                    index = 2;
                } else if (userAreaLevelBean.getGrade().equals("3")) {
                    districtList.clear();
                    districtList.addAll(JSONUtil.toJavaBeanList(DistricSelectBean.class, jsonArray));
                    index = 3;
                }
            }
            setListViewVisible();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        index = savedInstanceState.getInt("index");

        userAreaLevelBean = (UserAreaLevelBean) savedInstanceState.getSerializable("userAreaLevelBean");

        provinceList.clear();
        cityList.clear();
        districtList.clear();

        provinceList = (ArrayList<ProvinceSelectBean>) savedInstanceState.getSerializable("provinceList");
        cityList = (ArrayList<CitySelectBean>) savedInstanceState.getSerializable("cityList");
        districtList = (ArrayList<DistricSelectBean>) savedInstanceState.getSerializable("districtList");

        setListViewVisible();

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable("provinceList", provinceList);
        bundle.putSerializable("cityList", cityList);
        bundle.putSerializable("districtList", districtList);
        bundle.putInt("index", index);
        bundle.putSerializable("userAreaLevelBean", userAreaLevelBean);
        super.onSaveInstanceState(bundle);
    }
}
