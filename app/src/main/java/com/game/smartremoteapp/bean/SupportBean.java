package com.game.smartremoteapp.bean;

import java.io.Serializable;

/**
 * Created by mi on 2018/10/10.
 */

public class SupportBean  implements Serializable{
    private String allSupportNum;

    public String getAllSupportNum() {
        return allSupportNum;
    }

    public void setAllSupportNum(String allSupportNum) {
        this.allSupportNum = allSupportNum;
    }
}
