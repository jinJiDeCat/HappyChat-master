package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2017/1/4.
 */

public class GiftList {

    /**
     * nickname : 张三
     * member : 0
     * gpic : /Public/Uploads/2016-12-29/586482b7cb21f.jpg
     * gname : 礼物1
     * time : 1483503783
     * gcharm : 10
     */

    private String nickname;
    private String member;
    private String gpic;
    private String gname;
    private String time;
    private String gcharm;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getGpic() {
        return gpic;
    }

    public void setGpic(String gpic) {
        this.gpic = gpic;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGcharm() {
        return gcharm;
    }

    public void setGcharm(String gcharm) {
        this.gcharm = gcharm;
    }
}
