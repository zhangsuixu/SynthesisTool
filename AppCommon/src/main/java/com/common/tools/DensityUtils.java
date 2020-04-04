package com.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.common.base.BaseApp;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * 测量，测量单位转化
 */
public class DensityUtils {

    /*** px转DIP*/
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*** dp转px*/
    public static int dip2px(float dpValue) {
        final float scale = BaseApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*** sp转px */
    public static int sp2px(float spValue) {
        final float fontScale = BaseApp.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /*** 获取屏幕宽度*/
    public static int getWidth() {
        return BaseApp.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /*** 获取屏幕高度*/
    public static int getHeight() {
        return BaseApp.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据屏幕宽度来计算view的高
     *
     * @param px 1920x1080 下对应的view高度
     * @return view的高度
     */
    public static float getViewHeight(int px) {
        int h = getHeight();
        return px * (getWidth() / 1080f);
    }

    /*** 获取底部蓝高度*/
    public static int getNavigationBarHeight(Activity mActivity) {
        int height = 0;
        try {
            Resources resources = mActivity.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /*** 底部蓝是否存在*/
    public static boolean checkDeviceHasNavigationBar(Context context) {
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
        }
        return hasNavigationBar;

    }

    /*** 获取当前Actvity的截图*/
    @SuppressWarnings("deprecation")
    public static void takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);

        Bitmap b = null;
        if (b1 != null) {
            if (checkDeviceHasNavigationBar(activity)) {
                b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight - getNavigationBarHeight(activity));
            } else {
                b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
            }
            if (b != null) {
                view.destroyDrawingCache();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(FileUtils.getCacheCanDeleteImageDir().getAbsolutePath().concat("/screenshot.jpg"));
                    if (null != fos) {
                        b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.flush();
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    b.recycle();
                    b1.recycle();
                    b = null;
                    b1 = null;
                }
            }
        }
    }


    /**获取屏幕DisplayMetrics*/
    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    /**获取状态栏高度**/
    public static int getStatusBarHeight() {
        int statusBarHeight1 = dip2px(20f);//获取status_bar_height资源的ID
        int resourceId = BaseApp.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {//根据资源ID获取响应的尺寸值
            statusBarHeight1 = BaseApp.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }


}
