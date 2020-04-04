@file:Suppress("unused")

package com.ui.base

import android.app.Application
import androidx.multidex.MultiDex

import com.alibaba.android.arouter.launcher.ARouter
import com.common.base.BaseApp
import com.common.base.ComponentApplication

/** 底层会反射调用 忽删除*/
class UiApp : ComponentApplication() {

    public override fun init(application: Application) {
        MultiDex.install(application) //解除65536方法数限制

        ARouter.init(application)   //初始化路由器
    }

    override fun lazyInit(application: Application) {

    }

    fun getBaseApp(): BaseApp {
        return BaseApp.getBaseApp()
    }
}
