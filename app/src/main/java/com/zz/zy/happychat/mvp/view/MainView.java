package com.zz.zy.happychat.mvp.view;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by zzzy on 2016/12/1.
 */
public interface MainView {
    void initTab();
    void setCommendSel(Fragment fragment);
    void setTellSel(Fragment fragment);
    void setNearSel(Fragment fragment);
    void setMeSel(Fragment fragment);
    void setViewPagerCurrent(int position);
    void setViewPagerAdapter(List<Fragment> fragments);
    void setViewPagerScrollListener();
}
