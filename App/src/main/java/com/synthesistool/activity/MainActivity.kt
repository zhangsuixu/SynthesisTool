package com.synthesistool.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.tools.LogUtil
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
import com.ui.base.BaseMainPageFragment
import com.ui.widget.CustomViewTwo
import kotlinx.android.synthetic.main.act_main.*
import javax.inject.Inject


@Suppress("SpellCheckingInspection")
@Route(path = "/App/MainActivity")
class MainActivity : BaseActivity(), MainDraggerView {

    @Inject
    @JvmField
    var mMainDraggerPreseneter: MainDraggerPreseneter? = null

    private var mMainBottomList: ArrayList<MainBottomParam> = ArrayList()

    private var mainFragments: ArrayList<BaseMainPageFragment> = ArrayList()

    private var mMainActViewPageAdapter: MainActViewPageAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.act_main)

        setTitleVisible(View.GONE)

        DaggerMainDraggerComponent.builder()
                .mainDraggerModule(MainDraggerModule(this))
                .build()
                .inject(this)

        FragmentFactory.clearFragments()
    }

    override fun initData() {
//        showLoadingView()

        mMainDraggerPreseneter?.loadData()
    }

    override fun initEvent() {

    }

    override fun onReloadData() {
        initData()
    }

    override fun refreshView(response: MainPageParam) {
//        hideLoadingView()

        val mainFragmentParams = response.mainFragmentParams

        mainFragments.clear()

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

            if (mainBottoms[i].isSelected) {
                vp_content?.currentItem = i
                onFragmentPageSeleted(i)
            }

            mMainBottomList.add(index)
        }

        bnv_bottom_navigation.setData(mMainBottomList)
        bnv_bottom_navigation.setOnChooseListener(object : OnChooseListener {
            override fun onChoose(position: Int) {
                vp_content?.currentItem = position
                onFragmentPageSeleted(position)
            }
        })
    }

    private fun onFragmentPageSeleted(position: Int) {
        LogUtil.i("onFragmentPageSeleted$position")

        mainFragments[position].onFragmentSeleted()
    }

    private fun initMainViewPage(mainMainPageFragments: ArrayList<BaseMainPageFragment>) {
        if (null == mMainActViewPageAdapter) {
            mMainActViewPageAdapter = MainActViewPageAdapter(supportFragmentManager, mainMainPageFragments)

            vp_content?.adapter = mMainActViewPageAdapter
            vp_content?.offscreenPageLimit = mainMainPageFragments.size - 1 //启动就初始化所需的Fragment,具体Fragment的数据初始化可以在onFragmentSeleted回调时进行
            vp_content?.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {}
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
                override fun onPageSelected(p0: Int) {
                    mMainBottomList.indices.forEach { i ->
                        if (i == p0) {
                            mMainBottomList[i].isSelected = true
                            onFragmentPageSeleted(i)
                        } else {
                            mMainBottomList[i].isSelected = false
                        }
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
