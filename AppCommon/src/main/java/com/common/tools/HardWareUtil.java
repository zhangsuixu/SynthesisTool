package com.common.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.common.base.BaseApp;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class HardWareUtil {

    /*** 获取本地IP地址*/
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /*** 获取网络类型*/
    public static int getInternetType() {
        try {
            Context context = BaseApp.getContext().getApplicationContext();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                int type = networkInfo.getType();
                if (ConnectivityManager.TYPE_WIFI == type) {
                    return 3;//wifi
                } else if (ConnectivityManager.TYPE_MOBILE == type) {
                    return getMobileNetWork(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*** 10位唯一请求id，服务端分布式&apm可能会使用到*/
    public static String requestUid() {
        try {
            String requestUid = UUID.randomUUID().toString().substring(0, 10);
            return requestUid;
        } catch (Exception e) {

        }
        return "";
    }

    /*** 判断移动网络类型*/
    private static int getMobileNetWork(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                //4g网络
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return 2;
                //3g网络
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return 1;
                //2g网络
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return 0;
                //未知
                default:
                    return -1;
            }
        } catch (Exception e) {

        }
        return -1;

    }

    /**获取设备编号*/
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) BaseApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() == null) {
            return Settings.Secure.getString(BaseApp.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return tm.getDeviceId();
    }

    /**获取设备MAC地址*/
    @SuppressLint({"MissingPermission"})
    public static String getMacAdd() {
        try{
            WifiManager wifi = (WifiManager) BaseApp.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return splitMacAdd(wifi.getConnectionInfo().getMacAddress());
        }catch (Exception e){
            LogUtil.e(e,"get device mac address fail");
        }
        return "";
    }

    private static String splitMacAdd(String mac) {
        StringBuffer sb = new StringBuffer();
        if (null != mac && !"".equals(mac)) {
            String[] arr = mac.split(":");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
            }
        }
        return sb.toString();
    }

    /**获取手机品牌*/
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    /**获取手机型号*/
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**获取SDK版本号*/
    public static String getSDKVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    /**获取系统版本号*/
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**获取手机分辨率*/
    public static String getPhoneResolution() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowMgr = (WindowManager) BaseApp.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowMgr.getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            return width + "*" + height;
        } catch (Exception e) {
        }
        return "unknow";
    }

    /*** IMEI 全称 International Mobile Equipment Identity，中文翻译为国际移动装备辨识码.*/
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI() {
        try {
            TelephonyManager ts = (TelephonyManager) BaseApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            //noinspection ConstantConditions
            return ts.getDeviceId();
        } catch (Exception e) {
            LogUtil.e(e,"get device imei fail");
        }
        return "unkonw";
    }

    /*** 判断手机是否root，不弹出root请求框<br/>*/
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        return new File(binPath).exists() && isExecutable(binPath) || new File(xBinPath).exists() && isExecutable(xBinPath);
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);// 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }


}
