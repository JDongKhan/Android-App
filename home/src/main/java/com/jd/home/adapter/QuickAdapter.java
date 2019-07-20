package com.jd.home.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jd.home.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;


public class QuickAdapter extends BaseQuickAdapter<Map,BaseViewHolder> {

    public QuickAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Map item) {
        viewHolder.setText(R.id.text1, item.get("title").toString());
        viewHolder.setText(R.id.text2, "");
        Glide.with(mContext).load(R.drawable.default_icon).into((ImageView)viewHolder.getView(R.id.image1));
    }

}
