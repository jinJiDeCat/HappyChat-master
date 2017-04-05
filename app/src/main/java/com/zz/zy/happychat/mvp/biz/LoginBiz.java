package com.zz.zy.happychat.mvp.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.wuxiaolong.androidutils.library.RegexUtil;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginBiz implements ILoginBiz {
    @Override
    public void login(final Context context, String phone, String password, final OnLoginCallBack onLoginCallBack) {
        if(TextUtils.isEmpty(phone)){
            onLoginCallBack.fail("手机号不能为空");
        }else if(phone.length()!=11||!RegexUtil.checkPhone(phone)){
            onLoginCallBack.fail("手机号格式不正确");
        }else if(TextUtils.isEmpty(password)){
            onLoginCallBack.fail("密码不能为空");
        }else if(password.length()<6||password.length()>18){
            onLoginCallBack.fail("密码必须为6-18位");
        }else{
            Map<String,String> map=new HashMap<>();
            map.put("phone",phone);
            map.put("password",password);
            HttpUtils.post(context, MyApplication.BaseUrl + "index.php/api/index/login", map, new OnHttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    getToken(context,s,onLoginCallBack);
                }

                @Override
                public void onBefore() {
                    super.onBefore();
                    onLoginCallBack.onBefore();
                }

                @Override
                public void onAfter() {
                    super.onAfter();
                    onLoginCallBack.onAfter();
                }

                @Override
                public void onError(Exception e) {
                    onLoginCallBack.fail("登录失败");
                }
            });
        }
    }
    /**
     * 获取融云token
     */
    private void getToken(final Context context, String uid, final OnLoginCallBack onLoginCallBack){
        HttpUtils.get(context, MyApplication.BaseUrl + "index.php/api/Token/getToken?id="+uid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    onLoginCallBack.success(jsonObject.getString("token"),jsonObject.getString("uid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBefore() {
                super.onBefore();
                onLoginCallBack.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                onLoginCallBack.onAfter();
            }
            @Override
            public void onError(Exception e) {
                onLoginCallBack.fail("登录失败");
            }
        });
    }
}
