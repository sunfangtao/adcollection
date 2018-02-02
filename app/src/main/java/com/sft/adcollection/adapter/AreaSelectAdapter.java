package com.sft.adcollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sft.adcollection.R;
import com.sft.adcollection.bean.BaseAreaSelectBean;

import java.util.ArrayList;

import cn.sft.baseactivity.util.Util;

/**
 * Created by Administrator on 2016/9/6.
 */
public class AreaSelectAdapter<T extends BaseAreaSelectBean> extends BaseAdapter {

    private Context context;
    private ArrayList<BaseAreaSelectBean> dataList;
    private LayoutInflater inflater;

    public AreaSelectAdapter(Context context, ArrayList<BaseAreaSelectBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public BaseAreaSelectBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.area_select_item, parent, false);
            holder.nameTV = (TextView) convertView.findViewById(R.id.area_select_item_nametv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(getItem(position).getShowContent());

        return convertView;
    }

    public class ViewHolder {
        public TextView nameTV;
    }
}
