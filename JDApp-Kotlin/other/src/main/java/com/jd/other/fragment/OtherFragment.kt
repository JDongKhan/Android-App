package com.jd.other.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.jd.core.base.BaseFragment
import com.jd.core.base.adapter.BaseListViewAdapter
import com.jd.core.network.ServiceGenerator.Companion.instance
import com.jd.other.R
import com.jd.other.network.BookService
import com.jd.other.viewholder.OtherViewHolder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.zip.Inflater

/**
 * A simple [Fragment] subclass.
 */
class OtherFragment : BaseFragment() {
    var listView: ListView? = null
    private val items: MutableList<Map<String, Any>> = ArrayList()

    override fun initView(view: View) {
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
        val item2: MutableMap<String, Any> = HashMap()
        item2["title"] = "路由"
        item2["action"] = object : OnOtherClick {
            override fun onClick() {
                ARouter.getInstance().build("/test/activity1").navigation()
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

    private fun testNetwork() {

        instance.createService<BookService>(BookService::class.java)
            .getShop("63.223.108.42")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                Toast.makeText(this@OtherFragment.context, it.data, Toast.LENGTH_LONG).show()
            }



//            ?.subscribe(object : Observer<String?> {
//                override fun onSubscribe(d: Disposable) {
//                    Log.i("1111", "onSubscribe")
//                }
//
//                override fun onNext(s: String) {
//                    Log.i("1111", "onNext:$s")
//                    Toast.makeText(this@OtherFragment.context, s, Toast.LENGTH_LONG).show()
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.i("1111", "onError" + e.message)
//                    Toast.makeText(this@OtherFragment.context, e.message, Toast.LENGTH_LONG).show()
//                }
//
//                override fun onComplete() {
//                    Log.i("1111", "onComplete")
//                }
//            })
    }

    ///////////////////////////////////////
    interface OnOtherClick {
        fun onClick()
    }
}