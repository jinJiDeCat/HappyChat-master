package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.BlackAdapter;
import com.zz.zy.happychat.mvp.model.BlackList;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BlackListActivity extends AppCompatActivity {

    @BindView(R.id.lv_blacklist)
    XRecyclerView lvBlacklist;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        initRecyclerView();
    }


    private void initData() {
        getBlackList(sharedPreferences.getString("userid",""));
    }
    private void getBlackList(String userid){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/blacklist?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                lvBlacklist.refreshComplete();
                List<BlackList> blackLists= JSON.parseArray(s,BlackList.class);
                final BlackAdapter blackAdapter=new BlackAdapter(BlackListActivity.this,blackLists);
                blackAdapter.setOnItemClickListener(new BlackAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent=new Intent(BlackListActivity.this,RoomActivity.class);
                        intent.putExtra("userid",blackAdapter.getPersonLists().get(position).getKid());
                        startActivity(intent);
                    }
                });
                lvBlacklist.setAdapter(blackAdapter);

            }

            @Override
            public void onError(Exception e) {
                lvBlacklist.refreshComplete();
                lvBlacklist.setVisibility(View.GONE);
            }
        });
    }
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvBlacklist.setLoadingMoreEnabled(false);
        lvBlacklist.setLayoutManager(linearLayoutManager);
        lvBlacklist.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

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
