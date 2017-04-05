package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.zz.zy.happychat.adapter.HobbyAdapter;
import com.zz.zy.happychat.mvp.model.Hobby;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChooseHobbyActivity extends AppCompatActivity {

    @BindView(R.id.lv_hobby)
    ListView lvHobby;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    private List<Hobby> hobbies;
    private Map<String, String> indexs;
    private String hobby="";
    private SharedPreferences sp;
    private ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hobby);
        unbinder=ButterKnife.bind(this);
        sp=getSharedPreferences("data",MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        initData();
    }

    private void initData() {
        getZHobbyType();
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("保存中...");
        progressDialog.setCancelable(false);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(indexs==null){
                    Toast.makeText(ChooseHobbyActivity.this, "请至少选择一个兴趣", Toast.LENGTH_SHORT).show();
                }else{
                    for (Map.Entry<String,String> entry:indexs.entrySet()){
                        if(entry.getValue().equals("1")){
                            hobby+=(hobbies.get(Integer.parseInt(entry.getKey())).getHobby()+",");
                        }
                    }
                }
                editInfo(sp.getString("userid",""),hobby);
            }
        });

    }
    private void editInfo(String userid, final String hobby){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        map.put("hobby",hobby);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/editinfo", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Intent intent=new Intent();
                intent.putExtra("hobby",hobby);
                setResult(RESULT_OK,intent);
                finish();
            }

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
            public void onError(Exception e) {

            }
        });
    }
    private void getZHobbyType() {
        indexs = new HashMap<>();
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/hobbylist", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                hobbies = JSON.parseArray(s, Hobby.class);
                final HobbyAdapter hobbyAdapter = new HobbyAdapter(ChooseHobbyActivity.this, hobbies);
                lvHobby.setAdapter(hobbyAdapter);
                hobbyAdapter.setIndex(indexs);
                lvHobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (TextUtils.isEmpty(indexs.get(i + ""))) {
                            indexs.put(i + "", "1");
                        } else {
                            indexs.put(i + "", "");
                        }
                        hobbyAdapter.setIndex(indexs);
                    }
                });
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
