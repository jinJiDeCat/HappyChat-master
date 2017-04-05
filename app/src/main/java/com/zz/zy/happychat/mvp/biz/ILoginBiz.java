package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

import com.zz.zy.happychat.mvp.model.User;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface ILoginBiz {
    interface OnLoginCallBack{
        void success(String token,String userid);
        void fail(String reason);
        void onBefore();
        void onAfter();
    }
    void login(Context context, String phone, String password, OnLoginCallBack onLoginCallBack);
}
