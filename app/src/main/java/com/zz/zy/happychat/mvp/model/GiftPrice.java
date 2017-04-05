package com.zz.zy.happychat.mvp.model;

import com.zz.zy.happychat.MyApplication;

/**
 * Created by zzzy on 2016/12/30.
 */
public class GiftPrice {

    /**
     * id : 1
     * gpic : http://192.168.1.115/voice/Public/Uploads/2016-12-29/586482b7cb21f.jpg
     * gname : 礼物1
     * gprice : 35
     * gcharm : 10
     */

    private String id;
    private String gpic;
    private String gname;
    private String gprice;
    private String gcharm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGpic() {
        return gpic;
    }

    public void setGpic(String gpic) {
        this.gpic = MyApplication.BaseImage+gpic;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGprice() {
        return gprice;
    }

    public void setGprice(String gprice) {
        this.gprice = gprice;
    }

    public String getGcharm() {
        return gcharm;
    }

    public void setGcharm(String gcharm) {
        this.gcharm = gcharm;
    }
}
