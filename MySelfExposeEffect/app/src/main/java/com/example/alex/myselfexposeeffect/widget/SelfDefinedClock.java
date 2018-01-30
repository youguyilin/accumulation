package com.example.alex.myselfexposeeffect.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.example.alex.myselfexposeeffect.R;

import android.text.format.Time;

import java.nio.channels.FileLock;
import java.util.TimeZone;
import java.util.Timer;


/**
 * Created by Alex on 2018/1/26.
 */

public class SelfDefinedClock extends View {
    //记录当前时间
    private Time mTimer;

    private Drawable mHourDrawable;
    private Drawable mMinuteDrawable;
    private Drawable mDialDrawable;

    //用来记录表盘的宽高，以便我们在onMeasure中确定View的大小
    private int mDialWidth;
    private int mDialHeight;

    //用来记录View是否被加入到window中，我们在View attach到window的时候注册监听器，
    //记录时间的变化，重新绘制View，当View从window中剥离时，我们解除注册，因为View已经不可见了
    private boolean isAttached;

    private float mMinute;
    private Float mHour;

    //用来跟踪View的尺寸变化，变化时，我们在绘制自己时要进行适当的缩放
    private boolean mChanged;

    public SelfDefinedClock(Context context) {
        this(context, null);
    }

    public SelfDefinedClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelfDefinedClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置时钟
        if (mDialDrawable != null) {
            mDialDrawable = context.getResources().getDrawable(R.drawable.clock_dial);
        }
        if (mHourDrawable != null) {
            mHourDrawable = context.getResources().getDrawable(R.drawable.clock_hand_hour);
        }
        if (mMinuteDrawable != null) {
            mHourDrawable = context.getResources().getDrawable(R.drawable.clock_hand_hour);
        }

        mTimer = new Time();
        mDialWidth = mDialDrawable.getIntrinsicWidth();
        mDialHeight = mDialDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (width < mDialWidth && widthMode != MeasureSpec.UNSPECIFIED) {
            hScale = width / mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && height < mDialHeight) {
            vScale = height / mDialHeight;
        }
        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    private void onTimeChanged() {
        mTimer.setToNow();
        int hour = mTimer.hour;
        int minute = mTimer.minute;
        int second = mTimer.second;

        //由于Canlender 存在Linient模式，此时mimute和Hour有可能超过60和24
        mMinute = minute + second / 60.0f;
        mHour = hour + minute / 60.0f;
        mChanged = true;
    }

    /**
     * 注册一个广播，接收系统发出的时间变化广播，更新View的mTimer;
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent mIntent) {
            if (mIntent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String zone = mIntent.getStringExtra("time-zone");
                mTimer = new Time(TimeZone.getTimeZone(zone).getID());
            }
            //更新时间
            onTimeChanged();
            //引发重绘
            invalidate();
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isAttached) {
            isAttached = true;
            IntentFilter filter = new IntentFilter();
            //设置要监听的三种广播
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mReceiver, filter);
        }
        mTimer = new Time();
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isAttached) {
            getContext().unregisterReceiver(mReceiver);
            isAttached = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //用changed记录View的尺寸变化，并重置
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        //这里view的尺寸是可能变化的，可以从mchanged判断是否发生了变化，如果发生了变化，
        //就需要重新为时针、分针设置Bounds
        int availableWidth = super.getRight() - super.getLeft();
        int availableHeight = super.getBottom() - super.getTop();

        //获取View的中心点坐标
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final Drawable dial = mDialDrawable;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        //如果可用的宽高小于表盘图片的宽高，就要进行缩放，不过我们通常是缩放坐标系，而且影响是全局的
        if (availableHeight < h || availableWidth < w) {
            scaled = true;
            float scale = Math.min((float)availableWidth/w,(float)availableHeight/h);
            canvas.save();
            canvas.scale(scale,scale,x,y);
        }

    }
}
