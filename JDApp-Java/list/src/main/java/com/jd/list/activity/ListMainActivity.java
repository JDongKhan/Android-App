/**
 * Copyright 2017 Harish Sridharan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jd.list.activity;

import android.content.Intent;
import android.view.View;

import com.jd.core.base.BaseActivity;
import com.jd.list.R;
import com.jd.list.utils.BaseUtils;

public class ListMainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.list_activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.list_demo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BaseUtils.TYPE_LIST);
            }
        });

        findViewById(R.id.grid_demo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BaseUtils.TYPE_GRID);
            }
        });


        findViewById(R.id.list_second_demo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BaseUtils.TYPE_SECOND_LIST);
            }
        });

        findViewById(R.id.grid_second_demo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BaseUtils.TYPE_SECOND_GRID);
            }
        });

    }


    private void startDemo(int demoType) {
        Intent intent = new Intent(this, DemoActivity.class);
        intent.putExtra(DemoActivity.EXTRA_TYPE, demoType);
        startActivity(intent);
    }
}
