package com.zz.zy.happychat.mvp.model;


import java.util.List;

public class CommendData {

    /**
     * result : 200
     * resultNote : success
     * data : [{"id":"1","bpic":"/Public/Uploads/2017-02-14/58a2a05b73fd0.jpg"},{"id":"2","bpic":"/Public/Uploads/2017-02-14/58a2a04e1fbc9.jpg"}]
     * info : [{"id":"21","pict":"/Public/Uploads/2017-02-22/58ad3c2e66e1a.jpg","nickname":"语聊","member":"0","grade":"4","state":"1","online":"1"},{"id":"19","pict":"/Public/Uploads/2017-02-23/58ae123d5be61.jpg","nickname":"可乐","member":"0","grade":"5","state":"1","online":"1"},{"id":"22","pict":"/Public/Uploads/2017-02-22/58ad3f27085af.jpg","nickname":"框架","member":"0","grade":"5","state":"2","online":"1"},{"id":"23","pict":"/Public/Uploads/2017-02-22/58ad41b5685a5.jpg","nickname":"hr","member":"0","grade":"4","state":"2","online":"1"},{"id":"24","pict":"/Public/Uploads/2017-02-22/58ad70e8b51f0.jpg","nickname":"测试而已","member":"0","grade":"4","state":"2","online":"1"},{"id":"25","pict":"/Public/Uploads/2017-02-22/58ad7cdab7955.jpg","nickname":"啦啦啦哟","member":"0","grade":"5","state":"1","online":"1"},{"id":"26","pict":"/Public/Uploads/2017-02-23/58ae96fb5a0df.jpg","nickname":"奥利给","member":"0","grade":"0","state":"2","online":"1"}]
     */

    private int result;
    private String resultNote;
    private List<DataBean> data;
    private List<InfoBean> info;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * id : 1
         * bpic : /Public/Uploads/2017-02-14/58a2a05b73fd0.jpg
         */

        private String id;
        private String bpic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBpic() {
            return bpic;
        }

        public void setBpic(String bpic) {
            this.bpic = bpic;
        }
    }

    public static class InfoBean {
        /**
         * id : 21
         * pict : /Public/Uploads/2017-02-22/58ad3c2e66e1a.jpg
         * nickname : 语聊
         * member : 0
         * grade : 4
         * state : 1
         * online : 1
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
}
