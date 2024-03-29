package com.jd.home.fragment


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.*

import com.jd.core.mvvm.v.BaseFragment
import com.jd.core.base.adapter.BaseRecyclerViewAdapter
import com.jd.home.R
import com.jd.home.activity.HomeDetailActivity
import com.jd.home.adapter.HomeAdapter
import com.jd.home.databinding.FragmentHomeBinding

import java.util.ArrayList
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding:FragmentHomeBinding

    private var shopImage = intArrayOf(
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
//            R.drawable.shop_4,
            R.drawable.default_icon
    )
    private val items = ArrayList<Map<String, Any>>()

    override fun layoutView(): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView(view: View) {

        this.initList()
    }

    override fun loadData() {
    }

    private fun initList() {
        this.initData()

        //适配器
        val viewHolders = ArrayList<Int>()
        viewHolders.add(R.layout.home_fragment_banner_pager)
        viewHolders.add(R.layout.home_fragment_menu)
        viewHolders.add(R.layout.home_fragment_list_item)

        var homeAdapter =  HomeAdapter(requireContext(), this.items, viewHolders)
        //设置适配器
        binding.recyclerView.adapter = homeAdapter
        homeAdapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseRecyclerViewAdapter<*>, view: View, position: Int) {
                push(HomeDetailActivity::class.java)
            }
        })

        // 设置布局
        val gridLayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //设置滑动方向：纵向
        binding.recyclerView.layoutManager = gridLayoutManager
        //添加Android自带的分割线
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

    }

    private fun initData() {
        //广告占位
        val item0 = HashMap<String, Any>()
        items.add(item0)

        //菜单占位
        val item1 = HashMap<String, Any>()
        items.add(item1)

        for ( i in 0 .. 30) {
            val item2 = HashMap<String, Any>()
            item2["title"] = "每一个商品都有灵魂-$i"
            item2["image"] = shopImage[i%4]
            items.add(item2)
        }
    }

    private fun push(activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(this.activity, activityClass)
        this.startActivity(intent)
    }


    override fun preferredNavigationBarHidden(): Boolean {
        return true
    }

}// Required empty public constructor
