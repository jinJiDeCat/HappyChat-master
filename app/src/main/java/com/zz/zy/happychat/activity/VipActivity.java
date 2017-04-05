package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.ChangeVip;
import com.zz.zy.happychat.mvp.model.Config;
import com.zz.zy.happychat.mvp.model.Four;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class VipActivity extends AppCompatActivity {
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_toVip)
    TextView tvToVip;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        unbinder=ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        EventBus.getDefault().register(this);
        getFour(sharedPreferences.getString("userid",""));
        //getAction();
        //getInfo(sharedPreferences.getString("userid", ""));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeVip(ChangeVip changeVip){
        getFour(sharedPreferences.getString("userid",""));
    }
    @OnClick({R.id.fl_back, R.id.tv_toVip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_toVip:
                startActivity(new Intent(this, ChooseVipActivity.class));
                break;
        }
    }
    private void getFour(String userid){
        OkGo.get(MyApplication.BaseUrl+"index.php/api/index/getfour?id="+userid)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                Four four=JSON.parseObject(s,Four.class);
                                Four.InfoBean people = four.getInfo();
                                if (!people.getMember().equals("0")) {
                                    tvToVip.setVisibility(View.GONE);
                                    tvVip.setText("会员级别:会员");
                                }else{
                                    tvVip.setText("会员级别:普通会员");
                                }
                                List<Four.DataBean> configList = four.getData();
                                tvAction.setText(configList.get(0).getName() + ":每日免费通话" + configList.get(0).getIntro() +
                                        "分钟\n" + configList.get(1).getName() + ":每日免费通话" + configList.get(1).getIntro() + "分钟");
                            }else{
                                Toast.makeText(VipActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//    private void getInfo(String userid) {
//        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                People people = JSON.parseObject(s, People.class);
//                if (!people.getMember().equals("0")) {
//                    tvToVip.setVisibility(View.GONE);
//                    tvVip.setText("会员级别:会员");
//                }else{
//                    tvVip.setText("会员级别:普通会员");
//                }
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }

//    private void getAction() {
//        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/gettype", new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                configList = JSON.parseArray(s, Config.class);
//                tvAction.setText(configList.get(0).getName() + ":每日免费通话" + configList.get(0).getIntro() +
//                        "分钟\n" + configList.get(1).getName() + ":每日免费通话" + configList.get(1).getIntro() + "分钟");
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }
}
