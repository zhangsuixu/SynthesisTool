package com.common.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

public class MyService extends Service {

    private LocalBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("onBind :");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MyService getServices() {
            return MyService.this;
        }

        public void start() {
            Logger.d("start:");
        }

        public void end() {
            Logger.d("end:");
        }
    }

    @Override
    public void onCreate() {
        Logger.d("onCreate:");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand:");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.d("onDestroy:");
        super.onDestroy();
    }

    public String myWay() {
        Logger.d("myWay:hello world:");
        return "hello world";
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("onUnbind:");
        return super.onUnbind(intent);
    }
}
