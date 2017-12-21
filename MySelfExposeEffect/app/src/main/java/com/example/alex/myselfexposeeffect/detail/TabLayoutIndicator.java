package com.example.alex.myselfexposeeffect.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.alex.myselfexposeeffect.R;


/**
 * TabLayout指示器
 *
 * @author yuhushuan
 * @date 2017/10/9 17:11
 */
public class TabLayoutIndicator extends View {

    private Paint mPaint;
    private RectF mRect;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    int mIndicatorColor;
    int mIndicatorWidth;

    public TabLayoutIndicator(Context context) {
        this(context, null);
    }

    public TabLayoutIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayoutIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabLayoutIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabLayoutIndicator, defStyleAttr, 0);
        mIndicatorWidth = array.getDimensionPixelSize(R.styleable.TabLayoutIndicator_indicatorWidth, 0);
        mIndicatorColor = array.getColor(R.styleable.TabLayoutIndicator_indicatorColor, 0);
        array.recycle();


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setupWithTabLayout(final TabLayout tabLayout) {
        mTabLayout = tabLayout;
        tabLayout.addOnTabSelectedListener(mTabSelectedListener);
        tabLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mTabLayout.getScrollX() != getScrollX()) {
                    scrollTo(mTabLayout.getScrollX(), mTabLayout.getScrollY());
                }
            }
        });
    }

    public void setupWithViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRect != null) {
            int saveCount = canvas.save();
            canvas.drawRect(mRect, mPaint);
            canvas.restoreToCount(saveCount);
        }
    }

    private void computeIndicatorRect(int position, float positionOffset) {
        View tab = getTabView(position);

        float left = tab.getLeft();
        float right = tab.getRight();
        float top = tab.getTop() + getPaddingTop();
        float bottom = tab.getBottom() + getPaddingBottom();

        if (positionOffset > 0.f && position < mTabLayout.getTabCount() - 1) {
            View nextTab = getTabView(position + 1);

            float nextTabLeft = nextTab.getLeft();
            float nextTabRight = nextTab.getRight();

            left = nextTabLeft * positionOffset + left * (1f - positionOffset);
            right = (nextTabRight * positionOffset + right * (1f - positionOffset));
        }
        if (mIndicatorWidth != 0) {
            float center = (left + right) / 2;
            left = center - mIndicatorWidth / 2;
            right = center + mIndicatorWidth / 2;
        }
        if (mRect == null) {
            mRect = new RectF();
        }
        mRect.set(left, top, right, bottom);
    }

    private View getTabView(int position) {
        //TabLayout has only one child of LinearLayout
        ViewGroup tabStrip = (ViewGroup) mTabLayout.getChildAt(0);
        return tabStrip.getChildAt(position);
    }

    TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            computeIndicatorRect(position, positionOffset);
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
