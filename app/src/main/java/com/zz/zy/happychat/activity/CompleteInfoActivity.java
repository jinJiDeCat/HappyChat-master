package com.zz.zy.happychat.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;
import com.wuxiaolong.androidutils.library.TimeUtil;
import com.yanzhenjie.album.Album;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.Result;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.MyReceiveMessageListener;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CompleteInfoActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 0;
    private static final int REQUEST_RECORDAUDIO2 = 10;
    @BindView(R.id.tv_chooseBirth)
    TextView tvChooseBirth;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.iv_takephoto)
    CircleImageView ivTakephoto;
    @BindView(R.id.cb_man)
    CheckBox cbMan;
    @BindView(R.id.cb_woman)
    CheckBox cbWoman;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    private TimePickerView timePickerView;
    private String pic=null;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        StatusBarUtil.setColor(this, 0xffffffff, 0);
        unbinder=ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在保存...");
        progressDialog.setCancelable(false);
        initTimePicker();
        initView();
    }

    private void initTimePicker() {
        timePickerView =new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1900,2050);
        timePickerView.setTime(new Date());
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvChooseBirth.setText(new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(date));
            }
        });
    }

    private void initView() {
        tvChooseBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerView.show();
            }
        });
        cbMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cbWoman.setChecked(!b);
            }
        });
        cbWoman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cbMan.setChecked(!b);
            }
        });
    }

    @OnClick({R.id.tv_ok, R.id.iv_takephoto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if(TextUtils.isEmpty(pic)){
                    Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etNickname.getText().toString().trim())){
                    Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(tvChooseBirth.getText().toString().trim())){
                    Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
                }else{
                    changeInfo(getIntent().getStringExtra("userid")
                    ,new File(pic),cbMan.isChecked()?"男":"女",
                            etNickname.getText().toString().trim(),
                            ""+TimeUtil.beijingTime2UnixTimestamp(tvChooseBirth.getText().toString().trim(),"yyyy年MM月dd日")
                    );
                }
                break;
            case R.id.iv_takephoto:
//                Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO
//                        , 1              // 指定选择数量。
//                        , 0xffEF889D     // 指定Toolbar的颜色。
//                        , 0xffEF889D);  // 指定状态栏的颜色。
                ImagePicker imagePicker=ImagePicker.getInstance();
                imagePicker.setImageLoader(new ImageLoader(){

                    @Override
                    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                        Glide.with(activity).load(path).into(imageView);
                    }

                    @Override
                    public void clearMemoryCache() {

                    }
                });
                imagePicker.setShowCamera(true);
                imagePicker.setCrop(true);
                imagePicker.setSaveRectangle(true);
                imagePicker.setMultiMode(false);
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                imagePicker.setFocusWidth(600);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                imagePicker.setFocusHeight(600);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
                imagePicker.setOutPutY(800);//保存文件的高度。单位像素
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, ACTIVITY_REQUEST_SELECT_PHOTO);
                break;
        }
    }

    private void changeInfo(@NonNull final String userid, @NonNull final File pic,
                            @NonNull final String sex, @NonNull final String nickname, @NonNull final String birthday) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("id", userid);
        httpParams.put("nickname", nickname);
        httpParams.put("pict", pic);
        httpParams.put("sex", sex);
        httpParams.put("birthday", birthday);
        OkGo.post(MyApplication.BaseUrl + "index.php/api/index/editinfo").tag(this)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if(!progressDialog.isShowing()){
                            progressDialog.show();
                        }
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Result result = JSON.parseObject(s, Result.class);
                        if (result.getResult() == 200) {
                            getToken(CompleteInfoActivity.this,userid);
                        }
                    }
                });
//        Luban.get(this)
//                .load(pic)
//                .putGear(Luban.THIRD_GEAR)
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        if(!progressDialog.isShowing()){
//                            progressDialog.show();
//                        }
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if(progressDialog.isShowing()){
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(CompleteInfoActivity.this, "请重试", Toast.LENGTH_SHORT).show();
//                    }
//                }).launch();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if(data!=null&&requestCode==ACTIVITY_REQUEST_SELECT_PHOTO){
                // 拿到用户选择的图片路径List：
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Logger.w(images.get(0).path);
                pic=images.get(0).path;
                Glide.with(this).load(images.get(0).path).into(ivTakephoto);
            }
        }
//        if(resultCode==RESULT_OK){
//            switch (requestCode){
//                case ACTIVITY_REQUEST_SELECT_PHOTO:
//                    // 拿到用户选择的图片路径List：
//                    List<String> pathList = Album.parseResult(data);
//                    pic=pathList.get(0);
//                    Glide.with(this).load(pathList.get(0)).into(ivTakephoto);
//                    break;
//            }
//        }
    }
    /**
     * 获取融云token
     */
    private void getToken(final Context context, String uid){
        HttpUtils.get(context, MyApplication.BaseUrl + "index.php/api/Token/getToken?id="+uid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("userid",jsonObject.getString("uid"));
                    editor.putString("token",jsonObject.getString("token"));
                    editor.commit();
                    startActivity(new Intent(CompleteInfoActivity.this, MainActivity.class));
                    connect(jsonObject.getString("token"));
                    giveLong(jsonObject.getString("uid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });
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
    private void setCallLib(){
        RongCallClient.setReceivedCallListener(new IRongReceivedCallListener() {
            @Override
            public void onReceivedCall(RongCallSession rongCallSession) {
                callSession=rongCallSession;
                checkRecordPermission(rongCallSession);
            }

            @Override
            public void onCheckPermission(RongCallSession rongCallSession) {
                boolean hasPermission= ContextCompat.checkSelfPermission(CompleteInfoActivity.this, Manifest.permission
                        .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
                if(!hasPermission){
                    ActivityCompat.requestPermissions(CompleteInfoActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
                }else{
                    RongCallClient.getInstance().onPermissionGranted();
                }
            }
        });
    }

    private void checkRecordPermission(RongCallSession rongCallSession) {
        boolean hasPermission= ContextCompat.checkSelfPermission(this, Manifest.permission
                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO2);
        }else{
            Intent intent=new Intent(CompleteInfoActivity.this,DialogActivity.class);
            intent.putExtra("callid",rongCallSession.getCallId());
            intent.putExtra("id",rongCallSession.getTargetId());
            intent.putExtra("type",1);
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_RECORDAUDIO2){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(callSession!=null){
                    Intent intent=new Intent(CompleteInfoActivity.this,DialogActivity.class);
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
    private static final int REQUEST_RECORDAUDIO = 0;
    @Override
    public void onBackPressed() {
        if (timePickerView.isShowing()) {
            timePickerView.dismiss();
        } else {
            Toast.makeText(this, "请完善用户信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
