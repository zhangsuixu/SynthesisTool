package com.ui.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.service.bean.MainPageParam
import com.ui.R
import com.ui.adapter.MainBottomAdapter
import kotlinx.android.synthetic.main.view_bottom_navigation.view.*

class BottomNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mMainBottomAdapter: MainBottomAdapter? = null

    init {
        View.inflate(context, R.layout.view_bottom_navigation, this)
    }

    fun setOnChooseListener(listener : MainBottomAdapter.OnChooseListener){
        mMainBottomAdapter?.setOnChooseListener(listener)
    }

    fun refreshMaainBottom(bottomList: ArrayList<MainPageParam.MainBottomParam>?){
        mMainBottomAdapter?.setNewDatas(bottomList)
    }

    fun setData(bottomList: ArrayList<MainPageParam.MainBottomParam>?) {
        if(null == bottomList || bottomList.size <= 0){
            return
        }

        if(null == mMainBottomAdapter){
            val llManager = LinearLayoutManager(context)
            llManager.orientation = LinearLayoutManager.HORIZONTAL
            rv_bottom.layoutManager = llManager

            mMainBottomAdapter = MainBottomAdapter(context, bottomList)
            rv_bottom.adapter = mMainBottomAdapter
            return
        }

        mMainBottomAdapter?.setNewDatas(bottomList)
    }

}
