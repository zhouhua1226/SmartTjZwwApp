package com.game.smartremoteapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mi on 2018/12/28.
 */
public class RedPackageBean implements Serializable {
    private List<UserInfoBean>  userInfo;
    private String  redGold;
    private String  redNum;
    private String  id;
    private String  createTime;
    private String  tag;
    private String  version;
    private String  userId;
    private String  imgurl;
    private String  nickname;
    private List<RedWalletlistInfoBean>  sendlistInfo;
    private UserSumInfoBean  userSumInfo;
    private List<RedWalletlistInfoBean>  receiveListInfo;

    public List<RedWalletlistInfoBean> getSendlistInfo() {
        return sendlistInfo;
    }

    public void setSendlistInfo(List<RedWalletlistInfoBean> sendlistInfo) {
        this.sendlistInfo = sendlistInfo;
    }

    public List<RedWalletlistInfoBean> getReceiveListInfo() {
        return receiveListInfo;
    }

    public void setReceiveListInfo(List<RedWalletlistInfoBean> receiveListInfo) {
        this.receiveListInfo = receiveListInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public UserSumInfoBean getUserSumInfo() {
        return userSumInfo;
    }

    public void setUserSumInfo(UserSumInfoBean userSumInfo) {
        this.userSumInfo = userSumInfo;
    }



    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public List<UserInfoBean> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserInfoBean> userInfo) {
        this.userInfo = userInfo;
    }

    public String getRedGold() {
        return redGold;
    }

    public void setRedGold(String redGold) {
        this.redGold = redGold;
    }

    public String getRedNum() {
        return redNum;
    }

    public void setRedNum(String redNum) {
        this.redNum = redNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createTime;
    }

    public void setCreatetime(String createtime) {
        this.createTime = createtime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
