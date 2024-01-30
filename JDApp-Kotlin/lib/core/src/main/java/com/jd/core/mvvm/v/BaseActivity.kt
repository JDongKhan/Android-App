package com.jd.core.mvvm.v


import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.gyf.barlibrary.ImmersionBar
import com.jd.core.R
import com.jd.core.mvvm.vm.BaseViewModel
import com.jd.core.view.NavigationBar


abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mActivity: BaseActivity
    private var mImmersionBar: ImmersionBar? = null
    private var isDestroyed = false

    //导航
    lateinit var navigationBar: NavigationBar

    //contentView
    private lateinit var mContentView: View

    /*************************** 抽象方法  */
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract fun layoutView() : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        //导航
        this.navigationBar = this.findViewById(R.id.navigation_bar)
        //隐藏导航
        if (this.preferredNavigationBarHidden()) {
            this.navigationBar.visibility = View.GONE
        }

        val layoutView = this.layoutView()
        //添加子contentView
        val rl = this.findViewById<RelativeLayout>(R.id.base_content)
        rl.addView(layoutView)
        this.mContentView = layoutView
        //沉浸式状态栏
        initImmersionBar()
        //setImmeriveStatuBar();
        mActivity = this
        initView()

    }


    private fun initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this)
            mImmersionBar!!.init()
        }
        //        StatuBarCompat.setImmersiveStatusBar(true, Color.WHITE, this);
        // 所有子类都将继承这些相同的属性,暂时先不加,会导入全部状态栏都一致
        // mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.bar_grey).init();
    }

    override fun onDestroy() {
        super.onDestroy()
        //必须调用该方法，防止内存泄漏
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
        }
        isDestroyed = true
    }

    //隐藏导航
    protected open fun preferredNavigationBarHidden(): Boolean {
        return false
    }

    /**
     * 初始化数据
     */
    protected abstract fun initView()

}
