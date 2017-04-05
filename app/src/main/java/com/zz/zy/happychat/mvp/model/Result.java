package com.zz.zy.happychat.mvp.model;

import java.util.Objects;

/**
 * Created by zzzy on 2016/12/30.
 */
public class Result {

    /**
     * result : 200
     * resultNote : success
     * data :
     */

    private int result;
    private String resultNote;
    private Object data;

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

    public String getData() {
        return data.toString();
    }

    public void setData(Object data) {
        this.data = data;
    }
}
