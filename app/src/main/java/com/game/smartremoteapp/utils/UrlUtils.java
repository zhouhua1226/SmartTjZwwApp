package com.game.smartremoteapp.utils;

/**
 * Created by zhouh on 2017/9/8.
 */
public class UrlUtils {
    //视频url
    public static final String VIDEO_ROOT_URL = "https://open.ys7.com/";
    public static final String VIDEO_GET_TOKEN = "api/lapp/token/get";
    //getToken
    public static final String APPKEY = "appKey";
    public static final String APPSECRET = "appSecret";

    public static final  String APP_WEIXIN_ID ="wx23d0856f453d5dd3" ;
    public static final  String APP_WEIXIN_KEY ="d3f03de007c2faf1fa690378e10cc7f7" ;


    public static final  String APP_QQ_ID ="1106675519" ;
    public static final  String APP_QQ_KEY ="lc3yvvSbTbCI3G5j" ;

    public static final String CHANNEL = "channel";

    public static final String CTYPE = "ctype";
    public static final String AMOUNT = "amount";
    public static final String ID = "id";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String PW = "pw";
    public static final String CODE = "code";
    public static final String SMSCODE = "smsCode";
    public static final String APKNAME = "apkName";

    public static final String WXACCOUNT = "wxaccount";
    public static final String QQACCOUNT = "qqaccount";
    public static final String AGE = "age";
    public static final String IMAGE ="image" ;
    public static final String FACEIMAGE = "base64Image";
    public static final String NICKNANME = "nickName";
    public static final String USERNAME = "userName";
    public static final String USEPAYMONEY = "money";
    public static final String USERPLAYNUM = "gold";
    public static final String DOLLNAME = "dollName";
    public static final String TIME = "time";
    public static final String STATE = "state";
    public static final String GUESSID="guessId";
    public static final String GUESSPRONUM="afterVoting";   //追投期数
    public static final String GUESSMULTIPLE="multiple";   //竞猜倍数

    public static final String MODEL ="MODEL";
    public static final String PAYTYPE = "payType";
    public static final String USERID = "userId";
    public static final String WAGER = "wager";
    public static final String GUESSKEY = "guessKey";
    public static final String PLAYBACK = "playBackId";
    public static final String DOLLID = "dollId";
    public static final String PLAYID = "pid";
    public static final String CARD_TYPE ="cardtype" ;
    //设置收货人信息接口字段
    public static final String CONSIGNEENAME="name";
    public static final String CONSIGNEEPHONE="phone";
    public static final String CONSIGNEEADDRESS="address";
    public static final String CONSIGNEEUSERID="userId";
    //设置发货接口字段
    public static final String SENDGOODSID="id";
    public static final String SENDGOODSNUM="number";
    public static final String SENDGOODSSHXX="consignee";
    public static final String SENDGOODSREMARK="remark";
    public static final String SENDGOODSUSERID="userId";
    public static final String SENDGOODSMODE="mode";
    public static final String SENDGOODSCOSTNUM="costNum";
    public static final String SENDGOODSLEVEL ="level" ;
    public static final String FLAG ="flag" ;
    //微信QQ登录接口
    public static final String WXQQ_UID="uid";
    public static final String WXQQ_ACCESSTOKEN="accessToken";
    public static final String WXQQ_NICKNAME="nickName";
    public static final String WXQQ_IMAGEURL="imageUrl";
    public static final String WXQQ_CTYPE="ctype";
    public static final String WXQQ_CHANNEL="channel";
    public static final String LOGIN_CTYPE="ASDK";
    public static final String LOGIN_CTYPE_ASDK="ASDK";
    public static final String LOGIN_CHANNEL="ANDROID";
    public static final String CHANNELNUM = "channelNum";
    public static final String UID ="uid" ;
    public static final String JD ="jd" ;
    public static final String NAME ="name" ;
    public static final String GENDER ="gender" ;
    public static final String ICONURL ="iconurl" ;
    public static final String REGCHANNEL ="regChannel" ;
    public static final String PAYCHANNELTYPE ="payChannelType" ;
    public static final String  CTYPE_WX_PAY_ANROID="现在微信ANDROID";
    public static final String  CTYPE_APLIAY_ANROID="支付宝ANDROID";

    public static final String USERPLAYNUMBER = "num";
    //ysdk支付金额字段
    public static final String WXQQ_AMOUNT="amount";
    //签到接口
    public static final String SIGNTYPE="signType";
    //分类
    public static final String NEXTPAGE = "nextPage";
    public static final String CURRENTTYPE = "currentType";
    //邀请码
    public static final String USERAWARDCODE = "awardCode";
    public static final String DEVICETYPE="deviceType";   //手机机型
    public static final String OSVERSION="osVersion";     //系统版本
    public static final String APPVERSION="appVersion";   //app版本
    public static final String SFID="sfId";                //设备标识
    //加盟
    public static final String PROMOTEORDER_PROID="proManageId";
    public static final String PROMOTEORDER_PAYTYPE="payType";
    public static final String PROMOTE_CODE="promoteCode";
    //获取短信验证码以及绑定手机号
    public static final String PHONE_SMSTYPE="smsType";
    public static final String PHONE_phoneNumber="phoneNumber";
    public static final String PHONE_PHONECODE="phoneCode";
    public static final String PHONE_SMS_TYPE_1000="1000";  //("注册短信码","1000"),
    public static final String PHONE_SMS_TYPE_2000="2000";  //("绑定手机号","2000"),
    public static final String PHONE_SMS_TYPE_3000="3000";  //("修改银行卡信息","3000"), //不要传入手机号
    public static final String PHONE_SMS_TYPE_4000="4000";  //("提现短信码","4000"); //不要传入手机号
    //银行卡信息绑定
    public static final String BANK_ADDRESS="bankAddress";
    public static final String BANK_NAME="bankName";
    public static final String BANK_BRANCH="bankBranch";
    public static final String BANK_CARDNO="bankCardNo";
    public static final String BANK_IDNUMBER="idNumber";
    //提现
    public static final String DRAW_ORDERAMT="orderAmt";
    //房间游戏记录
    public static final String ROOMID="roomId";
    public static final String REGGOLD = "regGold";
    public static final String PAYOUTTYPE = "payOutType";
    public static final String REDUSERID ="redUserId" ;
    public static final String REDID ="redId" ;
    public static final String ISHIT ="ishit" ;
    //  http://47.100.15.18:8080(阿里云测服)
    //http://111.231.139.61:18081(汤姆抓娃娃腾讯云)
    public static final String URL = "http://111.231.139.61:18081";
    public static final String URL_TEST= "http://47.100.15.18:8080";

    //APP图片拼接url
    public static final String APPPICTERURL="http://111.231.139.61:8888/";
    //http://115.159.58.231:8888/(壕鑫腾讯云)   http://47.100.15.18:8888/(阿里云测服)
    //http://111.231.139.61:8888/ (汤姆抓娃娃腾讯云)

    // 47.100.8.129(测试)   111.231.74.65 (汤姆抓娃娃)
    public static final String SOCKET_IP="111.231.74.65";
    public static final String SOCKET_IP_TEST="47.100.8.129";//123.206.120.46(壕鑫正式)

    //彩票url
    public final static String GAMEINSTRUCTIONURL = "http://111.231.139.61:18081/docs/20180828/index.html";   	//60.55.47.172:8000(彩票测服)  106.75.143.0(彩票正服)

    //彩票url
    public final static String CPURL = "http://106.75.143.0/ajax/AppGateway.ashx";   	//60.55.47.172:8000(彩票测服)  106.75.143.0(彩票正服)

    //金币兑换金豆  	//60.55.47.172:8000(彩票测服)  106.75.143.0(彩票正服)
    public static final String BEANEXGOLDURL="http://106.75.143.0/WebService/WwjRegister.asmx/JDExchangeJB";

    //
    public static final String ADVERTYURL="http://111.231.139.61:18081/jc51/index.html";

    public static final String SERVICEURL="http://chat10.live800.com/live800/chatClient/chatbox.jsp?companyID=924464&configID=234256&jid=9174122739";

    //login-password
    public static final String LOGINPASSWORD = "/pooh-web/app/sms/userPassLogin";
    // regiter--phone password code
    public static final String REGITER = "/pooh-web/app/sms/userRegister";

    //支付宝订单信息接口
    public static final String  TRADEORDERALIPAY= "/pooh-web/app/pay/getTradeOrderAlipay";

    //getSmsCode
    public static final String GETSMSCODE = "/pooh-web/app/sms/getRegSMSCode";//"http://controller.ngrok.cc/m/sms/getSMSCodeLogin";
    //login
    public static final String LOGIN = "/pooh-web/app/sms/getSMSCodeLogin";//"http://controller.ngrok.cc/m/sms/getRegSMSCode";
    //login without code
    public static final String LOGINWITHOUTCODE = "/pooh-web/app/sms/getDoll";
    //娃娃图url
    public static final String PICTUREURL = "/pooh-web/uploadFiles/DollImage/";
    //充值卡url
    public static final String PAYCARDTPURL = "/pooh-web/app/pay/getPaycard/";
    //退出登录
    public static final String LOGOUT= "/pooh-web/app/loginOut.do";
    //头像上传
    public static final String FACEIMAGEURL = "/pooh-web/api/user/updateUser";

    //修改昵称
    public static final String UserNickNameURL = "/faceImage";

    //修改用户名  11/21 13：10
    public static final String USERNAMEURL = "/pooh-web/api/user/updateUserName";

    //头像上传成功返回的http://106.75.142.42:8080/faceImage/15335756655.png
    public static final String USERFACEIMAGEURL = APPPICTERURL;//+ "faceImage/";

    //充值接口
    public static final String USERPAYURL = "/pooh-web/pay/balance";

    //消费接口
    public static final String USERPLAYURL ="/pooh-web/pay/costBalance";

    //listRank
    public static final String LISTRANKURL = "/pooh-web/app/rank/rankList";

    //视屏上传upload
    public static final String UPLOADURL ="/pooh-web/play/regPlayBack";

    //获取视频回放列表
    public static final String VIDEOBACKURL ="/pooh-web/api/play/getPlayRecord";

    //获取房间用户头像
    public static final String CTRLUSERIMAGE ="/pooh-web/api/getUser";

    //跑马灯l
    public static final String getUserList ="/pooh-web/api/play/getUserList";

    //下注接口
    public static final String BETSURL ="/pooh-web/app/betgame/bets";

    //围观群众分发游戏场次
    public static final String PLAYIDURL ="/pooh-web/app/getPlayId";

    //开始游戏分发场次
    public static final String CREATPLAYLISTURL ="/pooh-web/pay/creatPlayList";

    //获取下注人数对比
    public static final String GETPONDURL="/pooh-web/app/betgame/getPond";

    //发货接口
    public static final String SENDGOODSURL="/pooh-web/app/goods/sendGoods";

    //收货人信息设置
    public static final String CONSIGNEEURL="/pooh-web/app/goods/cnsignee";

    //兑换接口
    public static final String EXCHANGEURL="/pooh-web/app/goods/conversionGoods";

    //兑换列表接口
    public static final String EXCHANGELISTURL="/pooh-web/app/goods/getConList";

    //获取用户信息接口
    public static final String GETUSERDATEURL="/pooh-web/api/user/getUserInfo";    //参数：userId 获取用户信息

    //微信QQ登录创建用户接口
    public static final String YSDKLOGINURL="/pooh-web/app/tencentLogin";

    //YSDK支付创建订单接口
    public static final String YSDKPAYORDERURL="/pooh-web/app/pay/getTradeOrder";

    //YSDK版自动登录
    public static final String YSDKAUTHLOGINURL="/pooh-web/app/tencentAutoLogin";

    //获取充值卡列表
    public static final String PAYCARDLISTURL="/pooh-web/app/pay/getPaycard";

    //用户竞猜记录
    public static final String GETGUESSDETAIL="/pooh-web/app/betgame/getGuessDetailTop10";

    //金币流水接口
    public static final String CURRENTACCOUNTURL="/pooh-web/app/betgame/getPaymenlist";

    //房间列表接口
    public static final String DOLLLISTURL="/pooh-web/app/doll/getDollList";

    //签到接口
    public static final String USERSIGNURL="/pooh-web/app/sign/sign";

    //轮播接口
    public static final String BANNERURL="/pooh-web/app/runimg/getRunImage";

    //轮播新接口
    public static final String BANNERURLNEW="/pooh-web/app/runimg/getRunImageNew";

    //排行榜当前用户排名接口
    public static final String RANKLISTURL="/pooh-web/app/rank/rankSelfList";

    //物流订单
    public static final String LOGISTICSORDERURL="/pooh-web/app/goods/getLogistics";

    //分类列表种类
    public static final String GETTOYTYPE = "/pooh-web/app/doll/getAllToyTypeList";

    //分类娃娃机
    public static final String GETTOYSBYTYPE = "/pooh-web/app/doll/getDollPage";

    //查询邀请码
    public static final String USERAWARDCODEURL="/pooh-web/app/award/getUserAwardCode";

    //兑换邀请码
    public static final String DOAWARDBYUSERCODEURL="/pooh-web/app/award/doAwardByUserCode";

    //竞猜跑马灯
    public static final String GUESSERLASTTENURL="/pooh-web/app/betgame/getGuesserlast10";

    //竞猜排行榜接口
    public static final String RANKBETLISTURL="/pooh-web/app/rank/rankBetList";

    //新娃娃排行榜接口
    public static final String RANKDOLLLISTURL="/pooh-web/app/rank/rankAndSelfList";

    //竞猜今日排行榜接口
    public static final String RANKBETLISTURLTODAY="/pooh-web/app/rank/rankBetListToday";

    //新娃娃排行今日榜接口
    public static final String RANKDOLLLISTURLTODAY="/pooh-web/app/rank/rankAndSelfListToday";

    //订单生成接口
    public static final String CREATEORDER="/pooh-web/app/pay/getTradeOrder_new";

    //h5推广
    public static final String PROTMOTEFORH5="/pooh-web/app/pay/promoteForH5";

    //首页动态跳图
    public static final String RUNIMGIMAGE="/pooh-web/app/runimg/images/news";

    //自动登录接口
    public static final String AUTHLOGINURL="/pooh-web/app/sms/getDoll";

    //查询可推广加盟的权益
    public static final String GETPROMOMOTEMANAGE="/pooh-web/app/promomote/getpromomoteManage.do";

    //购买加盟   /pooh-web/app/pay/commitPromoteOrderToGold.do
    public static final String PROMOTEORDERTOGOLD="/pooh-web/app/pay/commitPromoteOrderToGold.do";

    //查询用户已购买的推广加盟
    public static final String GETUSERPROMOTEINF="/pooh-web/app/promomote/getUserPromoteInf.do";

    //兑换推广码
    public static final String COMMITUSERPROMOTECODE="/pooh-web/app/promomote/commitUserPromoteCode.do";

    //用户现金余额查询
    public static final String GETUSERACCBALCOUNT= "/pooh-web/app/account/getUserAccBalCount.do";

    // 用户推广收益明细
    public static final String GETUSERPROMOTELIST="/pooh-web/app/account/getUserPromoteList.do";

    //新查询用户信息接口    /pooh-web/app/common/getAppUserInf.do
    public static final String GETAPPUSERINFURL="/pooh-web/app/common/getAppUserInf.do";

    //新获取短信验证码  /pooh-web/app/common/getPhoneCode.do
    public static final String GETPHONECODEURL="/pooh-web/app/common/getPhoneCode.do";

    //用户绑定手机号  /pooh-web/app/common/editAppUserPhone.do
    public static final String EDITAPPUSERPHONE="/pooh-web/app/common/editAppUserPhone.do";

    //用户银行卡信息绑定  /pooh-web/app/common/regBankInf.do
    public static final String REGBANKINFURL="/pooh-web/app/common/regBankInf.do";

    //用户提现申请   /pooh-web/app/account/doWithdrawCash.do
    public static final String DOWITHDRAWCASHURL="/pooh-web/app/account/doWithdrawCash.do";

    //查询账户收支流水  /pooh-web/app/account/getUserAccountDetailPage.do
    public static final String ACCOUNTDETAILURL="/pooh-web/app/account/getUserAccountDetailPage.do";

    //找回密码    /pooh-web/app/sms/resetPassword
    public static final String RESETPASSWORDURL="/pooh-web/app/sms/resetPassword";

    //房间游戏记录  /pooh-web/api/play/getGamelist
    public static final String ROOMGAMELIST="/pooh-web/api/play/getGamelist";

    //版本信息  /pooh-web/app/version/checkVersion
    public static final String CHECKVERSION="/pooh-web/app/version/checkVersion";


    //推币机个人信息投币记录
    public static final String GETCOINPUSHERRECONDLIST ="/pooh-web/app/coinPusher/getCoinPusherRecondList";

    //用户当天记出币总数
    public static final String GETCOINSUM ="/pooh-web/app/coinPusher/getCoinSum" ;

    //第三方登录
    public static final String WXREGISTER = "/pooh-web/app/sms/wxRegister";

    //分享
    public static final String SHAREGAME= "/pooh-web/app/pointsMall/shareGame";

    //积分商城
    public static final String INTEGRAL = "/pooh-web/app/pointsMall/getPointsMallUrl";

    //金币商城
    public static final String GOLDMAILURL = "/pooh-web/app/goldMall/getGoldMallUrl";

    //积分任务
    public static final String INTEGRALTASK = "/pooh-web/app/pointsMall/getPointsMallTask";

    //微信订单信息
    public static final String ORDERWXTRADE = "/pooh-web/app/pay/getTradeOrderxdpay";

    //新微信订单信息
    public static final String ORDERWXTRADENEW = "/pooh-web/app/pay/getTradeOrderxdpayVer";

    //新支付宝信订单信息
    public static final String ORDERAPALIYTRADENEW = "/pooh-web/app/pay/getTradeOrderAlipayVer";

    //彩票游戏登录
    public static final String CPGAMELOGINURL="/pooh-web/app/Jc51H5Game/login";

    //金币兑换金豆
    public static final String COINEXBEANSURL="/pooh-web/app/Jc51H5Game/getGoldenbean";

    //金币兑换
    public static final String DOREWARD ="/pooh-web/app/loginReward/doReward";
    //金币兑换列表
    public static final String REWARDGOLDBEAN ="/pooh-web/app/loginReward/getRewardInfo";
    //点赞
    public static final String DOSUPPORT ="/pooh-web/app/loginReward/doSupport" ;
    //娃娃墙
    public static final String DOLLPICTURE ="/pooh-web/api/play/getUserDollPicture" ;

    //房间娃娃数
    public static final String DOLLTOYNUM = "/pooh-web/app/doll/getDollToyNum";

    //等级
    public static final String USERLEVEL = "/pooh-web/app/level/getUserLevel";


    public static final String UPDATEUSERINFO = "/pooh-web/api/user/updateUserInfo";

    public static final String RECURL ="/pooh-web/app/pay/getRecUrl" ;
    public static final String LNVITATIONCODECONTROL_URL="/pooh-web/app/version/versionDisplay";

    public static final String SGAREREDPACKAGE ="/pooh-web/app/redPackage/shareRedPackage";

    public static final String SHOWREDPACKAGE ="/pooh-web/app/redPackage/showRedPackage";

    public static final String GETREDPACKAGE ="/pooh-web/app/redPackage/getRedPackage";

    public static final String GETREDPACKAGEINFO ="/pooh-web/app/redPackage/getRedPackageInfo";

    public static final String GETESNDREDPACKAGERECORD ="/pooh-web/app/redPackage/showUserSendRedPackageInfo" ;

    public static final String GETRESIVEREDPACKAGERECORD ="/pooh-web/app/redPackage/showUserGetRedPackageInfo" ;

    public static final String SLIDESHOW ="/pooh-web/app/redPackage/slideShow" ;
}