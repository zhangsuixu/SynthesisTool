package com.ui.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.common.base.BaseApp
import com.ui.R
import com.ui.tools.DensityTools
import com.ui.tools.SystemBarHelper
import kotlinx.android.synthetic.main.base_layout.view.*

class BaseLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.base_layout, this)
    }

    /**
     * 设置标题栏背景色 和 状态栏背景色
     * @param titleColor 标题栏背景色
     * @param statusColor 状态栏背景色
     */
    @Suppress("DEPRECATION")
    fun setTitleAndStatusBarColor(@ColorRes titleColor: Int, @ColorRes statusColor: Int) {
        btt_title.setBackgroundColor(resources.getColor(titleColor))
        v_status_bar.setBackgroundColor(resources.getColor(statusColor))
    }

    private var mContentView: View? = null

    private var mBaseContentView: BaseContentView? = null

    fun setContentView(view: View?) {
        if (null != view) {
            mContentView = view

            ll_base_layout.addView(mContentView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
    }

    /**
     *   1. 当页面初始化失败时,显示默认网络错误页面,点击后回调 onReload()方法
     *   2. 当页面不需要显示默认页面时,请勿调用该方法(按需添加BaseContentView)
     * */
    private fun showBaseContentView() {
        mContentView?.visibility = View.GONE

        if (null == mBaseContentView) {
            mBaseContentView = BaseContentView(context)
            mBaseContentView?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

            ll_base_layout.addView(mBaseContentView)
        }

        mBaseContentView?.visibility = View.VISIBLE
    }

    /** 显示默认错误页面 */
    fun showErrorContentView() {
        showBaseContentView()
        mBaseContentView?.showErrorView()
    }

    /** 显示主内容页面 */
    fun showContentView() {
        mContentView?.visibility = View.VISIBLE
        mBaseContentView?.visibility = View.GONE
    }

    fun setOnClickReloadListener(listener: OnClickListener) {
        mBaseContentView?.setOnClickReloadListener(listener)
    }

    /**设置状态栏是否展示*/
    fun setStatusBarVisible(visible: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (visible) {
                val layoutParams = v_status_bar.layoutParams
                layoutParams.height = DensityTools.getStatusBarHeight()
                v_status_bar.layoutParams = layoutParams

                v_status_bar.visibility = View.VISIBLE
            } else {
                v_status_bar.visibility = View.GONE
            }
        }
    }

    /**设置状态栏一体化*/
    fun setTranslucentStatus(activity: Activity) {
        val window = activity.window

        // 默认主色调为白色, 如果是6.0或者以上, 设置状态栏文字为黑色, 否则给状态栏着色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        //设置状态栏字体颜色
        SystemBarHelper.setStatusBarDarkMode(window)
    }

    /**设置标题栏是否展示*/
    fun setTitleVisible(visibility: Int) {
        btt_title.visibility = visibility
        v_status_bar.visibility = visibility
    }

    /**设置状态栏文本*/
    fun setTileText(@StringRes strId: Int) {
        btt_title.setTitleText(BaseApp.getResource().getString(strId))
    }

    /**设置状态栏文本*/
    fun setTitleText(titleText: String) {
        btt_title.setTitleText(titleText)
    }

    /**设置左侧图片点击事件*/
    fun setOnLeftIconClick(listener: OnClickListener) {
        btt_title.setOnLeftIconClick(listener)
    }
}
