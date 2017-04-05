package com.zz.zy.happychat.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxiaolong.androidutils.library.AppUtils;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.CallState;
import com.zz.zy.happychat.mvp.presenter.LoginPresenter;
import com.zz.zy.happychat.mvp.view.LoginView;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.MyReceiveMessageListener;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends BaseActivity implements LoginView {

    private static final int REQUEST_RECORDAUDIO = 0;
    private static final int REQUEST_RECORDAUDIO2 = 10;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_findPwd)
    TextView tvFindPwd;
    private LoginPresenter loginPresenter;
    private ProgressDialog pd;
    private SharedPreferences sp;
    @Override
    public void initView() {
        sp=getSharedPreferences("data",MODE_PRIVATE);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffffffff;
    }

    @Override
    public void initPresenter() {
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public void loginSuccess(String token,String userid) {

        SharedPreferences.Editor editor=sp.edit();
        editor.putString("userid",userid);
        editor.putString("token",token);
        Log.e("token",token);
        editor.commit();
        EventBus.getDefault().post(new CallState());
        connect(token);
        giveLong(userid);
        finish();
    }
    private void giveLong(String userid){
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/givelong?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    /**
     * 建立与融云服务器的连接
     *
     * @param token token
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
                    setCallLib();
                    Log.d("LoginActivity", "--onSuccess---" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
    private RongCallSession callSession=null;
    private void checkRecordPermission(RongCallSession rongCallSession) {
        boolean hasPermission= ContextCompat.checkSelfPermission(this, Manifest.permission
                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO2);
        }else{
            Intent intent=new Intent(LoginActivity.this,DialogActivity.class);
            intent.putExtra("callid",rongCallSession.getCallId());
            intent.putExtra("id",rongCallSession.getTargetId());
            intent.putExtra("type",1);
            startActivity(intent);
        }
    }
    private void setCallLib(){
        RongCallClient.setReceivedCallListener(new IRongReceivedCallListener() {
            @Override
            public void onReceivedCall(RongCallSession rongCallSession) {
                callSession=rongCallSession;
                checkRecordPermission(rongCallSession);
            }

            @Override
            public void onCheckPermission(RongCallSession rongCallSession) {
                boolean hasPermission= ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission
                        .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
                if(!hasPermission){
                    ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
                }else{
                    RongCallClient.getInstance().onPermissionGranted();
                }
            }
        });
    }
    @Override
    public void loginFail(String reason) {
        Toast.makeText(LoginActivity.this, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_RECORDAUDIO2){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(callSession!=null){
                    Intent intent=new Intent(LoginActivity.this,DialogActivity.class);
                    intent.putExtra("callid",callSession.getCallId());
                    intent.putExtra("id",callSession.getTargetId());
                    intent.putExtra("type",1);
                    startActivity(intent);
                }
            }
        }
        if(requestCode==REQUEST_RECORDAUDIO){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                RongCallClient.getInstance().onPermissionGranted();
            }else{
                RongCallClient.getInstance().onPermissionDenied();
            }
        }
    }

    @Override
    public void showProgress() {
        pd.setMessage("正在登陆");
        pd.show();
    }

    @Override
    public void dismissProgress() {
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }


    @OnClick({R.id.tv_findPwd, R.id.btn_login,R.id.fl_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_findPwd:
                startActivity(new Intent(this, FindPwd1Activity.class));
                break;
            case R.id.btn_login:
                AppUtils.hideSoftInput(this);
                loginPresenter.Login(this);
                break;
            case R.id.fl_back:
                finish();
                break;
        }
    }

    @OnClick(R.id.tv_register)
    public void onClick() {
        startActivity(new Intent(this,Register1Activity.class));
    }
}
