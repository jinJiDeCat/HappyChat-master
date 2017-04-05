package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;

import com.zz.zy.happychat.mvp.biz.FindPwd1Biz;
import com.zz.zy.happychat.mvp.biz.IFindPwd1Biz;
import com.zz.zy.happychat.mvp.view.FindPwd1View;

/**
 * Created by zzzy on 2016/12/2.
 */
public class FindPwd1Presenter extends BasePresenter {
    private FindPwd1View findPwd1View;
    private IFindPwd1Biz findPwd1Biz;
    public FindPwd1Presenter(FindPwd1View findPwd1View){
        this.findPwd1View=findPwd1View;
        this.findPwd1Biz=new FindPwd1Biz();
    }
    public void next(Context context){
        findPwd1Biz.next(context,findPwd1View.getPhone(), new IFindPwd1Biz.OnPhoneCallBack() {
            @Override
            public void success(String phone) {
                findPwd1View.success(phone);
            }

            @Override
            public void fail(String reason) {
                findPwd1View.fail(reason);
            }
        });
    }
}
