package com.common.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.R;
import com.common.base.BaseApp;

public class ToastUtils {
    private static Toast singleToast;
    private static Toast customerToast;

    public static void showShortToast(String str) {
        showToast(BaseApp.getContext().getApplicationContext(), str, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int strId) {
        String str = BaseApp.getContext().getResources().getString(strId);
        showShortToast(str);
    }

    public static void showLongToast(String str) {
        showToast(BaseApp.getContext().getApplicationContext(), str, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int strId) {
        String str = BaseApp.getContext().getResources().getString(strId);
        showLongToast(str);
    }

//    public static void showLongToast(Context context, String str) {
//        showToast(context, str, Toast.LENGTH_LONG);
//    }

    /**
     * 成功的 带图片的Toast
     *
     * @param toastText
     */
    public static void showCenterSucceedToast(String toastText) {
        Drawable drawable = BaseApp.getContext().getApplicationContext().getResources().getDrawable(R.drawable.ic_succeed);
        toastLayout(toastText, drawable, Gravity.CENTER);
    }

    /**
     * 警告 带图片的Toast
     *
     * @param toastText
     */
    public static void showCenterWarnToast(String toastText) {
        Drawable drawable = BaseApp.getContext().getApplicationContext().getResources().getDrawable(R.drawable.ic_warn);
        toastLayout(toastText, drawable, Gravity.CENTER);
    }

    public static void showCenterToast(String toastText) {
        toastLayout(toastText, null, Gravity.CENTER);
    }

    /**
     * @param toastText
     * @param drawable
     */
    private static void toastLayout(String toastText, Drawable drawable, int gravity) {
        try {

            Context context = BaseApp.getContext().getApplicationContext();

            LinearLayout layout = null;
            LinearLayout childLayout = null;
            if (null == layout) {
                layout = new LinearLayout(context);
            }

            if (null == childLayout) {
                childLayout = new LinearLayout(context);
                childLayout.setOrientation(LinearLayout.HORIZONTAL);
                childLayout.setGravity(Gravity.CENTER);
            }

            if (customerToast == null) {
                customerToast = new Toast(context);
            }

            TextView textView = null;
            if (null == textView) {
                textView = new TextView(context);
            }
            textView.setText(toastText);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setTextColor(Color.parseColor("#ffffff"));

            if (gravity == Gravity.BOTTOM) {
                customerToast.setGravity(gravity, 0, DensityUtils.dip2px(80));
            } else {
                customerToast.setGravity(gravity, 0, 0);
            }

            if (null != drawable) {

                ImageView iv = new ImageView(context);
                iv.setImageDrawable(drawable);

                childLayout.addView(iv);
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ivParams.setMargins(0, 0, DensityUtils.dip2px(8), 0);
                iv.setLayoutParams(ivParams);
            } else {
                textView.setGravity(Gravity.CENTER);
            }
            childLayout.addView(textView);

            LinearLayout.LayoutParams textViewParams = null;
            int width = DensityUtils.getWidth();
            if (toastText.length() > 15) {
                textViewParams = new LinearLayout.LayoutParams((int) (width / 1.2), LinearLayout.LayoutParams.WRAP_CONTENT);
            } else if (toastText.length() > 8 && toastText.length() < 15) {
                textViewParams = new LinearLayout.LayoutParams((int) (width / 1.6), LinearLayout.LayoutParams.WRAP_CONTENT);
            } else {
                textViewParams = new LinearLayout.LayoutParams((int) (width / 1.8), LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }

            childLayout.setLayoutParams(textViewParams);
//        childLayout.setPadding(0,DensityUtils.dip2px(10),0,DensityUtils.dip2px(10));

            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layout.setLayoutParams(layoutParams);
            layout.setBackgroundResource(R.drawable.toast_background);
            layout.setPadding(DensityUtils.dip2px(15), DensityUtils.dip2px(12), DensityUtils.dip2px(15), DensityUtils.dip2px(12));

            layout.addView(childLayout);
            customerToast.setView(layout);
            customerToast.setDuration(Toast.LENGTH_SHORT);
            customerToast.show();
        } catch (Exception e) {

        }

    }


    private static void showToast(final Context context, final String str, final int duration) {
        if (context != null && !TextUtils.isEmpty(str)) {
            //用Handler解决在线程中调用的问题
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (singleToast == null) {
                            singleToast = Toast.makeText(context, str, duration);
                        } else {
                            singleToast.setText(str);
                            singleToast.setDuration(duration);
                        }
                        singleToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}
