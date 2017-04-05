package com.zz.zy.happychat.mvp.model;

import java.util.List;

/**
 * Created by zzzy on 2017/2/24.
 */

public class Three {

    /**
     * result : 200
     * resultNote : success
     * data : [{"id":"1","cnum":"100","cprice":"10","give":"5"},{"id":"2","cnum":"200","cprice":"20","give":"30"}]
     * info : {"coinnum":"0"}
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
         * coinnum : 0
         */

        private String coinnum;

        public String getCoinnum() {
            return coinnum;
        }

        public void setCoinnum(String coinnum) {
            this.coinnum = coinnum;
        }
    }

    public static class DataBean {
        /**
         * id : 1
         * cnum : 100
         * cprice : 10
         * give : 5
         */

        private String id;
        private String cnum;
        private String cprice;
        private String give;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCnum() {
            return cnum;
        }

        public void setCnum(String cnum) {
            this.cnum = cnum;
        }

        public String getCprice() {
            return cprice;
        }

        public void setCprice(String cprice) {
            this.cprice = cprice;
        }

        public String getGive() {
            return give;
        }

        public void setGive(String give) {
            this.give = give;
        }
    }
}
