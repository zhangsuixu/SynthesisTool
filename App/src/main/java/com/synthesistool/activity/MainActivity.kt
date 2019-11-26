package com.synthesistool.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.service.bean.MainPageParam
import com.service.bean.MainPageParam.MainBottomParam
import com.synthesistool.R
import com.synthesistool.adapter.MainActViewPageAdapter
import com.synthesistool.component.DaggerMainDraggerComponent
import com.synthesistool.fragment.FragmentFactory
import com.synthesistool.module.MainDraggerModule
import com.synthesistool.presenter.MainDraggerPreseneter
import com.synthesistool.view.MainDraggerView
import com.ui.adapter.MainBottomAdapter.OnChooseListener
import com.ui.base.BaseActivity
import com.ui.base.BaseFragment
import kotlinx.android.synthetic.main.act_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainDraggerView {

    @Inject
    @JvmField
    var mMainDraggerPreseneter: MainDraggerPreseneter? = null

    private var mMainBottomList: ArrayList<MainBottomParam> = ArrayList()

    private var mMainActViewPageAdapter: MainActViewPageAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.act_main)

        setTitleVisible(View.GONE)

        DaggerMainDraggerComponent.builder()
                .mainDraggerModule(MainDraggerModule(this))
                .build()
                .inject(this)

        //每次界面重新初始化,清除已有fragment后重新添加
        FragmentFactory.clearFragments()
    }

    override fun initData() {
        showLoadingView()

        mMainDraggerPreseneter?.loadData()
    }

    override fun initEvent() {

    }

    override fun onReloadData() {
        initData()
    }

    override fun refreshView(response: MainPageParam) {
        showContent()

        hideLoadingView()

        val mainFragmentParams = response.mainFragmentParams

        val mainFragments = java.util.ArrayList<BaseFragment>()

        for (position in mainFragmentParams.indices) {
            mainFragments.add(FragmentFactory.getFragment(mainFragmentParams[position].fragmentsId))
        }

        initMainViewPage(mainFragments)

        val mainBottoms = response.mainBottoms

        for (i in mainBottoms.indices) {
            val index = MainPageParam().MainBottomParam(mainBottoms[i].name,
                    findMainBottomIconId(mainFragmentParams[i].fragmentsId),
                    findMainBottomDefaultIconId(mainFragmentParams[i].fragmentsId),
                    mainBottoms[i].isSelected)

            if(mainBottoms[i].isSelected){
                vp_content?.currentItem = i
            }

            mMainBottomList.add(index)
        }

        bnv_bottom_navigation.setData(mMainBottomList)
        bnv_bottom_navigation.setOnChooseListener(object : OnChooseListener{
            override fun onChoose(position: Int) {
                vp_content?.currentItem = position
            }
        })

    }

    private fun initMainViewPage(mainFragments : ArrayList<BaseFragment>) {
        if(null == mMainActViewPageAdapter){
            mMainActViewPageAdapter = MainActViewPageAdapter(supportFragmentManager, mainFragments)

            vp_content?.adapter = mMainActViewPageAdapter
            vp_content?.offscreenPageLimit = mainFragments.size - 1
            vp_content?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(p0: Int) {
                    mMainBottomList.indices.forEach { i ->
                        mMainBottomList[i].isSelected = i == p0
                    }

                    bnv_bottom_navigation.refreshMaainBottom(mMainBottomList)
                }
            })
        }
    }

    /*** 图片当然可以用网络图片,但网络图片加载受网络影响,这里仅作为使用案例 */
    private fun findMainBottomIconId(id: Int): Int {
        when (id) {
            1 -> return R.drawable.ic_star_black_144dp_one
            2 -> return R.drawable.ic_star_black_144dp_two
            3 -> return R.drawable.ic_star_black_144dp_three
        }

        return R.drawable.ic_star_black_144dp_default
    }

    /*** 图片当然可以用网络图片,但网络图片加载受网络影响,这里仅作为使用案例 */
    private fun findMainBottomDefaultIconId(id: Int): Int {
        when (id) {
            1 -> return R.drawable.ic_star_black_144dp_default
            2 -> return R.drawable.ic_star_black_144dp_default
            3 -> return R.drawable.ic_star_black_144dp_default
        }

        return R.drawable.ic_star_black_144dp_default
    }

    override fun initDataFail() {
        hideLoadingView()

        showErrorContentView()
    }
}
