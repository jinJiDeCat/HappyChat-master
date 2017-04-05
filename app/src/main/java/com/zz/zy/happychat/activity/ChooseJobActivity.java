package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.JobAdapter;
import com.zz.zy.happychat.mvp.model.Job;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChooseJobActivity extends AppCompatActivity {
    @BindView(R.id.lv_job)
    ListView lvJob;
    private List<Job> jobList;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_job);
        unbinder=ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initView();
        getJobType();
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("保存中...");
    }
    private void getJobType(){
        HttpUtils.get(this, MyApplication.BaseUrl+"index.php/api/index/professionlist", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                jobList=JSON.parseArray(s,Job.class);
                lvJob.setAdapter(new JobAdapter(ChooseJobActivity.this, jobList));
                lvJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     changeInfo(sharedPreferences.getString("userid",""),jobList.get(i).getProfession());
                                                 }
                                             }

                );

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @OnClick(R.id.fl_back)
    public void onClick() {
        finish();
    }
    private void changeInfo(@NonNull String userid,@NonNull final String job) {
        Map<String,String> map=new HashMap<>();
        map.put("profession",job);
        map.put("id",userid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/editinfo", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Intent intent=new Intent();
                intent.putExtra("job",job);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
