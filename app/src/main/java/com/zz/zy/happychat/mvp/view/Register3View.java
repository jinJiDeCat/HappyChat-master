package com.zz.zy.happychat.mvp.view;

/**
 * Created by zzzy on 2016/12/5.
 */
public interface Register3View {
    String getPassword();
    String getRePassword();
    void ok(String userid);
    void showTip(String tip);
    String getPhone();
    void showProgress();
    void dismissProgress();
}
