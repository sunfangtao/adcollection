/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sft.adcollection.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.sft.adcollection.R;
import com.sft.adcollection.base.BaseActivity;
import com.sft.adcollection.viewutil.showbigimage.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 显示大图
 */
public class ShowBigImageActivity extends BaseActivity {

    private PhotoView image;
    //
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_big_image);

        image = (PhotoView) findViewById(R.id.show_big_image);
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            url = getIntent().getStringExtra("url");

            if (!TextUtils.isEmpty(url)) {
                String type = getIntent().getStringExtra("type");
                if (type != null && type.equals("net")) {
                    Picasso.with(this).load(url).into(image);
                } else {
                    Picasso.with(this).load(new File(url)).into(image);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        url = savedInstanceState.getString("url");
        if (!TextUtils.isEmpty(url))
            Picasso.with(this).load(new File(url)).into(image);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("url", url);
        super.onSaveInstanceState(outState);
    }
}
