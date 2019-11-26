package com.common.widget.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.common.base.BaseApp;
import com.common.tools.LogUtil;
import com.orhanobut.logger.Logger;

public class CustomDialog extends Dialog {

    protected Activity mAttachActivity = null;
    protected View mContentView;
    protected int mTheme = 0, mWindowGravity = 0;
    protected boolean mIsFullScreen = false;

    public float getDefaultMargin() {
        return 60.0f;
    }

    /**
     * dialog宽度自适应内容
     */
    public boolean isWrapContent() {
        return false;
    }

    /**
     * （match screen width or have a little margin at left and right）and "mWindowGravity" in screen;
     *
     * @param context
     * @param layout
     * @param style
     * @param isFullScreen true表示dialog宽度=屏幕宽度，false表示dialog宽度=屏幕宽度-getDefaultMargin()
     */
    public CustomDialog(Activity context, int layout, int style, int windowGravity, boolean isFullScreen) {
        this(context, LayoutInflater.from(context).inflate(layout, null), style, windowGravity, isFullScreen);
    }

    /**
     * （match screen width or have a little margin at left and right）and "mWindowGravity" in screen;
     *
     * @param context
     * @param style
     * @param isFullScreen
     */
    public CustomDialog(Activity context, View contentView, int style, int windowGravity, boolean isFullScreen) {
        super(context, style);

        init(context, contentView, style, windowGravity, isFullScreen);
    }

    private void init(Activity context, View contentView, int style, int windowGravity, boolean isFullScreen) {
        this.mAttachActivity = context;
        this.mContentView = contentView;
        this.mTheme = style;
        this.mWindowGravity = windowGravity;
        this.mIsFullScreen = isFullScreen;

        initView(contentView);
        createDialog();
    }

    /**
     * 重写这个方法可以对view做自己的逻辑处理
     *
     * @param contentView 加载到dialog的view
     */
    protected void initView(View contentView) {

    }

    protected void createDialog() {
        setContentView(mContentView);

        Window window = getWindow();
        if(null == window) {
            LogUtil.d("createDialog() null == window");
            return;
        }

        WindowManager.LayoutParams windowParams = getWindowLayoutParams(window.getAttributes(), mContentView);
        window.setAttributes(windowParams);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    protected WindowManager.LayoutParams getWindowLayoutParams(WindowManager.LayoutParams windowParams, View contentView) {
        if (isWrapContent()) {
            FrameLayout.LayoutParams wrapContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            contentView.setLayoutParams(wrapContentParams);
            windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            int screenWidth = BaseApp.getContext().getResources().getDisplayMetrics().widthPixels;
            if (mIsFullScreen) {
                windowParams.width = screenWidth;
            } else {
                windowParams.width = screenWidth - dip2px(getDefaultMargin());
            }
        }

        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = mWindowGravity;

        return windowParams;
    }


    @Override
    public void show() {
        if (!mAttachActivity.isFinishing() && this != null && !this.isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (this != null && this.isShowing()) {
            super.dismiss();
        }
    }

    public static int dip2px(float dpValue) {
        final float scale = BaseApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
