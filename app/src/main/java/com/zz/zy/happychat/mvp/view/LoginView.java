package com.zz.zy.happychat.mvp.view;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface LoginView {
    public String getPhone();
    public String getPassword();
    public void loginSuccess(String token,String userid);
    public void loginFail(String reason);
    public void showProgress();
    public void dismissProgress();
}
