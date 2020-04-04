package com.remotelog.fragment

import android.view.View
import com.remotelog.R
import com.ui.base.BaseMainPageFragment

class RemoteLogFragment : BaseMainPageFragment() {

    override fun onFragmentSeleted() {
        //TODO 远程日志被选中
    }

    override fun initLayout(): View? {
        return View.inflate(context, R.layout.fgm_m_remote_log, null)
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}
