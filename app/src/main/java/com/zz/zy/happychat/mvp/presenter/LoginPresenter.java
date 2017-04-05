package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;

import com.zz.zy.happychat.mvp.biz.ILoginBiz;
import com.zz.zy.happychat.mvp.biz.LoginBiz;
import com.zz.zy.happychat.mvp.model.User;
import com.zz.zy.happychat.mvp.view.LoginView;


public class LoginPresenter extends BasePresenter {
    private LoginView loginView;
    private ILoginBiz loginBiz;
    public LoginPresenter(LoginView loginView){
        this.loginView=loginView;
        this.loginBiz=new LoginBiz();
    }

    public void Login(Context context){
        loginBiz.login(context,loginView.getPhone(), loginView.getPassword(), new ILoginBiz.OnLoginCallBack() {
            @Override
            public void success(String token,String userid) {
                loginView.loginSuccess(token,userid);
            }

            @Override
            public void fail(String reason) {
                loginView.loginFail(reason);
            }

            @Override
            public void onBefore() {
                loginView.showProgress();
            }

            @Override
            public void onAfter() {
                loginView.dismissProgress();
            }
        });
    }
}
