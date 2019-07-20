package com.jd.home.viewhodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.core.base.adapter.BaseViewHolder;
import com.jd.home.R;

import java.util.Map;

public class ListViewHolder implements BaseViewHolder {
    ImageView imageView;
    TextView textView1;
    TextView textView2;

    @Override
    public int layout_id() {
        return R.layout.home_list_item;
    }

    @Override
    public void onCreateView(View view) {
        imageView = (ImageView) view.findViewById(R.id.image1);
        textView1 = (TextView) view.findViewById(R.id.text1);
        textView2 = (TextView) view.findViewById(R.id.text2);
    }

    @Override
    public void onStart(Object data) {
        Map<String,Object> item = (Map<String,Object>)data;
//        imageView.setImageResource(Datas.get(i).getImageId());
        textView1.setText(item.get("title").toString());
    }
}
