package com.jd.home.adapter

import android.content.Context
import android.widget.GridView
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.jd.core.base.adapter.BaseRecyclerViewAdapter
import com.jd.core.utils.ConvertUtils
import com.jd.core.view.BannerPager
import com.jd.home.R
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(context: Context, mData: List<Map<*,*>>, viewHolders: List<Int>) : BaseRecyclerViewAdapter<Map<*, *>>(context, mData, viewHolders) {

    private var menus: Array<Map<String, Any>> = arrayOf(
            mapOf("title" to "今日新品","image" to R.drawable.red_heart),
            mapOf("title" to "热卖品类","image" to R.drawable.tag_bao),
            mapOf("title" to "美妆管","image" to R.drawable.tag_bg),
            mapOf("title" to "人气单品","image" to R.drawable.tag_hot),
            mapOf("title" to "韩风管","image" to R.drawable.red_heart),
            mapOf("title" to "流行资讯","image" to R.drawable.tag_bao),
            mapOf("title" to "时尚品类","image" to R.drawable.tag_bg),
            mapOf("title" to "疯抢单品","image" to R.drawable.tag_hot)
    )

    override fun indexOfLayoutsAtPosition(position: Int): Int {
        return if (position == 0) {
            0
        } else if (position == 1){
            1
        }  else 2
    }

    override fun convert(viewHolder: BaseRecyclerViewAdapter.BaseViewHolder, item: Map<*, *>) {
        if (viewHolder.itemType == 0) {
            val bannerPager = viewHolder.itemView as BannerPager
            bannerPager.adapter = IndexBannderAdapter(bannerPager.context, initBanner())
            var layoutParams = StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(bannerPager.context,300.0f))
            layoutParams.isFullSpan = true
            bannerPager.layoutParams = layoutParams
            return
        }else if (viewHolder.itemType == 1) {
            var gridView = viewHolder.itemView as GridView
            var layoutParams = StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT)
            layoutParams.isFullSpan = true
            gridView.layoutParams = layoutParams

            var form = arrayOf("image","title")
            var to = intArrayOf(R.id.grid_item_image,R.id.grid_item_title)
            gridView.adapter = SimpleAdapter(
                    gridView.context,
                    menus.toMutableList(),
                    R.layout.grid_image_text_item,
                    form,
                    to
            )
        } else {
            val textView = viewHolder.getView(R.id.list_item_title) as TextView
            textView.text = if (item["title"] == null) "" else item["title"]!!.toString()
            var imageView = viewHolder.getView(R.id.list_item_image) as ImageView
            imageView.setImageResource(item["image"] as Int)
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
