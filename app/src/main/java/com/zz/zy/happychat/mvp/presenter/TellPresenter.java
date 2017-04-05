package com.zz.zy.happychat.mvp.presenter;

import com.zz.zy.happychat.mvp.biz.ITellBiz;
import com.zz.zy.happychat.mvp.biz.TellBiz;
import com.zz.zy.happychat.mvp.view.TellView;


public class TellPresenter extends BasePresenter {
    private TellView tellView;
    private ITellBiz tellBiz;
    public TellPresenter(TellView tellView){
        this.tellView=tellView;
        this.tellBiz=new TellBiz();
    }
}
