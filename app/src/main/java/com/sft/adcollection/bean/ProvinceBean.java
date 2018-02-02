package com.sft.adcollection.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {
	private String name;
	private ArrayList<CityBean> cityList;
	
	public ProvinceBean() {
		super();
	}

	public ProvinceBean(String name, ArrayList<CityBean> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CityBean> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<CityBean> cityList) {
		this.cityList = cityList;
	}

}
