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
            FRAGMENT_HOME -> try {
                mainPageFragment = Class.forName("com.home.fragment.HomeMainPageFragment").newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                mainPageFragment = EmptyMainPageFragment()
            }

            FRAGMENT_LOCAL_LOG -> try {
                mainPageFragment = Class.forName("com.locallog.fragment.LocalLogMainPageFragment").newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                mainPageFragment = EmptyMainPageFragment()
            }

            FRAGMENT_REMOTE_LOG -> try {
                mainPageFragment = Class.forName("com.remotelog.fragment.RemoteLogMainPageFragment").newInstance() as BaseMainPageFragment
            } catch (e: Exception) {
                mainPageFragment = EmptyMainPageFragment()
            }

            else -> mainPageFragment = EmptyMainPageFragment()
        }

        mFragments[position] = mainPageFragment

        return mainPageFragment
    }
}
