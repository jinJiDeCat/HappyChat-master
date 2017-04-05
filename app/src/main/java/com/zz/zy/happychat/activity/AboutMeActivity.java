package com.zz.zy.happychat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutMeActivity extends AppCompatActivity {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv)
    TextView tv;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        getAboutMe();
    }

    private void getAboutMe(){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/about", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    tv.setText(jsonObject.getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @OnClick(R.id.fl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        unbinder.unbind();
    }
}
