package com.home.fragment

import android.view.View
import com.home.R
import com.ui.base.BaseMainPageFragment

/**
 *  首页模块
 *
 *  @author xu
 *  @time   2019/12/20
 */
@Suppress("unused")
class HomeFragment : BaseMainPageFragment() {

    override fun onFragmentSeleted() {
        //TODO 首页被选中
    }

    override fun initLayout(): View? {
        return View.inflate(context, R.layout.fgm_home_view, null)
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}
