package com.example.smartalert;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;
    private static Context baseContext;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        MyApplication.baseContext = getBaseContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Context getAppBaseContext() {
        return MyApplication.baseContext;
    }
}
