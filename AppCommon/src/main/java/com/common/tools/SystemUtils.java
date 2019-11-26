package com.common.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.common.base.BaseApp;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class SystemUtils {
    private static final String TAG = "SystemUtils";

    //判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
    public static boolean isEmulator() {
        try {
            TelephonyManager tm = (TelephonyManager) BaseApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint({"MissingPermission", "HardwareIds"}) String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")){
                return true;
            }
            return  (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        }catch (Exception ioe) {
            LogUtil.e(TAG,"判断是否模拟器失败");
        }
        return false;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     *  获取当前系统sdk版本号
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    public static String[] getPackageInfo() {
        // 获取版本号和版本名
        PackageManager pm = BaseApp.getContext().getPackageManager();
        String[] packageInfo = {"",""};
        try {
            PackageInfo pI = pm.getPackageInfo(BaseApp.getContext().getPackageName(), 0);
            packageInfo[0] = String.valueOf(pI.versionCode);
            packageInfo[1] = pI.versionName;
            LogUtil.e("mVersionCode " + packageInfo[0] + ",mVersionName " + packageInfo[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /*** 获取进程名*/
    public static String getProcessName() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" +  android.os.Process.myPid() + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isMainProcess(){
        return BaseApp.getContext().getPackageName().equals(getProcessName());
    }
}
