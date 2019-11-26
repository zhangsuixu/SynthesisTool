package com.locallog.fragment

import android.view.View
import com.common.tools.ToastUtils
import com.locallog.R
import com.ui.base.BaseMainPageFragment

class LocalLogMainPageFragment : BaseMainPageFragment() {

    override fun onFragmentSeleted() {
        //TODO 本地日志被选中
    }

    override fun initView(): View? {
        return View.inflate(context, R.layout.fgm_local_log_view, null)
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}
