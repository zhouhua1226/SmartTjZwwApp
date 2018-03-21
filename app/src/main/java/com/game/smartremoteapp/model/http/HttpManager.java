package com.game.smartremoteapp.model.http;

import com.game.smartremoteapp.bean.AppUserBean;
import com.game.smartremoteapp.bean.BetRecordBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.ListRankBean;
import com.game.smartremoteapp.bean.PondResponseBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.RoomListBean;
import com.game.smartremoteapp.bean.Token;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhouh on 2017/9/8.
 */
public class HttpManager {

    private static HttpManager httpManager;
    private Retrofit retrofit;
    private SmartService smartService;

    public static synchronized HttpManager getInstance() {
        if (httpManager == null) {
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    private HttpManager() {
        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(HttpInstance.getInstance().getClient())
                .baseUrl(UrlUtils.VIDEO_ROOT_URL)
                .build();
        smartService = retrofit.create(SmartService.class);
    }

    public void getVideoToken(String appkey, String appSecret, Subscriber<Result<Token>> subscriber) {
        Observable<Result<Token>> o = smartService.getVideoToken(appkey, appSecret);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //登录
    public void getLogin(String phone, String code, Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o = smartService.getLogin(phone, code);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //验证码登录
    public void getLoginCode(String phone, String password, Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o = smartService.getLogin(phone, password);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //密码登录
    public void getLoginPassword(String phone, String pass, RequestSubscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o =smartService.getLoginPassword(phone,pass);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //手机号注册
    public void getRegiter(String phone, String pass, String code, RequestSubscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o =smartService.getRegiter(phone,pass,code);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
    //验证码
    public void getCode(String phone, RequestSubscriber<Result<Token>> subscriber) {
        Observable<Result<Token>> o = smartService.getCode(phone);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //用户直接登录
    public void getLoginWithoutCode(String phone, Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o = smartService.getLoginWithOutCode(phone);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //上传头像
    public void getFaceImage(String userId, String faceImage, RequestSubscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getFaceImage(userId,faceImage);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

//    //修改昵称UserNickNameURL
//    public void getNickName(String phone,String nickName,Subscriber<Result>subscriber){
//        Observable<Result<HttpDataInfo>> o= smartService.getFaceImage(phone,nickName);
//        o.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

    //修改用户名   11/21 13：15
    public void getUserName(String userId,String nickName,Subscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getUserName(userId,nickName);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //充值   11/21 15：15
    public void getUserPay(String phone,String money,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o= smartService.getUserPay(phone,money);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //消费   11/21 16：15
    public void getUserPlayNum(String userId,String money,String dollId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o= smartService.getUserPlayNum(userId,money,dollId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //ListRank
    public void getListRank(Subscriber<Result<ListRankBean>> subscriber){
        Observable<Result<ListRankBean>> o= smartService.getListRank();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //视屏上传
    public void getRegPlayBack(String time,String userId,String state,String dollId,String guessId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o= smartService.getRegPlayBack(time,userId,state,dollId,guessId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取视频回放列表
    public void getVideoBackList(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o= smartService.getVideoBackList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //下注
    public void getBets(String userId,int wager,String guessKey,String guessId,
                        String dollID,Subscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getBets(userId,wager,guessKey,guessId,dollID);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //跑马灯
    public void getUserList(Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o= smartService.getUserList();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //游戏场次
    public void getPlayId(String dollname,Subscriber<Result<HttpDataInfo>>subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getPlayId(dollname);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //开始游戏创建场次
    public void  getCreatPlayList(String nickName,String dollname,Subscriber<Result<HttpDataInfo>>subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getCreatPlayList(nickName,dollname);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取下注人数
    public void  getPond(String playId,String dollId,Subscriber<Result<PondResponseBean>>subscriber){
        Observable<Result<PondResponseBean>> o =smartService.getPond(playId,dollId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //收货人信息
    public void getConsignee(String name,String phone,String address,String userID,Subscriber<Result<HttpDataInfo>>subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getConsignee(name,phone,address,userID);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //发货
    public void getSendGoods(String id,String number,String consignee,String remark,String userID,String mode,String costNum,Subscriber<Result<HttpDataInfo>>subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getSendGoods(id,number,consignee,remark,userID,mode,costNum);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //兑换游戏币
    public void getExChangeWWB(String id,String dollId,String number,String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getExchangeWWB(id,dollId,number,userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取兑换记录列表
    public void getExChangeList(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getExchangeList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //退出登录
    public void getLogout(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getLogout(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取用户信息
    public void getUserDate(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getUserDate(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //WXQQ登录
    public void getYSDKLogin(String uid,String accessToken,String nickName,String imageUrl,String ctype,String channel,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getYSDKLogin(uid,accessToken,nickName,imageUrl,ctype,channel);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //ysdk支付创建订单接口
    public void getYSDKPay(String userId,String accessToken,String amount,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getYSDKPay(userId,accessToken,amount);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //YSDK自动登录接口
    public void getYSDKAuthLogin(String userId,String accessToken,String ctype,String channel,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getYSDKAuthLogin(userId,accessToken,ctype,channel);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取充值卡列表
    public void getPayCardList(Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getPayCardList();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //用户竞猜记录

    public void getGuessDetail(String userId,Subscriber<Result<BetRecordBean>>subscriber){
        Observable<Result<BetRecordBean>> o =smartService.getGuessDetail(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取金币流水列表
    public void getPaymenList(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getPaymenList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取房间列表
    public void getDollList(Subscriber<Result<RoomListBean>> subscriber){
        Observable<Result<RoomListBean>> o =smartService.getToysByType("", 1);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //签到
    public void getUserSign(String userId,String signType,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getUserSign(userId,signType);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取轮播列表
    public void getBannerList(Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getBannerList();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //排行榜当前用户排名接口
    public void getNumRankList(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getNumRankList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //物流订单接口
    public void getLogisticsOrder(String userId,Subscriber<Result<HttpDataInfo>> subscriber){
        Observable<Result<HttpDataInfo>> o =smartService.getLogisticsOrder(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取类型
    public void getToyType(Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o =smartService.getToyType();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取分类娃娃机
    public void getToyListByType(String type, int page, Subscriber<Result<RoomListBean>> subscriber) {
        Observable<Result<RoomListBean>> o = smartService.getToysByType(type, page);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //查询邀请码接口
    public void getUserAwardCode(String userId, Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o = smartService.getUserAwardCode(Utils.deviceType, Utils.osVersion,
                Utils.appVersion,Utils.IMEI, UrlUtils.LOGIN_CTYPE,UrlUtils.LOGIN_CHANNEL, userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //兑换邀请码接口
    public void doAwardByUserCode(String userId,String awardCode, Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o = smartService.doAwardByUserCode(Utils.deviceType, Utils.osVersion,
                Utils.appVersion,Utils.IMEI, UrlUtils.LOGIN_CTYPE,UrlUtils.LOGIN_CHANNEL, userId,awardCode);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //竞猜跑马灯
    public void getGuesserlast10(Subscriber<Result<HttpDataInfo>> subscriber) {
        Observable<Result<HttpDataInfo>> o =smartService.getGuesserlast10();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //竞猜排行榜
    public void getRankBetList(String userId,Subscriber<Result<ListRankBean>> subscriber){
        Observable<Result<ListRankBean>> o =smartService.getRankBetList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //抓娃娃排行榜
    public void getRankDollList(String userId,Subscriber<Result<ListRankBean>> subscriber){
        Observable<Result<ListRankBean>> o =smartService.getRankDollList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    //支付宝支付
    public void getTradeOrderAlipay(String userId, String s, RequestSubscriber<Result<String>> subscriber) {
        Observable<Result<String>> o =smartService.getTradeOrderAlipay(userId,s);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }


}