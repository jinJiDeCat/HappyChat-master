package com.zz.zy.happychat.mvp.presenter;

import com.zz.zy.happychat.mvp.biz.INearBiz;
import com.zz.zy.happychat.mvp.biz.NearBiz;
import com.zz.zy.happychat.mvp.view.NearView;

/**
 * Created by zzzy on 2016/12/5.
 */
public class NearPresenter extends BasePresenter {
    private NearView nearView;
    private INearBiz nearBiz;
    public NearPresenter(NearView nearView){
        this.nearView=nearView;
        this.nearBiz=new NearBiz();
    }
    public void initRecyclerView(){
        nearView.initRecyclerView();
    }
}
