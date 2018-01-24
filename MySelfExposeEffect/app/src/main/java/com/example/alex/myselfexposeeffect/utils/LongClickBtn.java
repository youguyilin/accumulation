package com.example.alex.myselfexposeeffect.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Alex on 2018/1/23.
 */

public class LongClickBtn extends Button {
    long startClickTime;
    public LongClickBtn(Context context) {
        super(context);
    }

    public LongClickBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongClickBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LongClickBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startClickTime = System.currentTimeMillis();
                Log.e("startClickTime",startClickTime + "ms");
                break;
        }
        return super.onTouchEvent(event);
    }

    public long getStartClickTime() {
        return startClickTime;
    }
}
