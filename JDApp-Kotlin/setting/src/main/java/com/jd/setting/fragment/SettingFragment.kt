package com.jd.setting.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jd.core.base.BaseFragment
import com.jd.setting.R


/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_setting

    override fun initView(view: View) {
        this.navigationBar.setBackViewHidden(true)
        this.navigationBar.setTitle("功能")

    }

    override fun loadData() {
    }


}// Required empty public constructor
