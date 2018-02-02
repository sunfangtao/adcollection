package com.sft.adcollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sft.adcollection.R;
import com.sft.adcollection.bean.BaseAutoCompleteTextViewVO;

import java.util.List;

import cn.sft.baseactivity.util.Util;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/29.
 */
public class AutoCompleteTextViewAdapter<T extends BaseAutoCompleteTextViewVO> extends BaseAdapter implements Filterable {

    private SelectNameFilter filter;
    private List<T> consumerList;
    private LayoutInflater inflater;

    public AutoCompleteTextViewAdapter(Context context, List<T> consumerList) {
        this.consumerList = consumerList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return consumerList.size();
    }

    @Override
    public Object getItem(int position) {
        return consumerList.get(position);
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
            convertView = inflater.inflate(R.layout.spinner_dropdown_style, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.spinner_nametv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(consumerList.get(position).findTextShow());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new SelectNameFilter();
        return filter;
    }

    private class ViewHolder {
        public TextView name;
    }

    private class SelectNameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            results.values = consumerList;
            results.count = consumerList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            consumerList = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
