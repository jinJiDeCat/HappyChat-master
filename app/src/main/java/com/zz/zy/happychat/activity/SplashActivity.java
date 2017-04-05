package com.zz.zy.happychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.CanStart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        //EventBus.getDefault().post(new Init());
    }
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void start(CanStart canStart){
        new Thread(){
            @Override
            public void run() {
                super.run();
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }.start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(sharedPreferences.getBoolean("first",false)){
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this,FlipperActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
