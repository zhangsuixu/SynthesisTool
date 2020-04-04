package com.service.base;

import android.app.Application;

import com.common.base.ComponentApplication;
import com.common.tools.LogUtil;


public class ServiceApp extends ComponentApplication {

    @Override
    protected void init(Application application) {
        LogUtil.initLogger(DebugConfig.isDebug);//初始化日志打印工具
    }

    @Override
    public void lazyInit(Application application) {

    }

}
