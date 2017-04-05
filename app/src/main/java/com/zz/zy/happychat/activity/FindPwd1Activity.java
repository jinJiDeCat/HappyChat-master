package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.FindPwd1Presenter;
import com.zz.zy.happychat.mvp.view.FindPwd1View;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPwd1Activity extends BaseActivity implements FindPwd1View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private FindPwd1Presenter findPwd1Presenter;

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
        return R.layout.activity_find_pwd1;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        findPwd1Presenter = new FindPwd1Presenter(this);
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString().trim();
    }

    @Override
    public void success(String phone) {
        Intent intent = new Intent(this, FindPwd2Activity.class);
        intent.putExtra(FindPwd2Activity.EXTRA_PHONE, phone);
        startActivity(intent);
    }

    @Override
    public void fail(String reason) {
        Toast.makeText(FindPwd1Activity.this, reason, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_next)
    public void onClick() {
        findPwd1Presenter.next(this);
    }
}
