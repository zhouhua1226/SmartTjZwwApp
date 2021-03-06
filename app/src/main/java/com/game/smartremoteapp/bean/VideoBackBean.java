package com.game.smartremoteapp.bean;

import java.io.Serializable;

/**
 * Created by yincong on 2017/11/25 12:35
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class VideoBackBean implements Serializable{
    private boolean is_select;

    private String DOLL_NAME;
    private String DOLLID;
    private String STATE;
    private String NICKNAME;
    private String ID;
    private String CAMERA_DATE;
    private String STOP_FLAG;
    private String USERID;
    private String DOLL_URL;
    private String GOLD;
    private String COUNT;
    private String SENDGOODS;
    private String IMAGE_URL;
    private String GUESS_ID;
    private String CONVERSIONGOLD;
    private String POST_STATE;
    private String SEND_ORDER_ID;
    private String CREATE_DATE;
    private String MACHINE_TYPE;//娃娃机类型 1、娃娃机 2、推币机 3、金币机

    public boolean getIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getMACHINE_TYPE() {
        return MACHINE_TYPE;
    }

    public void setMACHINE_TYPE(String MACHINE_TYPE) {
        this.MACHINE_TYPE = MACHINE_TYPE;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getSEND_ORDER_ID() {
        return SEND_ORDER_ID;
    }

    public void setSEND_ORDER_ID(String SEND_ORDER_ID) {
        this.SEND_ORDER_ID = SEND_ORDER_ID;
    }

    public String getCAMERA_DATE() {
        return CAMERA_DATE;
    }

    public void setCAMERA_DATE(String CAMERA_DATE) {
        this.CAMERA_DATE = CAMERA_DATE;
    }

    public String getPOST_STATE() {
        return POST_STATE;
    }

    public void setPOST_STATE(String POST_STATE) {
        this.POST_STATE = POST_STATE;
    }

    public String getDOLL_NAME() {
        return DOLL_NAME;
    }

    public void setDOLL_NAME(String DOLL_NAME) {
        this.DOLL_NAME = DOLL_NAME;
    }

    public String getDOLLID() {
        return DOLLID;
    }

    public void setDOLLID(String DOLLID) {
        this.DOLLID = DOLLID;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSTOP_FLAG() {
        return STOP_FLAG;
    }

    public void setSTOP_FLAG(String STOP_FLAG) {
        this.STOP_FLAG = STOP_FLAG;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getDOLL_URL() {
        return DOLL_URL;
    }

    public void setDOLL_URL(String DOLL_URL) {
        this.DOLL_URL = DOLL_URL;
    }

    public String getGOLD() {
        return GOLD;
    }

    public void setGOLD(String GOLD) {
        this.GOLD = GOLD;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getSENDGOODS() {
        return SENDGOODS;
    }

    public void setSENDGOODS(String SENDGOODS) {
        this.SENDGOODS = SENDGOODS;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getGUESS_ID() {
        return GUESS_ID;
    }

    public void setGUESS_ID(String GUESS_ID) {
        this.GUESS_ID = GUESS_ID;
    }

    public String getCONVERSIONGOLD() {
        return CONVERSIONGOLD;
    }

    public void setCONVERSIONGOLD(String CONVERSIONGOLD) {
        this.CONVERSIONGOLD = CONVERSIONGOLD;
    }
}
