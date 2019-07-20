package com.jd.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.core.view.BannerPager;
import com.jd.home.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final List mData;

    public RecyclerViewAdapter(Context context,List mData) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_pager,parent,false);
            return new BannerViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            BannerViewHolder viewholder = (BannerViewHolder) holder;
            viewholder.showData();
            return;
        }
        ViewHolder viewholder = (ViewHolder) holder;
        Map map = (Map) mData.get(position-1);
        viewholder.setData(map);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return 1+ mData.size();
    }



    private static class BannerViewHolder extends RecyclerView.ViewHolder {
        BannerPager bannerPager;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerPager = (BannerPager) itemView;
            bannerPager.setOnPageClickListener(new BannerPager.OnPageClickListener() {
                @Override
                public void onPageClick(int position) {

                }
            });
        }

        public void showData() {
            bannerPager.setAdapter(new IndexBannderAdapter(itemView.getContext(), initBanner()));
        }

        private List initBanner() {
            List<Integer> list = new ArrayList<>();
            list.add(R.mipmap.ic_banner_1);
            list.add(R.mipmap.ic_banner_2);
            list.add(R.mipmap.ic_banner_3);
            list.add(R.mipmap.ic_banner_4);
            list.add(R.mipmap.ic_banner_5);
            return list;
        }

    }

    /**
     * RecyclerView的持有者类
     */
    private static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        private Map map;

        public void setData(Object data) {
            this.map = (Map)data;
            this.textView1.setText(map.get("title").toString());
        }

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image1);
            textView1 = (TextView) view.findViewById(R.id.text1);
            textView2 = (TextView) view.findViewById(R.id.text2);

            //添加点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnListClick click = (OnListClick) map.get("action");
                    if (click != null) {
                        click.onClick();
                    }
                }
            });
        }

    }

    ///////////////////////////////////////
    public static interface  OnListClick {
        public void onClick();
    }

}
