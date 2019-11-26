package com.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.common.tools.EventBusUtils

/**
 *  1.在子类中直接通过 EventBusUtils.register(this) 可直接注册,基类已处理销毁操作
 */
abstract class BaseFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
