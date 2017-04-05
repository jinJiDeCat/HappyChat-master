package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.FindPwd3Presenter;
import com.zz.zy.happychat.mvp.view.FindPwd3View;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPwd3Activity extends BaseActivity implements FindPwd3View {


    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private FindPwd3Presenter findPwd3Presenter;

    @Override
    public void initView() {
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_find_pwd3;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        findPwd3Presenter = new FindPwd3Presenter(this);
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
    public void success() {
        Toast.makeText(FindPwd3Activity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,LoginActivity.class));
    }

    @Override
    public void fail(String reason) {
        Toast.makeText(FindPwd3Activity.this, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPhone() {
        return getIntent().getStringExtra(FindPwd2Activity.EXTRA_PHONE);
    }

    @OnClick(R.id.btn_ok)
    public void onClick() {
        findPwd3Presenter.submit(this);
    }
}
