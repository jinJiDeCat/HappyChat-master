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
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.HistoryAdapter;
import com.zz.zy.happychat.mvp.model.History;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GetMoneyHistoryActivity extends AppCompatActivity {

    @BindView(R.id.lv_get_history)
    ListView lvGetHistory;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money_history);
        unbinder = ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        initData();
    }

    private void initData() {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/withdrawlist?id=" + sharedPreferences.getString("userid", ""), new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<History> histories = JSON.parseArray(s, History.class);
                lvGetHistory.setAdapter(new HistoryAdapter(GetMoneyHistoryActivity.this, histories));
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
