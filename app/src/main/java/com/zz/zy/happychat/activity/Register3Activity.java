package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.Register3Presenter;
import com.zz.zy.happychat.mvp.view.Register3View;
import butterknife.BindView;
import butterknife.OnClick;

public class Register3Activity extends BaseActivity implements Register3View {

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private Register3Presenter register3Presenter;
    private SharedPreferences sp;
    private ProgressDialog pd;
    @Override
    public void initView() {
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        sp=getSharedPreferences("data",MODE_PRIVATE);
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register3;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        register3Presenter = new Register3Presenter(this);
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public String getRePassword() {
        return etRepassword.getText().toString().trim();
    }

    @Override
    public void ok(String userid) {
        Intent intent=new Intent(this,CompleteInfoActivity.class);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    @Override
    public void showTip(String tip) {
        Toast.makeText(Register3Activity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPhone() {
        return getIntent().getStringExtra(Register1Activity.EXTRA_PHONE);
    }

    @Override
    public void showProgress() {
        pd.setMessage("正在注册");
        pd.show();
    }

    @Override
    public void dismissProgress() {
        if(pd.isShowing()){
            pd.dismiss();
        }
    }


    @OnClick(R.id.btn_ok)
    public void onClick() {
        register3Presenter.ok(this);
    }
}
