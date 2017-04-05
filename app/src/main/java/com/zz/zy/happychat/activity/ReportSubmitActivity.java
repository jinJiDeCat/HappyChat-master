package com.zz.zy.happychat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.ReasonAdapter;
import com.zz.zy.happychat.mvp.model.Reason;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ReportSubmitActivity extends AppCompatActivity {
    @BindView(R.id.lv_reason)
    ListView lvReason;
    private ReasonAdapter reasonAdapter;
    private SharedPreferences sp;
    private List<Reason> reasonList;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submit);
        unbinder=ButterKnife.bind(this);
        sp=getSharedPreferences("data",MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffffffff, 0);
        initView();
        initData();
    }

    private void initData() {
        getReason();
    }

    private void initView() {

    }

    @OnClick({R.id.fl_back, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_submit:
                if(reasonAdapter.getClickPosition()>-1){
                    submit(sp.getString("userid",""),reasonList.get(reasonAdapter.getClickPosition()).getId(),getIntent().getExtras().getString("bid"),getIntent().getExtras().getStringArrayList("images"));
                }else{
                    Toast.makeText(ReportSubmitActivity.this, "请选择举报原因", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void getReason(){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/reason", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                reasonList= JSON.parseArray(s,Reason.class);
                reasonAdapter=new ReasonAdapter(ReportSubmitActivity.this, reasonList);
                lvReason.setAdapter(reasonAdapter);
                lvReason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        reasonAdapter.setClickPosition(i);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void submit(String uid,String rid,String bid,ArrayList<String> paths){
        HttpParams httpParams=new HttpParams();
        httpParams.put("uid",uid);
        httpParams.put("rid",rid);
        httpParams.put("bid",bid);
        for (String path : paths) {
            httpParams.put("photo[]",new File(path));
        }
        OkGo.post(MyApplication.BaseUrl+"index.php/api/index/report").tag(this)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Toast.makeText(ReportSubmitActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
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
