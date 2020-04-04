package com.jd.other.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.jd.core.base.adapter.BaseListViewAdapter
import com.jd.other.R

class OtherViewHolder : BaseListViewAdapter.BaseViewHolder {
    lateinit internal var imageView: ImageView
    lateinit internal var textView1: TextView
    lateinit internal var textView2: TextView

    override fun layout_id(): Int {
        return R.layout.list_item
    }

    override fun onCreateView(view: View?) {
        imageView = view!!.findViewById<View>(R.id.image1) as ImageView
        textView1 = view.findViewById<View>(R.id.text1) as TextView
        textView2 = view.findViewById<View>(R.id.text2) as TextView
    }

    override fun onStart(data: Any?) {
        val item = data as Map<String, Any>?
        //        imageView.setImageResource(Datas.get(i).getImageId());
        textView1.text = item!!["title"]!!.toString()
    }
}
