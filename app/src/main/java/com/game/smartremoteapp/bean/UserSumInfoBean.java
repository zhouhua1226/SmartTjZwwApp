package com.game.smartremoteapp.bean;

/**
 * Created by mi on 2019/1/14.
 */

public class UserSumInfoBean {
    private String  NICKNAME;
    private String  SUMGOLD;
    private String  COUNT;
    private String  IMAGE_URL;

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getSUMGOLD() {
        return SUMGOLD;
    }

    public void setSUMGOLD(String SUMGOLD) {
        this.SUMGOLD = SUMGOLD;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }
}
