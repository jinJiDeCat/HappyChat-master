package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments;
    public MainViewPagerAdapter(FragmentManager fm,Context context,List<Fragment> fragments) {
        super(fm);
        this.context=context;
        this.fragments=fragments;
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
