package com.jd.list.activity

import android.content.Intent
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.alibaba.android.arouter.facade.annotation.Route
import com.jd.core.base.BaseActivity
import com.jd.list.R
import kotlinx.android.synthetic.main.list.*

@Route(path = "/test/activity1")
class ListActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.list
    }
    override fun initView() {
        // 1.准备集合数据
        val strs = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "09", "89", "77", "55", "ut", "sd", "gj", "gjk", "qw", "jhk")
        // 2.准备ArrayAdapter对象
        val adapter = ArrayAdapter(this,
                R.layout.item, strs)
        // 3.设置Adapter显示列表
        listView1!!.adapter = adapter
        listView1!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@ListActivity, ListMainActivity::class.java)
            startActivity(intent)
        }
    }


}
