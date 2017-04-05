package com.zz.zy.happychat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.PayHistoryAdapter;
import com.zz.zy.happychat.mvp.model.PayHistory;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PayHistoryActivity extends AppCompatActivity {

    @BindView(R.id.lv_pay_history)
    ListView lvPayHistory;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initData() {
        getPayHistory(sharedPreferences.getString("userid", ""));
    }

    private void getPayHistory(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getrecord?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Logger.e(s);
                try {
                    List<PayHistory> payHistories = JSON.parseArray(s, PayHistory.class);
                    lvPayHistory.setAdapter(new PayHistoryAdapter(PayHistoryActivity.this, payHistories));
                }catch (Exception e){
                    Logger.e(e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initView() {
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
