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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import com.jd.core.base.BaseActivity
import com.jd.list.R
import com.jd.list.adapters.CardAdapter
import com.jd.list.utils.BaseUtils
import com.jd.list.view.shimmer.ShimmerRecyclerView

import androidx.recyclerview.widget.RecyclerView

class DemoActivity : BaseActivity() {

    private var shimmerRecycler: ShimmerRecyclerView? = null
    private var mAdapter: CardAdapter? = null

    override fun layoutView(): View {
        val type = type
        val demoConfiguration = BaseUtils.getDemoConfiguration(type, this)
        val layoutId = demoConfiguration!!.layoutResource
        return LayoutInflater.from(this).inflate(layoutId,null)
    }

    private val type: Int
        get() = intent.getIntExtra(EXTRA_TYPE, BaseUtils.TYPE_LIST)

    override fun initView() {
        val type = type
        val layoutManager: RecyclerView.LayoutManager
        val demoConfiguration = BaseUtils.getDemoConfiguration(type, this)
        //        setTheme(demoConfiguration.getStyleResource());
        layoutManager = demoConfiguration!!.layoutManager

        this.navigationBar.setTitle(demoConfiguration.titleResource)

        shimmerRecycler = findViewById(R.id.shimmer_recycler_view)

        if (demoConfiguration.itemDecoration != null) {
            shimmerRecycler!!.addItemDecoration(demoConfiguration.itemDecoration)
        }

        mAdapter = CardAdapter()
        mAdapter!!.setType(type)

        shimmerRecycler!!.layoutManager = layoutManager
        shimmerRecycler!!.adapter = mAdapter
        shimmerRecycler!!.showShimmerAdapter()

        shimmerRecycler!!.postDelayed({ loadCards() }, 3000)

    }

    private fun loadCards() {
        val type = type

        mAdapter!!.setCards(BaseUtils.getCards(resources, type))
        shimmerRecycler!!.hideShimmerAdapter()
    }

    companion object {

        val EXTRA_TYPE = "type"
    }
}
