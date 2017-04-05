package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.mvp.model.Banner;
import com.zz.zy.happychat.mvp.model.Room;
import com.zz.zy.happychat.utils.HttpUtils;
import com.zz.zy.happychat.utils.OnHttpCallBack;

import java.util.ArrayList;
import java.util.List;


public class CommendBiz implements ICommendBiz {


    @Override
    public void getBannerList(Context context, final OnGetBannerCallBack onGetBannerCallBack) {
        HttpUtils.get(context, MyApplication.BaseUrl + "index.php/api/index/getbanner", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<Banner> banners=new ArrayList<>();
                banners= JSON.parseArray(s,Banner.class);
                onGetBannerCallBack.success(banners);
            }

            @Override
            public void onError(Exception e) {
                onGetBannerCallBack.fail();
            }
        });
    }

    @Override
    public void getRooms(Context context, final OnGetRoomCallBack onGetRoomCallBack) {
        HttpUtils.get(context, MyApplication.BaseUrl + "index.php/api/index/ulist", new OnHttpCallBack() {
            @Override
            public void onSuccess(String s) {
                List<Room> rooms=new ArrayList<>();
                rooms=JSON.parseArray(s,Room.class);
                onGetRoomCallBack.success(rooms);
            }

            @Override
            public void onError(Exception e) {
                onGetRoomCallBack.fail();
            }
        });
    }

//    @Override
//    public void getData(Context context, OnGetDataCallBack onGetDataCallBack) {
//        HttpUtils.get(context, MyApplication.BaseUrl + "index.php/api/index/test", new OnHttpCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                List<Room> rooms=new ArrayList<>();
//                rooms=JSON.parseArray(s,Room.class);
//                onGetRoomCallBack.success(rooms);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                onGetRoomCallBack.fail();
//            }
//        });
//    }


}
