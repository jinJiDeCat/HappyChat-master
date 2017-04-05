package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;

import com.zz.zy.happychat.mvp.biz.IRegister1Biz;
import com.zz.zy.happychat.mvp.biz.Register1Biz;
import com.zz.zy.happychat.mvp.view.Register1View;

/**
 * Created by zzzy on 2016/12/5.
 */
public class Register1Presenter extends BasePresenter {
    private Register1View register1View;
    private IRegister1Biz register1Biz;
    public Register1Presenter(Register1View register1View){
        this.register1View=register1View;
        this.register1Biz=new Register1Biz();
    }
    public void RegexPhone(Context context){
        register1Biz.RegexPhone(context,register1View.getPhone(), new IRegister1Biz.OnPhoneRegexCallBack() {
            @Override
            public void onSuccess() {
                register1View.next();
            }

            @Override
            public void onFail(String tip) {
                register1View.tip(tip);
            }

            @Override
            public void onBefore() {
                register1View.showProgress();
            }

            @Override
            public void onAfter() {
                register1View.dismissProgress();
            }
        });
    }
}
