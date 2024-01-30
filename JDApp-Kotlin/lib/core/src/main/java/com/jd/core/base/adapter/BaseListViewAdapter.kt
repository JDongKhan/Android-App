package com.jd.core.base.adapter


import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class BaseListViewAdapter(var context: Context, private val dataList: List<*>?, vararg viewHolders: Class<out BaseViewHolder>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var viewHolders: Array<out Class<out BaseViewHolder>>? = null

    init {
        this.viewHolders = viewHolders
    }

    override fun getCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getItem(arg0: Int): Any? {
        return dataList!![arg0]
    }

    override fun getItemId(arg0: Int): Long {
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        val type = this.indexOfLayoutsAtPosition(position)
        return if (type >= 0) {
            type
        } else super.getItemViewType(position)
    }

    override fun getViewTypeCount(): Int {
        return if (this.viewHolders == null) {
            1
        } else this.viewHolders!!.size
    }


    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View? {
        var mConvertView = convertView
        var viewHold: BaseViewHolder? = null
        if (mConvertView == null) {
            val index = this.indexOfLayoutsAtPosition(position)
            //取viewHolderClass类型
            val viewHolderClass = this.viewHolders!![index]
            try {
                viewHold = viewHolderClass.newInstance() as BaseViewHolder
            } catch (e: IllegalAccessException) {
                e.message?.let { Log.e("BaseListViewAdapter", it) }
            } catch (e: InstantiationException) {
                e.message?.let { Log.e("BaseListViewAdapter", it) }
            }

            val layout_id = viewHold!!.layoutId()
            mConvertView = layoutInflater.inflate(layout_id, null)
            if (mConvertView == null) {
                Log.e("BaseListViewAdapter", "请在[" + BaseViewHolder::class.java + "]类里面实现layout_id")
            } else {
                // -------------------绑定控件到viewHolder------------------------------------
                viewHold.onCreateView(mConvertView)
                // 缓存绑定
                mConvertView.tag = viewHold
            }
        } else {
            viewHold = mConvertView.tag as BaseViewHolder
        }
        viewHold.onStart(this.getItem(position))
        return mConvertView
    }


    abstract fun indexOfLayoutsAtPosition(position: Int): Int


    /** */
    interface BaseViewHolder {

        fun layoutId(): Int

        fun onCreateView(view: View?)

        fun onStart(data: Any?)
    }


}

