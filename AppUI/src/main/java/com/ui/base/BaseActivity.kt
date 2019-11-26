package com.ui.base

import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.common.base.BaseApp
import com.common.tools.EventBusUtils
import com.common.widget.LoadingDialog
import com.ui.R
import kotlinx.android.synthetic.main.base_layout.view.*

/**
 *   1.在子类中直接通过 EventBusUtils.register(this) 可直接注册,基类已处理销毁操作
 *   2.提供默认错误页面
 *   3.提供状态栏一体化设置方法(已默认开启,通过复写translateStatusBar更改配置)
 *   4.提供显示标题栏方法
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mBaseLayout: BaseLayout? = null
    private var mLoadingDialog : LoadingDialog? = null
    /**
     * 说明 : 如果系统版本低于等于
     *
     * @return true=使用状态栏一体化，false=不使用
     */
    private val isTranslateStatusBar: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && translateStatusBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView(savedInstanceState)
        initData()
        initEvent()
    }

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    protected abstract fun initEvent()

    override fun setContentView(layoutResID: Int) {
        this.setContentView(View.inflate(this, layoutResID, null))
    }

    override fun setContentView(view: View?) {
        mBaseLayout = BaseLayout(this)

        mBaseLayout?.setContentView(view)

        super.setContentView(mBaseLayout)

        //设置标题和状态栏颜色
        mBaseLayout?.setTitleAndStatusBarColor(R.color.color_00bbff, R.color.color_00bbff)

        //设置状态栏一体化
        initStatusBar()

        //设置标题左侧图标默认操作
        setOnLeftIconClick(View.OnClickListener { finish() })
    }

    /*** 初始化状态栏一体化*/
    private fun initStatusBar() {
        mBaseLayout?.setStatusBarVisible(isTranslateStatusBar)

        if (isTranslateStatusBar) {
            mBaseLayout?.setTranslucentStatus(this)
        }
    }

    /*** 设置标题栏是否展示*/
    open fun setTitleVisible(visibility: Int){
        mBaseLayout?.setTitleVisible(visibility)
    }

    /**
     * @return true=使用状态栏一体化，false=不使用
     */
    @Suppress("MemberVisibilityCanBePrivate")
    open fun translateStatusBar(): Boolean {
        return true
    }

    /** 设置标题文本  想要限制标题文本长度,是BaseTitle的职责，于BaseTitle里进行限制*/
    fun setTitleText(@StringRes strId: Int) {
        mBaseLayout?.setTitleText(BaseApp.getResource().getString(strId))
    }

    /** 设置标题文本  想要限制标题文本长度,是BaseTitle的职责，于BaseTitle里进行限制*/
    fun setTitleText(titleText: String) {
        mBaseLayout?.setTitleText(titleText)
    }

    /** 设置标题栏左侧图标点击事件 */
    @Suppress("MemberVisibilityCanBePrivate")
    fun setOnLeftIconClick(listener: View.OnClickListener) {
        mBaseLayout?.setOnLeftIconClick(listener)
    }

    /** 显示自定义内容布局  即setContentView(view : View?)设置的布局  */
    fun showContent() {
        mBaseLayout?.showContentView()
    }

    /** 显示默认错误页面 */
    fun showErrorContentView() {
        mBaseLayout?.showErrorContentView()

        setOnClickReloadListener(View.OnClickListener { onReloadData() })
    }

    @Suppress("MemberVisibilityCanBePrivate")
    open fun onReloadData(){

    }

    /** 显示错误页面时,设置点击回调 */
    @Suppress("MemberVisibilityCanBePrivate")
    fun setOnClickReloadListener(listener: View.OnClickListener) {
        mBaseLayout?.setOnClickReloadListener(listener)
    }

    fun showLoadingView(){
        if(null == mLoadingDialog){
            mLoadingDialog = LoadingDialog(this)
        }

        if(!mLoadingDialog!!.isShowing){
            mLoadingDialog!!.show()
        }

    }

    fun hideLoadingView(){
        if(null != mLoadingDialog && mLoadingDialog!!.isShowing){
            mLoadingDialog!!.dismiss()
        }
    }

    /** EventBusUtils已默认判断是否注册，已注册则在界面销毁时解除注册 */
    override fun onDestroy() {
        EventBusUtils.unregister(this)
        super.onDestroy()
    }

}
