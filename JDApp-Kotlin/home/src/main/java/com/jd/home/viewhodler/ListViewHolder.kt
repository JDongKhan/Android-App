package com.jd.home.viewhodler

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.jd.core.base.adapter.BaseListViewAdapter
import com.jd.home.R

class ListViewHolder : BaseListViewAdapter.BaseViewHolder {
    lateinit internal var imageView: ImageView
    lateinit internal var textView1: TextView

    override fun layout_id(): Int {
        return R.layout.home_fragment_list_item
    }

    override fun onCreateView(view: View?) {
        imageView = view!!.findViewById<View>(R.id.list_item_image) as ImageView
        textView1 = view.findViewById<View>(R.id.list_item_title) as TextView
    }

    override fun onStart(data: Any?) {
        val item = data as Map<String, Any>?
        //        imageView.setImageResource(Datas.get(i).getImageId());
        textView1.text = item!!["title"]!!.toString()
    }
}
