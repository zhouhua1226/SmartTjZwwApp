package com.game.smartremoteapp.protocol;

import java.io.Serializable;

/**
 * Created by yincong on 2018/8/22 15:32
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class JCResult implements Serializable{

    String error;
    String msg;
    String JD;

    public String getError() {
        return error == null ? "" : error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJD() {
        return JD == null ? "" : JD;
    }

    public void setJD(String JD) {
        this.JD = JD;
    }
}
