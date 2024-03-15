package com.jd.core.base;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.jd.core.R;
import com.jd.core.utils.AppLog;
import com.jd.core.view.NavigationBar;


public abstract class BaseFragment extends Fragment {
    protected Context mContext;

    public ImmersionBar mImmersionBar;

    //导航
    public NavigationBar navigationBar;

    //contentView
    public View contentView;

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
        View view = inflater.inflate(R.layout.base_layout, container, false);
        //导航
        this.navigationBar = view.findViewById(R.id.navigation_bar);
        //隐藏导航
        if (this.preferredNavigationBarHidden() == true) {
            this.navigationBar.setVisibility(View.GONE);
        }

        int layout_id = this.getLayoutId();
        if (layout_id > 0) {
            //添加子contentView
            RelativeLayout rl = view.findViewById(R.id.base_content);
            View contentView = inflater.inflate(layout_id, null, false);
            rl.addView(contentView);
            this.contentView = contentView;
        }
        //沉浸式状态栏
        initImmersionBar();
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
        initView(view);
    }


    @Override
    public void onResume() {
        super.onResume();
        AppLog.i("当前运行的fragment:" + getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    /**
     * 隐藏导航
     */
    protected boolean preferredNavigationBarHidden() {
        return false;
    }

    /***************************** 抽象方法 ***********************************/
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initView(View view);

}
