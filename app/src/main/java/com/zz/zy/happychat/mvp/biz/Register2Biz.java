package com.zz.zy.happychat.mvp.biz;

/**
 * Created by zzzy on 2016/12/5.
 */
public class Register2Biz implements IRegister2Biz {
    @Override
    public void next(String code, OnRegexCodeCallBack onRegexCodeCallBack) {
        onRegexCodeCallBack.onSuccess();
    }
}
