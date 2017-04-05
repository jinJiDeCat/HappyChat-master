package com.zz.zy.happychat.mvp.model;

import java.util.List;

/**
 * Created by zzzy on 2017/2/24.
 */

public class Four {

    /**
     * result : 200
     * resultNote : success
     * data : [{"id":"1","intro":"5","name":"普通用户"},{"id":"2","intro":"30","name":"会员特权"}]
     * info : {"id":"21","pict":"/Public/Uploads/2017-02-22/58ad3c2e66e1a.jpg","nickname":"语聊","sex":"女","birthday":"1077379200","old":"0","constellation":"","voice":"","profession":"公务员","hobby":"","member":"0","coinnum":"0","level":"1","experience":"11","state":"1","grade":"4","fansnum":"1","focusnum":"0","blacknum":"0","ftime":"19","num":"11","set":"0","give":"1","phone":"13140167960","password":"e10adc3949ba59abbe56e057f20f883e","online":"1","ttime":"1487917839"}
     */

    private int result;
    private String resultNote;
    private InfoBean info;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class InfoBean {
        /**
         * id : 21
         * pict : /Public/Uploads/2017-02-22/58ad3c2e66e1a.jpg
         * nickname : 语聊
         * sex : 女
         * birthday : 1077379200
         * old : 0
         * constellation :
         * voice :
         * profession : 公务员
         * hobby :
         * member : 0
         * coinnum : 0
         * level : 1
         * experience : 11
         * state : 1
         * grade : 4
         * fansnum : 1
         * focusnum : 0
         * blacknum : 0
         * ftime : 19
         * num : 11
         * set : 0
         * give : 1
         * phone : 13140167960
         * password : e10adc3949ba59abbe56e057f20f883e
         * online : 1
         * ttime : 1487917839
         */

        private String id;
        private String pict;
        private String nickname;
        private String sex;
        private String birthday;
        private String old;
        private String constellation;
        private String voice;
        private String profession;
        private String hobby;
        private String member;
        private String coinnum;
        private String level;
        private String experience;
        private String state;
        private String grade;
        private String fansnum;
        private String focusnum;
        private String blacknum;
        private String ftime;
        private String num;
        private String set;
        private String give;
        private String phone;
        private String password;
        private String online;
        private String ttime;

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getOld() {
            return old;
        }

        public void setOld(String old) {
            this.old = old;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getVoice() {
            return voice;
        }

        public void setVoice(String voice) {
            this.voice = voice;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getCoinnum() {
            return coinnum;
        }

        public void setCoinnum(String coinnum) {
            this.coinnum = coinnum;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getFansnum() {
            return fansnum;
        }

        public void setFansnum(String fansnum) {
            this.fansnum = fansnum;
        }

        public String getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(String focusnum) {
            this.focusnum = focusnum;
        }

        public String getBlacknum() {
            return blacknum;
        }

        public void setBlacknum(String blacknum) {
            this.blacknum = blacknum;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }

        public String getGive() {
            return give;
        }

        public void setGive(String give) {
            this.give = give;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getTtime() {
            return ttime;
        }

        public void setTtime(String ttime) {
            this.ttime = ttime;
        }
    }

    public static class DataBean {
        /**
         * id : 1
         * intro : 5
         * name : 普通用户
         */

        private String id;
        private String intro;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
