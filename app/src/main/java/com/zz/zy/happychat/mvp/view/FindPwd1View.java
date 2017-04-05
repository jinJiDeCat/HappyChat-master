package com.zz.zy.happychat.mvp.view;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface FindPwd1View {
    public String getPhone();
    public void success(String phone);
    public void fail(String reason);
}
