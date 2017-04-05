package com.zz.zy.happychat.mvp.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by zzzy on 2016/12/20.
 */
public class Gift {

    /**
     * id : 1
     * title : 玫瑰
     * image : http://192.168.1.112/voiceChat/./Public/gift_image/
     * price : 10.00
     * charm_value : 10
     * state : 1
     * addtime : null
     */

    private String id;
    private String title;
    private String image;
    private String price;
    private String charm_value;
    private String state;
    private String addtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCharm_value() {
        return charm_value;
    }

    public void setCharm_value(String charm_value) {
        this.charm_value = charm_value;
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
