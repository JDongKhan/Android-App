package com.jd.core.mvvm.v


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider

import com.gyf.barlibrary.ImmersionBar
import com.jd.core.R
import com.jd.core.mvvm.vm.BaseViewModel
import com.jd.core.view.NavigationBar


abstract class BaseFragment : Fragment() {
    private var mContext: Context? = null
    private var mImmersionBar: ImmersionBar? = null

    //导航
    lateinit var navigationBar: NavigationBar

    //contentView
    private lateinit var mContentView: View
    /***************************** 抽象方法  */
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract fun layoutView() : View


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.base_layout, container, false)
        //导航
        this.navigationBar = view.findViewById(R.id.navigation_bar)
        //隐藏导航
        if (this.preferredNavigationBarHidden()) {
            this.navigationBar.visibility = View.GONE
        }

        val layoutView = this.layoutView()
        //添加子contentView
        val rl = view.findViewById<RelativeLayout>(R.id.base_content)
        rl.addView(layoutView)
        this.mContentView = layoutView
        //沉浸式状态栏
        initImmersionBar()
        return view
    }

    private fun initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this)
            mImmersionBar!!.init()
        }
    }

    /**
     * 获取子ViewModel
     */
    fun findViewModel(clazz:Class<out BaseViewModel>) : BaseViewModel {
        return ViewModelProvider(this)[clazz]
    }

    /**
     * 返回view
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
        }
    }

    /**
     * 隐藏导航
     */
    protected open fun preferredNavigationBarHidden(): Boolean {
        return false
    }

    /**
     * 初始化数据
     */
    protected abstract fun initView(view: View)

    /**
     * 懒加载数据
     */
    protected  abstract  fun loadData()

}
