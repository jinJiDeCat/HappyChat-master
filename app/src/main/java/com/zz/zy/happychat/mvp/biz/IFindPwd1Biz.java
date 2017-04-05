package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface IFindPwd1Biz {
    interface OnPhoneCallBack{
        void success(String phone);
        void fail(String reason);
    }
    public void next(Context context, String phone, OnPhoneCallBack onPhoneCallBack);
}
