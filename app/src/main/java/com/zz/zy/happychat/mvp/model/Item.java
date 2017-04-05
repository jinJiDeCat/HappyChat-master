package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2016/12/7.
 */
public class Item {
    public int id;
    public String name;

    public Item() {
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
