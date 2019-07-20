package com.jd.home.adapter;

import android.content.Context;
import android.widget.TextView;

import com.jd.core.base.adapter.BaseRecyclerViewAdapter;
import com.jd.core.view.BannerPager;
import com.jd.home.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeAdapter extends BaseRecyclerViewAdapter<Map> {

    public HomeAdapter(Context context, List mData, List<Integer> viewHolders) {
        super(context, mData, viewHolders);
    }

    @Override
    protected int indexOfLayoutsAtPosition(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Map item) {
        if(viewHolder.getItemType() == 0) {
            BannerPager bannerPager = (BannerPager) viewHolder.itemView;
            bannerPager.setAdapter(new IndexBannderAdapter(bannerPager.getContext(), initBanner()));
            return;
        } else {
            TextView textView = (TextView) viewHolder.getView(R.id.text1);
            textView.setText(item.get("title").toString());
        }
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
