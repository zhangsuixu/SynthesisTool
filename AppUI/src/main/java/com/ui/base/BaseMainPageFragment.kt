package com.ui.base

/**
 *  首页Fragment基类
 */
abstract class BaseMainPageFragment : BaseFragment(){

    /** 当前页面被选中时触发 */
    abstract fun onFragmentSeleted()

}
