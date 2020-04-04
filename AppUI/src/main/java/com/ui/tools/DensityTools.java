package com.ui.tools;

import com.common.base.BaseApp;

import java.lang.reflect.Method;

/**
 * 测量，测量单位转化
 */
@SuppressWarnings("all")
public class DensityTools {

    /**
     * px转DIP
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApp.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApp.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spValue) {
        final float fontScale =  BaseApp.getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWidth() {
        return  BaseApp.getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight() {
        return BaseApp.getDisplayMetrics().heightPixels;
    }

    /**
     * UI出一套以 1920 * 1080基准的图，那么动态获取当前手机宽度值计算方式是  UI图px值 / 1080f * 当前屏幕宽度
     */
    public static float getViewWidth(int px) {
        return px * (getWidth() / 1080f);
    }

    /**
     * UI出一套以 1920 * 1080基准的图，那么动态获取当前手机高度值计算方式是  UI图px值 / 1920f * 当前屏幕高度
     */
    public static float getViewHeight(int px) {
        return px * (getHeight() / 1920f);
    }

    /**
     * 获取底部导航栏高度
     */
    public static int getNavigationBarHeight() {
        int height = 0;
        try {
            int resourceId = BaseApp.getResource().getIdentifier("navigation_bar_height", "dimen", "android");
            height = BaseApp.getResource().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 底部导航栏是否存在
     */
    @SuppressWarnings("all")
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;

        int id = BaseApp.getResource().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = BaseApp.getResource().getBoolean(id);
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
        }
        return hasNavigationBar;

    }

    /**获取状态栏高度**/
    public static int getStatusBarHeight() {
        int statusBarHeight = dip2px(20f);//获取status_bar_height资源的ID
        int resourceId = BaseApp.getResource().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {//根据资源ID获取响应的尺寸值
            statusBarHeight = BaseApp.getResource().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


}
