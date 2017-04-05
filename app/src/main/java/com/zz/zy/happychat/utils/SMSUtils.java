package com.zz.zy.happychat.utils;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class SMSUtils {
    public interface SMSCallBack{
        void onSuccess( String s);
        void onError(Exception e);
    }
    public static void sendSMS(Context context, String phone, String mcode, final SMSCallBack smsCallBack){
        Map<String,String> map=new HashMap<>();
        HttpParams httpParams=new HttpParams();
        map.put("method","alibaba.aliqin.fc.sms.num.send");
        map.put("app_key","23599420");
        map.put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("format","json");
        map.put("v","2.0");
        map.put("sign_method","md5");
        map.put("sms_type","normal");
        map.put("sms_free_sign_name","搜新科技");
        map.put("sms_param","{\"code\":\""+mcode+"\"}");
        map.put("rec_num",phone);
        map.put("sms_template_code","SMS_40140035");
        try {
            httpParams.put("sign",signTopRequest(map,"3f30e61778dd72bcdc2eb4205a5f6331"));
            httpParams.put(map);
            OkGo.post("http://gw.api.taobao.com/router/rest").tag(context)
                    .params(httpParams)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("sms",s);
                            smsCallBack.onSuccess(s);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            smsCallBack.onError(e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String signTopRequest(Map<String, String> params, String secret) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(secret);
        for (String key : keys) {
            String value = params.get(key);
            query.append(key).append(value);
        }
        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        query.append(secret);
        bytes = encryptMD5(query.toString());
        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
    public static byte[] encryptMD5(String data) throws IOException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(data.getBytes("UTF-8"));
        return md5.digest();
    }
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
    public static String GenerateCode(){
        return (int)((Math.random()*9+1)*100000)+"";
    }
}