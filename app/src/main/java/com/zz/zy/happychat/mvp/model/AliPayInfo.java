package com.zz.zy.happychat.mvp.model;

/**
 * Created by zzzy on 2017/1/12.
 */

public class AliPayInfo {

    /**
     * app_id : 2016122804689088
     * method : alipay.trade.app.pay
     * sign_type : RSA2
     * charset : utf-8
     * timestamp : 2017-01-13 17:53:35
     * version : 1.0
     * notify_url : http://www.souxinw.com/index.php/Api/Alipay/paysuccess.html
     * biz_content : {"subject":"\u804a\u5e01","out_trade_no":"20170113175335","total_amount":"0.01","product_code":"QUICK_MSECURITY_PAY"}
     * sign : xHu/SwtjW931cI50s0j1GU7mCwVI6Ci68zjzSwJpa7Ukl3wuAGrL/lhbeldLEQrspksusFFodhTp4nUG8BhvtvHFoQJGGB+xTEvI+OTmvfZC4wX4Kmh+WXJ5chD1pdd1JuImSTHXCmsf7erGoXRQPn8AJnXg2YvHaIVa9qcmpRiAcWff91y+9bSETfQ0vxNy1tADGth85ttqZ0Nu4CID5a4FyC4v67mSy1l+G7amBH3x1GYeo4hlEN7UIXkx6/azyo171sC7ixw/YVgIbqCNHBimJuUSHz/6u9s4ktiVdv3nrWfcCCIVtaW3qsl/V0H7bCwtFLTbdBVbkprxm1oG6g==
     */

    private String app_id;
    private String method;
    private String sign_type;
    private String charset;
    private String timestamp;
    private String version;
    private String notify_url;
    private String biz_content;
    private String sign;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
