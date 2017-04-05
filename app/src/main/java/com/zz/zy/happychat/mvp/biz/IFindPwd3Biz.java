package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface IFindPwd3Biz {
    interface OnSubmitPWDCallBack{
        void success();
        void fail(String reason);
    }
    public void submit(Context context,String phone,String password, String repassword, OnSubmitPWDCallBack onSubmitPWDCallBack);
}
