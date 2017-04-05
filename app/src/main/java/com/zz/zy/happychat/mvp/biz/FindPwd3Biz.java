package com.zz.zy.happychat.mvp.biz;

import android.content.Context;
import android.text.TextUtils;

import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzzy on 2016/12/2.
 */
public class FindPwd3Biz implements IFindPwd3Biz {

    @Override
    public void submit(Context context, String phone, String password, String repassword, final OnSubmitPWDCallBack onSubmitPWDCallBack) {
        if(TextUtils.isEmpty(password)){
            onSubmitPWDCallBack.fail("密码不能为空");
        }else if(password.length()<6||password.length()>18){
            onSubmitPWDCallBack.fail("密码必须为6-18位");
        }else if(!password.equals(repassword)){
            onSubmitPWDCallBack.fail("两次密码不相同");
        }else{
            Map<String,String> map=new HashMap<>();
            map.put("phone",phone);
            map.put("password",password);
            HttpUtils.post(context, MyApplication.BaseUrl+"index.php/api/index/editpwd", map, new OnHttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    onSubmitPWDCallBack.success();
                }

                @Override
                public void onError(Exception e) {
                    onSubmitPWDCallBack.fail("失败");
                }
            });
        }
    }
}
