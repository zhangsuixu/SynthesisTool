package com.synthesistool.module

import com.synthesistool.view.MainDraggerView
import dagger.Module
import dagger.Provides

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Module
class MainDraggerModule(private val mView: MainDraggerView) {

    @Provides
    fun provideMainView(): MainDraggerView {
        return mView
    }
}
