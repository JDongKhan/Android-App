package com.jd.core.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView

import com.jd.core.R


/**
 * @author wangjindong
 */
class NavigationBar : RelativeLayout {

    private val mHeight = 80
    private val padding_top = 20

    //backView
    private var navigation_back: LinearLayout? = null
    private var navigation_back_text: TextView? = null

    //titleView
    private var navigation_title: TextView? = null

    //detailView
    private var navigation_detail: TextView? = null

    //rightViews
    private var rightView: LinearLayout? = null


    constructor(context: Context) : super(context) {
        this.createView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.createView(context)
    }

    private fun createView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this, true)
        this.navigation_back = this.findViewById(R.id.navigation_back)
        this.navigation_back_text = this.findViewById(R.id.navigation_back_text)
        this.navigation_title = this.findViewById(R.id.navigation_title)
        this.navigation_detail = this.findViewById(R.id.navigation_detail)
        this.rightView = this.findViewById(R.id.navigation_rightView)
        //返回事件
        this.navigation_back!!.setOnClickListener { v ->
            val activity = v.context as Activity
            activity.finish()
        }
    }

    //隐藏左侧返回按钮
    fun setBackViewHidden(hidden: Boolean) {
        if (hidden) {
            this.navigation_back!!.visibility = View.GONE
        } else {
            this.navigation_back!!.visibility = View.VISIBLE
        }
    }

    //设置title
    fun setTitle(title: String) {
        this.navigation_title!!.text = title
    }

    fun setTitle(title: Int) {
        this.navigation_title!!.setText(title)
    }
}
