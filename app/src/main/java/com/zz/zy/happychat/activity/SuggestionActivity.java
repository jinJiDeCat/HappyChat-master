package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SuggestionActivity extends AppCompatActivity {

    @BindView(R.id.et_text)
    EditText etText;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        initView();
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在提交...");
        progressDialog.setCancelable(false);
    }
    @OnClick({R.id.fl_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.btn_submit:
                if(TextUtils.isEmpty(etText.getText().toString().trim())){
                    Toast.makeText(SuggestionActivity.this, "意见不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    suggestion(sharedPreferences.getString("userid",""),etText.getText().toString().trim());
                }
                break;
        }
    }
    private void suggestion(String uid,String content){
        Map<String,String> map=new HashMap<>();
        map.put("id",uid);
        map.put("content",content);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/addadvice", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(SuggestionActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
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
