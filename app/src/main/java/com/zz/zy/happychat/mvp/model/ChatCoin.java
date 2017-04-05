package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2016/12/20.
 */
public class ChatCoin {


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
