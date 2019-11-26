package com.remotelog.presenter

import android.util.Log
import com.common.tools.LogUtil

import com.remotelog.view.RemoteLogView

import javax.inject.Inject

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
class RemoteLogPreseneter {

    private val mView: RemoteLogView

    @Inject
    public constructor(mView: RemoteLogView) {
        this.mView = mView
    }

    fun loadData() {
        LogUtil.d("+++++++++++++++++++", "RemoteLog模块 数据加载完成")

        //回调方法成功时
        mView.refreshView()
    }
}
