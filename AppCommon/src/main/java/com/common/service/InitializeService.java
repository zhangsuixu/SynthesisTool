package com.common.service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.common.base.ComponentApplication;

public class InitializeService extends IntentService {
    private static final String APP_INIT_ACTION = "com.synthesistool";

    public InitializeService() {
        super("InitializeService");
    }

    private static final String APPLICATION_LAZY_INIT = "application_lazy_init";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void startIt(Context application, String[] modulesList) {
        mContext = application;

        Intent intent = new Intent(application, InitializeService.class);
        intent.setAction(APP_INIT_ACTION);
        intent.putExtra(APPLICATION_LAZY_INIT, modulesList);
        application.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (APP_INIT_ACTION.equals(action)) {
                performInit(intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
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
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
