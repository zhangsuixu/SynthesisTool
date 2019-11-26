package com.locallog

import android.app.Application
import android.util.Log

import com.common.base.ComponentApplication
import com.common.tools.LogUtil

/**
 * 在BaseApp通过反射执行
 */
class LocalLogApplication : ComponentApplication() {

    public override fun init(application: Application) {
    }

    override fun lazyInit(application: Application) {
    }
}
