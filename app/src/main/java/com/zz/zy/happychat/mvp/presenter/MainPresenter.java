package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zz.zy.happychat.Constants;
import com.zz.zy.happychat.mvp.biz.IMainBiz;
import com.zz.zy.happychat.mvp.biz.MainBiz;
import com.zz.zy.happychat.mvp.view.MainView;

import java.util.List;

/**
 * Created by zzzy on 2016/12/1.
 */
public class MainPresenter extends BasePresenter {
    private MainView mainView;
    private IMainBiz mainBiz;
    private List<Fragment> fragments;
    public MainPresenter(MainView mainView){
        this.mainView=mainView;
        this.mainBiz=new MainBiz();
    }
    /**
     * 初始化底部Tab
     */
    public void initTab(){
        mainView.initTab();
    }

    public void setViewPagerCurrent(int position){
        mainView.setViewPagerCurrent(position);
    }

    public void initViewPager(){
        this.fragments=mainBiz.initFragments();
        mainView.setViewPagerAdapter(this.fragments);
        mainView.setViewPagerScrollListener();
    }

    public void setChooseTabWithViewPager(int i){
        switch (i){
            case 0:
                mainView.setCommendSel(this.fragments.get(i));
                break;
            case 1:
                mainView.setTellSel(this.fragments.get(i));
                break;
            case 2:
                mainView.setNearSel(this.fragments.get(i));
                break;
            case 3:
                mainView.setMeSel(this.fragments.get(i));
                break;
        }
    }

    public void setChooseTab(int i){
        switch (i){
            case 0:
                mainView.setViewPagerCurrent(i);
                mainView.setCommendSel(this.fragments.get(i));
                break;
            case 1:
                mainView.setViewPagerCurrent(i);
                mainView.setTellSel(this.fragments.get(i));
                break;
            case 2:
                mainView.setViewPagerCurrent(i);
                mainView.setNearSel(this.fragments.get(i));
                break;
            case 3:
                mainView.setViewPagerCurrent(i);
                mainView.setMeSel(this.fragments.get(i));
                break;
        }
    }
}
