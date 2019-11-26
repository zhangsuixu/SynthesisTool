package com.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.common.base.BaseApp;

import java.lang.reflect.Method;

/**
 * 界面相关工具类
 */
public class UIUtils {

    private UIUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /***  得到全局上下文*/
    public static Context getContext() {
        return BaseApp.getContext();
    }

    /*** 得到资源类*/
    public static Resources getResources() {
        return getContext().getResources();
    }

    /*** 获取对应资源id的字符串*/
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /***  获取主线程Handler*/
    public static Handler getHandler() {
        return BaseApp.getHandler();
    }

    /***  获取主线程id*/
    public static int getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    /*** 获取主线程*/
    public static Thread getMainThread() {
        return BaseApp.getMainThread();
    }

    /***  保证在主线程中执行任务*/
    public static void postTaskSafely(Runnable task) {
        if (getMainThreadId() == android.os.Process.myTid()) {
            task.run();
        } else {
            getHandler().post(task);
        }
    }

    /**
     * 是否主线程
     */
    public static boolean isMainThread() {
        return getMainThreadId() == android.os.Process.myTid() ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }


    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) BaseApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) BaseApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = BaseApp.getContext().getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /*** 检查是否有底部导航栏*/
    public static boolean isOffset(Context context) {
        return getDecorViewHeight(context) > getScreenHeight(context);
    }

    public static int getDecorViewHeight(Context context) {
        return ((Activity) context).getWindow().getDecorView().getHeight();
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);//当前activity
        return dm.heightPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /*** 检查是否有底部导航栏*/
    private static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            //TODO
        }

        return hasNavigationBar;
    }
}
