package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.Register2Presenter;
import com.zz.zy.happychat.mvp.view.Register2View;
import com.zz.zy.happychat.utils.SMSUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register2Activity extends BaseActivity implements Register2View {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sendCode)
    TextView tvSendCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    private Register2Presenter register2Presenter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(Integer.parseInt(tvSendCode.getText().toString())-1<=0){
                        this.removeMessages(0);
                        tvSendCode.setText("发送");
                        tvSendCode.setClickable(true);
                    }else{
                        tvSendCode.setText(Integer.parseInt(tvSendCode.getText().toString())-1+"");
                        this.sendEmptyMessageDelayed(0,1000);
                    }

                    break;
            }
        }
    };
    private String code;

    @Override
    public void initView() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_register2;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        register2Presenter=new Register2Presenter(this);
    }


    @OnClick({R.id.tv_sendCode, R.id.btn_next,R.id.fl_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sendCode:
                tvSendCode.setClickable(false);
                tvSendCode.setText("60");
                handler.sendEmptyMessageDelayed(0,1000);
                code= SMSUtils.GenerateCode();
                Logger.d(code);
                SMSUtils.sendSMS(this, getIntent().getStringExtra(Register1Activity.EXTRA_PHONE), code, new SMSUtils.SMSCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(Register2Activity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                break;
            case R.id.btn_next:
                //register2Presenter.next();
                if(!TextUtils.isEmpty(code)&&etCode.getText().toString().trim().equals(code)){
                    Intent intent=new Intent(this,Register3Activity.class);
                    intent.putExtra(Register1Activity.EXTRA_PHONE,getIntent().getStringExtra(Register1Activity.EXTRA_PHONE));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fl_back:
                finish();
                break;
        }
    }

    @Override
    public void setPhoneTip() {

    }

    @Override
    public String getCode() {
        return etCode.getText().toString().trim();
    }

    @Override
    public void next() {
        Intent intent=new Intent(this,Register3Activity.class);
        intent.putExtra(Register1Activity.EXTRA_PHONE,getIntent().getStringExtra(Register1Activity.EXTRA_PHONE));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    public void fail(String tip) {
        Toast.makeText(Register2Activity.this, tip, Toast.LENGTH_SHORT).show();
    }
}
