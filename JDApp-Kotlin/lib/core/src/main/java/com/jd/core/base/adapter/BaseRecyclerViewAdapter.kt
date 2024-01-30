package com.jd.core.base.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * adapter
 */
abstract class BaseRecyclerViewAdapter<T>(private val mContext: Context, private val mData: List<T>, private val viewHolders: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO 后续寻找优雅的解耦方案 ，因为ViewHolder实例化需要先取view，所以无法从ViewHolder的实例去取布局，故使用了mapper方案
        val layoutId = this.viewHolders[viewType]
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val vh = CommonViewHolder(view)
        vh.itemType = viewType
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as CommonViewHolder
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

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //让外面告诉我映射关系
    protected abstract fun indexOfLayoutsAtPosition(position: Int): Int

    protected abstract fun convert(viewHolder: CommonViewHolder, item: T)



    /** 内部类 */
    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mViews: SparseArray<View> = SparseArray();
        var itemType: Int = 0

        fun getView(viewId: Int): View? {
            var view: View? = mViews.get(viewId);
            if (view == null) {
                view =  itemView.findViewById(viewId)
                mViews.put(viewId,view)
            }
            return view;
        }

        fun setText(viewId: Int,text: String): CommonViewHolder {
           val tv: TextView = getView(viewId) as TextView
            tv.text = text
            return this
        }

        fun setImageResource(viewId: Int,resId: Int): CommonViewHolder {
            val imageView:ImageView = getView(viewId) as ImageView
            imageView.setImageResource(resId)
            return this
        }


    }


    /** */
    interface OnItemClickListener {
        fun onItemClick(adapter: BaseRecyclerViewAdapter<*>, view: View, position: Int)
    }

}
