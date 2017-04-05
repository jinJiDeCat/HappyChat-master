package com.zz.zy.happychat.mvp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.mvp.biz.CommendBiz;
import com.zz.zy.happychat.mvp.biz.ICommendBiz;
import com.zz.zy.happychat.mvp.model.Banner;
import com.zz.zy.happychat.mvp.model.CommendData;
import com.zz.zy.happychat.mvp.model.Room;
import com.zz.zy.happychat.mvp.view.CommendView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Response;


public class CommendPresenter extends BasePresenter {
    private CommendView commendView;
    private ICommendBiz commendBiz;
    private Context context;
    public CommendPresenter(Context context,CommendView commendView){
        this.context=context;
        this.commendView=commendView;
        this.commendBiz=new CommendBiz();
    }
//    public void refresh(){
//        commendBiz.getBannerList(context, new ICommendBiz.OnGetBannerCallBack() {
//            @Override
//            public void success(List<Banner> banners) {
//                commendView.refreshBanner(banners);
//                commendBiz.getRooms(context, new ICommendBiz.OnGetRoomCallBack() {
//                    @Override
//                    public void success(List<Room> rooms) {
//                        commendView.refreshRecyclerView(rooms);
//                    }
//
//                    @Override
//                    public void fail() {
//
//                    }
//                });
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });
//
//    }
    public void refresh(final Context context){
        OkGo.get(MyApplication.BaseUrl+"index.php/api/index/test")
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("result")==200){
                                CommendData commendData= JSON.parseObject(s,CommendData.class);
                                commendView.refreshBanner(commendData.getData());
                                commendView.refreshRecyclerView(commendData.getInfo());
                            }else{
                                Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//    public void initBanner(){
//        commendBiz.getBannerList(context, new ICommendBiz.OnGetBannerCallBack() {
//            @Override
//            public void success(List<Banner> banners) {
//                commendView.initBanner(banners);
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });
//
//    }
//    public void initRecycler(){
//        commendBiz.getRooms(context, new ICommendBiz.OnGetRoomCallBack() {
//            @Override
//            public void success(List<Room> rooms) {
//                commendView.initRecyclerView(rooms);
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });
//
//    }
    public void showShare(Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("语聊");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("语聊");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(context);
    }
}
