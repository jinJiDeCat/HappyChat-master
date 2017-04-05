package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.TimePickerView;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.TimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class YearsActivity extends AppCompatActivity {

    @BindView(R.id.rl_years)
    RelativeLayout rlYears;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_star)
    TextView tvStar;
    private TimePickerView timePickerView;
    private String birthday;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_years);
        unbinder=ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        initTimePicker();
        initView();
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("保存中...");
        String year = getIntent().getStringExtra("year");
        if(year.contains(" ")){
            String[] strings = year.split(" ");
            tvAge.setText(strings[0]);
            tvStar.setText(strings[1]);
        }

    }

    private void initTimePicker() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1900,2050);
        timePickerView.setTime(new Date());
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvAge.setText((new Date().getYear()-date.getYear())+"");
                tvStar.setText(TimeUtils.dateToStar(date));
                birthday=date.getTime()/1000+"";
                Log.w("birthday",birthday);
            }
        });
    }

    @OnClick({R.id.fl_back, R.id.tv_ok, R.id.rl_years})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.tv_ok:
                changeInfo(sharedPreferences.getString("userid",""),birthday);
                break;
            case R.id.rl_years:
                timePickerView.show();
                break;
        }
    }
    private void changeInfo(@NonNull String userid, @NonNull final String birthday) {
        Map<String,String> map=new HashMap<>();
        map.put("birthday",birthday);
        map.put("id",userid);
        HttpUtils.post(this, MyApplication.BaseUrl + "index.php/api/index/editinfo", map, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Intent intent=new Intent();
                intent.putExtra("year",tvAge.getText().toString().trim()+" "+tvStar.getText().toString().trim());
                intent.putExtra("birthday",birthday);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if(!progressDialog.isShowing())
                    progressDialog.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
