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
import com.zz.zy.happychat.adapter.FanAdapter;
import com.zz.zy.happychat.mvp.model.PersonList;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FansListActivity extends AppCompatActivity {

    @BindView(R.id.lv_fans)
    XRecyclerView lvFans;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        initRecyclerView();
        initData();
    }

    private void initData() {
        getFanList(sharedPreferences.getString("userid",""));
    }
    private void getFanList(String userid){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/fanslist?uid=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                lvFans.refreshComplete();
                List<PersonList> personLists= JSON.parseArray(s,PersonList.class);
                final FanAdapter fanAdapter=new FanAdapter(FansListActivity.this,personLists);
                fanAdapter.setOnItemClickListener(new FanAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent=new Intent(FansListActivity.this,RoomActivity.class);
                        intent.putExtra("userid",fanAdapter.getPersonLists().get(position).getId());
                        startActivity(intent);
                    }
                });
                lvFans.setAdapter(fanAdapter);
            }

            @Override
            public void onError(Exception e) {
                lvFans.refreshComplete();
                lvFans.setVisibility(View.GONE);
            }
        });
    }
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvFans.setLoadingMoreEnabled(false);
        lvFans.setLayoutManager(linearLayoutManager);
        lvFans.setLoadingListener(new XRecyclerView.LoadingListener() {
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
