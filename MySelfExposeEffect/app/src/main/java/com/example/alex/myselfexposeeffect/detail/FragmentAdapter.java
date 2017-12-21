package com.example.alex.myselfexposeeffect.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * TODO
 *
 * @author YinGen Chu
 * @date 2017/12/6 14:23.
 * @email: youguyilin@126.com
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private List<FragmentTab> mFragmentTabs;

    public FragmentAdapter(Context context, FragmentManager fragmentManager, List<FragmentTab> fragmentTabs) {
        super(fragmentManager);
        mContext = context;
        mFragmentManager = fragmentManager;
        mFragmentTabs = fragmentTabs;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmentTab = mFragmentTabs.get(position).getFragment();
        if (fragmentTab == null){
            String fname = mFragmentTabs.get(position).getCls().getName();
            Bundle args = mFragmentTabs.get(position).getArgs();
            fragmentTab = Fragment.instantiate(mContext,fname,args);
            mFragmentTabs.get(position).setFragment(fragmentTab);
        }
        return mFragmentTabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragmentTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTabs.get(position).getTitle();
    }


}
