package com.service.bean;


import com.service.base.BaseBean;

/**
 *   日志文件列表
 */
public class LoggerInfo extends BaseBean<LoggerInfo> {
    public String name;
    public String path;
    public String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
