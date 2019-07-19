package com.jd.core.base;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.jd.core.utils.AppLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    private Unbinder unBinder;
    public ImmersionBar mImmersionBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unBinder = ButterKnife.bind(this , view);
        //沉浸式状态栏
        initImmersionBar();
        initTitle();

        return view ;
    }

    /**
     * 返回view
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        init(view);
        initView();
    }


    @Override
    public void onResume() {
        super.onResume();
        AppLog.i("当前运行的fragment:" + getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (mImmersionBar != null){
            mImmersionBar.destroy();
        }
    }


    private void initImmersionBar() {
        if (mImmersionBar == null){
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();
        }
    }

    protected void init(View view){};
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayout();

    /**
     * 初始化标题
     */
    protected abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initView();

}
