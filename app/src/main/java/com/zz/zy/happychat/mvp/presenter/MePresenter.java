package com.zz.zy.happychat.mvp.presenter;

import com.zz.zy.happychat.mvp.biz.IMeBiz;
import com.zz.zy.happychat.mvp.biz.MeBiz;
import com.zz.zy.happychat.mvp.view.MeView;

/**
 * Created by zzzy on 2016/12/5.
 */
public class MePresenter extends BasePresenter{
    private IMeBiz meBiz;
    private MeView meView;
    public MePresenter(MeView meView){
        this.meView=meView;
        this.meBiz=new MeBiz();
    }
}
