package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.VipAdapter;
import com.zz.zy.happychat.mvp.model.ChangeVip;
import com.zz.zy.happychat.mvp.model.VipPrice;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChooseVipActivity extends AppCompatActivity {

    @BindView(R.id.lv_vip)
    ListView lvVip;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private VipAdapter vipAdapter;
    private Unbinder unbinder;
    private List<VipPrice> vipPrices=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vip);
        unbinder = ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        initData();
    }

    private void initData() {
        getVipList();
    }

    private void getVipList() {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/memberlist", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                vipPrices = JSON.parseArray(s, VipPrice.class);
                vipAdapter.setVipPrices(vipPrices);
                lvVip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        vipAdapter.setClickItem(i);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在操作...");
        vipAdapter = new VipAdapter(ChooseVipActivity.this, vipPrices);
        lvVip.setAdapter(vipAdapter);
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vipAdapter.getClickItem()<0){
                    Toast.makeText(ChooseVipActivity.this, "请先选择会员种类", Toast.LENGTH_SHORT).show();
                }else{
                    openMember(sharedPreferences.getString("userid",""),vipAdapter.getVipPrices().get(vipAdapter.getClickItem()).getId());
                }
            }
        });
    }
    private void openMember(String userid,String hid){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        map.put("hid",hid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/openmember", map, new OnHttpCallBack() {
            @Override
            public void onBefore() {
                super.onBefore();
                if(!progressDialog.isShowing())
                    progressDialog.show();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(ChooseVipActivity.this, "开通成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new ChangeVip());
                finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ChooseVipActivity.this, "聊币不足，请先充值", Toast.LENGTH_SHORT).show();
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
