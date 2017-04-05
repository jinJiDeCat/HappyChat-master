package com.zz.zy.happychat.mvp.view;

import com.zz.zy.happychat.mvp.model.Banner;
import com.zz.zy.happychat.mvp.model.CommendData;
import com.zz.zy.happychat.mvp.model.Room;

import java.util.List;


public interface CommendView {
    void initBanner(List<CommendData.DataBean> banners);
    void initRecyclerView(List<CommendData.InfoBean> rooms);
    void refreshBanner(List<CommendData.DataBean> banners);
    void refreshRecyclerView(List<CommendData.InfoBean> rooms);
}
