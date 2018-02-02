package com.sft.adcollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.adcollection.R;
import com.sft.adcollection.bean.BaseSpinnerVO;

import java.util.ArrayList;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/22.
 */
public class MySpinnerAdapter<T extends BaseSpinnerVO> extends BaseAdapter {
    private Context context;
    private ArrayList<T> valueList;
    private LayoutInflater mInflater;
    // 当前选中的下标
    private int selectIndex;

    public MySpinnerAdapter(Context context, ArrayList<T> valueList) {
        this.valueList = valueList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public View getDropDownView(int index, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.spinner_dropdown_style, null);
        TextView text = (TextView) convertView.findViewById(R.id.spinner_nametv);
        text.setText(valueList.get(index).findSpinnerShow());
        return convertView;
    }

    public T getItem(int paramInt) {
        return valueList.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.spinner_style, null);
        TextView text = (TextView) convertView.findViewById(R.id.spinner_selectnametv);
        text.setText(valueList.get(index).findSpinnerShow());
        selectIndex = index;
        return convertView;
    }

    public String getSelectValue() {
        return getItem(selectIndex).findSpinnerShow();
    }

    public int getSelectIndex() {
        return selectIndex;
    }
}
