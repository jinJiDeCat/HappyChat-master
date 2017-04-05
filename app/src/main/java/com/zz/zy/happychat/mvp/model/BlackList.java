package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2017/1/11.
 */

public class BlackList {

    /**
     * kid : 16
     * nickname : aaaaa
     * pict : /Public/Uploads/2017-01-10/5874b7dbc6d75.jpg
     * sex : 女
     */

    private String kid;
    private String nickname;
    private String pict;
    private String sex;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
