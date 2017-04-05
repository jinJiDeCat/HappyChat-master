package com.zz.zy.happychat.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zz.zy.happychat.Constants;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.RechangeAdapter;
import com.zz.zy.happychat.alipay.Contact;
import com.zz.zy.happychat.alipay.OrderInfoUtil2_0;
import com.zz.zy.happychat.mvp.model.AliPayInfo;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.mvp.model.Three;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.PayResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class RechargeActivity extends AppCompatActivity {

    private static final int ALIPAY_CODE = 0;
    @BindView(R.id.lv_rechange)
    ListView lvRechange;
    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    @BindView(R.id.ll_alipay)
    LinearLayout llAlipay;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_leave)
    TextView tvLeave;
    private IWXAPI api;
    String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
    private List<Three.DataBean> chatCoinList;
    private RechangeAdapter rechangeAdapter;
    private ProgressDialog progressDialog;
    //private CompositeSubscription compositeSubscription;
    private SharedPreferences sharedPreferences;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALIPAY_CODE:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Logger.e(resultInfo);
                    Logger.e(resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        getThree(sharedPreferences.getString("userid",""));
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        initView();
        getThree(sharedPreferences.getString("userid",""));
        //getInfo(sharedPreferences.getString("userid",""));
        //getChatCoinPrice();
    }

    private void wxPay() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        Toast.makeText(this, "获取订单中...", Toast.LENGTH_SHORT).show();
        HttpUtils.get(this, url, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                if (TextUtils.isEmpty(s)) {
                    Log.d("PAY_GET", "服务器请求错误");
                    Toast.makeText(RechargeActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject json = new JSONObject(s);
                        if (!json.has("retcode")) {
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");
                            req.extData = "app data"; // optional
                            Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                        } else {
                            Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                            Toast.makeText(RechargeActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("PAY_GET", "异常：" + e.getMessage());
                Toast.makeText(RechargeActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

    }

    @OnClick({R.id.fl_back, R.id.tv_history, R.id.ll_wechat, R.id.ll_alipay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_history:
                startActivity(new Intent(this, PayHistoryActivity.class));
                break;
            case R.id.ll_wechat:
                Toast.makeText(this, "暂不支持", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_alipay:
                if(rechangeAdapter.getClickPosition()>-1){
                    sendAliPayInfo(sharedPreferences.getString("userid",""),
                            chatCoinList.get(rechangeAdapter.getClickPosition()).getId());
                }else{
                    Toast.makeText(this, "请先选择金额", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private String UrlEncode(String str){
        String result = null;
        try {
            result=URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    private void sendAliPayInfo(String userid,String ChatCoinId){
        Map<String,String> map=new HashMap<>();
        map.put("uid",userid);
        map.put("id",ChatCoinId);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/alipay/pay", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
//                AliPayInfo aliPayInfo=JSON.parseObject(s,AliPayInfo.class);
//                Logger.e(aliPayInfo.getBiz_content());
//                try {
//                    Logger.e(URLDecoder.decode(aliPayInfo.getBiz_content(),"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                String orderInfo = null;
//                try {
//                    orderInfo = "app_id="+UrlEncode(URLDecoder.decode(aliPayInfo.getApp_id(),"UTF-8"))+
//                                "&biz_content="+ UrlEncode(URLDecoder.decode(aliPayInfo.getBiz_content(),"UTF-8"))+
//                                "&charset="+ UrlEncode(URLDecoder.decode(aliPayInfo.getCharset(),"UTF-8"))+
//                            //"&format="+UrlEncode("json")+
//                            "&method="+ UrlEncode(URLDecoder.decode(aliPayInfo.getMethod(),"UTF-8")) +
//                            "&notify_url="+UrlEncode(URLDecoder.decode(aliPayInfo.getNotify_url(),"UTF-8"))+
//                                "&sign_type="+ UrlEncode(URLDecoder.decode(aliPayInfo.getSign_type(),"UTF-8"))+
//                                "&timestamp="+ UrlEncode(URLDecoder.decode(UnixToTime(Long.parseLong(aliPayInfo.getTimestamp())),"UTF-8"))+
//                                "&version="+ UrlEncode(URLDecoder.decode(aliPayInfo.getVersion(),"UTF-8"))+
//                                "&sign="+ UrlEncode(URLDecoder.decode(aliPayInfo.getSign(),"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

//                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Contact.APPID,
//                        aliPayInfo.getBiz_content(),
//                        UnixToTime(Long.parseLong(aliPayInfo.getTimestamp())),
//                        aliPayInfo.getNotify_url()
//                        );
//                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//                String sign = OrderInfoUtil2_0.getSign(params, Contact.RSA2_PRIVATE, true);
//                final String orderInfo = orderParam + "&" + sign;
                Logger.e(s);
                AliPay(s);
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.setMessage("正在支付...");
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private String UnixToTime(long time){
        long date=time*1000;
        Logger.e(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(date)));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(date));
    }
    private void  AliPay(final String payInfo){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                PayTask alipay=new PayTask(RechargeActivity.this);
                Map<String,String> result=alipay.payV2(payInfo,true);
                Message message=new Message();
                message.what=ALIPAY_CODE;
                message.obj=result;
                handler.sendMessage(message);
            }
        };
        Thread payThread = new Thread(runnable);
        payThread.start();
    }
    private void getThree(String userid){
        OkGo.get(MyApplication.BaseUrl+"index.php/api/index/getthree?id="+userid)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                Three three=JSON.parseObject(s,Three.class);
                                tvLeave.setText(three.getInfo().getCoinnum());
                                chatCoinList=three.getData();
                                rechangeAdapter = new RechangeAdapter(RechargeActivity.this, chatCoinList);
                                lvRechange.setAdapter(rechangeAdapter);
                                lvRechange.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        rechangeAdapter.setClickPosition(i);
                                    }
                                });
                            }else{
                                Toast.makeText(RechargeActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//    private void getChatCoinPrice() {
//        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/coin", new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                chatCoinList = JSON.parseArray(s, ChatCoin.class);
//                rechangeAdapter = new RechangeAdapter(RechargeActivity.this, chatCoinList);
//                lvRechange.setAdapter(rechangeAdapter);
//                lvRechange.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        rechangeAdapter.setClickPosition(i);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

//        compositeSubscription=new CompositeSubscription();
//        Log.w("http","start");
//        Subscription subscription=Http.getInstance().getChatCoinPrice()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ChatCoin2>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.w("http","complete");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.w("http",e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ChatCoin2 chatCoin) {
//                        Log.e("http",chatCoin.getRows().toString());
//                        lvRechange.setAdapter(new RechangeAdapter(RechargeActivity.this,chatCoin));
//                    }
//                });
//        compositeSubscription.add(subscription);
//    }

    private void getInfo(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                People people = JSON.parseObject(s, People.class);
                tvLeave.setText(people.getCoinnum());

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(compositeSubscription!=null&&!compositeSubscription.isUnsubscribed()){
//            compositeSubscription.unsubscribe();
//        }
        if (api != null) {
            api.unregisterApp();
        }
    }
}
