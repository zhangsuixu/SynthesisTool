package com.ui.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.ui.R
import kotlinx.android.synthetic.main.view_count_down.view.*

class CountDownView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var mCount = 2

    init {
        View.inflate(context, R.layout.view_count_down, this)

        tv_des.text = "$mCount"
    }

    private var mListener: OnCountDownFinishedListener? = null

    @Suppress("HandlerLeak")
    var mHandler: Handler = Handler()

    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            mCount--
            tv_des.text = "$mCount"

            if (mCount <= 0) {
                mListener?.onFinish()
                return
            }

            mHandler.postDelayed(this, 1000)
        }
    }

    @Synchronized
    fun startCountDown() {
        if (mCount.toString().contentEquals(tv_des.text)) {
            mCount--
            tv_des.text = "$mCount"
            mHandler.postDelayed(mRunnable, 1000)
        }
    }

    interface OnCountDownFinishedListener {
        fun onFinish()
    }

    fun setOnCountDownFinished(onCountDownFinishedListener: OnCountDownFinishedListener) {
        this.mListener = onCountDownFinishedListener
    }

    fun onDestroy() {
        mListener = null

        mHandler.removeCallbacksAndMessages(null)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}
