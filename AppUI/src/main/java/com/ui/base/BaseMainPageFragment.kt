package com.ui.base

/**
 *  首页Fragment基类
 */
abstract class BaseMainPageFragment : BaseFragment(){

    /** 当前页面被选中时触发,进行实际的初始化 */
    abstract fun onFragmentSeleted()

}
