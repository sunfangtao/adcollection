package com.sft.adcollection.common;

import android.content.res.AssetManager;

import com.sft.adcollection.bean.CityBean;
import com.sft.adcollection.bean.DistrictBean;
import com.sft.adcollection.bean.ProvinceBean;
import com.sft.adcollection.bean.UserAreaLevelBean;
import com.sft.adcollection.bean.UserBean;
import com.sft.adcollection.bean.UserLocationBean;
import com.sft.adcollection.util.XmlParserHandler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.sft.baseactivity.base.BaseApplication;

/**
 *
 */
public class CollectionApplication extends BaseApplication {

    private static CollectionApplication app;
    private UserBean userBean;
    private UserLocationBean userLocationBean;
    private UserAreaLevelBean userAreaLevelBean;
    private ArrayList<ProvinceBean> provinceList;
    /**
     * 所有省
     */
    protected ArrayList<String> provinceNameList = new ArrayList<>();
    /**
     * key - 省 value - 市
     */
    protected Map<String, ArrayList<String>> citisDataMap = new HashMap<String, ArrayList<String>>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, ArrayList<String>> districtDataMap = new HashMap<String, ArrayList<String>>();

    @Override
    public void onCreate() {
        super.onCreate();

        if (app == null) {
            synchronized (CollectionApplication.class) {
                if (app == null) {
                    app = this;
                }
            }
        }

        initProvinceData();
    }

    public static CollectionApplication getInstance() {
        return app;
    }

    public String getBasePath() {
        return util.getSDPath() + File.separator + getPackageName() + File.separator;
    }

    public UserBean getUserBean() {
        if (this.userBean == null) {
            List<UserBean> userBeanList = (List<UserBean>) util.readList("userBeanList");
            if (userBeanList != null && userBeanList.size() > 0) {
                this.userBean = userBeanList.get(0);
            }
        }
        return this.userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
        // 登录成功后存储用户信息（持久化）
        List<UserBean> userBeanList = new ArrayList<>();
        userBeanList.add(userBean);
        util.saveList(userBeanList, "userBeanList");
    }

    public UserAreaLevelBean getUserGrade() {
        if (userAreaLevelBean == null) {
            List<UserAreaLevelBean> userAreaLevelBeen = (List<UserAreaLevelBean>) util.readList(getUserBean().getLoginName() + "userAreaLevelBean");
            if (userAreaLevelBeen != null && userAreaLevelBeen.size() > 0) {
                this.userAreaLevelBean = userAreaLevelBeen.get(0);
            }
        }
        return userAreaLevelBean;
    }

    public void setUserGrade(UserAreaLevelBean userAreaLevelBean) {
        this.userAreaLevelBean = userAreaLevelBean;
        List<UserAreaLevelBean> userAreaLevelBeanList = new ArrayList<>();
        userAreaLevelBeanList.add(userAreaLevelBean);
        util.saveList(userAreaLevelBeanList, getUserBean().getLoginName() + "userAreaLevelBean");
    }

    public UserLocationBean getUserLocationBean() {
        if (this.userLocationBean == null) {
            List<UserLocationBean> userLocationList = (List<UserLocationBean>) util.readList(getUserBean().getLoginName() + "userLocationList");
            if (userLocationList != null && userLocationList.size() > 0) {
                this.userLocationBean = userLocationList.get(0);
            }
        }
        return new UserLocationBean(userLocationBean);
    }

    public void setUserLocationBean(UserLocationBean userLocationBean) {
        this.userLocationBean = userLocationBean;
        List<UserLocationBean> userLocationList = new ArrayList<>();
        userLocationList.add(userLocationBean);
        util.saveList(userLocationList, getUserBean().getLoginName() + "userLocationList");
    }

    public ArrayList<String> getProvinceNameList() {
        if (provinceNameList == null)
            initProvinceData();
        return provinceNameList;
    }

    public Map<String, ArrayList<String>> getDistrictDataMap() {
        if (districtDataMap == null)
            initProvinceData();
        return districtDataMap;
    }

    public Map<String, ArrayList<String>> getCitisDataMap() {
        if (citisDataMap == null)
            initProvinceData();
        return citisDataMap;
    }

    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceData() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            int length = provinceList.size();

            provinceNameList.clear();
            citisDataMap.clear();
            districtDataMap.clear();

            for (int i = 0; i < length; i++) {
                ProvinceBean provinceBean = provinceList.get(i);
                provinceNameList.add(provinceBean.getName());

                ArrayList<CityBean> cityList = provinceBean.getCityList();
                ArrayList<String> cityNameList = new ArrayList<>();

                int cityLength = cityList.size();
                for (int j = 0; j < cityLength; j++) {
                    CityBean cityBean = cityList.get(j);
                    cityNameList.add(cityBean.getName());

                    ArrayList<DistrictBean> districtList = cityBean.getDistrictList();
                    ArrayList<String> districtNameList = new ArrayList<>();
                    int districtLength = districtList.size();
                    for (int k = 0; k < districtLength; k++) {
                        districtNameList.add(districtList.get(k).getName());
                    }
                    districtDataMap.put(cityList.get(j).getName(), districtNameList);
                }

                citisDataMap.put(provinceBean.getName(), cityNameList);
            }
        } catch (Exception e) {

        }
    }
}
