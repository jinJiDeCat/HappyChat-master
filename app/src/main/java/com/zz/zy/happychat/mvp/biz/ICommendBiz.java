package com.zz.zy.happychat.mvp.biz;

import android.content.Context;

import com.zz.zy.happychat.mvp.model.Banner;
import com.zz.zy.happychat.mvp.model.Room;

import java.util.List;


public interface ICommendBiz {
    interface OnGetBannerCallBack{
        void success(List<Banner> banners);
        void fail();
    }
    void getBannerList(Context context, OnGetBannerCallBack onGetBannerCallBack);
    void getRooms(Context context,OnGetRoomCallBack onGetRoomCallBack);
    interface OnGetRoomCallBack{
        void success(List<Room> rooms);
        void fail();
    }
//    void getData(Context context,OnGetDataCallBack onGetDataCallBack);
//    interface OnGetDataCallBack{
//        void success(List<Room> rooms);
//        void fail();
//    }
}
