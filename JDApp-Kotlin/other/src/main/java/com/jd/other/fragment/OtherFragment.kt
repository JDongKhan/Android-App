package com.jd.other.fragment


import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast

import com.alibaba.android.arouter.launcher.ARouter
import com.jd.core.base.BaseFragment
import com.jd.core.base.adapter.BaseListViewAdapter
import com.jd.core.network.ServiceGenerator
import com.jd.other.R
import com.jd.other.network.BookService
import com.jd.other.viewholder.OtherViewHolder
import kotlinx.android.synthetic.main.fragment_other.*

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap


/**
 * A simple [Fragment] subclass.
 */
class OtherFragment : BaseFragment() {

    private val items = ArrayList<Map<String, Any>>()

    override fun layoutView(): View {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_other,null)
    }
    override fun initView(view: View) {
        this.navigationBar.setBackViewHidden(true)
        this.navigationBar.setTitle("功能")

        this.initData()
        val lazyAdapter = object : BaseListViewAdapter(requireActivity(), this.items, OtherViewHolder::class.java) {
            override fun indexOfLayoutsAtPosition(position: Int): Int {
                return 0
            }
        }
        simpleListView!!.adapter = lazyAdapter //导入

        //点击事件
        simpleListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = this@OtherFragment.items[position]
            val click = item["action"] as OnOtherClick?
            click?.onClick()
        }
    }

    override fun loadData() {
    }


    override fun preferredNavigationBarHidden(): Boolean {
        return false
    }

    private fun initData() {
        val item1 = HashMap<String, Any>()
        item1["title"] = "网络"
        item1["action"] = object : OnOtherClick {
            override fun onClick() {
                testNetwork()
            }
        }
        items.add(item1)

        val item2 = HashMap<String, Any>()
        item2["title"] = "路由"
        item2["action"] = object : OnOtherClick {
            override fun onClick() {
                ARouter.getInstance().build("/test/activity1").navigation()
            }
        }
        items.add(item2)

        val item3 = HashMap<String, Any>()
        item3["title"] = "功能菜单"
        item3["action"] = object : OnOtherClick {
            override fun onClick() {

            }
        }
        items.add(item3)

        val item4 = HashMap<String, Any>()
        item4["title"] = "设置"
        item4["action"] = object : OnOtherClick {
            override fun onClick() {

            }
        }
        items.add(item4)
    }


    private fun testNetwork() {
        val call = ServiceGenerator.createService(BookService::class.java).getShop("63.223.108.42")
        Toast.makeText(activity,"网络请求开始",Toast.LENGTH_LONG).show();
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val body = response.body()
                    if (body != null) {
                        val s = body.string()
                        Log.e("network", s)
                    }
                    Toast.makeText(activity,"网络请求成功",Toast.LENGTH_LONG).show();
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("network", t.toString())
                Toast.makeText(activity,"网络请求失败",Toast.LENGTH_LONG).show();
            }
        })
    }


    ///////////////////////////////////////
    interface OnOtherClick {
        fun onClick()
    }
}// Required empty public constructor
