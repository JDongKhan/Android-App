package com.jd.other.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.jd.config.RoutePath
import com.jd.core.mvvm.v.BaseFragment
import com.jd.core.base.adapter.BaseListViewAdapter
import com.jd.core.log.LogUtils
import com.jd.core.network.Network.Companion.instance
import com.jd.other.R
import com.jd.other.network.BookService
import com.jd.other.viewholder.OtherViewHolder
import com.jd.other.vm.OtherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class OtherFragment : BaseFragment() {
    private var listView: ListView? = null
    private val items: MutableList<Map<String, Any>> = ArrayList()
    private lateinit var viewModel: OtherViewModel

    override fun initView(view: View) {
        viewModel = ViewModelProvider(this)[OtherViewModel::class.java]
        listView = view.findViewById(R.id.simpleListView)
        navigationBar.setBackViewHidden(true)
        navigationBar.setTitle("功能")
        initData()
        val lazyAdapter: BaseListViewAdapter =
            object : BaseListViewAdapter(requireActivity(), items, OtherViewHolder::class.java) {
                override fun indexOfLayoutsAtPosition(position: Int): Int {
                    return 0
                }
            }
        //导入
        listView?.adapter = lazyAdapter

        //点击事件
        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = items[position]
            val click = item["action"] as OnOtherClick?
            click?.onClick()
        }
    }

    override fun loadData() {

    }

    override fun layoutView(): View {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_other,null)
    }

    override fun preferredNavigationBarHidden(): Boolean {
        return false
    }

    private fun initData() {
        val item1: MutableMap<String, Any> = HashMap()
        item1["title"] = "网络"
        item1["action"] = object : OnOtherClick {
            override fun onClick() {
                testNetwork()
            }
        }
        items.add(item1)

        val item11: MutableMap<String, Any> = HashMap()
        item11["title"] = "网络2"
        item11["action"] = object : OnOtherClick {
            override fun onClick() {
                testNetwork2()
            }
        }
        items.add(item11)


        val item2: MutableMap<String, Any> = HashMap()
        item2["title"] = "路由"
        item2["action"] = object : OnOtherClick {
            override fun onClick() {
                ARouter.getInstance().build(RoutePath.List.TEST).navigation(context)
            }
        }
        items.add(item2)
        val item3: MutableMap<String, Any> = HashMap()
        item3["title"] = "功能菜单"
        item3["action"] = object : OnOtherClick {
            override fun onClick() {}
        }
        items.add(item3)
        val item4: MutableMap<String, Any> = HashMap()
        item4["title"] = "设置"
        item4["action"] = object : OnOtherClick {
            override fun onClick() {}
        }
        items.add(item4)
    }


    /**
     * 第一种用法
     */
    private fun testNetwork() {
        val result =  instance.createService(BookService::class.java).getShop("63.223.108.42")
        result?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
        val  s = result?.subscribe({
            Toast.makeText(this@OtherFragment.context, it.data, Toast.LENGTH_LONG).show()
        }) {
            Toast.makeText(this@OtherFragment.context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 第二种用法
     */
    fun getShop() {
        lifecycleScope.launch {
            val responseAsync = async ( SupervisorJob() + Dispatchers.IO) {
                return@async instance.createService(BookService::class.java).getShop2("63.223.108.42")
            }
            val result = responseAsync.await()
            LogUtils.d("network",result.data)
        }
    }

    private fun testNetwork2(){
        viewModel.text.observe(viewLifecycleOwner
        ) { t ->
            LogUtils.d("network", "数据变化了$t")
        }
        viewModel.request()
    }

    ///////////////////////////////////////
    interface OnOtherClick {
        fun onClick()
    }
}