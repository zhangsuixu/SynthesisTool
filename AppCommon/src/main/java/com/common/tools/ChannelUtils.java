package com.common.tools;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.common.base.BaseApp;
import com.common.tools.sharePref.SharePrefUtil;


public class ChannelUtils {
    public static String getChannel() {
        String channel = "";
        try {
            //本地已经有渠道了，直接使用本地的渠道，否则的话去取AndroidMainfest的渠道
            if (hasAppChannel()) {
                return SharePrefUtil.getAppChannel();
            }

            String packageName = BaseApp.getContext().getPackageName();
            ApplicationInfo appInfo = BaseApp.getContext().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
            SharePrefUtil.setAppChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * 是否已经设置过APP的下载渠道
     */
    private static boolean hasAppChannel() {
        return StringUtils.isNotNull(SharePrefUtil.getAppChannel());
    }
}
