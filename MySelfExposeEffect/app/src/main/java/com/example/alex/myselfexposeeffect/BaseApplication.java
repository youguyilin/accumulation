package com.example.alex.myselfexposeeffect;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 2018/1/22.
 */

public class BaseApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
