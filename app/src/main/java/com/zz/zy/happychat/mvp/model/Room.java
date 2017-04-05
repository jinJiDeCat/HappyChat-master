package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2016/12/2.
 */
public class Room {


    /**
     * id : 13
     * pict :
     * nickname : AAAAA级
     * member : 0
     * grade : 0
     * state : 2
     * online : 0
     */

    private String id;
    private String pict;
    private String nickname;
    private String member;
    private String grade;
    private String state;
    private String online;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
