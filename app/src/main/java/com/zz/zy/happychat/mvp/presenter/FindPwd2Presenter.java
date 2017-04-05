package com.zz.zy.happychat.mvp.presenter;

import com.zz.zy.happychat.mvp.biz.FindPwd2Biz;
import com.zz.zy.happychat.mvp.biz.IFindPwd2Biz;
import com.zz.zy.happychat.mvp.view.FindPwd2View;

/**
 * Created by zzzy on 2016/12/2.
 */
public class FindPwd2Presenter extends BasePresenter {
    private FindPwd2View findPwd2View;
    private IFindPwd2Biz findPwd2Biz;
    public FindPwd2Presenter(FindPwd2View findPwd2View){
        this.findPwd2View=findPwd2View;
        this.findPwd2Biz=new FindPwd2Biz();
    }
    public void getCode(String phone){
        findPwd2Biz.getCode(phone, new IFindPwd2Biz.OnGetCodeCallBack() {
            @Override
            public void success() {
                findPwd2View.onGetCodeSuccess();
            }

            @Override
            public void fail(String reason) {
                findPwd2View.onGetCodeFail(reason);
            }
        });
    }
    public void submitCode(){
        findPwd2Biz.submitCode(findPwd2View.getCode(), new IFindPwd2Biz.OnSubmitCodeCallBack() {
            @Override
            public void success() {
                findPwd2View.success();
            }

            @Override
            public void fail(String reason) {
                findPwd2View.fail(reason);
            }
        });
    }
}
