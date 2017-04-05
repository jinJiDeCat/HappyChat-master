package com.zz.zy.happychat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.People;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GradeActivity extends AppCompatActivity {

    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_experience)
    TextView tvExperience;
    @BindView(R.id.progreeBar)
    ProgressBar progreeBar;
    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0xffEF889D, 0);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        getInfo(sharedPreferences.getString("userid", ""));
    }

    @OnClick(R.id.fl_back)
    public void onClick() {
        finish();
    }

    private void getInfo(String userid) {
        HttpUtils.get(this, MyApplication.BaseUrl + "index.php/api/index/getinfo?id=" + userid, new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                People people = JSON.parseObject(s, People.class);
                Glide.with(GradeActivity.this).load(MyApplication.BaseImage + people.getPict()).into(ivIcon);
                tvNickname.setText(people.getNickname());
                tvSex.setText(people.getSex());
                ivSex.setImageResource(people.getSex().equals("男") ? R.drawable.bainanxing : R.drawable.bainvxing);
                int level = Integer.parseInt(people.getLevel());
                int experience = Integer.parseInt(people.getExperience());
                Logger.d("level,"+level);
                tvExperience.setText("还差" + (level* 200 - experience) + "点经验值升级，聊天可以获得经验哦！");
                progreeBar.setMax(level* 200);
                progreeBar.setProgress(experience);
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
