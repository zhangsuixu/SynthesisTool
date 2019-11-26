package com.ui.base

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

import com.ui.R
import kotlinx.android.synthetic.main.base_title.view.*

class BaseTitle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.base_title, this)
    }

    fun setOnLeftIconClick(listener: OnClickListener?) {
        if (null != listener) {
            v_left_icon.setOnClickListener(listener)
        }
    }

    fun setTitleText(str: String?) {
        tv_title.text = when (TextUtils.isEmpty(str)) {
            true -> ""
            false -> str
        }
    }

}
