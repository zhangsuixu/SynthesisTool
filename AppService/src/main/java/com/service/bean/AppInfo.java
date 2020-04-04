package com.service.bean;

import com.service.base.BaseBean;

import java.util.ArrayList;

/**
 * 时间 :  2019/7/27;
 * 版本号 :
 */
public class AppInfo extends BaseBean<AppInfo> {

    public ArrayList<AppItem> apps;
    public int totalPageSize;

    public static class AppItem {
        public String fileName;         // 文件名      mall
        public String fileVersion;      //文件版本号  3.4.10
        public String fileEnvrionment;  // 环境  online   test   dev
        public String fileDes;          // 描述
        public String fileTime;         // 新增文件时间
        public String downloadUrl;      // 下载地址
        public String packageName;
    }
}
