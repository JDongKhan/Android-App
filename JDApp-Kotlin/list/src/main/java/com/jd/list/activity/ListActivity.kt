package com.jd.list.activity

import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.alibaba.android.arouter.facade.annotation.Route
import com.jd.config.RoutePath
import com.jd.core.base.BaseActivity
import com.jd.list.R
import com.jd.list.databinding.ListBinding

@Route(path = RoutePath.List.TEST)
class ListActivity : BaseActivity() {
    private lateinit var binding:ListBinding

    override fun layoutView(): View {
        binding = ListBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun initView() {
        // 1.准备集合数据
        val strs = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "09", "89", "77", "55", "ut", "sd", "gj", "gjk", "qw", "jhk")
        // 2.准备ArrayAdapter对象
        val adapter = ArrayAdapter(this,
                R.layout.item, strs)
        // 3.设置Adapter显示列表
        binding.listView1.adapter = adapter
        binding.listView1.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@ListActivity, ListMainActivity::class.java)
            startActivity(intent)
        }
    }


}
