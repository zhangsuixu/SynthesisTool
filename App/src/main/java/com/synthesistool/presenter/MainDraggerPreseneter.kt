package com.synthesistool.presenter

import com.common.tools.ToastUtils
import com.service.base.BaseBean
import com.service.bean.MainPageParam
import com.service.net.Api.initHomePage
import com.service.net.utils.BaseRequestAgent
import com.synthesistool.view.MainDraggerView
import com.ui.base.BasePresenter
import javax.inject.Inject

/**
 * 时间 :  2019/10/6;
 * 版本号 :
 */
class MainDraggerPreseneter @Inject constructor(private var mView: MainDraggerView?) : BasePresenter {

    fun loadData() {
        initHomePage(object : BaseRequestAgent.ResponseListener<MainPageParam> {
            override fun onSuccess(response: MainPageParam) {
                if(response.data.mainBottoms == null || response.data.mainBottoms.size <=0
                        || response.data.mainFragmentParams == null || response.data.mainFragmentParams.size <= 0){
                    ToastUtils.showCenterWarnToast("网络错误,请稍后重试")

                    mView?.initDataFail()
                    return
                }

                mView?.refreshView(response.data)
            }

            override fun onError(errorBean: BaseBean<*>) {
                ToastUtils.showCenterWarnToast(errorBean.message)

                mView?.initDataFail()
            }
        })
    }

    override fun onDestory() {
        mView = null
    }
}
