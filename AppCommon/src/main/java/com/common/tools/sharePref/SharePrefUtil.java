package com.common.tools.sharePref;

import android.text.TextUtils;

import com.common.tools.StringUtils;

import java.util.UUID;

/**
 * 用于本地持久化存取
 */
public class SharePrefUtil extends AbsSharePref {

    private final String spName = "app_setting";
    private static SharePrefUtil sharePrefUtil;

    private static final String HEAD_SERVER_TOKEN = "SERVER_TOKEN";
    private static final String HEAD_UUID = "UUID";
    private static final String APP_CHANNEL = "APP_CHANNEL";
    private static final String HEAD_PHONE = "HEAD_PHONE";

    @Override
    public String getSharedPreferencesName() {
        return spName;
    }

    @Override
    protected String getPutObjId() {
        return null;
    }

    public static synchronized SharePrefUtil getInstance() {
        if (sharePrefUtil == null) {
            sharePrefUtil = new SharePrefUtil();
        }
        return sharePrefUtil;
    }

    public static String getAppToken() {
        return getInstance().getString(HEAD_SERVER_TOKEN);
    }

    public synchronized static void setAppToken(String token) {
        getInstance().putString(HEAD_SERVER_TOKEN, token);
    }

    public static String getUUId() {
        String deviceId = "";
        try {
            deviceId = getInstance().getString(HEAD_UUID);
            if (StringUtils.isNull(deviceId)) {
                String imei = "";//获取imei
                if (!TextUtils.isEmpty(imei)) {
                    deviceId = imei;
                } else {
                    deviceId = UUID.randomUUID().toString();
                }
                getInstance().putString(HEAD_UUID, deviceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    public static void setAppChannel(String channel) {
        getInstance().putString(APP_CHANNEL, channel);
    }

    public static String getAppChannel() {
        return getInstance().getString(APP_CHANNEL);
    }

    public static void setPhone(String phone) {
        getInstance().putString(HEAD_PHONE, phone);
    }

    public static String getPhone() {
        return getInstance().getString(HEAD_PHONE);
    }
}
