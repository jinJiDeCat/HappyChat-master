package com.zz.zy.happychat.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.mylhyl.superdialog.SuperDialog;
import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.GiftRecyclerAdapter;
import com.zz.zy.happychat.adapter.HobbyGvAdapter;
import com.zz.zy.happychat.mvp.model.GiftList;
import com.zz.zy.happychat.mvp.model.Item;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.mvp.model.TwoInfo;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.TimeUtils;
import com.zz.zy.happychat.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

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
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import okhttp3.Call;
import okhttp3.Response;

public class RoomActivity extends AppCompatActivity {

    private static final int REQUEST_RECORDAUDIO2 = 10;
    @BindView(R.id.recycler_gift)
    GridView recyclerGift;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.iv_dialog)
    ImageView ivDialog;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.recyclerView_hobby)
    RecyclerView recyclerViewHobby;
    @BindView(R.id.tv_audioTime)
    TextView tvAudioTime;
    @BindView(R.id.ll_gift)
    LinearLayout llGift;
    @BindView(R.id.fl_giftmore)
    FrameLayout flGiftmore;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private boolean hasLike = false;
    private SharedPreferences sharedPreferences;
    private TwoInfo.InfoBean people;
    private TwoInfo.DataBean mePeople;
    private boolean hasBlack = false;
    private Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        unBinder=ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        initView();
        initGiftRecyclerView();
        initData();
    }

    private void initData() {
        Logger.e("getMyInfo");
        //getMyInfo(sharedPreferences.getString("userid",""));
        getTwo(sharedPreferences.getString("userid",""),getIntent().getStringExtra("userid"));


    }

    private void initGiftRecyclerView() {
        recyclerGift.setFocusable(false);
    }

    private void getGiftList(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/giftlist", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<GiftList> giftListList = JSON.parseArray(s, GiftList.class);
                recyclerGift.setAdapter(new GiftRecyclerAdapter(RoomActivity.this, giftListList));
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initView() {


    }

    @OnClick({R.id.iv_more, R.id.fl_back, R.id.iv_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.iv_more:
                if (!TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    final List<Item> list = new ArrayList<>();
                    if (hasBlack)
                        list.add(new Item(1, "取消拉黑"));
                    else
                        list.add(new Item(1, "拉黑"));
                    list.add(new Item(2, "举报"));
                    new SuperDialog.Builder(this)
                            .setCanceledOnTouchOutside(false)
                            .setItems(list, new SuperDialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    switch (position) {
                                        case 0:
                                            if (hasBlack)
                                                exitBlack(sharedPreferences.getString("userid", ""), people.getId());
                                            else
                                                joinBlack(sharedPreferences.getString("userid", ""), people.getId());
                                            break;
                                        case 1:
                                            Intent intent = new Intent(RoomActivity.this, ReportActivity.class);
                                            intent.putExtra("bid", people.getId());
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .build();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.iv_like:
                if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    focus(sharedPreferences.getString("userid", ""), people.getId());
                }
                break;
        }
    }

    private void isBlack(String userid, String kid) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userid);
        map.put("lid", kid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/question", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("focus") == 1) {
                        hasLike = true;
                        ivLike.setImageResource(R.drawable.like);
                    } else {
                        hasLike = false;
                        ivLike.setImageResource(R.drawable.xin);
                    }
                    hasBlack = (jsonObject.getInt("black") == 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initRecyclerview(String[] strings) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHobby.setLayoutManager(linearLayoutManager);
        recyclerViewHobby.setAdapter(new HobbyGvAdapter(this, strings));
    }

    private void audioInit(String url_audio) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url_audio);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.e("time", mediaPlayer.getDuration() + "");
                    tvAudioTime.setText(mediaPlayer.getDuration() / 1000 + "''");
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.sanjiao);
                    isPlay=false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    ivPlay.setImageResource(R.drawable.sanjiao);
                    mediaPlayer.pause();
                    isPlay=false;
                }else{
                    ivPlay.setImageResource(R.drawable.pause_small);
                    mediaPlayer.start();
                    isPlay=true;
                }
            }
        });
    }
    private boolean isPlay=false;
    private void exitBlack(String userid, String kid) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userid);
        map.put("kid", kid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/exitblack", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(RoomActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                hasBlack = false;
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RoomActivity.this, "请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinBlack(String userid, String kid) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userid);
        map.put("kid", kid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/joinblack", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(RoomActivity.this, "拉黑成功", Toast.LENGTH_SHORT).show();
                hasBlack = true;
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RoomActivity.this, "请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
        unBinder.unbind();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            wifiLock.release();
            mediaPlayer = null;
        }

    }
    private void getTwo(String userid,String bid){
        OkGo.get(MyApplication.BaseUrl+"index.php/api/index/gettwo?id="+userid+"&bid="+bid)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                TwoInfo twoInfo=JSON.parseObject(s,TwoInfo.class);
                                mePeople=twoInfo.getData();
                                people = twoInfo.getInfo();
                                tvId.setText(people.getId());
                                tvNickname.setText(people.getNickname());
                                Glide.with(RoomActivity.this).load(MyApplication.BaseImage + people.getPict()).into(ivAvatar);
                                if (!TextUtils.isEmpty(people.getBirthday())) {
                                    Date birthday = new Date(Long.parseLong(people.getBirthday()) * 1000);
                                    tvYear.setText(new Date().getYear() - birthday.getYear() + " " + TimeUtils.dateToStar(birthday));
                                }
                                tvSex.setText(people.getSex());
                                if (people.getHobby().contains(",")) {
                                    initRecyclerview(people.getHobby().split(","));
                                }
                                if (!TextUtils.isEmpty(people.getVoice())) {
                                    audioInit(MyApplication.BaseImage + people.getVoice());
                                }
                                isBlack(sharedPreferences.getString("userid", ""), people.getId());
                                getGiftList(people.getId());
                                flGiftmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(RoomActivity.this, GiftListActivity.class);
                                        intent.putExtra("userid", people.getId());
                                        startActivity(intent);
                                    }
                                });
                                ivDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Logger.e(Integer.parseInt(people.getCoinnum())+"");
                                        Logger.e(Integer.parseInt(people.getFtime())+"");
                                        Logger.e((Integer.parseInt(people.getCoinnum())+
                                                Integer.parseInt(people.getFtime())<=0)+"");
                                        if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
                                            startActivity(new Intent(RoomActivity.this, LoginActivity.class));
                                        }else if(!people.getState().equals("2")){
                                            Toast.makeText(RoomActivity.this, "对方当前离线", Toast.LENGTH_SHORT).show();
                                        }else if(people.getOnline().equals("2")){
                                            Toast.makeText(RoomActivity.this, "对方正在通话中", Toast.LENGTH_SHORT).show();
                                        }else if(
                                                Integer.parseInt(mePeople.getCoinnum())+
                                                        Integer.parseInt(mePeople.getFtime())<=0
                                                ){
                                            Toast.makeText(RoomActivity.this, "余额不足，请充值", Toast.LENGTH_SHORT).show();
                                        } else {
                                            checkRecordPermission();

                                        }
                                    }
                                });
                                Logger.e("finish");
                            }else{
                                Toast.makeText(RoomActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//    private void getMyInfo(String userid) {
//        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                mePeople = JSON.parseObject(s, People.class);
//                Logger.e("getInfo");
//                getInfo(getIntent().getStringExtra("userid"));
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }
//    private void getInfo(String userid) {
//        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                Logger.e("json");
//                people = JSON.parseObject(s, People.class);
//                tvId.setText(people.getId());
//                tvNickname.setText(people.getNickname());
//                Glide.with(RoomActivity.this).load(MyApplication.BaseImage + people.getPict()).into(ivAvatar);
//                if (!TextUtils.isEmpty(people.getBirthday())) {
//                    Date birthday = new Date(Long.parseLong(people.getBirthday()) * 1000);
//                    tvYear.setText(new Date().getYear() - birthday.getYear() + " " + TimeUtils.dateToStar(birthday));
//                }
//                tvSex.setText(people.getSex());
//                if (people.getHobby().contains(",")) {
//                    initRecyclerview(people.getHobby().split(","));
//                }
//                if (!TextUtils.isEmpty(people.getVoice())) {
//                    audioInit(MyApplication.BaseImage + people.getVoice());
//                }
//                isBlack(sharedPreferences.getString("userid", ""), people.getId());
//                getGiftList(people.getId());
//                flGiftmore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(RoomActivity.this, GiftListActivity.class);
//                        intent.putExtra("userid", people.getId());
//                        startActivity(intent);
//                    }
//                });
//                ivDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Logger.e(Integer.parseInt(people.getCoinnum())+"");
//                        Logger.e(Integer.parseInt(people.getFtime())+"");
//                        Logger.e((Integer.parseInt(people.getCoinnum())+
//                                Integer.parseInt(people.getFtime())<=0)+"");
//                        if (TextUtils.isEmpty(sharedPreferences.getString("userid", ""))) {
//                            startActivity(new Intent(RoomActivity.this, LoginActivity.class));
//                        }else if(!people.getState().equals("2")){
//                            Toast.makeText(RoomActivity.this, "对方当前离线", Toast.LENGTH_SHORT).show();
//                        }else if(people.getOnline().equals("2")){
//                            Toast.makeText(RoomActivity.this, "对方正在通话中", Toast.LENGTH_SHORT).show();
//                        }else if(
//                                Integer.parseInt(mePeople.getCoinnum())+
//                                        Integer.parseInt(mePeople.getFtime())<=0
//                                ){
//                            Toast.makeText(RoomActivity.this, "余额不足，请充值", Toast.LENGTH_SHORT).show();
//                        } else {
//                            checkRecordPermission();
//
//                        }
//                    }
//                });
//                Logger.e("finish");
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//    }
    private void checkRecordPermission() {
        boolean hasPermission= ContextCompat.checkSelfPermission(this, Manifest.permission
                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO2);
        }else{
            Intent intent=new Intent(this, DialogActivity.class);
            intent.putExtra("id",people.getId());
            intent.putExtra("type",0);
            intent.putExtra("left",(Integer.parseInt(people.getCoinnum())+
                    Integer.parseInt(people.getFtime()))*60);
            Logger.e((Integer.parseInt(people.getCoinnum())+
                    Integer.parseInt(people.getFtime()))*60+"");
            startActivity(intent);
        }
    }

    private void focus(String uid, String bid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("fid", bid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/focus", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                if (!hasLike) {
                    hasLike = true;
                    ivLike.setImageResource(R.drawable.like);
                } else {
                    hasLike = false;
                    ivLike.setImageResource(R.drawable.xin);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_RECORDAUDIO2){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(this, DialogActivity.class);
                intent.putExtra("id",people.getId());
                intent.putExtra("type",0);
                intent.putExtra("left",(Integer.parseInt(people.getCoinnum())+
                        Integer.parseInt(people.getFtime()))*60);
                startActivity(intent);
            }
        }
    }
}