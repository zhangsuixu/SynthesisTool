package com.synthesistool.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ui.base.BaseMainPageFragment

class MainActViewPageAdapter(fm: FragmentManager, data: List<BaseMainPageFragment>) : FragmentPagerAdapter(fm) {

    private var mData: List<BaseMainPageFragment> = data

    override fun getItem(i: Int): Fragment? {
        return mData[i]
    }

    override fun getCount(): Int {
        if (mData.isEmpty()) {
            return 0
        }
        return mData.size
    }
}
