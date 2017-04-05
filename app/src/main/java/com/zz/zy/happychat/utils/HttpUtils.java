package com.zz.zy.happychat.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.zz.zy.happychat.mvp.model.Result;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class HttpUtils {
    public static void post(Context context, String url, Map<String,String> map, final OnHttpCallBack onHttpCallBack){
        OkGo.post(url).tag(context).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Result result = null;
                try {
                    result=JSON.parseObject(s, Result.class);

                }catch (Exception e){
                    Log.e("httpresult","json解析错误");
                }
                if(result!=null){
                    if(result.getResult()==200){
                        onHttpCallBack.onSuccess(result.getData());
                    }else{
                        onHttpCallBack.onError(new RuntimeException(result.getResultNote()));
                    }
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                onHttpCallBack.onError(e);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                onHttpCallBack.onBefore();
            }

            @Override
            public void onAfter(String s, Exception e) {
                super.onAfter(s, e);
                onHttpCallBack.onAfter();
            }
        });
    }
    public static void get(Context context, String url, final OnHttpCallBack onHttpCallBack){
        OkGo.get(url).tag(context).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Result result = null;
                try {
                    result=JSON.parseObject(s, Result.class);

                }catch (Exception e){
                    Log.e("httpresult","json解析错误");
                }
                if(result!=null){
                    if(result.getResult()==200){
                        onHttpCallBack.onSuccess(result.getData());
                    }else{
                        onHttpCallBack.onError(new RuntimeException(result.getResultNote()));
                    }
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                onHttpCallBack.onError(e);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                onHttpCallBack.onBefore();
            }

            @Override
            public void onAfter(String s, Exception e) {
                super.onAfter(s, e);
                onHttpCallBack.onAfter();
            }
        });
    }
}
