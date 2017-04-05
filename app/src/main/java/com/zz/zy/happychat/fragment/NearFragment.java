package com.zz.zy.happychat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.wuxiaolong.androidutils.library.TimeUtil;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.activity.DialogActivity;
import com.zz.zy.happychat.activity.GiftListActivity;
import com.zz.zy.happychat.activity.LoginActivity;
import com.zz.zy.happychat.activity.RoomActivity;
import com.zz.zy.happychat.adapter.NearAdapter;
import com.zz.zy.happychat.mvp.model.NearList;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.mvp.presenter.NearPresenter;
import com.zz.zy.happychat.mvp.view.NearView;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

public class NearFragment extends Base2Fragment implements NearView {
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    private NearPresenter nearPresenter;
//    private String[] opts={
//            "删除该信息"
//    };
    private List<NearList> nearLists=new ArrayList<>();
    private NearAdapter nearAdapter;
    private SharedPreferences sharedPreferences;
    @Override
    protected void initData() {
        getNearList(sharedPreferences.getString("userid",""));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_near;
    }

    @Override
    public void initView(View view) {
        sharedPreferences=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        nearPresenter.initRecyclerView();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getNearList(String userid){

        HttpUtils.get(getActivity(), MyApplication.BaseUrl + "index.php/api/index/lastlist?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                nearLists.clear();
                xrecyclerview.refreshComplete();
                nearLists=JSON.parseArray(s,NearList.class);
                nearAdapter.setNearLists(nearLists);
                Logger.d(s);
            }

            @Override
            public void onError(Exception e) {
                nearLists.clear();
            }
        });
    }
    @Override
    public void initPresenter() {
        nearPresenter=new NearPresenter(this);
    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerview.setLayoutManager(linearLayoutManager);
        xrecyclerview.setLoadingMoreEnabled(false);
        nearAdapter=new NearAdapter(getActivity(),nearLists);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNearList(sharedPreferences.getString("userid",""));
            }

            @Override
            public void onLoadMore() {

            }
        });
        nearAdapter.setOnItemLongClickListener(new NearAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(int position) {

            }
        });
        xrecyclerview.setAdapter(nearAdapter);
//        nearAdapter.setOnItemLongClickListener(new NearAdapter.OnItemLongClickListener() {
//            @Override
//            public void OnItemLongClick(final int position) {
//                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
//                adb.setItems(opts, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                adb.show();
//            }
//        });

    }
}
