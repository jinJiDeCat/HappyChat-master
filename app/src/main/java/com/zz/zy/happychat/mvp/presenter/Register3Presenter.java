package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;

import com.zz.zy.happychat.mvp.biz.IRegister3Biz;
import com.zz.zy.happychat.mvp.biz.Register3Biz;
import com.zz.zy.happychat.mvp.view.Register3View;

/**
 * Created by zzzy on 2016/12/5.
 */
public class Register3Presenter extends BasePresenter {
    private Register3View register3View;
    private IRegister3Biz register3Biz;
    public Register3Presenter(Register3View register3View){
        this.register3Biz=new Register3Biz();
        this.register3View=register3View;
    }
    public void ok(Context context){
        register3Biz.regexPassword(context,register3View.getPhone(),register3View.getPassword(), register3View.getRePassword(), new IRegister3Biz.OnRegexPasswordCallBack() {
            @Override
            public void success(String userid) {
                register3View.ok(userid);
            }

            @Override
            public void fail(String reason) {
                register3View.showTip(reason);
            }

            @Override
            public void onBefore() {
                register3View.showProgress();
            }

            @Override
            public void onAfter() {
                register3View.dismissProgress();
            }
        });
    }
}
