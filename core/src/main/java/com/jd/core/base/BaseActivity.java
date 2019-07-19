package com.jd.core.base;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unBinder;
    public BaseActivity mActivity ;
    public ImmersionBar mImmersionBar;
    protected boolean isDestory = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unBinder = ButterKnife.bind(this);
        //沉浸式状态栏
        initImmersionBar();
        //setImmeriveStatuBar();
        mActivity = this ;

        initTitle();
        initView();
    }

    private void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        // 所有子类都将继承这些相同的属性,暂时先不加,会导入全部状态栏都一致
        // mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.bar_grey).init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null) {
            unBinder.unbind();
        }
        //必须调用该方法，防止内存泄漏
        if (mImmersionBar != null){
            mImmersionBar.destroy();
        }
        isDestory = true;
    }



    /**
     * 沉浸式状态栏
     */
    protected void setImmeriveStatuBar() {
        mImmersionBar.init();
//        StatuBarCompat.setImmersiveStatusBar(true, Color.WHITE, this);
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化标题
     */
    protected abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initView();

}
