package com.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.common.tools.EventBusUtils
import com.common.tools.LogUtil

abstract class BaseFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtil.i(this.javaClass.name)

        val contentView : View = initView() ?: return TextView(context)

        initData()
        initEvent()

        return contentView
    }

    abstract fun initView() : View?

    abstract fun initData()

    abstract fun initEvent()

    override fun onDestroy() {
        EventBusUtils.unregister(this)
        super.onDestroy()
    }
}
