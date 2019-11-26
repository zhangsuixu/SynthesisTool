package com.remotelog.module

import com.remotelog.view.RemoteLogView

import dagger.Module
import dagger.Provides

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Module
class RemoteLogModule(private val mView: RemoteLogView) {

    @Provides
    fun provideMainView(): RemoteLogView {
        return mView
    }
}
