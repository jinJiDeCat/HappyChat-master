package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.wuxiaolong.androidutils.library.AppUtils;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.RoomAdapter;
import com.zz.zy.happychat.mvp.model.CommendData;
import com.zz.zy.happychat.mvp.model.Room;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.xrecyclerview)
    RecyclerView xrecyclerview;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et)
    EditText et;
    private RoomAdapter roomAdapter;
    private List<CommendData.InfoBean> roomList=new ArrayList<>();
    private ProgressDialog progressDialog;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        xrecyclerview.setLayoutManager(gridLayoutManager);
        roomAdapter = new RoomAdapter(this, roomList);
        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(SearchActivity.this, RoomActivity.class);
                intent.putExtra("userid",roomAdapter.getRoomList().get(position).getId());
                startActivity(intent);
            }
        });
        xrecyclerview.setAdapter(roomAdapter);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search(et.getText().toString().trim());
                }
                return false;
            }
        });
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void search(String nickname) {
        AppUtils.hideSoftInput(this);
        roomList.clear();
        roomAdapter.setDataChange(roomList);
        Map<String, String> map = new HashMap<>();
        map.put("nickname", nickname);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/search", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                roomList = JSON.parseArray(s, CommendData.InfoBean.class);
                Collections.sort(roomList, new Comparator<CommendData.InfoBean>() {
                            @Override
                            public int compare(CommendData.InfoBean room, CommendData.InfoBean t1) {
                                return Integer.parseInt(t1.getState())-Integer.parseInt(room.getState());
                            }
                        }

                );
                roomAdapter.setDataChange(roomList);
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.setMessage("正在搜索...");
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SearchActivity.this, "没有查找到", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.fl_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_search:
                if(!TextUtils.isEmpty(et.getText().toString().trim())){
                    search(et.getText().toString().trim());
                }else{
                    Toast.makeText(SearchActivity.this, "请输入查找的用户昵称", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
