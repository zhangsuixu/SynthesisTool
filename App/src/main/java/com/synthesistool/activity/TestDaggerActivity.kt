package com.synthesistool.activity

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.common.tools.LogUtil
import com.synthesistool.R
import com.synthesistool.component.DaggerTestDraggerComponent
import com.synthesistool.module.TestDraggerModule
import com.synthesistool.presenter.TestDraggerPreseneter
import com.synthesistool.view.TestDraggerView
import com.ui.base.BaseActivity
import javax.inject.Inject

/**
 * 示例代码
 * 时间 :  2019/10/6;
 */
class TestDaggerActivity : BaseActivity(), TestDraggerView {

    @Inject
    @JvmField
    var mTestDraggerPreseneter: TestDraggerPreseneter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.act_test_dagger)

        DaggerTestDraggerComponent.builder()
                .testDraggerModule(TestDraggerModule(this))
                .build()
                .inject(this)

        setTitleText("测试")
    }

    override fun initData() {
        mTestDraggerPreseneter?.loadData()

        ARouter.getInstance().build("/MRemoteLog/RemoteLogActivity").navigation()
    }

    override fun initEvent() {

    }

    override fun refreshView() {
        LogUtil.d("+++++++++++++++++++", "进行界面刷新")
    }

    override fun onDestroy() {
        mTestDraggerPreseneter?.onDestory()
        super.onDestroy()
    }
}
