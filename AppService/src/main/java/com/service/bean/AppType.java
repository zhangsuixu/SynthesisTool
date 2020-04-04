package com.service.bean;

/**
 *  应用类型信息
 */
public class AppType{
    public String appName;
    public String fileName;
    public String fileDirs;
    public String filePath;
    public String packageName;

    public AppType(String appName, String fileName, String fileDirs, String filePath,String packageName) {
        this.appName = appName;
        this.fileName = fileName;
        this.fileDirs = fileDirs;
        this.filePath = filePath;
        this.packageName = packageName;
    }
}
