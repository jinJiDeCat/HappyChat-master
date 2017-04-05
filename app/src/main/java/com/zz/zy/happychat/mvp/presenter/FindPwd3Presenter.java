package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;

import com.zz.zy.happychat.mvp.biz.FindPwd3Biz;
import com.zz.zy.happychat.mvp.biz.IFindPwd3Biz;
import com.zz.zy.happychat.mvp.view.FindPwd3View;

/**
 * Created by zzzy on 2016/12/2.
 */
public class FindPwd3Presenter extends BasePresenter {
    private IFindPwd3Biz findPwd3Biz;
    private FindPwd3View findPwd3View;
    public FindPwd3Presenter(FindPwd3View findPwd3View){
        this.findPwd3Biz=new FindPwd3Biz();
        this.findPwd3View=findPwd3View;
    }
    public void submit(Context context){
        findPwd3Biz.submit(context,findPwd3View.getPhone(),findPwd3View.getPassword(), findPwd3View.getRePassword(), new IFindPwd3Biz.OnSubmitPWDCallBack() {
            @Override
            public void success() {
                findPwd3View.success();
            }

            @Override
            public void fail(String reason) {
                findPwd3View.fail(reason);
            }
        });
    }
}
