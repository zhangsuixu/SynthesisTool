package com.synthesistool.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.synthesistool.fragment.FragmentFactory
import com.ui.base.BaseFragment

class MainActViewPageAdapter(fm: FragmentManager, datas: List<BaseFragment>) : FragmentPagerAdapter(fm) {

    private var mDatas: List<BaseFragment> = datas

    override fun getItem(i: Int): Fragment? {
        return mDatas[i]
    }

    override fun getCount(): Int {
        if (mDatas.isEmpty()) {
            return 0
        }
        return mDatas.size
    }
}
