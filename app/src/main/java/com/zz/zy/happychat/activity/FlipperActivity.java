package com.zz.zy.happychat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.adapter.MainViewPagerAdapter;
import com.zz.zy.happychat.fragment.Image1Fragment;
import com.zz.zy.happychat.fragment.Image2Fragment;
import com.zz.zy.happychat.fragment.Image3Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FlipperActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipper);
        StatusBarUtil.setTranslucentForImageView(this, 0,null);
        unbinder=ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new Image1Fragment());
        fragmentList.add(new Image2Fragment());
        fragmentList.add(new Image3Fragment());
        viewpager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(),this,fragmentList));
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
