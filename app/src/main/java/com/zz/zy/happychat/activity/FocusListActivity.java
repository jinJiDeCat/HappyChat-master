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

public class FocusListActivity extends AppCompatActivity {

    @BindView(R.id.lv_focus)
    XRecyclerView lvFocus;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_list);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        initRecyclerView();
        initData();
    }

    private void initData() {
        getFocusList(sharedPreferences.getString("userid",""));
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvFocus.setLoadingMoreEnabled(false);
        lvFocus.setLayoutManager(linearLayoutManager);
        lvFocus.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });

    }
    private void getFocusList(String userid){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/focuslist?uid=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<PersonList> personLists= JSON.parseArray(s,PersonList.class);
                final FanAdapter fanAdapter=new FanAdapter(FocusListActivity.this,personLists);
                fanAdapter.setOnItemClickListener(new FanAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        lvFocus.refreshComplete();
                        Intent intent=new Intent(FocusListActivity.this,RoomActivity.class);
                        intent.putExtra("userid",fanAdapter.getPersonLists().get(position).getId());
                        startActivity(intent);
                    }
                });
                lvFocus.setAdapter(fanAdapter);

            }

            @Override
            public void onError(Exception e) {
                lvFocus.refreshComplete();
                lvFocus.setVisibility(View.GONE);
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
