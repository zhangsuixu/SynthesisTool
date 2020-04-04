package com.ui.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ui.R

/**
 * 独立抽取出默认的ContentView,以便展示默认的页面,比如错误页,当然根据不同公司要求可能不仅仅只有错误页
 */
class BaseContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.base_empty_view, this)

        this.visibility = View.GONE
    }

    fun showErrorView(){
        this.visibility = View.VISIBLE
    }

    fun showEmptyView(){
        this.visibility = View.GONE
    }

    fun setOnClickReloadListener(listener : OnClickListener){
        setOnClickListener(listener)
    }
}