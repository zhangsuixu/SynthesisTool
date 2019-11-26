package com.remotelog.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.alibaba.android.arouter.facade.annotation.Route
import com.common.tools.LogUtil
import com.remotelog.R
import com.remotelog.component.DaggerRemoteLogComponent
import com.remotelog.module.RemoteLogModule
import com.remotelog.presenter.RemoteLogPreseneter
import com.remotelog.view.RemoteLogView

import javax.inject.Inject

/**
 * 时间 :  2019/10/3;
 * 版本号 :
 */
@Route(path = "/MRemoteLog/RemoteLogActivity")
class RemoteLogActivity : AppCompatActivity(), RemoteLogView {

    @Inject
    @JvmField
    var mRemoteLogPreseneter: RemoteLogPreseneter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_remote_log)

        DaggerRemoteLogComponent.builder()
                .remoteLogModule(RemoteLogModule(this))
                .build()
                .inject(this)

        mRemoteLogPreseneter?.loadData()
    }

    override fun refreshView() {
        LogUtil.d("+++++++++++++++++++", "RemoteLog模块 刷新数据")

    }
}
