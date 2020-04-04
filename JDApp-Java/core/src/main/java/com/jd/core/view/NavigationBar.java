package com.jd.core.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.jd.core.R;


/**
 * @author wangjindong
 */
public class NavigationBar extends RelativeLayout {

    private int height = 80;
    private int padding_top = 20;

    //backView
    private LinearLayout navigation_back;
    private TextView navigation_back_text;

    //titleView
    private TextView navigation_title;

    //detailView
    private TextView navigation_detail;

    //rightViews
    private LinearLayout rightView;



    public NavigationBar(Context context) {
        super(context);
        this.createView(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.createView(context);
    }

    private void createView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this, true);
        this.navigation_back = this.findViewById(R.id.navigation_back);
        this.navigation_back_text = this.findViewById(R.id.navigation_back_text);
        this.navigation_title = this.findViewById(R.id.navigation_title);
        this.navigation_detail = this.findViewById(R.id.navigation_detail);
        this.rightView = this.findViewById(R.id.navigation_rightView);
        //返回事件
        this.navigation_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }
        });
    }

    //隐藏左侧返回按钮
    public void setBackViewHidden(boolean hidden) {
        if (hidden) {
            this.navigation_back.setVisibility(View.GONE);
        } else {
            this.navigation_back.setVisibility(View.VISIBLE);
        }
    }

    //设置title
    public void setTitle(String title) {
        this.navigation_title.setText(title);
    }

    public void setTitle(int title) {
        this.navigation_title.setText(title);
    }
}
