package com.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.common.tools.LogUtil;

public class CustomView extends View {

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
        this.setClickable(true);
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = Color.RED;

    private void initDraw() {
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d(event.getAction() + "---");
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        printMode(widthMeasureSpec);
        printMode(heightMeasureSpec);

        LogUtil.d(MeasureSpec.getSize(widthMeasureSpec) + "----" + MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void printMode(int widthMeasureSpec) {
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
