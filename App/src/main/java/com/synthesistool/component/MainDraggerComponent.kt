package com.synthesistool.component

import com.synthesistool.activity.MainActivity
import com.synthesistool.module.MainDraggerModule
import dagger.Component

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
@Component(modules = [MainDraggerModule::class])
interface MainDraggerComponent {
    fun inject(activity: MainActivity)
}
