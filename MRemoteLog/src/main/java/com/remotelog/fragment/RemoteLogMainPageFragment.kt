package com.remotelog.fragment

import android.view.View
import com.common.tools.ToastUtils
import com.remotelog.R
import com.ui.base.BaseMainPageFragment

class RemoteLogMainPageFragment : BaseMainPageFragment() {

    override fun onFragmentSeleted() {
        //TODO 远程日志被选中
    }

    override fun initView(): View? {
        return View.inflate(context, R.layout.fgm_remote_log_view, null)
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}
