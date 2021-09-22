package com.jd.home.activity

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jd.core.base.BaseActivity
import com.jd.home.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeDetailActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.home_detail_activity
    }

    override fun initView() {
        this.navigationBar.setTitle("首页-详情")
        // 设置布局
        val linearLayoutManager = LinearLayoutManager(this)
        //设置滑动方向：纵向
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }
}