package com.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.tools.LogUtil;

/**
 * 时间 :  2020/3/17;
 * 版本号 :
 */
public class CustomFramelayout extends FrameLayout {
    public CustomFramelayout(@NonNull Context context) {
        super(context);
    }

    public CustomFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.d("dispatchTouchEvent" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.d("CustomFramelayout onInterceptTouchEvent");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("dispatchTouchEvent" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                LogUtil.d("UNSPECIFIED");
                break;
            case MeasureSpec.AT_MOST:
                LogUtil.d("AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                LogUtil.d("EXACTLY");
                break;
            default:
                LogUtil.d("default");
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
