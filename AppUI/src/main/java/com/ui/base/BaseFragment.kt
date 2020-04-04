package com.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.common.tools.EventBusUtils
import com.common.tools.LogUtil

abstract class BaseFragment : androidx.fragment.app.Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtil.i(this.javaClass.name)

        return initLayout() ?: return TextView(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
        initEvent()

        super.onActivityCreated(savedInstanceState)
    }

    abstract fun initLayout() : View?

    abstract fun initView()

    abstract fun initData()

    abstract fun initEvent()

    override fun onDestroy() {
        EventBusUtils.unregister(this)
        super.onDestroy()
    }
}
