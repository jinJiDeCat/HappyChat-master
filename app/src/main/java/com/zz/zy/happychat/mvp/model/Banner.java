package com.zz.zy.happychat.mvp.model;

import com.zz.zy.happychat.MyApplication;

/**
 * Created by zzzy on 2016/12/2.
 */
public class Banner {

    /**
     * id : 1
     * bpic : /Public/Uploads/2016-12-29/58648c9e76417.jpg
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
        this.bpic = MyApplication.BaseImage+bpic;
    }
}
