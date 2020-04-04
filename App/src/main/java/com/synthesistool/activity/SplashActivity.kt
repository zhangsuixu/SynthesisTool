package com.synthesistool.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.common.tools.CommonUtils

import com.synthesistool.R
import com.ui.base.BaseActivity
import com.ui.widget.CountDownView
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.act_splash.*

class SplashActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.act_splash)

        setTitleVisible(View.GONE)
    }

    override fun initData() {
        tv_app_version.text = CommonUtils.getVersionCode()

        requestPagePermission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE)
    }

    override fun initEvent() {
        view_count_down.setOnCountDownFinished(object : CountDownView.OnCountDownFinishedListener{
            override fun onFinish() {
                ARouter.getInstance()
                        .build("/App/MainActivity")
                        .withTransition(R.anim.slide_in_bottom,R.anim.slide_out_bottom)
                        .navigation()
                finish()
            }
        })
    }

    override fun onPagePermissionSuccess(){
        view_count_down.startCountDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        view_count_down?.onDestroy()
    }
}
