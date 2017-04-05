package com.zz.zy.happychat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.activity.LoginActivity;
import com.zz.zy.happychat.mvp.model.CallState;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.mvp.presenter.TellPresenter;
import com.zz.zy.happychat.mvp.view.TellView;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.view.DiffuseView;
import com.zz.zy.happychat.view.RadarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TellFragment extends Base2Fragment implements TellView {

    @BindView(R.id.id_scan_circle)
    RadarView scanCicle;
    @BindView(R.id.ll_call)
    LinearLayout llCall;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rl_scanner)
    RelativeLayout rlScanner;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.rl_call)
    RelativeLayout rlCall;
    @BindView(R.id.diffuseView)
    DiffuseView diffuseView;
    private TellPresenter tellPresenter;
    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    if (ivSmall2.getVisibility() == View.VISIBLE && ivSmall.getVisibility() == View.VISIBLE) {
//                        ivSmall2.setVisibility(View.GONE);
//                        ivSmall.setVisibility(View.VISIBLE);
//                        ivMiddle.setVisibility(View.VISIBLE);
//                    } else if (ivMiddle.getVisibility() == View.VISIBLE && ivSmall.getVisibility() == View.VISIBLE) {
//                        ivSmall.setVisibility(View.GONE);
//                        ivLarge.setVisibility(View.VISIBLE);
//                    } else if (ivLarge.getVisibility() == View.VISIBLE && ivMiddle.getVisibility() == View.VISIBLE) {
//                        ivMiddle.setVisibility(View.GONE);
//                        ivLarge.setVisibility(View.GONE);
//                        ivSmall.setVisibility(View.VISIBLE);
//                        ivSmall2.setVisibility(View.VISIBLE);
//                    }
//                    this.sendEmptyMessageDelayed(0, 500);
//                    break;
//            }
        }
    };

    @Override
    protected void initData() {
        getInfo(sharedPreferences.getString("userid",""));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_tell;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        handler.sendEmptyMessageDelayed(0, 600);
        diffuseView.start();
        //rlCall.setVisibility(View.GONE);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeState(CallState callState){
        getInfo(sharedPreferences.getString("userid",""));
    }
    @Override
    public void initPresenter() {
        tellPresenter = new TellPresenter(this);
    }

    private void changeInfo(@NonNull String userid, @NonNull final String state) {
        Map<String,String> map=new HashMap<>();
        map.put("state",state);
        map.put("id",userid);
        HttpUtils.post(getActivity(), MyApplication.BaseUrl + "index.php/api/index/editinfo", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @OnClick({R.id.tv_cancel, R.id.ll_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                rlCall.setVisibility(View.VISIBLE);
                changeInfo(sharedPreferences.getString("userid",""),"1");
                break;
            case R.id.ll_call:
                if(TextUtils.isEmpty(sharedPreferences.getString("userid",""))){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("提示").setMessage("是否接听TA来电?").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rlCall.setVisibility(View.GONE);
                            changeInfo(sharedPreferences.getString("userid",""),"2");
                        }
                    }).setNegativeButton("否", null).show();
                }
                break;
        }
    }
    private void getInfo(String userid) {
        HttpUtils.get(getActivity(), MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                People people=JSON.parseObject(s,People.class);
                if(people.getState().equals("2")){
                    rlCall.setVisibility(View.GONE);
                }else{
                    rlCall.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Exception e) {
                rlCall.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeMessages(0);
        EventBus.getDefault().unregister(this);
    }
}
