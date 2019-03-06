package com.game.smartremoteapp.bean;

import java.io.Serializable;

/**
 * Created by mi on 2018/12/28.
 */

public class UserInfoBean implements Serializable {
    private String  gold;
    private String  imgurl;
    private String  createtime;
    private String  redPackage_Id;
    private String  nickname;
    private String  redUserId;
    private String  userId;
    private String  id;

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRedPackage_Id() {
        return redPackage_Id;
    }

    public void setRedPackage_Id(String redPackage_Id) {
        this.redPackage_Id = redPackage_Id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRedUserId() {
        return redUserId;
    }

    public void setRedUserId(String redUserId) {
        this.redUserId = redUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
