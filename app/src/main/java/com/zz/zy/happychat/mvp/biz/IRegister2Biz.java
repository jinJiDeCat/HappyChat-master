package com.zz.zy.happychat.mvp.biz;

/**
 * Created by zzzy on 2016/12/5.
 */
public interface IRegister2Biz {
    interface OnRegexCodeCallBack{
        void onSuccess();
        void onFail(String result);
    }
    void next(String code,OnRegexCodeCallBack onRegexCodeCallBack);
}
