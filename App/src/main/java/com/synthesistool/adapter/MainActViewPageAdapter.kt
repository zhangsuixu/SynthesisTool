package com.synthesistool.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ui.base.BaseMainPageFragment

class MainActViewPageAdapter(fm: FragmentManager, data: List<BaseMainPageFragment>) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mData: List<BaseMainPageFragment> = data

    override fun getItem(i: Int): Fragment {
        return mData[i]
    }

    override fun getCount(): Int {
        if (mData.isEmpty()) {
            return 0
        }
        return mData.size
    }
}
