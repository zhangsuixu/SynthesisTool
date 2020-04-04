package com.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.common.service.InitApplicationService;
import com.common.service.InitializeService;

/**
 * 基类Application
 * 这里提供通过反射的方式初始化个个module里的Application.
 */
public class BaseApp extends Application {

    /***   存储需要动态初始化的module的Application类 */
    private static final String[] modulesList = {
            "com.ui.base.UiApp",
            "com.service.base.ServiceApp"
    };

    private static Context mApplicationContext;

    private static BaseApp mBaseApp;

    private static Handler mHandler;

    private static int mMainThreadId;

    private static Thread mMainThread;

    @Override
    public void onCreate() {
        super.onCreate();

        mBaseApp = this; //获取BaseApp自己

        mApplicationContext = getApplicationContext();  //得到全局上下文

        mHandler = new Handler(); //主线程中的Handler

        mMainThreadId = android.os.Process.myPid(); //主线程id

        mMainThread = Thread.currentThread();   //主线程

        modulesApplicationInit();   //初始化其他组件

        InitApplicationService.startService(this, modulesList);  //延时初始化其他组件
    }

    private void modulesApplicationInit() {
        for (String moduleImpl : modulesList) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof ComponentApplication) {
                    ((ComponentApplication) obj).init(this);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public static BaseApp getBaseApp() {
        return mBaseApp;
    }

    public static Context getContext() {
        return mApplicationContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static Resources getResource() {
        return mApplicationContext.getResources();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getResource().getDisplayMetrics();
    }

    public static AssetManager getAsset() {
        return getResource().getAssets();
    }

}
