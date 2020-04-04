package com.synthesistool.fragment

import android.annotation.SuppressLint
import com.ui.base.BaseMainPageFragment
import java.util.*

/**
 * 通过工厂获取首页所需指定的Fragment,若没所需Fragment,返回EmptyMainPageFragment();
 */
object FragmentFactory {

    private const val FRAGMENT_HOME = 1
    private const val FRAGMENT_LOCAL_LOG = 2
    private const val FRAGMENT_REMOTE_LOG = 3

    private const val FRAGMENT_HOME_PACKAGE_NAME = "com.home.fragment.HomeFragment"
    private const val FRAGMENT_LOCAL_LOG_PACKAGE_NAME = "com.locallog.fragment.LocalLogFragment"
    private const val FRAGMENT_REMOTE_LOG_PACKAGE_NAME =  "com.remotelog.fragment.RemoteLogFragment"

    @SuppressLint("UseSparseArrays")
    val mFragments = HashMap<Int, BaseMainPageFragment>()

    val fragments: Map<Int, BaseMainPageFragment>
        get() = mFragments

    val count: Int
        get() = mFragments.size

    fun clearFragments() {
        mFragments.clear()
    }

    /**
     * 通过反射获取首页Fragment,不同Fragment所属module不同,不依赖时显示空布局
     */
    fun getFragment(position: Int): BaseMainPageFragment {
        var mainPageFragment: BaseMainPageFragment

        if (mFragments.containsKey(position)) {
            return mFragments[position]!!
        }

        when (position) {
            FRAGMENT_HOME -> mainPageFragment = try {
                Class.forName(FRAGMENT_HOME_PACKAGE_NAME).newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                EmptyMainPageFragment()
            }

            FRAGMENT_LOCAL_LOG -> mainPageFragment = try {
                Class.forName(FRAGMENT_LOCAL_LOG_PACKAGE_NAME).newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                EmptyMainPageFragment()
            }

            FRAGMENT_REMOTE_LOG -> mainPageFragment = try {
                Class.forName(FRAGMENT_REMOTE_LOG_PACKAGE_NAME).newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                EmptyMainPageFragment()
            }

            else -> mainPageFragment = EmptyMainPageFragment()
        }

        mFragments[position] = mainPageFragment

        return mainPageFragment
    }
}
