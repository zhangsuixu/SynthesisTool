package com.service.bean;

import com.service.base.BaseBean;

import java.util.List;

/**
 *   apk文件列表
 */
public class LoggerList extends BaseBean<LoggerList> {

    public int pageCode; //当前页

    public int totalCount; //总记录数

    public int pageSize; //记录条数

    public List<LoggerInfo> fileList; //文件列表

    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<LoggerInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<LoggerInfo> fileList) {
        this.fileList = fileList;
    }
}
