package com.zz.zy.happychat.mvp.biz;

import android.content.Context;
import android.text.TextUtils;

import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register3Biz implements IRegister3Biz {
    @Override
    public void regexPassword(Context context, String phone, String password, String repassword, final OnRegexPasswordCallBack onRegexPasswordCallBack) {
        if(TextUtils.isEmpty(password)){
            onRegexPasswordCallBack.fail("密码不能为空");
        }else if(password.length()<6||password.length()>18){
            onRegexPasswordCallBack.fail("密码必须为6-18位");
        }else if(!password.equals(repassword)){
            onRegexPasswordCallBack.fail("两次输入密码不一致");
        }else{
            Map<String,String> map=new HashMap<>();
            map.put("phone",phone);
            map.put("password",password);
            HttpUtils.post(context, MyApplication.BaseUrl+"index.php/api/index/register", map, new OnHttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        onRegexPasswordCallBack.success(jsonObject.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {
                    onRegexPasswordCallBack.fail("该手机号已经注册");
                }

                @Override
                public void onBefore() {
                    super.onBefore();
                    onRegexPasswordCallBack.onBefore();
                }

                @Override
                public void onAfter() {
                    super.onAfter();
                    onRegexPasswordCallBack.onAfter();
                }
            });
        }
    }
}
