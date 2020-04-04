package com.jd.core.base.adapter;



import java.util.List;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListViewAdapter extends BaseAdapter {
	private List<?> dataList;
	private LayoutInflater layoutInflater;
	private Class<?>[] viewHolders = null;
	public Context context;

	public BaseListViewAdapter(Context context, List<?> dataList, Class<? extends  BaseViewHolder>... viewHolders) {
		this.dataList = dataList;
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.viewHolders = viewHolders;
	}

	@Override
	public int getCount() {
		if(dataList == null){
			return 0;
		}
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		int type  = this.indexOfLayoutsAtPosition(position);
		if(type >= 0){
			return type;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		if(this.viewHolders == null){
			return 1;
		}
		return this.viewHolders.length;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		BaseViewHolder viewHold = null;
		if (convertView == null) {
			int index = this.indexOfLayoutsAtPosition(position);
			//取viewHolderClass类型
			Class<?> viewHolderClass = this.viewHolders[index];
			try {
				viewHold = (BaseViewHolder)viewHolderClass.newInstance();
			} catch (IllegalAccessException e) {
				Log.e("BaseListViewAdapter",e.getMessage());
			} catch (InstantiationException e) {
				Log.e("BaseListViewAdapter",e.getMessage());
			}
			int layout_id = viewHold.layout_id();
			convertView = layoutInflater.inflate(layout_id, null);
			if (convertView == null) {
				Log.e("BaseListViewAdapter", "请在[" + BaseViewHolder.class + "]类里面实现layout_id");
			} else {
				// -------------------绑定控件到viewHolder------------------------------------
				viewHold.onCreateView(convertView);
				// 缓存绑定
				convertView.setTag(viewHold);
			}
		} else {
			viewHold = (BaseViewHolder)convertView.getTag();
		}
		viewHold.onStart(this.getItem(position));
		return convertView;
	}



	public abstract int indexOfLayoutsAtPosition(int position);


	/*****************************************************/
	public static interface BaseViewHolder {

		public int layout_id();

		public void onCreateView(View view);

		public void onStart(Object data);
	}


}

