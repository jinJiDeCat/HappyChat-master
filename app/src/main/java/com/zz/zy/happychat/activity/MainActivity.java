package com.zz.zy.happychat.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.MainViewPagerAdapter;
import com.zz.zy.happychat.fragment.MeFragment;
import com.zz.zy.happychat.mvp.model.GetPush;
import com.zz.zy.happychat.mvp.model.GiftMessage;
import com.zz.zy.happychat.mvp.presenter.MainPresenter;
import com.zz.zy.happychat.mvp.view.MainView;
import com.zz.zy.happychat.service.ForeService;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.MyReceiveMessageListener;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;

public class MainActivity extends BaseActivity implements MainView {

    private static final int REQUEST_RECORDAUDIO = 0;
    private static final int REQUEST_RECORDAUDIO2 = 10;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_commend)
    ImageView ivCommend;
    @BindView(R.id.tv_commend)
    TextView tvCommend;
    @BindView(R.id.iv_tell)
    ImageView ivTell;
    @BindView(R.id.tv_tell)
    TextView tvTell;
    @BindView(R.id.iv_near)
    ImageView ivNear;
    @BindView(R.id.tv_near)
    TextView tvNear;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.tv_me)
    TextView tvMe;
    @BindView(R.id.ll_commend)
    LinearLayout llCommend;
    @BindView(R.id.ll_tell)
    LinearLayout llTell;
    @BindView(R.id.ll_near)
    LinearLayout llNear;
    @BindView(R.id.ll_me)
    LinearLayout llMe;
    private MainPresenter mainPresenter;
    private boolean isFullScreen = false;
    private MeFragment meFragment;
    private SharedPreferences sharedPreferences;
    private RongCallSession CallSession=null;
    @Override
    public void initView() {
        Intent  intent =new Intent(getApplicationContext(), ForeService.class);
        startService(intent);
        EventBus.getDefault().register(this);
        viewpager.setOffscreenPageLimit(3);
        mainPresenter.initViewPager();
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        if(!TextUtils.isEmpty(sharedPreferences.getString("userid",""))){
            giveLong(sharedPreferences.getString("userid",""));
        }
        if(!TextUtils.isEmpty(sharedPreferences.getString("token",""))){
            connect(sharedPreferences.getString("token",""));
        }
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
                    RongCallClient.setReceivedCallListener(new IRongReceivedCallListener() {
                        @Override
                        public void onReceivedCall(RongCallSession rongCallSession) {
                            CallSession=rongCallSession;
                            checkRecordPermission(rongCallSession);

                        }

                        @Override
                        public void onCheckPermission(RongCallSession rongCallSession) {
                            boolean hasPermission= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                                    .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
                            if(!hasPermission){
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
                            }else{
                                RongCallClient.getInstance().onPermissionGranted();
                            }
                        }
                    });
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

    private void checkRecordPermission(RongCallSession rongCallSession) {
        boolean hasPermission= ContextCompat.checkSelfPermission(this, Manifest.permission
                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO2);
        }else{
            Intent intent=new Intent(MainActivity.this,DialogActivity.class);
            intent.putExtra("callid",rongCallSession.getCallId());
            intent.putExtra("id",rongCallSession.getTargetId());
            intent.putExtra("type",1);
            startActivity(intent);
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int setStatusBarColor() {
        return 0xffEF889D;
    }

    @Override
    public void initPresenter() {
        mainPresenter = new MainPresenter(this);
    }


    @Override
    public void initTab() {
        ivCommend.setImageResource(R.drawable.king_nor);
        tvCommend.setTextColor(0xff888888);
        ivTell.setImageResource(R.drawable.tell_nor);
        tvTell.setTextColor(0xff888888);
        ivNear.setImageResource(R.drawable.near_nor);
        tvNear.setTextColor(0xff888888);
        ivMe.setImageResource(R.drawable.me_nor);
        tvMe.setTextColor(0xff888888);
    }

    @Override
    public void setCommendSel(Fragment fragment) {
        if (isFullScreen) {
            resetFragmentView(fragment);
        }
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        ivCommend.setImageResource(R.drawable.king_sel);
        tvCommend.setTextColor(0xffef889d);
    }

    @Override
    public void setTellSel(Fragment fragment) {
        isFullScreen = true;
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        ivTell.setImageResource(R.drawable.tell_sel);
        tvTell.setTextColor(0xffef889d);
    }

    @Override
    public void setNearSel(Fragment fragment) {
        if (isFullScreen) {
            resetFragmentView(fragment);
        }
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        ivNear.setImageResource(R.drawable.near_sel);
        tvNear.setTextColor(0xffef889d);
    }

    @Override
    public void setMeSel(Fragment fragment) {
        isFullScreen = true;
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        ivMe.setImageResource(R.drawable.me_sel);
        tvMe.setTextColor(0xffef889d);
    }

    @Override
    public void setViewPagerCurrent(int position) {
        viewpager.setCurrentItem(position, false);

    }

    @Override
    public void setViewPagerAdapter(List<Fragment> fragments) {
        this.meFragment = (MeFragment) fragments.get(3);
        viewpager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), this, fragments));
    }

    @Override
    public void setViewPagerScrollListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mainPresenter.initTab();
                mainPresenter.setChooseTabWithViewPager(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ll_commend, R.id.ll_tell, R.id.ll_near, R.id.ll_me})
    public void onClick(View view) {
        mainPresenter.initTab();
        switch (view.getId()) {
            case R.id.ll_commend:
                mainPresenter.setChooseTab(0);
                mainPresenter.setViewPagerCurrent(0);

                break;
            case R.id.ll_tell:
                mainPresenter.setChooseTab(1);
                mainPresenter.setViewPagerCurrent(1);

                break;
            case R.id.ll_near:
                mainPresenter.setChooseTab(2);
                mainPresenter.setViewPagerCurrent(2);
                break;
            case R.id.ll_me:
                mainPresenter.setChooseTab(3);
                mainPresenter.setViewPagerCurrent(3);
                break;
        }
    }

    public void resetFragmentView(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentView = findViewById(android.R.id.content);
            if (contentView != null) {
                ViewGroup rootView;
                rootView = (ViewGroup) ((ViewGroup) contentView).getChildAt(0);
                if (rootView.getPaddingTop() != 0) {
                    rootView.setPadding(0, 0, 0, 0);
                }
            }
            if (fragment.getView() != null)
                fragment.getView().setPadding(0, getStatusBarHeight(this), 0, 0);
        }
    }

    /**
     * 获得状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        meFragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        meFragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==REQUEST_RECORDAUDIO2){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(CallSession!=null){
                    Intent intent=new Intent(MainActivity.this,DialogActivity.class);
                    intent.putExtra("callid",CallSession.getCallId());
                    intent.putExtra("id",CallSession.getTargetId());
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

    private boolean canFinish = false;
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;
        MyHandler(MainActivity mainActivity){
            weakReference=new WeakReference<>(mainActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity=weakReference.get();
            if(mainActivity!=null){
                switch (msg.what) {
                    case 0:
                        mainActivity.initCanFinish();
                        break;
                }
            }
        }
    }
    private Handler handler = new MyHandler(this);

    void initCanFinish(){
        canFinish=false;
    }

    @Override
    public void onBackPressed() {
        if (canFinish) {
            super.onBackPressed();
        } else {
            canFinish = true;
            handler.sendEmptyMessageDelayed(0, 2000);
            Toast.makeText(this, "再次点击,退出语聊", Toast.LENGTH_SHORT).show();

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connectRong(GetPush getPush){
        connect(sharedPreferences.getString("token",""));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showGiftMsg(GiftMessage giftMessage){
        Toast.makeText(this, giftMessage.getMsg(), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(!TextUtils.isEmpty(sharedPreferences.getString("token","")))
            RongIMClient.getInstance().disconnect();
    }
}
