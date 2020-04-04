package com.common.widget.dialog;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.R;

public class LoadingDialog extends ProgressDialog {
    public static LoadingDialog dialog = null;


    private LoadingDialog(Activity context) {
        super(context);
    }

    public static void dismissIfExist() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** 使用该构造方法设置主题为透明*/
    public LoadingDialog(Activity context, int theme) {
        super(context, theme);
    }

    @Override
    public void setProgress(int progress) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        if (imageView != null) {
            AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
            if (spinner != null) {
                spinner.start();
            }
        }
    }

    public static void showIfNotExist(Activity context, boolean canceledOnTouchOutside) {
        showIfNotExist(context, "", canceledOnTouchOutside, null);
    }

    public static void showIfNotExist(Activity context, String message, boolean canceledOnTouchOutside) {
        showIfNotExist(context, message, canceledOnTouchOutside, null);
    }

    public static void showIfNotExist(Activity context, int messageId, boolean canceledOnTouchOutside) {
        showIfNotExist(context, context.getResources().getString(messageId), canceledOnTouchOutside, null);
    }

    public static void showIfNotExist(Activity context, String message, boolean canceledOnTouchOutside, OnDismissListener onDismissListener) {
        try {
            if (context != null && !(dialog != null && dialog.isShowing())) {
                dialog = getDialog(context, message, canceledOnTouchOutside, onDismissListener);
            }
        } catch (Exception e) {//BadTokenException
            e.printStackTrace();
        }
    }

    public static void showIfNotExist(Activity context, int strId, boolean canceledOnTouchOutside, OnDismissListener onDismissListener) {
        showIfNotExist(context, context.getResources().getString(strId), canceledOnTouchOutside, onDismissListener);
    }

    private static View getView(Context context) {
        // 加载自定义显示布局
        View view = View.inflate(context, R.layout.progress_hud, null);
        ImageView spinnerImageView = (ImageView) view.findViewById(R.id.spinnerImageView);
        // 给布局添加动画
        // spinnerImageView.setBackgroundResource(R.anim.alertdialog_animation);
        spinnerImageView.setBackgroundResource(R.drawable.spinner);
        // 获取动画
        final AnimationDrawable animation = (AnimationDrawable) spinnerImageView.getBackground();
        // 设置动画执行多次
        animation.setOneShot(false);
        new Runnable() {
            @Override
            public void run() {
                // 开启动画
                animation.start();
            }
        };
        return view;
    }

    public static LoadingDialog getDialog(Activity context, String message, boolean canceledOnTouchOutside, OnDismissListener onDismissListener) {
        // 初始化dialog 加载透明样式
        LoadingDialog dialog = new LoadingDialog(context, R.style.ProgressLoading);
        dialog.show();
        // 加载自定义布局
        dialog.setContentView(getView(context));

        TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(message)) {
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText(message);
        } else {
            tv_message.setVisibility(View.GONE);
        }

        // 设置点击空白区域可以隐藏dialog
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.setCancelable(true);
        // dialog隐藏时的回调
        dialog.setOnDismissListener(onDismissListener);
        // dialog窗口居中显示
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        return dialog;
    }
}
