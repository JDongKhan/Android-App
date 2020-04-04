package com.jd.home.fragment


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.view.View

import com.jd.core.base.BaseFragment
import com.jd.home.R
import com.jd.home.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*

import java.util.ArrayList
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private val items = ArrayList<Map<String, Any>>()

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun initView(view: View) {
        this.initList()
    }

    private fun initList() {
        this.initData()
        // 设置布局
        val linearLayoutManager = LinearLayoutManager(this.context)
        //设置滑动方向：纵向
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView!!.layoutManager = linearLayoutManager
        //添加Android自带的分割线
        recyclerView!!.addItemDecoration(DividerItemDecoration(this.context!!, DividerItemDecoration.VERTICAL))

        val viewHolders = ArrayList<Int>()
        viewHolders.add(R.layout.banner_pager)
        viewHolders.add(R.layout.home_list_item)

        //设置适配器
        recyclerView!!.adapter = HomeAdapter(this.activity!!, this.items, viewHolders)
    }

    private fun initData() {
        val item0 = HashMap<String, Any>()
        items.add(item0)

        val item1 = HashMap<String, Any>()
        item1["title"] = "LIST DEMO"
        items.add(item1)

        val item2 = HashMap<String, Any>()
        item2["title"] = "GRID DEMO"
        items.add(item2)

        val item3 = HashMap<String, Any>()
        item3["title"] = "SECOND_GRID"
        items.add(item3)

        val item4 = HashMap<String, Any>()
        item4["title"] = "自定义List"
        items.add(item4)
    }

    private fun push(activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(this.activity, activityClass)
        this.startActivity(intent)
    }


    override fun preferredNavigationBarHidden(): Boolean {
        return true
    }

}// Required empty public constructor
