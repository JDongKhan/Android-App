package com.jd.home.activity

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jd.core.base.BaseActivity
import com.jd.home.R
import com.jd.home.databinding.HomeDetailActivityBinding

class HomeDetailActivity : BaseActivity() {

    private lateinit var binding: HomeDetailActivityBinding
    override fun layoutView(): View {
        binding = HomeDetailActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        this.navigationBar.setTitle("首页-详情")
        // 设置布局
        val linearLayoutManager = LinearLayoutManager(this)
        //设置滑动方向：纵向
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerView.layoutManager = linearLayoutManager
        //添加Android自带的分割线
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }
}