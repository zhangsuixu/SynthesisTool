package com.home.fragment

import android.view.View
import com.common.tools.ToastUtils
import com.home.R
import com.ui.base.BaseMainPageFragment

class HomeMainPageFragment : BaseMainPageFragment(){

    override fun onFragmentSeleted() {
        //TODO 首页被选中
    }

    override fun initView(): View? {
        return View.inflate(context, R.layout.fgm_home_view, null)
    }

    override fun initData() {

    }

    override fun initEvent() {

    }


}
