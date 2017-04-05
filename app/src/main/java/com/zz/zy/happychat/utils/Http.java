package com.zz.zy.happychat.utils;

import android.util.Log;

import com.zz.zy.happychat.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zzzy on 2016/12/20.
 */
public class Http {
//    private static Retrofit retrofit;
//    private static ApiService apiService;
//    private Http(){}
//    public static ApiService getInstance(){
//        if(apiService==null){
//            OkHttpClient httpClient = new OkHttpClient();
//            if (BuildConfig.DEBUG) {
//                httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Log.w("retrofit",chain.request().url().toString());
//                        return chain.proceed(chain.request());
//                    }
//                }).build();
//            }
//            retrofit=new Retrofit.Builder()
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(httpClient)
//                    .baseUrl("http://192.168.1.112/")
//                    .build();
//            apiService=retrofit.create(ApiService.class);
//        }
//        return apiService;
//    }
}
