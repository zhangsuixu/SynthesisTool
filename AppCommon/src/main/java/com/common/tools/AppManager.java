package com.common.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Stack;

/**
 * 对已经打开的所有activity 提供管理的类
 */
public class AppManager {

    private static Stack<Activity> activityStack;

    public static AppManager getInstance() {
        return AppManagerHolder.appManager;
    }

    private static class AppManagerHolder {
        private static final AppManager appManager = new AppManager();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取最上层的Activity
     */
    public Activity getCurrentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束指定的Activity
     */
    @SuppressWarnings("WeakerAccess")
    public void finishActivity(Activity activity) {
        if (null != activity) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finfishActivity(Class<?> cls) {
        for (Activity acticity : activityStack) {
            if (acticity.getClass().equals(cls)) {
                finishActivity(acticity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    @SuppressWarnings("WeakerAccess")
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            LogUtil.e(e,"AppManager : AppExit exception");
        }
    }

}
