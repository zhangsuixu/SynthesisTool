package com.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.common.service.InitializeService;

/**
 * 时间 :  2019/10/4;
 * 版本号 :
 */
public class BaseApp extends Application {

    private static final String[] modulesList = {
            "com.synthesistool.base.App",
            "com.locallog.LocalLogApplication",
            "com.remotelog.RemoteLogApplication",
            "com.ui.base.AppUiApplication",
            "com.service.base.AppServiceApplication"
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

        InitializeService.startIt(this, modulesList);  //延时初始化其他组件
    }

    private void modulesApplicationInit() {
        for (String moduleImpl : modulesList) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof ComponentApplication) {
                    ((ComponentApplication) obj).init(this);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public static BaseApp getBaseApp(){
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
