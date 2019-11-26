package com.synthesistool.module

import com.synthesistool.view.TestDraggerView

import dagger.Module
import dagger.Provides

/**
 * 示例代码
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Module
class TestDraggerModule(private val mView: TestDraggerView) {

    @Provides
    fun provideMainView(): TestDraggerView {
        return mView
    }
}
