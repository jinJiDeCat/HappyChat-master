package com.zz.zy.happychat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;


import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        StatusBarUtil.setColor(this,setStatusBarColor(),0);
        //StatusBarUtil.setTranslucent(this);
        unbinder=ButterKnife.bind(this);
        initPresenter();
        initView();
    }
    public abstract void initView();
    public abstract int getContentView();
    public abstract int setStatusBarColor();
    public abstract void initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);

        unbinder.unbind();

    }
}
