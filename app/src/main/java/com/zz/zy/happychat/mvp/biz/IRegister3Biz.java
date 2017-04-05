package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

/**
 * Created by zzzy on 2016/12/5.
 */
public interface IRegister3Biz {
    interface OnRegexPasswordCallBack{
        void success(String uerid);
        void fail(String reason);
        void onBefore();
        void onAfter();
    }
    void regexPassword(Context context,String phone, String password, String repassword, OnRegexPasswordCallBack onRegexPasswordCallBack);
}
