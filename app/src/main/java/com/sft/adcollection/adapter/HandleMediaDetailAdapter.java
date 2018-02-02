package com.sft.adcollection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sft.adcollection.R;
import com.sft.adcollection.activity.HandleMediaDetailActivity;
import com.sft.adcollection.bean.ADHandleDetailBean;
import com.sft.adcollection.common.CollectionApplication;
import com.sft.adcollection.common.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.sft.baseactivity.util.Util;

/**
 * Created by Administrator on 2016/9/6.
 */
public class HandleMediaDetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ADHandleDetailBean> dataList;
    private LayoutInflater inflater;

    private CollectionApplication app;

    private Util util;

    public HandleMediaDetailAdapter(Context context, ArrayList<ADHandleDetailBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        if (app == null) {
            app = CollectionApplication.getInstance();
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ADHandleDetailBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.handle_media_detail_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.handlemedia_item_image);
            holder.pageTV = (TextView) convertView.findViewById(R.id.handlemedia_item_pagetv);
            holder.mediaCodeTV = (TextView) convertView.findViewById(R.id.handlemedia_item_mediacodetv);
            holder.photoBtn = (ImageButton) convertView.findViewById(R.id.handlemedia_item_photoimg);
            holder.lawCodeTV = (TextView) convertView.findViewById(R.id.handlemedia_item_lawcodetv);
            holder.consumerTV = (TextView) convertView.findViewById(R.id.handlemedia_item_consumertv);
            holder.supplierTV = (TextView) convertView.findViewById(R.id.handlemedia_item_suppliertv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ADHandleDetailBean adHandleDetailBean = dataList.get(position);

        holder.pageTV.setText(context.getString(R.string.lawpage) + adHandleDetailBean.getBm());
        holder.mediaCodeTV.setText(context.getString(R.string.mediacode) + adHandleDetailBean.getMtzlBianhao());

        holder.lawCodeTV.setText("违法编号：" + adHandleDetailBean.getWfbhId());
        holder.consumerTV.setText("采购商：" + adHandleDetailBean.getJgname());
        holder.supplierTV.setText("供应商：" + adHandleDetailBean.getGggs());

        try {
            String src = adHandleDetailBean.getTbAjlbZj().getSrc();
            Picasso.with(context).load(Constant.IPPORT + src).into(holder.image);
        } catch (Exception e) {

        }

        if (!app.getUserGrade().getGrade().equals("4")) {
            holder.photoBtn.setVisibility(View.GONE);
        } else {
            holder.photoBtn.setVisibility(View.VISIBLE);
        }

        holder.photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(HandleMediaDetailActivity.class.getName()).putExtra("isTakePhoto", true).putExtra("position", position));
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendBroadcast(new Intent(HandleMediaDetailActivity.class.getName()).putExtra("isShowBigIm", true).putExtra("position", position));
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public TextView pageTV;
        public TextView mediaCodeTV;
        public ImageButton photoBtn;
        public TextView lawCodeTV;
        public TextView consumerTV;
        public TextView supplierTV;
    }
}
