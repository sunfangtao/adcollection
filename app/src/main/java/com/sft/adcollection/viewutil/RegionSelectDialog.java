package com.sft.adcollection.viewutil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sft.adcollection.R;
import com.sft.adcollection.activity.MapActivity;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.common.CollectionApplication;

import java.util.ArrayList;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.WheelVerticalView;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * 项目 ADCollection
 * Created by SunFangtao on 2016/8/31.
 */
public class RegionSelectDialog extends Dialog implements View.OnClickListener {

    public static final int PROVINCE_STYLE = 1;
    public static final int CITY_STYLE = 2;
    public static final int DISTRICT_STYLE = 3;

    private CollectionApplication app;
    private Context context;
    private WheelVerticalView provinceWheel, cityWheel, districtWheel;
    private Button confirmBtn, cancleBtn;

    private String key;
    private int style;

    private ArrayList<String> provinceNameList = new ArrayList<>();
    private ArrayList<String> cityNameList = new ArrayList<>();
    private ArrayList<String> districtNameList = new ArrayList<>();

    public RegionSelectDialog(Context context, int style, String key) {
        super(context, R.style.Region_Select_Dialog);
        init(context, style, key);
    }

    private void init(Context context, int style, String key) {
        this.context = context;
        this.key = key;
        this.style = style;
        this.app = CollectionApplication.getInstance();
        create(context);
    }

    public void create(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setCanceledOnTouchOutside(false);
        View layout = inflater.inflate(R.layout.dialog_region_select, null);
        provinceWheel = (WheelVerticalView) layout.findViewById(R.id.regionselect_privince);
        cityWheel = (WheelVerticalView) layout.findViewById(R.id.regionselect_city);
        districtWheel = (WheelVerticalView) layout.findViewById(R.id.regionselect_district);
        cancleBtn = (Button) layout.findViewById(R.id.regionselect_cancelbtn);
        confirmBtn = (Button) layout.findViewById(R.id.regionselect_confirmbtn);

        switch (style) {
            case PROVINCE_STYLE:
                provinceWheel.setVisibility(View.VISIBLE);
                cityWheel.setVisibility(View.VISIBLE);
                districtWheel.setVisibility(View.VISIBLE);
                provinceNameList = app.getProvinceNameList();
                cityNameList = app.getCitisDataMap().get(provinceNameList.get(0));
                districtNameList = app.getDistrictDataMap().get(cityNameList.get(0));
                break;
            case CITY_STYLE:
                provinceWheel.setVisibility(View.GONE);
                cityWheel.setVisibility(View.VISIBLE);
                districtWheel.setVisibility(View.VISIBLE);
                cityNameList = app.getCitisDataMap().get(key);
                districtNameList = app.getDistrictDataMap().get(cityNameList.get(0));
                break;
            case DISTRICT_STYLE:
                provinceWheel.setVisibility(View.GONE);
                cityWheel.setVisibility(View.GONE);
                districtWheel.setVisibility(View.VISIBLE);
                districtNameList = app.getDistrictDataMap().get(key);
                break;
        }

        initWheelData();

        cancleBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(layout);
    }

    private void initWheelData() {
        provinceWheel.setVisibleItems(5);
        provinceWheel.setViewAdapter(new WheelAdapter(context, provinceNameList));
        provinceWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {

            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                cityNameList = app.getCitisDataMap().get(provinceNameList.get(wheel.getCurrentItem()));
                districtNameList = app.getDistrictDataMap().get(cityNameList.get(0));
                cityWheel.setViewAdapter(new WheelAdapter(context, cityNameList));
                districtWheel.setViewAdapter(new WheelAdapter(context, districtNameList));
                cityWheel.setCurrentItem(0);
                districtWheel.setCurrentItem(0);
            }
        });

        cityWheel.setVisibleItems(5);
        cityWheel.setViewAdapter(new WheelAdapter(context, cityNameList));
        cityWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {

            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                districtNameList = app.getDistrictDataMap().get(cityNameList.get(wheel.getCurrentItem()));
                districtWheel.setViewAdapter(new WheelAdapter(context, districtNameList));
                districtWheel.setCurrentItem(0);
            }
        });


        districtWheel.setVisibleItems(5);
        districtWheel.setViewAdapter(new WheelAdapter(context, districtNameList));
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = BaseActivity.screenWidth; //设置宽度
        getWindow().setAttributes(lp);
    }

    /**
     * Adapter for Wheel
     */
    private class WheelAdapter extends AbstractWheelTextAdapter {

        private ArrayList<String> list;

        protected WheelAdapter(Context context, ArrayList<String> list) {
            super(context);
            this.list = new ArrayList<>(list);
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regionselect_cancelbtn:
                dismiss();
                break;
            case R.id.regionselect_confirmbtn:
                Intent intent = new Intent(MapActivity.class.getName());
                intent.putExtra("isObtainHandleAD",true);
                intent.putExtra("province", provinceNameList.get(provinceWheel.getCurrentItem()));
                intent.putExtra("city", cityNameList.get(cityWheel.getCurrentItem()));
                intent.putExtra("district", districtNameList.get(districtWheel.getCurrentItem()));
                context.sendBroadcast(intent);
                dismiss();
                break;
        }
    }
}
