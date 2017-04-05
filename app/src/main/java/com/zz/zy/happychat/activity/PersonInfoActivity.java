package com.zz.zy.happychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.HobbyGvAdapter;
import com.zz.zy.happychat.mvp.model.CallState;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;
import com.zz.zy.happychat.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imlib.RongIMClient;

public class PersonInfoActivity extends AppCompatActivity {
    private static final int ACTIVITY_NICKNAME = 0;
    private static final int ACTIVITY_YEAR = 1;
    private static final int ACTIVITY_JOB = 2;
    private static final int ACTIVITY_HOBBY = 3;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_nickname)
    LinearLayout llNickname;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.ll_year)
    LinearLayout llYear;
    @BindView(R.id.tv_jobs)
    TextView tvJobs;
    @BindView(R.id.ll_job)
    LinearLayout llJob;
    @BindView(R.id.recyclerView_hobby)
    RecyclerView recyclerViewHobby;
    @BindView(R.id.ll_hobby)
    LinearLayout llHobby;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.ll_focus)
    LinearLayout llFocus;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.tv_black)
    TextView tvBlack;
    @BindView(R.id.ll_blackList)
    LinearLayout llBlackList;
    @BindView(R.id.ll_report)
    LinearLayout llReport;
    @BindView(R.id.ll_aboutMe)
    LinearLayout llAboutMe;
    @BindView(R.id.btn_exit)
    Button btnExit;
    private Unbinder unbinder;
    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        getUserInfo(sharedPreferences.getString("userid",""));
    }

    private void initRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHobby.setLayoutManager(linearLayoutManager);
    }

    private void initView() {
        initRecyclerview();
    }

    @OnClick({R.id.fl_back, R.id.ll_nickname, R.id.ll_year, R.id.ll_job, R.id.ll_hobby, R.id.ll_focus, R.id.ll_fans, R.id.ll_blackList, R.id.ll_report, R.id.ll_aboutMe, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.ll_nickname:
                Intent intent1 = new Intent(this, NickNameActivity.class);
                intent1.putExtra("nickname", tvNickname.getText().toString().trim());
                startActivityForResult(intent1, ACTIVITY_NICKNAME);
                break;
            case R.id.ll_year:
                Intent intent2 = new Intent(this, YearsActivity.class);
                intent2.putExtra("year", tvYear.getText().toString().trim());
                startActivityForResult(intent2, ACTIVITY_YEAR);
                break;
            case R.id.ll_job:
                Intent intent3 = new Intent(this, ChooseJobActivity.class);
                startActivityForResult(intent3, ACTIVITY_JOB);
                break;
            case R.id.ll_hobby:
                Intent intent4 = new Intent(this, ChooseHobbyActivity.class);
                startActivityForResult(intent4, ACTIVITY_HOBBY);
                break;
            case R.id.ll_focus:
                startActivity(new Intent(this, FocusListActivity.class));
                break;
            case R.id.ll_fans:
                startActivity(new Intent(this, FansListActivity.class));
                break;
            case R.id.ll_blackList:
                startActivity(new Intent(this, BlackListActivity.class));
                break;
            case R.id.ll_report:
                startActivity(new Intent(this, SuggestionActivity.class));
                break;
            case R.id.ll_aboutMe:
                startActivity(new Intent(this, AboutMeActivity.class));
                break;
            case R.id.btn_exit:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token","");
                editor.putString("userid","");
                editor.apply();
                RongIMClient.getInstance().disconnect();
                RongIMClient.getInstance().logout();
                Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new CallState());
                finish();
                break;
        }
    }

    private void getUserInfo(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                People people = JSON.parseObject(s, People.class);
                tvNickname.setText(people.getNickname());
                if(!TextUtils.isEmpty(people.getBirthday())){
                    Date birthday=new Date(Long.parseLong(people.getBirthday())*1000);
                    tvYear.setText(new Date().getYear()-birthday.getYear()+" "+ TimeUtils.dateToStar(birthday));
                }
                tvJobs.setText(people.getProfession());
                if(people.getHobby().contains(",")){
                    recyclerViewHobby.setAdapter(new HobbyGvAdapter(PersonInfoActivity.this, people.getHobby().split(",")));
                }
                tvFocus.setText(people.getFocusnum());
                tvFans.setText(people.getFansnum());
                tvBlack.setText(people.getBlacknum());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_NICKNAME:
                    tvNickname.setText(data.getStringExtra("nickname"));
                    break;
                case ACTIVITY_YEAR:
                    tvYear.setText(data.getStringExtra("year"));
                    break;
                case ACTIVITY_JOB:
                    tvJobs.setText(data.getStringExtra("job"));
                    break;
                case ACTIVITY_HOBBY:
                    String hobby=data.getStringExtra("hobby");
                    Log.e("hobby",hobby);
                    recyclerViewHobby.setAdapter(new HobbyGvAdapter(this, hobby.split(",")));
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkGo.getInstance().cancelTag(this);
    }
}
