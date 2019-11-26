package com.synthesistool.fragment

import android.annotation.SuppressLint
import com.ui.base.BaseFragment
import java.util.*

/**
 * 通过工厂获取首页所需Fragment
 */
object FragmentFactory {

    private const val FRAGMENT_HOME = 1
    private const val FRAGMENT_LOCAL_LOG = 2
    private const val FRAGMENT_REMOTE_LOG = 3

    @SuppressLint("UseSparseArrays")
    val mFragments = HashMap<Int, BaseFragment>()

    val fragments: Map<Int, BaseFragment>
        get() = mFragments

    val count: Int
        get() = mFragments.size

    fun clearFragments() {
        mFragments.clear()
    }

    /**
     * 通过反射获取首页Fragment,不同Fragment所属module不同,不依赖时显示空布局
     */
    fun getFragment(position: Int): BaseFragment {
        var fragment: BaseFragment

        if (mFragments.containsKey(position)) {
            return mFragments[position]!!
        }

        when (position) {
            FRAGMENT_HOME -> try {
                fragment = Class.forName("com.home.fragment.HomeFragment").newInstance() as BaseFragment
            } catch (e: Exception) {
                fragment = EmptyFragment()
            }

            FRAGMENT_LOCAL_LOG -> try {
                fragment = Class.forName("com.locallog.fragment.LocalLogFragment").newInstance() as BaseFragment
            } catch (e: Exception) {
                fragment = EmptyFragment()
            }

            FRAGMENT_REMOTE_LOG -> try {
                fragment = Class.forName("com.remotelog.fragment.RemoteLogFragment").newInstance() as BaseFragment
            } catch (e: Exception) {
                fragment = EmptyFragment()
            }

            else -> fragment = EmptyFragment()
        }

        mFragments[position] = fragment

        return fragment
    }
}
