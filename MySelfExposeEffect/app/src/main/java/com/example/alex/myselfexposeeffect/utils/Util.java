package com.example.alex.myselfexposeeffect.utils;

/**
 * Created by Alex on 2018/1/23.
 */

public class Util {

    private static long lastClickTime = 0;//上次点击的时间

    /**
     * 防止用户快速点击，造成多次点击事件
     * @return
     */
    public static boolean isFastClick() {
        int spaceTime = 1000;//时间间隔
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击
        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = false;
        } else {
            isAllowClick = true;
        }
        lastClickTime = currentTime;
        return isAllowClick;
    }
}
