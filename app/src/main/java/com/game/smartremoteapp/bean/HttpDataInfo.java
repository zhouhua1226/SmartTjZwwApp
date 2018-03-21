package com.game.smartremoteapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouh on 2017/11/1.
 */
public class HttpDataInfo implements Serializable{
    private String accessToken;
    private String sessionID;
    private UserBean appUser;
    private SRStoken srsToken;
    private List<ZwwRoomBean> dollList;
    private List<VideoBackBean> playback;
    private List<VideoBackBean> dollCount;
    private BetsBackBean betsBackBean;
    private List<PlayBackBean> playBackBeen;
    private VideoBackBean playBack;
    private List<ExChangeMoneyBean> conversionList;
    private OrderBean Order;
    private List<PayCardBean> paycard;
    private List<UserPaymentBean> paymentList;
    private SignBean sign;
    private List<BannerBean> runImage;
    private List<LogisticsBean> logistics;
    private List<ToyTypeBean> toyTypeList;
    private String codeVale;
    private AwardCodeBean awardPd;
    private String awradFlag;
    private AwardCodePdBean setPd;
    private List<GuessLastBean> pondList;

    public List<GuessLastBean> getPondList() {
        return pondList;
    }

    public void setPondList(List<GuessLastBean> pondList) {
        this.pondList = pondList;
    }

    public String getAwradFlag() {
        return awradFlag;
    }

    public void setAwradFlag(String awradFlag) {
        this.awradFlag = awradFlag;
    }

    public AwardCodePdBean getSetPd() {
        return setPd;
    }

    public void setSetPd(AwardCodePdBean setPd) {
        this.setPd = setPd;
    }

    public String getCodeVale() {
        return codeVale;
    }

    public void setCodeVale(String codeVale) {
        this.codeVale = codeVale;
    }

    public AwardCodeBean getAwardPd() {
        return awardPd;
    }

    public void setAwardPd(AwardCodeBean awardPd) {
        this.awardPd = awardPd;
    }

    public List<LogisticsBean> getLogistics() {
        return logistics;
    }

    public void setLogistics(List<LogisticsBean> logistics) {
        this.logistics = logistics;
    }

    public List<BannerBean> getRunImage() {
        return runImage;
    }

    public void setRunImage(List<BannerBean> runImage) {
        this.runImage = runImage;
    }

    public SRStoken getSrsToken() {
        return srsToken;
    }

    public void setSrsToken(SRStoken srsToken) {
        this.srsToken = srsToken;
    }

    public SignBean getSign() {
        return sign;
    }

    public void setSign(SignBean sign) {
        this.sign = sign;
    }

    public SRStoken getSRStoken() {
        return srsToken;
    }

    public void setSRStoken(SRStoken srsToken) {
        this.srsToken = srsToken;
    }

    public List<UserPaymentBean> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<UserPaymentBean> paymentList) {
        this.paymentList = paymentList;
    }

    public List<PayCardBean> getPaycard() {
        return paycard;
    }

    public void setPaycard(List<PayCardBean> paycard) {
        this.paycard = paycard;
    }

    public OrderBean getOrder() {
        return Order;
    }

    public void setOrder(OrderBean order) {
        Order = order;
    }

    public List<ExChangeMoneyBean> getConversionList() {
        return conversionList;
    }

    public void setConversionList(List<ExChangeMoneyBean> conversionList) {
        this.conversionList = conversionList;
    }

    public VideoBackBean getPlayBack() {
        return playBack;
    }

    public void setPlayBack(VideoBackBean playBack) {
        this.playBack = playBack;
    }

    public List<PlayBackBean> getPlayBackBeen() {
        return playBackBeen;
    }

    public void setPlayBackBeen(List<PlayBackBean> playBackBeen) {
        this.playBackBeen = playBackBeen;
    }

    public BetsBackBean getBetsBackBean() {
        return betsBackBean;
    }

    public void setBetsBackBean(BetsBackBean betsBackBean) {
        this.betsBackBean = betsBackBean;
    }

    public List<VideoBackBean> getDollCount() {
        return dollCount;
    }

    public void setDollCount(List<VideoBackBean> dollCount) {
        this.dollCount = dollCount;
    }

    public List<VideoBackBean> getPlayback() {
        return playback;
    }

    public void setPlayback(List<VideoBackBean> playback) {
        this.playback = playback;
    }


    public void setAppUser(UserBean appUser) {
        this.appUser = appUser;
    }

    public void setDollList(List<ZwwRoomBean> dollList) {
        this.dollList = dollList;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<ZwwRoomBean> getDollList() {
        return dollList;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UserBean getAppUser() {
        return appUser;
    }

    public void setToyTypeList(List<ToyTypeBean> toyTypeList) {
        this.toyTypeList = toyTypeList;
    }

    public List<ToyTypeBean> getToyTypeList() {
        return toyTypeList;
    }
}
