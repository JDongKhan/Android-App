package com.jd.core.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.jd.core.R;
import com.jd.core.view.NavigationBar;


public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity mActivity;
    public ImmersionBar mImmersionBar;
    protected boolean isDestory = false;

    //导航
    public NavigationBar navigationBar;

    //contentView
    public View contentView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        //导航
        this.navigationBar = this.findViewById(R.id.navigation_bar);
        //隐藏导航
        if (this.preferredNavigationBarHidden() == true) {
            this.navigationBar.setVisibility(View.GONE);
        }

        int layout_id = this.getLayoutId();
        if (layout_id > 0) {
            //添加子contentView
            RelativeLayout rl = this.findViewById(R.id.base_content);
            View contentView = LayoutInflater.from(this).inflate(layout_id, null);
            rl.addView(contentView);
            this.contentView = contentView;
        }

        //沉浸式状态栏
        initImmersionBar();
        //setImmeriveStatuBar();
        mActivity = this;

        initView();
    }

    private void initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();
        }
//        StatuBarCompat.setImmersiveStatusBar(true, Color.WHITE, this);
        // 所有子类都将继承这些相同的属性,暂时先不加,会导入全部状态栏都一致
        // mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.bar_grey).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须调用该方法，防止内存泄漏
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        isDestory = true;
    }

    //隐藏导航
    protected boolean preferredNavigationBarHidden() {
        return false;
    }

    /*************************** 抽象方法 ***********************************/
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initView();

}
