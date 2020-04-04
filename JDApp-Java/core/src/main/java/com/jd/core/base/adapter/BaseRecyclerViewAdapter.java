package com.jd.core.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final List<T> mData;
    private List<Integer> viewHolders;
    private OnItemClickListener itemClickListener;

    public BaseRecyclerViewAdapter(Context context, List<T> mData, List<Integer> viewHolders) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.mData = mData;
        this.viewHolders = viewHolders;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO 寻找优雅的解耦方案 ，因为ViewHolder实例化需要先取view，所以无法从ViewHolder的实例去取布局，故使用了mapper方案
        Integer layout_id = this.viewHolders.get(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(layout_id,parent,false);
        BaseViewHolder vh = new BaseViewHolder(view);
        vh.setItemType(viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        final BaseViewHolder viewHolder = (BaseViewHolder) holder;
        this.convert(viewHolder, mData.get(position));
        //添加点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(BaseRecyclerViewAdapter.this,viewHolder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return this.indexOfLayoutsAtPosition(position);
    }

    //让外面告诉我映射关系
    protected abstract int indexOfLayoutsAtPosition(int position);

    protected abstract void convert(BaseViewHolder viewHolder, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /*******************************************************/
    public static class ViewHolderMapper {
        Class<? extends BaseViewHolder> viewHolderClass;
        int layout_id;
        public ViewHolderMapper(int layout_id, Class<? extends BaseViewHolder> viewHolderClass) {
            this.layout_id = layout_id;
            this.viewHolderClass = viewHolderClass;
        }
    }

    /*******************************************************/
    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private int itemType;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public View getView(int viewID) {
            return itemView.findViewById(viewID);
        }
    }


    /*******************************************************/
    public static interface  OnItemClickListener {
        void onItemClick(BaseRecyclerViewAdapter adapter, View view, int position);
    }

}
