/**
 * Copyright 2017 Harish Sridharan
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jd.list.activity

import android.content.Intent
import android.view.View

import com.jd.core.base.BaseActivity
import com.jd.list.R
import com.jd.list.R2
import com.jd.list.utils.BaseUtils
import kotlinx.android.synthetic.main.list_activity_main.*


class ListMainActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.list_activity_main
    }

    override fun initView() {
        list_demo_button.setOnClickListener {
            startDemo(BaseUtils.TYPE_LIST)
        }

        grid_demo_button.setOnClickListener {
            startDemo(BaseUtils.TYPE_GRID)
        }

        list_second_demo_button.setOnClickListener {
            startDemo(BaseUtils.TYPE_SECOND_LIST)
        }

        grid_second_demo_button.setOnClickListener {
            startDemo(BaseUtils.TYPE_SECOND_GRID)
        }

    }

    private fun startDemo(demoType: Int) {
        val intent = Intent(this, DemoActivity::class.java)
        intent.putExtra(DemoActivity.EXTRA_TYPE, demoType)
        startActivity(intent)
    }
}
