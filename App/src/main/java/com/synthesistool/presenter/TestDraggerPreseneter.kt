package com.synthesistool.presenter

import com.common.tools.LogUtil
import com.synthesistool.view.TestDraggerView
import com.ui.base.BasePresenter
import javax.inject.Inject

/**
 * 示例代码
 * 时间 :  2019/10/6;
 * 版本号 :
 */
class TestDraggerPreseneter @Inject constructor(private var mView: TestDraggerView?) : BasePresenter {

    fun loadData() {
        LogUtil.d("+++++++++++++++++++", "数据加载完成")

        //回调方法成功时
        mView?.refreshView()
    }

    override fun onDestory() {
        mView = null
    }
}
