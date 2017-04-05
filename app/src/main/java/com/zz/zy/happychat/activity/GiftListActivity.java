package com.zz.zy.happychat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.GiftAdapter;
import com.zz.zy.happychat.mvp.model.GiftList;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GiftListActivity extends AppCompatActivity {

    @BindView(R.id.lv_gift)
    ListView lvGift;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_list);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        getGiftList(getIntent().getStringExtra("userid"));
    }

    private void initView() {
        lvGift.addHeaderView(View.inflate(this, R.layout.gift_header, null));

    }
    private void getGiftList(String userid){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/giftlist", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<GiftList> giftListList= JSON.parseArray(s,GiftList.class);
                lvGift.setAdapter(new GiftAdapter(GiftListActivity.this,giftListList));
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }

    @OnClick(R.id.fl_back)
    public void onClick() {
        finish();
    }
}
