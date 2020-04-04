package com.common.service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.common.base.ComponentApplication;

public class InitApplicationService extends JobIntentService {

    private static final int JOB_ID = 10002;
    private static final String APPLICATION_LAZY_INIT = "application_lazy_init";
    private static final String APP_INIT_ACTION = "com.synthesistool";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void startService(Context application, String[] modulesList) {
        mContext = application;

        Intent intent = new Intent(application, InitializeService.class);
        intent.setAction(APP_INIT_ACTION);
        intent.putExtra(APPLICATION_LAZY_INIT, modulesList);

        enqueueWork(application, InitApplicationService.class, JOB_ID, intent);

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (APP_INIT_ACTION.equals(action)) {
            performInit(intent);
        }
    }

    private void performInit(Intent intent) {
        String[] modulesList = intent.getStringArrayExtra(APPLICATION_LAZY_INIT);

        if (modulesList == null || modulesList.length <= 0) {
            return;
        }

        for (String moduleImpl : modulesList) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof ComponentApplication && null != mContext) {
                    ((ComponentApplication) obj).lazyInit((Application) mContext);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
