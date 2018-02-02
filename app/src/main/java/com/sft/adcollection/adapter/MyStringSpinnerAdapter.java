package com.sft.adcollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.adcollection.R;

import java.util.List;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/22.
 */
public class MyStringSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> valueList;
    private LayoutInflater mInflater;
    // 当前选中的下标
    private int selectIndex;

    public MyStringSpinnerAdapter(Context context, List<String> valueList) {
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
        text.setText(valueList.get(index));
        return convertView;
    }

    public String getItem(int paramInt) {
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
        text.setText(valueList.get(index));
        selectIndex = index;
        return convertView;
    }

    public String getSelectValue() {
        return getItem(selectIndex);
    }

    public int getSelectIndex() {
        return selectIndex;
    }
}
