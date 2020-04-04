package com.service.base;


/**
 *  环境配置
 */
public class DebugConfig {

    public static final boolean isTest = true;
    public static final boolean isDev = false;
    public static final boolean isOnline = false;

    public static boolean isDebug;//true=测试，开发环境 .false=正式环境

    static {
        if (isTest || isDev) {
            isDebug = true;
        } else {
            isDebug = false;
        }
    }

//    public static String getBaseUrl() {
//        if (isOnline) {
//            return BASE_URL_ONLINE;
//        } else if (isDev) {
//            return BASE_URL_DEV;
//        } else {
//            return BASE_URL_TEST;
//        }
//    }

    public static String getBaseUrl() {
        return "http://47.240.8.235/";
    }
}

