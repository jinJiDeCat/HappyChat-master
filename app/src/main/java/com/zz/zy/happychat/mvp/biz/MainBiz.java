package com.zz.zy.happychat.mvp.biz;

import android.support.v4.app.Fragment;

import com.zz.zy.happychat.fragment.CommendFragment;
import com.zz.zy.happychat.fragment.MeFragment;
import com.zz.zy.happychat.fragment.NearFragment;
import com.zz.zy.happychat.fragment.TellFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzy on 2016/12/1.
 */
public class MainBiz implements IMainBiz {
    @Override
    public List<Fragment> initFragments() {
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new CommendFragment());
        fragments.add(new TellFragment());
        fragments.add(new NearFragment());
        fragments.add(new MeFragment());
        return fragments;
    }
}
