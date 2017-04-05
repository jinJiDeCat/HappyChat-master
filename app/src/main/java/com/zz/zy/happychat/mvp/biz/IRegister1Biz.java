package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

/**
 * Created by zzzy on 2016/12/5.
 */
public interface IRegister1Biz {
    interface OnPhoneRegexCallBack{
        void onSuccess();
        void onFail(String tip);
        void onBefore();
        void onAfter();
    }
    void RegexPhone(Context context, String phone, OnPhoneRegexCallBack onPhoneRegexCallBack);
}
