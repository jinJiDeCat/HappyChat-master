package com.zz.zy.happychat.mvp.presenter;

import com.zz.zy.happychat.mvp.biz.IRegister2Biz;
import com.zz.zy.happychat.mvp.biz.Register2Biz;
import com.zz.zy.happychat.mvp.view.Register2View;

/**
 * Created by zzzy on 2016/12/5.
 */
public class Register2Presenter extends BasePresenter {
    private IRegister2Biz register2Biz;
    private Register2View register2View;
    public Register2Presenter(Register2View register2View){
        this.register2View=register2View;
        this.register2Biz=new Register2Biz();
    }
    public void next(){
        register2Biz.next(register2View.getCode(), new IRegister2Biz.OnRegexCodeCallBack() {
            @Override
            public void onSuccess() {
                register2View.next();
            }

            @Override
            public void onFail(String result) {
                register2View.fail(result);
            }
        });
    }
}
