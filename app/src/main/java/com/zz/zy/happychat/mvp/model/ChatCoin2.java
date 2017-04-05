package com.zz.zy.happychat.mvp.model;

import java.util.List;

/**
 * Created by zzzy on 2016/12/20.
 */
public class ChatCoin2 {

    /**
     * status : 1
     * info : 所需要的列表数据！！！
     * token : tkv3gor542ppv6osiqv8usdm00
     * total : 3
     * rows : [{"id":"1","number":"10","price":"10.00","present":"5","state":"1","addtime":""},{"id":"2","number":"100","price":"20.00","present":"10","state":"1","addtime":""},{"id":"3","number":"1000","price":"39.00","present":"15","state":"1","addtime":""}]
     */

    private int status;
    private String info;
    private String token;
    private String total;
    /**
     * id : 1
     * number : 10
     * price : 10.00
     * present : 5
     * state : 1
     * addtime :
     */

    private List<RowsBean> rows;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private String id;
        private String number;
        private String price;
        private String present;
        private String state;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPresent() {
            return present;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
