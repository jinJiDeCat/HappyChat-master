package com.zz.zy.happychat.mvp.biz;

import android.content.Context;
import android.text.TextUtils;

import com.wuxiaolong.androidutils.library.RegexUtil;


public class Register1Biz implements IRegister1Biz {
    @Override
    public void RegexPhone(Context context, String phone, final OnPhoneRegexCallBack onPhoneRegexCallBack) {
        if(TextUtils.isEmpty(phone)){
            onPhoneRegexCallBack.onFail("手机号不能为空");
        }else if(phone.length()!=11||!RegexUtil.checkPhone(phone)){
            onPhoneRegexCallBack.onFail("手机号格式不正确");
        }else{
            onPhoneRegexCallBack.onSuccess();
        }

    }
}
