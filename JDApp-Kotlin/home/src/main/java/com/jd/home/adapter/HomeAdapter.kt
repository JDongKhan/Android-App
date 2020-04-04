package com.jd.home.adapter

import android.content.Context
import android.widget.TextView

import com.jd.core.base.adapter.BaseRecyclerViewAdapter
import com.jd.core.view.BannerPager
import com.jd.home.R
import java.util.*

class HomeAdapter(context: Context, mData: List<Map<*,*>>, viewHolders: List<Int>) : BaseRecyclerViewAdapter<Map<*, *>>(context, mData, viewHolders) {

    override fun indexOfLayoutsAtPosition(position: Int): Int {
        return if (position == 0) {
            0
        } else 1
    }

    override fun convert(viewHolder: BaseRecyclerViewAdapter.BaseViewHolder, item: Map<*, *>) {
        if (viewHolder.itemType == 0) {
            val bannerPager = viewHolder.itemView as BannerPager
            bannerPager.adapter = IndexBannderAdapter(bannerPager.context, initBanner())
            return
        } else {
            val textView = viewHolder.getView(R.id.text1) as TextView
            textView.text = if (item["title"] == null) "" else item["title"]!!.toString()
        }
    }

    private fun initBanner(): MutableList<Int> {
        val list = ArrayList<Int>()
        list.add(R.mipmap.ic_banner_1)
        list.add(R.mipmap.ic_banner_2)
        list.add(R.mipmap.ic_banner_3)
        list.add(R.mipmap.ic_banner_4)
        list.add(R.mipmap.ic_banner_5)
        return list
    }
}
