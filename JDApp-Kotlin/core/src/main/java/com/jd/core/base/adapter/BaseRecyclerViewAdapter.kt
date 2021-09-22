package com.jd.core.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

/**
 *
 */
abstract class BaseRecyclerViewAdapter<T>(private val mContext: Context, private val mData: List<T>, private val viewHolders: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var itemClickListener: OnItemClickListener? = null

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO 寻找优雅的解耦方案 ，因为ViewHolder实例化需要先取view，所以无法从ViewHolder的实例去取布局，故使用了mapper方案
        val layout_id = this.viewHolders[viewType]
        val view = LayoutInflater.from(parent.context).inflate(layout_id, parent, false)
        val vh = BaseViewHolder(view)
        vh.itemType = viewType
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as BaseViewHolder
        this.convert(viewHolder, mData[position])

        if (viewHolder.itemView !is AdapterView<*>) {
            //添加点击事件
            viewHolder.itemView.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(this@BaseRecyclerViewAdapter, viewHolder.itemView, position)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return this.indexOfLayoutsAtPosition(position)
    }

    //让外面告诉我映射关系
    protected abstract fun indexOfLayoutsAtPosition(position: Int): Int

    protected abstract fun convert(viewHolder: BaseViewHolder, item: T)

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /** */
    class ViewHolderMapper(internal var layout_id: Int, internal var viewHolderClass: Class<out BaseViewHolder>)

    /** */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemType: Int = 0

        fun getView(viewID: Int): View {
            return itemView.findViewById(viewID)
        }
    }


    /** */
    interface OnItemClickListener {
        fun onItemClick(adapter: BaseRecyclerViewAdapter<*>, view: View, position: Int)
    }

}
