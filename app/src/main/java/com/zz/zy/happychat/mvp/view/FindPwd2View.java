package com.zz.zy.happychat.mvp.view;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface FindPwd2View {
    public void success();
    public void fail(String reason);
    public void onGetCodeSuccess();
    public void onGetCodeFail(String reason);
    public String getCode();
    public void setTip(String phone);
}
