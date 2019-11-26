package com.remotelog.component

import com.remotelog.activity.RemoteLogActivity
import com.remotelog.module.RemoteLogModule

import dagger.Component

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Component(modules = [RemoteLogModule::class])
interface RemoteLogComponent {
    fun inject(activity: RemoteLogActivity)
}
