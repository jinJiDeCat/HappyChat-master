package com.zz.zy.happychat.mvp.biz;

import com.zz.zy.happychat.utils.SMSUtils;

/**
 * Created by zzzy on 2016/12/2.
 */
public class FindPwd2Biz implements IFindPwd2Biz {
    @Override
    public void getCode(String phone, OnGetCodeCallBack onGetCodeCallBack) {
        onGetCodeCallBack.success();
    }

    @Override
    public void submitCode(String phone, OnSubmitCodeCallBack onSubmitCodeCallBack) {
        onSubmitCodeCallBack.success();
    }
}
