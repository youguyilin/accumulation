package com.example.alex.myselfexposeeffect.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragment 的标签信息
 * <p>
 * 一般与{@link android.support.design.widget.TabLayout.Tab} 及
 * {@link android.support.v4.app.FragmentStatePagerAdapter}配合使用
 *
 * @author yuhushuan
 * @date 2017/8/14 12:03
 */
public class FragmentTab {
    private final Class<? extends Fragment> cls;
    private final Bundle args;
    private Fragment fragment;
    private String title;

    public FragmentTab(Class<? extends Fragment> cls, Bundle args) {
        this.cls = cls;
        this.args = args;
    }

    public FragmentTab(Class cls) {
        this(cls, null);
    }

    public Class<? extends Fragment> getCls() {
        return cls;
    }

    public Bundle getArgs() {
        return args;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public FragmentTab setTitle(String title) {
        this.title = title;
        return this;
    }
}
