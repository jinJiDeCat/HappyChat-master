package com.zz.zy.happychat.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.activity.RoomActivity;
import com.zz.zy.happychat.activity.SearchActivity;
import com.zz.zy.happychat.adapter.RoomAdapter;
import com.zz.zy.happychat.mvp.model.CommendData;
import com.zz.zy.happychat.mvp.model.Room;
import com.zz.zy.happychat.mvp.presenter.CommendPresenter;
import com.zz.zy.happychat.mvp.view.CommendView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


public class CommendFragment extends BaseFragment implements CommendView {

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    Banner banner;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    private CommendPresenter commendPresenter;
    private View headerView;
    private RoomAdapter roomAdapter;

    @Override
    protected void initData() {
//        commendPresenter.initBanner();
//        commendPresenter.initRecycler();
        OkGo.get(MyApplication.BaseUrl+"index.php/api/index/test")
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                CommendData commendData= JSON.parseObject(s,CommendData.class);
                                initBanner(commendData.getData());
                                initRecyclerView(commendData.getInfo());
                            }else{
                                Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_commend;
    }

    @Override
    public void initView(View view) {
        headerView=View.inflate(getActivity(),R.layout.commend_header,null);
        banner= (Banner) headerView.findViewById(R.id.banner);

    }

    @Override
    public void initPresenter() {
        commendPresenter = new CommendPresenter(getActivity(),this);
    }

    @Override
    public void initBanner(List<CommendData.DataBean> banners) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(getActivity()).load(path).into(imageView);
            }
        });
        List<String> images=new ArrayList<>();
        for (CommendData.DataBean banner1 : banners) {
            Logger.e(banner1.getBpic());
            images.add(MyApplication.BaseImage+banner1.getBpic());
        }
        banner.setImages(images);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }

    @Override
    public void initRecyclerView(final List<CommendData.InfoBean> rooms) {
        Collections.sort(rooms, new Comparator<CommendData.InfoBean>() {
            @Override
            public int compare(CommendData.InfoBean room, CommendData.InfoBean t1) {
                return Integer.parseInt(t1.getState())-Integer.parseInt(room.getState());
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        xrecyclerview.addHeaderView(headerView);
        xrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xrecyclerview.setPullRefreshEnabled(true);
        xrecyclerview.setLoadingMoreEnabled(false);
        xrecyclerview.setLayoutManager(gridLayoutManager);
        roomAdapter=new RoomAdapter(getActivity(),rooms);
        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("userid",roomAdapter.getRoomList().get(position).getId());
                getActivity().startActivity(intent);
            }
        });
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                commendPresenter.refresh(getActivity());
            }

            @Override
            public void onLoadMore() {

            }
        });
        xrecyclerview.setAdapter(roomAdapter);
    }

    @Override
    public void refreshBanner(List<CommendData.DataBean> banners) {
        List<String> images=new ArrayList<>();
        for (CommendData.DataBean banner1 : banners) {
            images.add(banner1.getBpic());
        }
        banner.setImages(images);
        banner.start();
    }

    @Override
    public void refreshRecyclerView(List<CommendData.InfoBean> rooms) {
        Collections.sort(rooms, new Comparator<CommendData.InfoBean>() {
            @Override
            public int compare(CommendData.InfoBean room, CommendData.InfoBean t1) {
                return Integer.parseInt(t1.getState())-Integer.parseInt(room.getState());
            }
        });
        roomAdapter.setDataChange(rooms);
        xrecyclerview.refreshComplete();
    }


    @OnClick({R.id.ll_search, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.ll_share:
                commendPresenter.showShare(getActivity());
                break;
        }
    }

}
