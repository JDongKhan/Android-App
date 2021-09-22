package com.jd.home.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jd.home.R


class QuickAdapter(layoutResId: Int, data: List<Map<*,*>>?) : BaseQuickAdapter<Map<*, *>, BaseViewHolder>(layoutResId, data) {

    override fun convert(viewHolder: BaseViewHolder, item: Map<*, *>) {
        viewHolder.setText(R.id.list_item_title, item["title"]!!.toString())
        Glide.with(mContext).load(R.drawable.default_icon).into(viewHolder.getView(R.id.list_item_image) as ImageView)
    }

}
