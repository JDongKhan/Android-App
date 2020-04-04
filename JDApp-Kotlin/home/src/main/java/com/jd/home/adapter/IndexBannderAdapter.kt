package com.jd.home.adapter

import android.content.Context

import com.jd.core.view.BannerPager

class IndexBannderAdapter(context: Context, list: MutableList<Int>) : BannerPager.BannerAdapter<Int>(context, list) {

    override fun getCount(): Int {
        return super.getCount()
    }
}

