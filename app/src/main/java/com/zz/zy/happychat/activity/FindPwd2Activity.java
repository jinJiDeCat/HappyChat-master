package com.zz.zy.happychat.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.presenter.FindPwd2Presenter;
import com.zz.zy.happychat.mvp.view.FindPwd2View;
import com.zz.zy.happychat.utils.SMSUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class FindPwd2Activity extends BaseActivity implements FindPwd2View {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sendCode)
    TextView tvSendCode;
    private String code;
    private FindPwd2Presenter findPwd2Presenter;
    public static final String EXTRA_PHONE="phone";
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
    @Override
    public void initView() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_find_pwd2;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        findPwd2Presenter = new FindPwd2Presenter(this);
    }



    @Override
    public void success() {
        Intent intent=new Intent(this,FindPwd3Activity.class);
        intent.putExtra(EXTRA_PHONE,getIntent().getStringExtra(EXTRA_PHONE));
        startActivity(intent);
    }

    @Override
    public void fail(String reason) {
        Toast.makeText(FindPwd2Activity.this, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetCodeSuccess() {
        Toast.makeText(FindPwd2Activity.this, "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetCodeFail(String reason) {
        Toast.makeText(FindPwd2Activity.this, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getCode() {
        return etCode.getText().toString().trim();
    }

    @Override
    public void setTip(String phone) {
        tvTip.setText(phone);
    }

    @OnClick({R.id.tv_sendCode, R.id.btn_next,R.id.fl_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sendCode:
                tvSendCode.setClickable(false);
                tvSendCode.setText("60");
                handler.sendEmptyMessageDelayed(0,1000);
                code=SMSUtils.GenerateCode();
                SMSUtils.sendSMS(this, getIntent().getStringExtra(EXTRA_PHONE), code, new SMSUtils.SMSCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(FindPwd2Activity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                //findPwd2Presenter.getCode(getIntent().getStringExtra(EXTRA_PHONE));
                break;
            case R.id.btn_next:
                //findPwd2Presenter.submitCode();
                if(!TextUtils.isEmpty(code)&&etCode.getText().toString().trim().equals(code)){
                    Intent intent=new Intent(this,FindPwd3Activity.class);
                    intent.putExtra(EXTRA_PHONE,getIntent().getStringExtra(EXTRA_PHONE));
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
