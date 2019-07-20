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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jd.core.base.BaseActivity;
import com.jd.list.R;
import com.jd.list.R2;
import com.jd.list.utils.BaseUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.OnClick;

public class ListMainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.list_activity_main;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R2.id.list_demo_button)
    public void onClick1(View view) {
        startDemo(BaseUtils.TYPE_LIST);
    }

    @OnClick(R2.id.grid_demo_button)
    public void onClick2(View view) {
        startDemo(BaseUtils.TYPE_GRID);
    }

    @OnClick(R2.id.list_second_demo_button)
    public void onClick3(View view) {
        startDemo(BaseUtils.TYPE_SECOND_LIST);
    }

    @OnClick(R2.id.grid_second_demo_button)
    public void onClick4(View view) {
        startDemo(BaseUtils.TYPE_SECOND_GRID);
    }

    private void startDemo(int demoType) {
        Intent intent = new Intent(this, DemoActivity.class);
        intent.putExtra(DemoActivity.EXTRA_TYPE, demoType);
        startActivity(intent);
    }
}
