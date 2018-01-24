package com.example.alex.myselfexposeeffect.utils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;

/**
 * Created by Alex on 2018/1/23.
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {
    private static long DELAY_TIME = 1000;
    private static long lastClickTime;

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        //            Log.e("time","间隔" + timeD + "--ID--"+ mView.getId());
        if (0 < timeD && timeD < DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onClick(View v) {
        if (isFastDoubleClick()) {
            return;
        }
        onNoFastClick(v);
    }

    /**
     * 设置默认快速点击事件时间间隔
     *
     * @param delay_time
     * @return
     */
    public OnNoDoubleClickListener setLastClickTime(long delay_time) {
        DELAY_TIME = delay_time;
        return this;
    }

    /**
     * 快速点击事件回调方法
     *
     * @param v
     */
    public abstract void onNoFastClick(View v);
}
