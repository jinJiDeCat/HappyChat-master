package com.zz.zy.happychat.mvp.view;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface FindPwd3View {
    public String getPassword();
    public String getRePassword();
    public void success();
    public void fail(String reason);
    public String getPhone();
}
