package com.example.alex.myselfexposeeffect.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.GestureUtils;
import android.renderscript.Script;
import android.support.v7.widget.DialogTitle;

import com.example.alex.myselfexposeeffect.BaseApplication;

/**
 * Created by Alex on 2018/1/22.
 */

public class PreferenceCache {
    private static final String GESTURE_TIME = "gesture_time";
    private static final String GESTURE_FLAG = "gesture_flg";

    public static void putGestureTime(long time){
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(GESTURE_TIME,time);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(){
        BaseApplication application = (BaseApplication) BaseApplication.getContext();
        return application.getSharedPreferences("alex", Context.MODE_PRIVATE);
    }

    public static long getGestureTime(){
        return  getSharedPreferences().getLong(GESTURE_TIME,0);
    }

    public static void putGestureFlag(boolean flg){
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(GESTURE_FLAG,flg);
        editor.apply();
    }

    public static boolean getGestureFlag(){
        return getSharedPreferences().getBoolean(GESTURE_FLAG,false);
    }
}
