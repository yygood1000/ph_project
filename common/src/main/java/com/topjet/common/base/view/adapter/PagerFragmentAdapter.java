package com.topjet.common.base.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * creator: zhulunjun
 * time:    2017/9/8
 * describe: fragment 的 viewpager适配器
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {

    private  ArrayList<Fragment> fragments;

    public PagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
