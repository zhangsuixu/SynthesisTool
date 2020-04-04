package com.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.common.R;

/**
 *  加载框
 */
public class LoadingDialog extends Dialog {
    private TextView tv_text;

    public LoadingDialog(Context context) {
        super(context);

        Window window = getWindow();
        if(null != window)  getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //设置对话框背景透明

        setContentView(R.layout.loading_view);
        tv_text = findViewById(R.id.tv_loading_text);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        tv_text.setText(message);
        return this;
    }

}
