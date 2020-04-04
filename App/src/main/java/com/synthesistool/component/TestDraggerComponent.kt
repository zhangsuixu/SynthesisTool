package com.synthesistool.component

import com.synthesistool.activity.TestDaggerActivity
import com.synthesistool.module.TestDraggerModule

import dagger.Component

/**
 * 示例代码
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Component(modules = [TestDraggerModule::class])
interface TestDraggerComponent {
    fun inject(activity: TestDaggerActivity)
}
