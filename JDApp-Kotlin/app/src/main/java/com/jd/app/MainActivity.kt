package com.jd.app

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import android.widget.ImageView
import android.widget.TextView


import com.google.android.material.tabs.TabLayout
import com.jd.core.base.BaseActivity
import com.jd.home.fragment.HomeFragment
import com.jd.list.fragment.ListFragment
import com.jd.other.fragment.OtherFragment
import com.jd.setting.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList
import java.util.HashMap


class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    internal var items: MutableList<Map<String, Any>> = ArrayList()

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        //初始化数据
        this.initData()
        //viewpager
        viewpager!!.addOnPageChangeListener(this)
        viewpager!!.adapter = ViewPagerAdapter(supportFragmentManager)

        tab_layout!!.setupWithViewPager(viewpager)

        setTabLayout()

        tab_layout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                selectTab(tab)
            }
        })

    }

    private fun setTabLayout() {
        for (i in items.indices) {
            val item = items[i]
            val tab = tab_layout!!.getTabAt(i)
            tab!!.setCustomView(R.layout.view_main_tab)
            val tabView = tab.customView

            val iconView = tabView!!.findViewById<ImageView>(R.id.tab_icon_iv)
            val titleView = tabView.findViewById<TextView>(R.id.tab_title_tv)

            val selectedImage = Integer.valueOf(item["selectedImage"]!!.toString())
            val image = Integer.valueOf(item["image"]!!.toString())

            val titleString = item["title"]!!.toString()

            titleView.text = titleString
            titleView.setTextColor(ContextCompat.getColor(this, R.color.black))

            if (i == 0) {
                iconView.setImageResource(selectedImage)
            } else {
                iconView.setImageResource(image)
            }

        }
    }

    private fun selectTab(tab: TabLayout.Tab) {
        for (i in items.indices) {
            val item = items[i]
            val tmpTab = tab_layout!!.getTabAt(i)


            val iconView = tmpTab!!.customView!!.findViewById<ImageView>(R.id.tab_icon_iv)
            val titleView = tmpTab.customView!!.findViewById<TextView>(R.id.tab_title_tv)
            val image = Integer.valueOf(item["image"]!!.toString())
            val selectedImage = Integer.valueOf(item["selectedImage"]!!.toString())

            if (tab === tmpTab) {
                iconView.setImageResource(selectedImage)
            } else {
                iconView.setImageResource(image)
            }
        }
    }


    private fun initData() {
        val item1 = HashMap<String, Any>()
        item1["title"] = "首页"
        item1["image"] = R.drawable.icon_one_selected
        item1["selectedImage"] = R.mipmap.tab_home_selected
        item1["fragment"] = HomeFragment()
        items.add(item1)


        val item2 = HashMap<String, Any>()
        item2["title"] = "列表"
        item2["image"] = R.drawable.icon_two_selected
        item2["selectedImage"] = R.mipmap.tab_home_selected
        item2["fragment"] = ListFragment()
        items.add(item2)

        val item3 = HashMap<String, Any>()
        item3["title"] = "功能菜单"
        item3["image"] = R.drawable.icon_three_selected
        item3["selectedImage"] = R.mipmap.tab_home_selected
        item3["fragment"] = OtherFragment()
        items.add(item3)

        val item4 = HashMap<String, Any>()
        item4["title"] = "设置"
        item4["image"] = R.drawable.icon_four_selected
        item4["selectedImage"] = R.mipmap.tab_home_selected
        item4["fragment"] = SettingFragment()
        items.add(item4)
    }

    /******************************* viewpage   */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    internal inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return (items[position]["fragment"] as Fragment?)!!
        }

        override fun getCount(): Int {
            return 4
        }
    }

    /**
     * 隐藏导航
     */
    override fun preferredNavigationBarHidden(): Boolean {
        return true
    }


}
