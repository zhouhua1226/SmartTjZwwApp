package com.game.smartremoteapp.bean;

import java.io.Serializable;

/**
 * Created by mi on 2018/9/30.
 */

public class RewardGoldManagerBean implements Serializable {

    private String GOLD;
    private String WORD;
    private String SUPPORTNUM;
    private String REWARDGOLDMANAGER_ID;
    private UpdateTimeBaean UPDATETIME;

    public String getGOLD() {
        return GOLD;
    }

    public void setGOLD(String GOLD) {
        this.GOLD = GOLD;
    }

    public String getWORD() {
        return WORD;
    }

    public void setWORD(String WORD) {
        this.WORD = WORD;
    }

    public String getSUPPORTNUM() {
        return SUPPORTNUM;
    }

    public void setSUPPORTNUM(String SUPPORTNUM) {
        this.SUPPORTNUM = SUPPORTNUM;
    }

    public String getREWARDGOLDMANAGER_ID() {
        return REWARDGOLDMANAGER_ID;
    }

    public void setREWARDGOLDMANAGER_ID(String REWARDGOLDMANAGER_ID) {
        this.REWARDGOLDMANAGER_ID = REWARDGOLDMANAGER_ID;
    }

    public UpdateTimeBaean getUPDATETIME() {
        return UPDATETIME;
    }

    public void setUPDATETIME(UpdateTimeBaean UPDATETIME) {
        this.UPDATETIME = UPDATETIME;
    }
}
