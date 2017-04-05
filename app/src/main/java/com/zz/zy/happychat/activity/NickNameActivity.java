package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

public class NickNameActivity extends AppCompatActivity {

    @BindView(R.id.et_nickname)
    EditText etNickname;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        etNickname.setText(getIntent().getStringExtra("nickname"));
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("保存中...");
    }

    @OnClick({R.id.fl_back, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_ok:
                changeInfo(sharedPreferences.getString("userid",""),etNickname.getText().toString().trim());
                break;
        }
    }
    private void changeInfo(@NonNull String userid, @NonNull final String nickname) {
        Map<String,String> map=new HashMap<>();
        map.put("nickname",nickname);
        map.put("id",userid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/editinfo", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Intent intent=new Intent();
                intent.putExtra("nickname",etNickname.getText().toString().trim());
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
