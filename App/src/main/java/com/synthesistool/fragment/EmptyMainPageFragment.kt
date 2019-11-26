package com.synthesistool.fragment

import android.view.View
import com.common.tools.ToastUtils

import com.ui.base.BaseMainPageFragment

class EmptyMainPageFragment : BaseMainPageFragment() {

    override fun onFragmentSeleted() {
        ToastUtils.showCenterSucceedToast("EmptyMainPageFragment 被选中了")
    }

    override fun initView(): View? {
        return null
    }

    override fun initData() {

    }

    override fun initEvent() {

    }


}