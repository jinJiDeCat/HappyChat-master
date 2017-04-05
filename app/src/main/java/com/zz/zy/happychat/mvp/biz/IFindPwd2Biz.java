package com.zz.zy.happychat.mvp.biz;

/**
 * Created by zzzy on 2016/12/2.
 */
public interface IFindPwd2Biz {
    interface OnGetCodeCallBack{
        void success();
        void fail(String reason);
    }
    interface OnSubmitCodeCallBack{
        void success();
        void fail(String reason);
    }
    public void getCode(String phone,OnGetCodeCallBack onGetCodeCallBack);
    public void submitCode(String phone,OnSubmitCodeCallBack onSubmitCodeCallBack);
}
