package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.Register1Presenter;
import com.zz.zy.happychat.mvp.view.Register1View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register1Activity extends BaseActivity implements Register1View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    private Register1Presenter register1Presenter;
    public static final String EXTRA_PHONE="phone";
    private ProgressDialog pd;
    @Override
    public void initView() {
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register1;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        register1Presenter = new Register1Presenter(this);
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString().trim();
    }

    @Override
    public void next() {
        Intent intent=new Intent(this,Register2Activity.class);
        intent.putExtra(EXTRA_PHONE,etPhone.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void tip(String tip) {
        Toast.makeText(Register1Activity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        pd.setMessage("验证手机号");
        pd.show();
    }

    @Override
    public void dismissProgress() {
        if(pd.isShowing()){
            pd.dismiss();
        }
    }


    @OnClick(R.id.btn_next)
    public void onClick() {
        register1Presenter.RegexPhone(this);
    }

}
