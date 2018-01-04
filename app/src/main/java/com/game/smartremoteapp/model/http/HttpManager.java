package com.game.smartremoteapp.model.http;

import com.game.smartremoteapp.bean.AppUserBean;
import com.game.smartremoteapp.bean.ListRankBean;
import com.game.smartremoteapp.bean.LoginInfo;
import com.game.smartremoteapp.bean.PondResponseBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.Token;
import com.game.smartremoteapp.utils.UrlUtils;
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
    public void getLogin(String phone, String code, Subscriber<Result<LoginInfo>> subscriber) {
        Observable<Result<LoginInfo>> o = smartService.getLogin(phone, code);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //验证码
    public void getCode(String phone, Subscriber<Result<Token>> subscriber) {
        Observable<Result<Token>> o = smartService.getCode(phone);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //用户直接登录
    public void getLoginWithoutCode(String userId,
                                    Subscriber<Result<LoginInfo>> subscriber) {
        Observable<Result<LoginInfo>> o = smartService.getLoginWithOutCode(userId);
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

    //获取用户信息
    public void getUserDate(String userId,Subscriber<Result<LoginInfo>>subscriber){
        Observable<Result<LoginInfo>> o= smartService.getUserDate(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //修改用户名   11/21 13：15
    public void getUserName(String userId,String nickName,Subscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getUserName(userId,nickName);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //充值   11/21 15：15
    public void getUserPay(String userId,String money,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o= smartService.getUserPay(userId,money);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //消费   11/21 16：15
    public void getUserPlayNum(String userId,String money,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o= smartService.getUserPlayNum(userId,money);
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
    public void getRegPlayBack(int id,String time,String nickname,String state,String dollname,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o= smartService.getRegPlayBack(id,time,nickname,state,dollname);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取视频回放列表
    public void getVideoBackList(String userId,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o= smartService.getVideoBackList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取房间用户头像
    public void getCtrlUserImage(String userId,Subscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getCtrlUserImage(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //下注
    public void getBets(String userId,int wager,String guessKey,int playBackId,
                        String dollID,Subscriber<Result<AppUserBean>> subscriber){
        Observable<Result<AppUserBean>> o= smartService.getBets(userId,wager,guessKey,playBackId,dollID);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //跑马灯
    public void getUserList(Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o= smartService.getUserList();
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //游戏场次
    public void getPlayId(String dollname,Subscriber<Result<LoginInfo>>subscriber){
        Observable<Result<LoginInfo>> o =smartService.getPlayId(dollname);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //开始游戏创建场次
    public void  getCreatPlayList(String nickName,String dollname,Subscriber<Result<LoginInfo>>subscriber){
        Observable<Result<LoginInfo>> o =smartService.getCreatPlayList(nickName,dollname);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取下注人数
    public void  getPond(int playId,Subscriber<Result<PondResponseBean>>subscriber){
        Observable<Result<PondResponseBean>> o =smartService.getPond( playId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //收货人信息
    public void getConsignee(String name,String phone,String address,String userID,Subscriber<Result<LoginInfo>>subscriber){
        Observable<Result<LoginInfo>> o =smartService.getConsignee(name,phone,address,userID);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //发货
    public void getSendGoods(String id,String number,String consignee,String remark,String userID,Subscriber<Result<LoginInfo>>subscriber){
        Observable<Result<LoginInfo>> o =smartService.getSendGoods(id,number,consignee,remark,userID);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //兑换游戏币
    public void getExChangeWWB(String id,String dollName,String number,String userId,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o =smartService.getExchangeWWB(id,dollName,number,userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //获取兑换记录列表
    public void getExChangeList(String userId,Subscriber<Result<LoginInfo>> subscriber){
        Observable<Result<LoginInfo>> o =smartService.getExchangeList(userId);
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
