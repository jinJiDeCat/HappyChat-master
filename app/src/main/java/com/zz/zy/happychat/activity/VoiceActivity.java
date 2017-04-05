package com.zz.zy.happychat.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.view.VoiceLineView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class VoiceActivity extends AppCompatActivity implements Runnable {
    private static final int REQUEST_RECORDAUDIO = 0;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.voiceView)
    VoiceLineView voiceView;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.ib_record)
    ImageButton ibRecord;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    private boolean hasRecord = false;
    private boolean isPlaying = false;
    private MediaRecorder mMediaRecorder;
    private Unbinder unbinder;
//    在子线程中处理增加音量和 动画效果
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mMediaRecorder == null) return;
                    double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
                    double db = 0;// 分贝
                    //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
                    //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
                    //同时，也可以配置灵敏度sensibility
                    if (ratio > 1)
                        db = 20 * Math.log10(ratio);
                    //只要有一个线程，不断调用这个方法，就可以使波形变化
                    //主要，这个方法必须在ui线程中调用
                    voiceView.setVolume((int) (db));
                    break;
                case 1:
                    int time = Integer.parseInt(tvTime.getText().toString().trim().substring(0, tvTime.getText().toString().trim().indexOf("s"))) + 1;
                    tvTime.setText(time + "s");
                    this.sendEmptyMessageDelayed(1, 1000);
                    break;
            }
        }
    };
    private Thread thread;
    private boolean isStart = false;
    private boolean canTime = false;
    private MediaPlayer mp;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        unbinder=ButterKnife.bind(this);
//
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
//        开启一个子线程 处理点击事件
        new Thread(this).start();
//        录音
     /*  ibRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        按下的时候先判断是否有录音的权限
                        boolean hasPermission= ContextCompat.checkSelfPermission(VoiceActivity.this, Manifest.permission
                                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
                        if(!hasPermission){
                            ActivityCompat.requestPermissions(VoiceActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
                        }else{

                            if(isStart==true){
                                stopVoice();
                            }else{
                                ibRecord.setBackgroundResource(R.drawable.record_background);
                                startVoice();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        stopVoice();
                        break;

                }
                return true;
            }
        });*/
   /*     price_id_buy.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                *************
                return <span style="color:#ff0000;">true</span>;
            }
        });
        price_id_buy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });*/

ibRecord.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                ibRecord.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        boolean hasPermission= ContextCompat.checkSelfPermission(VoiceActivity.this, Manifest.permission
                                .RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;
                        if(!hasPermission){
                            ActivityCompat.requestPermissions(VoiceActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_RECORDAUDIO);
                        }else{

                            if(isStart==true){
                                stopVoice();
                            }else{
                                ibRecord.setBackgroundResource(R.drawable.record_background);
                                startVoice();
                                hasRecord = true;
                            }
                        }
                        return true;
                    }
                });
                ibRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"请录音",Toast.LENGTH_SHORT).show();
                    }
                });
                break ;
            case MotionEvent.ACTION_UP:
                stopVoice();


                break;
        }
        return false;
    }
});

//        开始播放
/*        llPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp = new MediaPlayer();
                try {
                    if (hasRecord) {
                        mp.setDataSource(new File(Environment.getExternalStorageDirectory().getPath(), "hello.mp3").getPath());
                        mp.prepare();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
//                        设置图片
                                ivPlay.setImageResource(R.drawable.pause);
//                        开始播放
                                mediaPlayer.start();
                            }
                        });
//                        设置播放完成
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
//                        设置图片
                                ivPlay.setImageResource(R.drawable.bo);
//                        停止播放
                                mp.release();
                                mp = null;
                            }
                        });
                    } else {
                        Toast.makeText(VoiceActivity.this, "请先录音", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });*/
//播放键
        llPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp = new MediaPlayer();
                llPlay.setEnabled(false);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        ivPlay.setImageResource(R.drawable.pause);
                        isPlaying=true;
                        mediaPlayer.start();
                    }
                });
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ivPlay.setImageResource(R.drawable.bo);
                        mp.release();
                        isPlaying=false ;
                        llPlay.setEnabled(true);
                        mp = null;
                    }
                });
                try {
                    if (hasRecord) {
                        mp.setDataSource(new File(Environment.getExternalStorageDirectory().getPath(), "hello.mp3").getPath());
                        mp.prepare();
                    } else {
                        llPlay.setEnabled(true);
                        Toast.makeText(VoiceActivity.this, "请先录音", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initView();
    }
//请求权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_RECORDAUDIO){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startVoice();
            }
        }
    }

    private void initView() {
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在上传...");
        progressDialog.setCancelable(false);
//        设置完成 不明白的是这个东西有什么用
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasRecord){
                    if (isPlaying){
                        mp.pause();
                    }
                   btnOk.setEnabled(false);
                    editInfo(sharedPreferences.getString("userid",""),new File(Environment.getExternalStorageDirectory().getPath(), "hello.mp3"));
                    btnOk.setEnabled(true);
                }else{
                    Toast.makeText(VoiceActivity.this, "请先录音", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//停止录音
    private void stopVoice() {
        isStart = false;
        handler.removeMessages(1);
        if(mMediaRecorder!=null){
            try {
                //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
                //报错为：RuntimeException:stop failed
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();

        }catch (Exception e) {

        }
        //added by ouyang end
            mMediaRecorder.release();

            mMediaRecorder = null;
        }
    }
//开始录音的方法
    private void startVoice() {
//        开始录音了就把有录音设置为真
        if (mMediaRecorder == null)
//            新建一个录音者
            mMediaRecorder = new MediaRecorder();
//        音频输入源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                stopVoice();
            }
        });
//        第三步：设置输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//设置音频编码为默认
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //        设置文件的输出路径
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.mp3");
//       文件是否存在
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        音频设置输出路径
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }



//        设置状态为开始
        isStart = true;
        tvTime.setText("0s");
        handler.sendEmptyMessageDelayed(1, 1000);

    }
//这个方法是干什么呢?? 上传音频
    private void editInfo(String userid,File file){
        HttpParams httpParams=new HttpParams();
        httpParams.put("id",userid);
        httpParams.put("voice",file);
        OkGo.post(MyApplication.BaseUrl+"index.php/api/index/editinfo").tag(this)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if(!progressDialog.isShowing())
                            progressDialog.show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//    开启子线程后在子线程中做的事情 一直监听
    @Override
    public void run() {
        while (true) {
            if (isStart) {
                handler.sendEmptyMessage(0);
                try {
//                    防止死循环运行过多造成cpu运行过高
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
