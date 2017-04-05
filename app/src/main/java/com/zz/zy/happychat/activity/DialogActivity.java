package com.zz.zy.happychat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.GiftPagerAdapter;
import com.zz.zy.happychat.adapter.HobbyGvAdapter;
import com.zz.zy.happychat.mvp.model.GiftPrice;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.utils.BitmapUtils;
import com.zz.zy.happychat.utils.DensityUtil;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.TimeUtils;
import com.zz.zy.happychat.view.SquareImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import okhttp3.Call;
import okhttp3.Response;

public class DialogActivity extends AppCompatActivity {

    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_reject)
    TextView tvReject;
    @BindView(R.id.tv_recieve)
    TextView tvRecieve;
    @BindView(R.id.ll_options)
    LinearLayout llOptions;
    @BindView(R.id.iv_bigAudio)
    ImageView ivBigAudio;
//    @BindView(R.id.iv_noAudio)
//    ImageView ivNoAudio;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_disconnect)
    TextView tvDisconnect;
    @BindView(R.id.fl_options)
    FrameLayout flOptions;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_close)
    TextView tvClose;
    private int position = 0;
    private PopupWindow popupWindow;
    private People TargetPeople;
    private People CurrentPeople;
    private TextView tv_leave;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    private AlertDialog ad_info,ad_star;
    private MediaPlayer mediaPlayer;
    private AssetManager assetManager;
    private boolean hasDialog=false;
    private long time=0;
    private int HasTime=0;
    private String callid;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    time++;
                    if(getIntent().getIntExtra("type",0)==0){
                        if(HasTime<0){
                            RongCallClient.getInstance().hangUpCall(callid);
                        }
                        HasTime--;
                    }
                    tvState.setText(TimeUtils.TimeToString(time));
                    this.sendEmptyMessageDelayed(0,1000);
                    break;
            }
        }
    };
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/online?id=" +
                sharedPreferences.getString("userid", "") + "&line=1", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        assetManager = DialogActivity.this.getAssets();
        initView();
        initData();
    }

    private void initData() {
//        RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE,getIntent().getStringExtra("id")
//        ,null, RongCallCommon.CallMediaType.AUDIO,"语音通话");
        getTargetInfo(getIntent().getStringExtra("id"));
        getCurrentInfo(sharedPreferences.getString("userid", ""));
    }

    private boolean isBigAudio = false;

    private void initView() {
        initRongCallListener();
        initGift();
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switch (getIntent().getIntExtra("type", 0)) {
            case 0://主动拨打电话
                List<String> users=new ArrayList<>();
                users.add(sharedPreferences.getString("userid",""));
                users.add(getIntent().getStringExtra("id"));
                HasTime=getIntent().getIntExtra("left",0);
                RongCallClient.getInstance().startCall(
                        Conversation.ConversationType.PRIVATE,
                        getIntent().getStringExtra("id"),
                        users,
                        RongCallCommon.CallMediaType.AUDIO,
                        "语音"
                );
                //播放asset文件夹下边的音频文件
                try {
                    AssetFileDescriptor afd = assetManager.openFd("voip_ring_call.mp3");
                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1://被动接听电话
                try {
                    AssetFileDescriptor afd = assetManager.openFd("voip_ring_called.mp3");
                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                llOptions.setVisibility(View.VISIBLE);
                tvDisconnect.setVisibility(View.GONE);
                tvDisconnect.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        RongCallClient.getInstance().hangUpCall(getIntent().getStringExtra("callid"));
                                                    }
                                                }
                );
                break;
        }
        tvRecieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongCallClient.getInstance().acceptCall(getIntent().getStringExtra("callid"));
                llOptions.setVisibility(View.GONE);
                tvDisconnect.setVisibility(View.VISIBLE);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
            }
        });
        tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("callid",getIntent().getStringExtra("callid"));
                RongCallClient.getInstance().hangUpCall(getIntent().getStringExtra("callid"));
            }
        });
        ivBigAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBigAudio=!isBigAudio;
                RongCallClient.getInstance().setEnableSpeakerphone(isBigAudio);
                //ivBigAudio.setImageResource(isBigAudio?R.drawable.bg_circle_blue:R.drawable.bg_border_circle_white);
                if(isBigAudio){
                    ivBigAudio.setBackgroundResource(R.drawable.bg_circle_blue);
                }else{
                    ivBigAudio.setBackgroundResource(R.drawable.bg_border_circle_white);
                }
            }
        });
//        ivNoAudio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("isMicrophoneMute =" + mAudioManager.isMicrophoneMute());
//                mAudioManager.setMicrophoneMute(!mAudioManager.isMicrophoneMute());
//                //ivNoAudio.setImageResource(isBigAudio?R.drawable.bg_circle_blue:R.drawable.bg_border_circle_white);
//                if(!mAudioManager.isMicrophoneMute()){
//                    ivNoAudio.setBackgroundResource(R.drawable.bg_border_circle_white);
//                }else{
//                    ivNoAudio.setBackgroundResource(R.drawable.bg_circle_blue);
//
//                }
//            }
//        });

    }

    /**
     * 通话结束时记录通话时间
     * @param userid 当前用户id
     * @param targetId 对方id
     * @param longtime 通话分钟数
     * @param endTime 结束时间戳
     */
    private void sendCallTime(String userid,String targetId,String longtime,String endTime){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        map.put("bid",targetId);
        map.put("longtime",longtime);
        map.put("end",endTime);
        Logger.d("longtime,"+longtime);
        Logger.d("endtime,"+endTime);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/calltime", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    /**
     * 通话结束时记录通话时间
     * @param userid 当前用户id
     * @param targetId 对方id
     * @param longtime 通话分钟数
     * @param endTime 结束时间戳
     */
    private void bsendCallTime(String userid,String targetId,String longtime,String endTime){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        map.put("bid",targetId);
        map.put("longtime",longtime);
        map.put("end",endTime);
        Logger.d("longtime,"+longtime);
        Logger.d("endtime,"+endTime);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/bcalltime", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void initRongCallListener() {
        RongCallClient.getInstance().setVoIPCallListener(new IRongCallListener() {
            @Override
            public void onCallOutgoing(final RongCallSession rongCallSession, SurfaceView surfaceView) {
                tvState.setText("已拨出");
                callid=rongCallSession.getCallId();
                tvDisconnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RongCallClient.getInstance().hangUpCall(rongCallSession.getCallId());
                        if(mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                    }
                });
            }

            @Override
            public void onCallConnected(RongCallSession rongCallSession, SurfaceView surfaceView) {
                tvState.setText("正在呼叫...");
            }

            @Override
            public void onCallDisconnected(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {
                //Toast.makeText(DialogActivity.this, "通话结束", Toast.LENGTH_SHORT).show();
                Log.e("onCallDisconnected",callDisconnectedReason.getValue()+"");
                if(hasDialog){
                    handler.removeMessages(0);
                    int min= (int) Math.ceil((rongCallSession.getEndTime()-rongCallSession.getStartTime())/60000.0);
                    if(getIntent().getIntExtra("type",0)==0){
                        sendCallTime(
                                sharedPreferences.getString("userid",""),
                                getIntent().getStringExtra("id"),
                                min+"",
                                rongCallSession.getEndTime()/1000+""
                        );
                    }else{
                        bsendCallTime(
                                getIntent().getStringExtra("id"),
                                sharedPreferences.getString("userid",""),
                                min+"",
                                rongCallSession.getEndTime()/1000+""
                        );
                    }
                    tvClose.setVisibility(View.VISIBLE);
                    tvDisconnect.setVisibility(View.GONE);
                    llOptions.setVisibility(View.GONE);
                    showStar();
                }else{
                    finish();
                }
            }

            @Override
            public void onRemoteUserRinging(String s) {
                tvState.setText("对方正在响铃...");
            }

            @Override
            public void onRemoteUserJoined(String s, RongCallCommon.CallMediaType callMediaType, SurfaceView surfaceView) {
                mediaPlayer.stop();
                hasDialog=true;
                HasTime--;
                tvState.setText("0:00");
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void onRemoteUserInvited(String s, RongCallCommon.CallMediaType callMediaType) {

            }

            @Override
            public void onRemoteUserLeft(String s, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {
                //Toast.makeText(DialogActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
                //finish();
                RongCallClient.getInstance().hangUpCall(callid);
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
            }

            @Override
            public void onMediaTypeChanged(String s, RongCallCommon.CallMediaType callMediaType, SurfaceView surfaceView) {

            }

            @Override
            public void onError(RongCallCommon.CallErrorCode callErrorCode) {
                Log.e("error",callErrorCode.getValue()+"");
                //Toast.makeText(DialogActivity.this, callErrorCode.getValue()+"", Toast.LENGTH_SHORT).show();
                if(getIntent().getIntExtra("type",0)==0){
                    tvClose.setVisibility(View.VISIBLE);
                    tvDisconnect.setVisibility(View.GONE);
                    llOptions.setVisibility(View.GONE);
                }else{
                    finish();
                }
            }

            @Override
            public void onRemoteCameraDisabled(String s, boolean b) {

            }
        });
    }

    private void initGift() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_gift, null);
        tv_leave = (TextView) view.findViewById(R.id.tv_leave);
        RecyclerViewPager recyclerViewPager = (RecyclerViewPager) view.findViewById(R.id.recyclerView);
        LinearLayout ll_indicator = (LinearLayout) view.findViewById(R.id.ll_indicator);
        initViewPager(recyclerViewPager, ll_indicator);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.bg_white)));
    }

    /**
     * 弹出评价对话框
     */
    private void showStar(){
        ad_star=null;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_mark, null, false);
        CircleImageView iv_icon= (CircleImageView) view.findViewById(R.id.iv_icon);
        final RatingBar ratingBar= (RatingBar) view.findViewById(R.id.ratingBar);
        final TextView tv_msg= (TextView) view.findViewById(R.id.tv_msg);
        final TextView tv_submit= (TextView) view.findViewById(R.id.tv_submit);
        Glide.with(this).load(MyApplication.BaseImage+TargetPeople.getPict()).into(iv_icon);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating()<1){
                    Toast.makeText(DialogActivity.this, "请评价", Toast.LENGTH_SHORT).show();
                }else{
                    tv_submit.setClickable(false);
                    giveStar(sharedPreferences.getString("userid",""),getIntent().getStringExtra("id"),((int)ratingBar.getRating())+"");
                }
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tv_msg.setText((int)v+"分");
            }
        });
        adb.setView(view);
        ad_star=adb.show();
    }

    /**
     * 提交评价信息
     * @param userid 当前用户id
     * @param targetId 对方id
     * @param star 评价星级
     */
    private void giveStar(String userid, String targetId, String star){
        Map<String,String> map=new HashMap<>();
        map.put("id",userid);
        map.put("eid",targetId);
        map.put("grade",star);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/evaluation", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ad_star.dismiss();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /**
     * 显示对方详细信息
     */
    private void showInfo() {
        ad_info=null;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_personinfo, null, false);
        TextView tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
        TextView tv_year= (TextView) view.findViewById(R.id.tv_year);
        ImageView iv_sex= (ImageView) view.findViewById(R.id.iv_sex);
        ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
        SquareImageView iv_icon= (SquareImageView) view.findViewById(R.id.iv_icon);
        ImageView iv_vip= (ImageView) view.findViewById(R.id.iv_vip);
        RecyclerView recyclerViewHobby= (RecyclerView) view.findViewById(R.id.recyclerView_hobby);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHobby.setLayoutManager(linearLayoutManager);
        if(TargetPeople.getHobby().contains(",")){
            recyclerViewHobby.setAdapter(new HobbyGvAdapter(this, TargetPeople.getHobby().split(",")));
        }
        Glide.with(this).load(MyApplication.BaseImage+TargetPeople.getPict()).into(iv_icon);
        tv_nickname.setText(TargetPeople.getNickname());
        iv_vip.setVisibility(TargetPeople.getMember().equals("0")?View.GONE:View.VISIBLE);
        iv_sex.setImageResource(TargetPeople.getSex().equals("男")?R.drawable.fennanxing:R.drawable.fennvxing);

        if(!TextUtils.isEmpty(TargetPeople.getBirthday())){
            Date birthday=new Date(Long.parseLong(TargetPeople.getBirthday())*1000);
            tv_year.setText(new Date().getYear()-birthday.getYear()+" "+ TimeUtils.dateToStar(birthday));
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad_info.dismiss();
            }
        });
        adb.setView(view);
        ad_info=adb.show();
    }

    /**
     * 显示礼物列表
     */
    private void showGift() {
        popupWindow.showAtLocation(ivGift, Gravity.BOTTOM, 0, 0);

    }

    private void initIndicator(LinearLayout ll, final RecyclerViewPager recyclerViewPager, List<GiftPrice> giftPrices) {
        final List<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < (int) Math.ceil(giftPrices.size() / 8.0); i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.drawable.bg_circle_gray);
            } else {
                imageView.setImageResource(R.drawable.bg_circle_white);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtil.dip2px(this, 15), DensityUtil.dip2px(this, 15));
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(DensityUtil.dip2px(this, 3), DensityUtil.dip2px(this, 3), DensityUtil.dip2px(this, 3), DensityUtil.dip2px(this, 3));
            imageViews.add(imageView);
            ll.addView(imageView);
        }
        recyclerViewPager.setOnScrollChangeListener(new RecyclerViewPager.OnScrollChangeListener() {
            @Override
            public void onScrollChange() {
                if (position != recyclerViewPager.getCurrentPosition()) {
                    position = recyclerViewPager.getCurrentPosition();
                    for (int j = 0; j < imageViews.size(); j++) {
                        if (position == j) {
                            imageViews.get(j).setImageResource(R.drawable.bg_circle_gray);
                        } else {
                            imageViews.get(j).setImageResource(R.drawable.bg_circle_white);
                        }
                    }
                }
            }
        });

    }

    private void initViewPager(RecyclerViewPager recyclerViewPager, LinearLayout linearLayout) {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPager.setLayoutManager(layout);
        getGiftPrice(recyclerViewPager, linearLayout);
    }

    @OnClick({R.id.iv_icon, R.id.iv_gift, R.id.tv_reject, R.id.tv_recieve})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                showInfo();
                break;
            case R.id.iv_gift:
                showGift();
                break;
            case R.id.tv_reject:
                finish();
                break;
            case R.id.tv_recieve:
                break;
        }
    }

    private void getGiftPrice(final RecyclerViewPager recyclerViewPager, final LinearLayout ll_indicator) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/giftprice", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                final List<GiftPrice> giftPriceList = JSON.parseArray(s, GiftPrice.class);
                GiftPagerAdapter giftPagerAdapter = new GiftPagerAdapter(DialogActivity.this, giftPriceList);
                giftPagerAdapter.setOnItemClickListener(new GiftPagerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int page, final int position) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(DialogActivity.this);
                        adb.setMessage("立即赠送");
                        adb.setCancelable(false);
                        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(DialogActivity.this, "" + (page * 8 + position), Toast.LENGTH_SHORT).show();
                                if (Integer.parseInt(giftPriceList.get(page * 8 + position).getGprice()) >
                                        Integer.parseInt(CurrentPeople.getCoinnum())) {
                                    Toast.makeText(DialogActivity.this, "聊币不足", Toast.LENGTH_SHORT).show();
                                } else {
                                    giveGift(sharedPreferences.getString("userid", ""), getIntent().getStringExtra("id")
                                            , giftPriceList.get(page * 8 + position).getId(),
                                            Integer.parseInt(giftPriceList.get(page * 8 + position).getGprice()),
                                            giftPriceList.get(page * 8 + position));
                                }
                            }
                        }).setNegativeButton("取消", null).show();
                    }
                });
                recyclerViewPager.setAdapter(giftPagerAdapter);
                initIndicator(ll_indicator, recyclerViewPager, giftPriceList);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(DialogActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTargetInfo(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                TargetPeople = JSON.parseObject(s, People.class);
                tvNickname.setText(TargetPeople.getNickname());
                Glide.with(DialogActivity.this).load(MyApplication.BaseImage + TargetPeople.getPict()).into(ivIcon);
//                OkGo.get(MyApplication.BaseImage + TargetPeople.getPict()).tag(DialogActivity.this)
//                        .execute(new BitmapCallback() {
//                            @Override
//                            public void onSuccess(Bitmap bitmap, Call call, Response response) {
//                                rlParent.setBackground(new BitmapDrawable(getResources(), bitmap));
//                            }
//                        });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getCurrentInfo(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                CurrentPeople = JSON.parseObject(s, People.class);
                tv_leave.setText("当前聊币:" + CurrentPeople.getCoinnum());
                //HasTime=(Integer.parseInt(CurrentPeople.getCoinnum())+Integer.parseInt(CurrentPeople.getFtime()))*60;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void giveGift(String uid, final String bid, String gid, final int price, final GiftPrice giftPrice) {
        Logger.e(HasTime+"");
        if(getIntent().getIntExtra("type",0)==0&&HasTime<price*60){
            Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("uid", uid);
            map.put("bid", bid);
            map.put("gid", gid);
            HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/givegift", map, new OnHttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    Toast.makeText(DialogActivity.this, "赠送成功", Toast.LENGTH_SHORT).show();
                    TextMessage txtMsg = TextMessage.obtain("对方赠送给你"+giftPrice.getGname());
                    RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, bid, txtMsg, "对方赠送了"+giftPrice.getGname()+"x1", "对方赠送了"+giftPrice.getGname()+"x1", new IRongCallback.ISendMediaMessageCallback() {
                        @Override
                        public void onProgress(io.rong.imlib.model.Message message, int i) {

                        }

                        @Override
                        public void onCanceled(io.rong.imlib.model.Message message) {

                        }

                        @Override
                        public void onAttached(io.rong.imlib.model.Message message) {

                        }

                        @Override
                        public void onSuccess(io.rong.imlib.model.Message message) {
                            Logger.e("对方赠送给你"+giftPrice.getGname());
                        }

                        @Override
                        public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    CurrentPeople.setCoinnum((Integer.parseInt(CurrentPeople.getCoinnum())-price)+"");
                    tv_leave.setText("当前聊币:" + CurrentPeople.getCoinnum());
                    if(getIntent().getIntExtra("type",0)==0){
                        HasTime=HasTime-price*60;
                    }
                }

                @Override
                public void onError(Exception e) {
                    //Toast.makeText(DialogActivity.this, "赠送失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        RongCallClient.getInstance().setEnableSpeakerphone(false);
        //mAudioManager.setMicrophoneMute(false);
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/online?id=" +
                sharedPreferences.getString("userid", "") + "&line=2", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
