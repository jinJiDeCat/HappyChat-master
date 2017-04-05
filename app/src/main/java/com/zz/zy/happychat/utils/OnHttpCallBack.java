package com.zz.zy.happychat.utils;

/**
 * Created by zzzy on 2016/12/9.
 */
public abstract class OnHttpCallBack{
    public abstract void onSuccess(String s);
    public abstract void onError(Exception e);
    public void onBefore(){}
    public void onAfter(){}

}
