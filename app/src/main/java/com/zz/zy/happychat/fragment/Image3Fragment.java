package com.zz.zy.happychat.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.activity.MainActivity;
import com.zz.zy.happychat.activity.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Image3Fragment extends Fragment {
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.btn)
    Button btn;
    private Unbinder unbinder;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_image, null);
        unbinder = ButterKnife.bind(this, view);
        sharedPreferences=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        Glide.with(getActivity()).load(R.drawable.flipper3).into(iv);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("first",true);
                editor.commit();
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
