package com.common.tools;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.common.base.BaseApp;
import com.orhanobut.logger.Logger;

/***   对Logger日志框架进行包装,以便后续方便更换日志打印工具*/
public class LogUtil {

    private static boolean allowLog = true;

    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (allowLog)
            Logger.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.d(message, args);
    }

    public static void d(@Nullable Object object) {
        if (allowLog)
            Logger.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.e(null, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.w(message, args);
    }

    public static void wtf(@NonNull String message, @Nullable Object... args) {
        if (allowLog)
            Logger.wtf(message, args);
    }

    public static void json(@Nullable String json) {
        if (allowLog)
            Logger.json(json);
    }

    public static void xml(@Nullable String xml) {
        if (allowLog)
            Logger.xml(xml);
    }

    /*** 线上包也会打印这个日志*/
    @SuppressWarnings("unused")
    public static void onlineLog(String content) {
        //noinspection CatchMayIgnoreException
        try {
            logNoLimit(Log.INFO, BaseApp.getContext().getPackageName(), content);
        } catch (Exception e) {
        }
    }

    /*** 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制 所以这里使用自己分节的方式来输出足够长度的message*/
    public static void logNoLimit(int level, String tag, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        int lineContentCapacity = 3000;
        if (content.length() > lineContentCapacity) {
            for (int i = 0; i < content.length(); i += lineContentCapacity) {
                if (i + lineContentCapacity < content.length()) {
                    log(level, tag, content.substring(i, i + lineContentCapacity));
                } else {
                    log(level, tag, content.substring(i));
                }
            }
        } else {
            log(level, tag, content);
        }
    }

    private static void log(int level, String tag, String content) {
        switch (level) {
            case Log.INFO:
                Logger.i(tag, content);
                break;
            case Log.DEBUG:
                Logger.d(tag, content);
                break;
            case Log.WARN:
                Logger.w(tag, content);
                break;
            case Log.ERROR:
                Logger.e(tag, content);
                break;
            case Log.VERBOSE:
                Logger.v(tag, content);
                break;
        }
    }


}