package com.game.smartremoteapp.model.http;


import com.game.smartremoteapp.bean.AlipayBean;
import com.game.smartremoteapp.bean.AppInfo;
import com.game.smartremoteapp.bean.AppUserBean;
import com.game.smartremoteapp.bean.BetRecordBean;
import com.game.smartremoteapp.bean.CoinListBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.LevelBean;
import com.game.smartremoteapp.bean.ListRankBean;
import com.game.smartremoteapp.bean.LoginRewardGoldBean;
import com.game.smartremoteapp.bean.NowPayBean;
import com.game.smartremoteapp.bean.OrderBean;
import com.game.smartremoteapp.bean.PictureListBean;
import com.game.smartremoteapp.bean.PondResponseBean;
import com.game.smartremoteapp.bean.RedInfoBean;
import com.game.smartremoteapp.bean.RedPackageBean;
import com.game.smartremoteapp.bean.RedPackageListBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.RoomListBean;
import com.game.smartremoteapp.bean.SupportBean;
import com.game.smartremoteapp.bean.Token;
import com.game.smartremoteapp.bean.ToyNumBean;
import com.game.smartremoteapp.protocol.JCResult;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.utils.UrlUtils;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhouh on 2017/9/8.
 */
public interface SmartService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.VIDEO_GET_TOKEN)
    Observable<Result<Token>> getVideoToken(
            @Field(UrlUtils.APPKEY) String appKey,
            @Field(UrlUtils.APPSECRET) String appSecret);

    //登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.LOGIN)
    Observable<Result<HttpDataInfo>> getLogin(
            @Field(UrlUtils.PHONE) String phone,
            @Field(UrlUtils.SMSCODE) String code
    );


    //验证码
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETSMSCODE)
    Observable<Result<Void>> getCode(
            @Field(UrlUtils.PHONE) String phone,
            @Field(UrlUtils.CTYPE) String ctype,
            @Field(UrlUtils.CHANNEL) String channel);

    //不需要验证码直接登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.LOGINWITHOUTCODE)
    Observable<Result<HttpDataInfo>> getLoginWithOutCode(
            @Field(UrlUtils.PHONE) String phone);

    //头像上传
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.FACEIMAGEURL)
    Observable<Result<AppUserBean>> getFaceImage(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.FACEIMAGE) String base64Image
    );

    //修改昵称
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.UserNickNameURL)
    Observable<Result> getNickName(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.NICKNANME) String nickname
    );

    //修改用户名   11/21 13：15
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERNAMEURL)
    Observable<Result<AppUserBean>> getUserName(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.NICKNANME) String nickName
    );

    //充值   11/22 15：15
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERPAYURL)
    Observable<Result<HttpDataInfo>> getUserPay(
            @Field(UrlUtils.PHONE) String phone,
            @Field(UrlUtils.USEPAYMONEY) String money
    );

    //消费   11/22 16：15
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERPLAYURL)
    Observable<Result<HttpDataInfo>> getUserPlayNum(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.USERPLAYNUM) String gold,
            @Field(UrlUtils.DOLLID) String dollId

    );

    //ListRank
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.LISTRANKURL)
    Observable<Result<ListRankBean>> getListRank(
    );

    //视屏上传
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.UPLOADURL)
    Observable<Result<HttpDataInfo>> getRegPlayBack(
            @Field(UrlUtils.TIME) String time,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.STATE) String state,
            @Field(UrlUtils.DOLLID) String dollId,
            @Field(UrlUtils.GUESSID) String guessId


    );

    //获取视频回放列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.VIDEOBACKURL)
    Observable<Result<HttpDataInfo>> getVideoBackList(
            @Field(UrlUtils.USERID) String userId
    );

    //获取房间用户头像
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CTRLUSERIMAGE)
    Observable<Result<AppUserBean>> getCtrlUserImage(
            @Field(UrlUtils.NICKNANME) String phone
    );

    //下注
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.BETSURL)
    Observable<Result<AppUserBean>> getBets(
            @Field(UrlUtils.USERID) String userID,
            @Field(UrlUtils.WAGER) Integer wager,
            @Field(UrlUtils.GUESSKEY) String guessKey,
            @Field(UrlUtils.GUESSID) String guessId,
            @Field(UrlUtils.DOLLID) String dollID,
            @Field(UrlUtils.GUESSPRONUM) int afterVoting,
            @Field(UrlUtils.GUESSMULTIPLE) int multiple,
            @Field(UrlUtils.FLAG) String flag);

    //跑马灯
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.getUserList)
    Observable<Result<HttpDataInfo>> getUserList();

    //开始游戏分发场次
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CREATPLAYLISTURL)
    Observable<Result<HttpDataInfo>> getCreatPlayList(
            @Field(UrlUtils.NICKNANME) String nickName,
            @Field(UrlUtils.DOLLNAME) String dollName
    );

    //围观群众分发场次
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.PLAYIDURL)
    Observable<Result<HttpDataInfo>> getPlayId(
            @Field(UrlUtils.DOLLID) String dollId
    );

    //获取下注人数
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETPONDURL)
    Observable<Result<PondResponseBean>> getPond(
            @Field(UrlUtils.PLAYID) String playId,
            @Field(UrlUtils.DOLLID) String dollId
    );

    //收货人信息
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CONSIGNEEURL)
    Observable<Result<HttpDataInfo>> getConsignee(
            @Field(UrlUtils.CONSIGNEENAME) String name,
            @Field(UrlUtils.CONSIGNEEPHONE) String phone,
            @Field(UrlUtils.CONSIGNEEADDRESS) String address,
            @Field(UrlUtils.CONSIGNEEUSERID) String userID
    );

    //发货
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.SENDGOODSURL)
    Observable<Result<HttpDataInfo>> getSendGoods(
            @Field(UrlUtils.SENDGOODSID) String id,
            @Field(UrlUtils.SENDGOODSNUM) String number,
            @Field(UrlUtils.SENDGOODSSHXX) String consignee,
            @Field(UrlUtils.SENDGOODSREMARK) String remark,
            @Field(UrlUtils.SENDGOODSUSERID) String userId,
            @Field(UrlUtils.SENDGOODSMODE) String mode,
            @Field(UrlUtils.SENDGOODSCOSTNUM) String costNum,
            @Field(UrlUtils.SENDGOODSLEVEL) String level
    );


    //兑换游戏币
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.EXCHANGEURL)
    Observable<Result<HttpDataInfo>> getExchangeWWB(
            @Field(UrlUtils.SENDGOODSID) String id,
            @Field(UrlUtils.DOLLID) String dollId,
            @Field(UrlUtils.SENDGOODSNUM) String number,
            @Field(UrlUtils.USERID) String userId
    );

    //兑换记录列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.EXCHANGELISTURL)
    Observable<Result<HttpDataInfo>> getExchangeList(
            @Field(UrlUtils.USERID) String userID
    );

    //退出登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.LOGOUT)
    Observable<Result<HttpDataInfo>> getLogout(
            @Field(UrlUtils.USERID) String userID
    );

    //获取用户信息接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETUSERDATEURL)
    Observable<Result<HttpDataInfo>> getUserDate(
            @Field(UrlUtils.USERID) String userId
    );

    //WXQQ登录接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.YSDKLOGINURL)
    Observable<Result<HttpDataInfo>> getYSDKLogin(
            @Field(UrlUtils.WXQQ_UID) String uid,
            @Field(UrlUtils.WXQQ_ACCESSTOKEN) String accessToken,
            @Field(UrlUtils.WXQQ_NICKNAME) String nickName,
            @Field(UrlUtils.WXQQ_IMAGEURL) String imageUrl,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel
    );

    //YSDK支付接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.YSDKPAYORDERURL)
    Observable<Result<HttpDataInfo>> getYSDKPay(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.WXQQ_ACCESSTOKEN) String accessToken,
            @Field(UrlUtils.WXQQ_AMOUNT) String amount
    );

    //YSDK自动登录接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.YSDKAUTHLOGINURL)
    Observable<Result<HttpDataInfo>> getYSDKAuthLogin(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.WXQQ_ACCESSTOKEN) String accessToken,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel
    );

    //获取充值卡列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.PAYCARDLISTURL)
    Observable<Result<HttpDataInfo>> getPayCardList();

    //用户竞猜记录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETGUESSDETAIL)
    Observable<Result<BetRecordBean>> getGuessDetail(
            @Field(UrlUtils.USERID) String userId
    );

    //用户金币流水  getPaymenlist
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CURRENTACCOUNTURL)
    Observable<Result<HttpDataInfo>> getPaymenList(
            @Field(UrlUtils.USERID) String userId
    );

    //获取房间列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.DOLLLISTURL)
    Observable<RoomListBean> getDollList();

    //签到接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERSIGNURL)
    Observable<Result<HttpDataInfo>> getUserSign(
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.SIGNTYPE) String signType
    );

    //获取轮播列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.BANNERURL)
    Observable<Result<HttpDataInfo>> getBannerList();

    //获取轮播列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.BANNERURLNEW)
    Observable<Result<HttpDataInfo>> getBannernewList(@Field(UrlUtils.APKNAME) String apkName,
                                                      @Field(UrlUtils.CHANNEL) String channel);

    //个人排名
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RANKLISTURL)
    Observable<Result<HttpDataInfo>> getNumRankList(
            @Field(UrlUtils.USERID) String userId
    );

    //物流订单查询
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.LOGISTICSORDERURL)
    Observable<Result<HttpDataInfo>> getLogisticsOrder(
            @Field(UrlUtils.USERID) String userId
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.GETTOYTYPE)
    Observable<Result<HttpDataInfo>> getToyType();

    //房间列表下一页接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETTOYSBYTYPE)
    Observable<Result<RoomListBean>> getToysByType(
            @Field(UrlUtils.CURRENTTYPE) String currentType,
            @Field(UrlUtils.NEXTPAGE) Integer nextPage
    );

    //查询邀请码接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERAWARDCODEURL)
    Observable<Result<HttpDataInfo>> getUserAwardCode(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId
    );

    //兑换邀请码接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOAWARDBYUSERCODEURL)
    Observable<Result<HttpDataInfo>> doAwardByUserCode(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.USERAWARDCODE) String awardCode
    );

    //竞猜跑马灯  /pooh-web/app/getGuesserlast10
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.GUESSERLASTTENURL)
    Observable<Result<HttpDataInfo>> getGuesserlast10();

    //竞猜排行
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RANKBETLISTURLTODAY)
    Observable<Result<ListRankBean>> getRankBetTodayList(
            @Field(UrlUtils.USERID) String userId
    );

    //新抓娃娃排行
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RANKDOLLLISTURLTODAY)
    Observable<Result<ListRankBean>> getRankDollTodayList(
            @Field(UrlUtils.USERID) String userId
    );


    //竞猜排行
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RANKBETLISTURL)
    Observable<Result<ListRankBean>> getRankBetList(
            @Field(UrlUtils.USERID) String userId
    );

    //新抓娃娃排行
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RANKDOLLLISTURL)
    Observable<Result<ListRankBean>> getRankDollList(
            @Field(UrlUtils.USERID) String userId
    );

    //支付宝充值订单信息
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.TRADEORDERALIPAY)
    Observable<Result<AlipayBean>> getTradeOrderAlipay(@Field(UrlUtils.USERID) String userId,
                                                       @Field(UrlUtils.AMOUNT) String amount,
                                                       @Field(UrlUtils.REGGOLD) String regGold,
                                                       @Field(UrlUtils.PAYOUTTYPE) String payOutType,
                                                       @Field(UrlUtils.PAYTYPE) String payType,
                                                       @Field(UrlUtils.WXQQ_CTYPE) String ctype,
                                                       @Field(UrlUtils.WXQQ_CHANNEL) String loginChannel);

    //手机号注册
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.REGITER)
    Observable<Result<HttpDataInfo>> getRegiter(@Field(UrlUtils.PHONE) String phone,
                                                @Field(UrlUtils.PASSWORD) String password,
                                                @Field(UrlUtils.SMSCODE) String code,
                                                @Field(UrlUtils.CHANNELNUM) String channelNum);

    //手机号密码登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.LOGINPASSWORD)
    Observable<Result<HttpDataInfo>> getLoginPassword(@Field(UrlUtils.PHONE) String phone, @Field(UrlUtils.PW) String pass,
                                                      @Field(UrlUtils.CTYPE) String ctype, @Field(UrlUtils.CHANNEL) String channel);

    //自动登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.AUTHLOGINURL)
    Observable<Result<HttpDataInfo>> getAuthLogin(@Field(UrlUtils.USERID) String userId,
                                                  @Field(UrlUtils.CTYPE) String ctype,
                                                  @Field(UrlUtils.CHANNEL) String channel);


    //查询可推广加盟的接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETPROMOMOTEMANAGE)
    Observable<Result<HttpDataInfo>> getPromomoteManage(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel
    );

    //购买推广加盟的接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.PROMOTEORDERTOGOLD)
    Observable<Result<HttpDataInfo>> getPromomoteOrder(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PROMOTEORDER_PROID) String proManageId,
            @Field(UrlUtils.PROMOTEORDER_PAYTYPE) String payType
    );

    //查询用户已购买的加盟
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETUSERPROMOTEINF)
    Observable<Result<HttpDataInfo>> getUserPromoteInf(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId
    );

    //兑换推广码
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.COMMITUSERPROMOTECODE)
    Observable<Result<HttpDataInfo>> getCommitUserPromoteCode(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PROMOTE_CODE) String promoteCode
    );

    //查询用户现金余额
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETUSERACCBALCOUNT)
    Observable<Result<HttpDataInfo>> getUserAccBalCount(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId
    );

    //用户推广收益明细
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETUSERPROMOTELIST)
    Observable<Result<HttpDataInfo>> getUserPromoteList(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId
    );

    //新查询用户个人信息接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETAPPUSERINFURL)
    Observable<Result<HttpDataInfo>> getAppUserInf(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId
    );

    //新获取短信验证吗
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETPHONECODEURL)
    Observable<Result<HttpDataInfo>> getPhoneCode(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.PHONE_SMSTYPE) String smsType,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PHONE_phoneNumber) String phoneNumber
    );

    //用户绑定手机号
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.EDITAPPUSERPHONE)
    Observable<Result<HttpDataInfo>> getEditUserPhone(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.PHONE_SMSTYPE) String smsType,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PHONE_phoneNumber) String phoneNumber,
            @Field(UrlUtils.PHONE_PHONECODE) String phoneCode
    );

    //用户绑定银行卡
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.REGBANKINFURL)
    Observable<Result<HttpDataInfo>> getRegBankInf(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PHONE_SMSTYPE) String smsType,
            @Field(UrlUtils.PHONE_phoneNumber) String phoneNumber,
            @Field(UrlUtils.PHONE_PHONECODE) String phoneCode,
            @Field(UrlUtils.BANK_ADDRESS) String bankAddress,
            @Field(UrlUtils.BANK_NAME) String bankName,
            @Field(UrlUtils.BANK_BRANCH) String bankBranch,
            @Field(UrlUtils.BANK_CARDNO) String bankCardNo,
            @Field(UrlUtils.BANK_IDNUMBER) String idNumber,
            @Field(UrlUtils.USERNAME) String userName
    );

    //用户提现申请
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOWITHDRAWCASHURL)
    Observable<Result<HttpDataInfo>> doWithdrawCash(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.PHONE_SMSTYPE) String smsType,
            @Field(UrlUtils.DRAW_ORDERAMT) String orderAmt,
            @Field(UrlUtils.PHONE_PHONECODE) String phoneCode
    );

    //账户收支流水
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.ACCOUNTDETAILURL)
    Observable<Result<HttpDataInfo>> getUserAccountDetailPage(
            @Field(UrlUtils.DEVICETYPE) String deviceType,
            @Field(UrlUtils.OSVERSION) String osVersion,
            @Field(UrlUtils.APPVERSION) String appVersion,
            @Field(UrlUtils.SFID) String sfId,
            @Field(UrlUtils.WXQQ_CTYPE) String ctype,
            @Field(UrlUtils.WXQQ_CHANNEL) String channel,
            @Field(UrlUtils.USERID) String userId,
            @Field(UrlUtils.NEXTPAGE) String nextPage
    );

    //忘记密码
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RESETPASSWORDURL)
    Observable<Result<HttpDataInfo>> getResetPass(
            @Field(UrlUtils.PHONE) String phone,
            @Field(UrlUtils.CODE) String code,
            @Field(UrlUtils.PASSWORD) String password
    );

    //房间游戏记录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.ROOMGAMELIST)
    Observable<Result<HttpDataInfo>> getRoomGamelist(
            @Field(UrlUtils.ROOMID) String roomId
    );

    //版本信息
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.CHECKVERSION)
    Observable<Result<AppInfo>> checkVersion();

    //投币记录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETCOINPUSHERRECONDLIST)
    Observable<Result<CoinListBean>> getCoinRecord(@Field(UrlUtils.USERID) String userId);

    //用户当天记出币总数
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETCOINSUM)
    Observable<Result<HttpDataInfo>> getUserSumCoin(@Field(UrlUtils.USERID) String userId);

    //第三方登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.WXREGISTER)
    Observable<Result<HttpDataInfo>> wxRegister(@Field(UrlUtils.UID) String uid, @Field(UrlUtils.NAME) String name,
                                                @Field(UrlUtils.GENDER) String gender,@Field(UrlUtils.ICONURL) String profile_image_url,
                                                @Field(UrlUtils.REGCHANNEL) String regChannel, @Field(UrlUtils.CHANNELNUM) String channelNum);
    //分享
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.SHAREGAME)
    Observable<Result<Void>> shareGame(@Field(UrlUtils.USERID)String userId);

    //积分商城
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.INTEGRAL)
    Observable<Result<String>> getPointsMallUrl(@Field(UrlUtils.USERID) String userId);

    //积分任务
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.INTEGRALTASK)
    Observable<Result<String>> getPointsMallTask(@Field(UrlUtils.USERID)String userId);

    //现在支付
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.ORDERWXTRADE)
    Observable<NowPayBean<OrderBean>> getNowPayOrder(@Field(UrlUtils.USERID) String userId,
                                                   @Field(UrlUtils.AMOUNT) String amount,
                                                   @Field(UrlUtils.PAYCHANNELTYPE) String payChannelType,
                                                   @Field(UrlUtils.REGGOLD) String regGold,
                                                   @Field(UrlUtils.PAYOUTTYPE) String payOutType,
                                                   @Field(UrlUtils.PAYTYPE) String payType,
                                                   @Field(UrlUtils.WXQQ_CTYPE) String ctype,
                                                   @Field(UrlUtils.WXQQ_CHANNEL) String loginChannel);

    //现在微信支付
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.ORDERWXTRADENEW)
    Observable<NowPayBean<OrderBean>> getNowWXPayOrder(@Field(UrlUtils.USERID) String userId,
                                                       @Field(UrlUtils.PLAYID) String pid,
                                                       @Field(UrlUtils.PAYCHANNELTYPE) String payChannelType,
                                                       @Field(UrlUtils.APPVERSION) String appversion,
                                                        @Field(UrlUtils.PAYOUTTYPE) String payOutType,
                                                       @Field(UrlUtils.PAYTYPE) String payType,
                                                       @Field(UrlUtils.WXQQ_CTYPE) String ctype,
                                                       @Field(UrlUtils.WXQQ_CHANNEL) String loginChannel);
    //支付宝支付
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.ORDERAPALIYTRADENEW)
    Observable<Result<AlipayBean>> getApaliyPayOrder(@Field(UrlUtils.USERID) String userId,
                                                     @Field(UrlUtils.PLAYID) String pid,
                                                     @Field(UrlUtils.APPVERSION) String appversion,
                                                     @Field(UrlUtils.PAYOUTTYPE) String payOutType,
                                                     @Field(UrlUtils.PAYTYPE) String payType,
                                                     @Field(UrlUtils.WXQQ_CTYPE) String ctype,
                                                     @Field(UrlUtils.WXQQ_CHANNEL) String loginChannel);

    //查询CP游戏豆
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CPURL)
    Observable<JCResult> getCPGameBeans(@Field(JCUtils.OPT) String opt,
                                      @Field(JCUtils.AUTH) String auth);

    //CP游戏登录
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.CPGAMELOGINURL)
    Observable<Result<HttpDataInfo>> getCPGameLogin(@Field(UrlUtils.USERID) String userId);

    //金币兑换CP游戏豆
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.COINEXBEANSURL)
    Observable<Result<HttpDataInfo>> getCoinEXGoldenbean(@Field(UrlUtils.USERID) String userId,
                                                         @Field(JCUtils.BEANNUM) String beanNum);

    //CP游戏豆兑换金币
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.BEANEXGOLDURL)
    Observable<JCResult> getBeanEXGold(@Field("wwjuserid") String userId,
                                                   @Field("userid") String uid ,
                                                   @Field(UrlUtils.JD) String  jd);


    //金币商城
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GOLDMAILURL)
    Observable<Result<String>> getGoldMallUrl(@Field(UrlUtils.USERID) String userId);

    //金币兑换列表
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOREWARD)
    Observable<Result<String>> doReward(@Field(UrlUtils.USERID) String userId);

    //金币兑换接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.REWARDGOLDBEAN)
    Observable<Result<LoginRewardGoldBean>> getRewardInfo(@Field(UrlUtils.USERID) String userId);

    //点赞接口
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOSUPPORT)
    Observable<Result<SupportBean>> doSupport(@Field(UrlUtils.USERID)String userId);

    //娃娃墙
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOLLPICTURE)
    Observable<Result<PictureListBean>> getUserDollPicture(@Field(UrlUtils.USERID)String userId);

    //房间娃娃墙
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.DOLLTOYNUM)
    Observable<Result<ToyNumBean>> getDollToyNum(@Field(UrlUtils.ROOMID)String roomId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.USERLEVEL)
    Observable<Result<LevelBean>> getgetUserLevel(@Field(UrlUtils.USERID)String userId);

    //完善用户信息
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.UPDATEUSERINFO)
    Observable<Result<HttpDataInfo>> perfectInformation(@Field(UrlUtils.USERID) String userId,
                                                        @Field("nickname") String nickname,
                                                        @Field(UrlUtils.GENDER) String gender,
                                                        @Field(UrlUtils.AGE) int age,
                                                        @Field(UrlUtils.WXACCOUNT)String weixinnumber,
                                                        @Field(UrlUtils.QQACCOUNT)String qqnumber);

    //完善用户信息
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.RECURL)
    Observable<Result<String>> getRecUrl(@Field(UrlUtils.APPVERSION)String appVersion, @Field(UrlUtils.USERID)String userId);


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.LNVITATIONCODECONTROL_URL)
    Observable<Result<HttpDataInfo>> getLnvitationCodeControl();


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.SGAREREDPACKAGE)
    Observable<Result<Void>> shareRedPackage(@Field(UrlUtils.USERID)String userId, @Field(UrlUtils.USERPLAYNUM) String redGold, @Field(UrlUtils.USERPLAYNUMBER)String redNumber);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.SHOWREDPACKAGE)
    Observable<Result<RedPackageListBean>> showRedPackage(@Field(UrlUtils.USERID)String userId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETREDPACKAGE)
    Observable<Result<Void>> getRedPackage(@Field(UrlUtils.USERID)String userId, @Field(UrlUtils.REDUSERID)String redUserId, @Field(UrlUtils.REDID)String redId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETREDPACKAGEINFO)
    Observable<Result<RedInfoBean>> getRedPackdetail(@Field(UrlUtils.REDID)String redId);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETESNDREDPACKAGERECORD)
    Observable<Result<RedPackageBean>> getSendRedPackageInfo(@Field(UrlUtils.USERID)String userId, @Field(UrlUtils.TIME)String time);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(UrlUtils.GETRESIVEREDPACKAGERECORD)
    Observable<Result<RedPackageBean>> getResiveRedPackageInfo(@Field(UrlUtils.USERID)String userId, @Field(UrlUtils.TIME)String time);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET(UrlUtils.SLIDESHOW)
    Observable<Result<HttpDataInfo>> getSlideShowList();
}
