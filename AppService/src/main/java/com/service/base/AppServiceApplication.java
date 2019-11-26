package com.service.base;

import android.app.Application;

import com.common.base.ComponentApplication;
import com.common.tools.loggerx.MCsvFormatStrategy;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


public class AppServiceApplication extends ComponentApplication {

    @Override
    protected void init(Application application) {
        initHttpLog();
    }

    @Override
    public void lazyInit(Application application) {

    }

    /**
     * 用于网络请求日志打印
     */
    private void initHttpLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                    .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                    .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("综合工具")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        FormatStrategy diskFormatStrategy = MCsvFormatStrategy.newBuilder()
                .tag("综合工具")
                .defPath("SynthesisTool")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(diskFormatStrategy));
    }
}
