package com.game.smartremoteapp.activity.ctrl.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartremoteapp.activity.home.BetRecordActivity;
import com.game.smartremoteapp.activity.home.NavigationPageActivity;
import com.game.smartremoteapp.activity.wechat.WeChatPayActivity;
import com.game.smartremoteapp.bean.AppUserBean;
import com.game.smartremoteapp.bean.GuessLastBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Marquee;
import com.game.smartremoteapp.bean.PondResponseBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.UserBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.CatchDollResultDialog;
import com.game.smartremoteapp.view.FillingCurrencyDialog;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.QuizInstrictionDialog;
import com.game.smartremoteapp.view.RoomMarqueeView;
import com.game.smartremoteapp.view.TimeCircleProgressView;
import com.game.smartremoteapp.view.VibratorView;
import com.gatz.netty.global.AppGlobal;
import com.gatz.netty.global.ConnectResultEvent;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iot.game.pooh.server.entity.json.MoveControlResponse;
import com.iot.game.pooh.server.entity.json.announce.GatewayPoohStatusMessage;
import com.iot.game.pooh.server.entity.json.announce.LotteryDrawAnnounceMessage;
import com.iot.game.pooh.server.entity.json.app.AppInRoomResponse;
import com.iot.game.pooh.server.entity.json.app.AppOutRoomResponse;
import com.iot.game.pooh.server.entity.json.enums.MoveType;
import com.iot.game.pooh.server.entity.json.enums.PoohAbnormalStatus;
import com.iot.game.pooh.server.entity.json.enums.ReturnCode;
import com.umeng.analytics.game.UMGameAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by zhouh on 2017/9/8. SomeCommits
 */
public class CtrlActivity extends Activity implements IctrlView {
    @BindView(R.id.realplay_sv)
    SurfaceView mRealPlaySv;
    @BindView(R.id.ctrl_gif_view)
    GifView ctrlGifView;
    @BindView(R.id.ctrl_fail_iv)
    ImageView ctrlFailIv;
    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.coin_tv)
    TextView coinTv;
    @BindView(R.id.recharge_button)
    ImageButton rechargeButton;
    @BindView(R.id.front_image)
    ImageView topImage;
    @BindView(R.id.back_image)
    ImageView belowImage;
    @BindView(R.id.left_image)
    ImageView leftImage;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.startgame_ll)
    LinearLayout startgameLl;
    @BindView(R.id.recharge_ll)
    LinearLayout rechargeLl;
    @BindView(R.id.catch_ll)
    RelativeLayout catchLl;
    @BindView(R.id.operation_rl)
    RelativeLayout operationRl;
    @BindView(R.id.doll_name)
    TextView dollNameTv;
    @BindView(R.id.player_counter_tv)
    TextView playerCounterIv;
    @BindView(R.id.player2_iv)
    ImageView playerSecondIv;
    @BindView(R.id.main_player_iv)
    ImageView playerMainIv;
    @BindView(R.id.player_name_tv)
    TextView playerNameTv;
    @BindView(R.id.ctrl_status_iv)
    ImageView ctrlStatusIv;
    @BindView(R.id.ctrl_time_progress_view)
    TimeCircleProgressView timeCircleProgressView;
    @BindView(R.id.ctrl_dollgold_tv)
    TextView ctrlDollgoldTv;
    @BindView(R.id.ctrl_change_camera_iv)
    ImageView ctrlChangeCameraIv;

    private static final String TAG = "CtrlActivity---";
    @BindView(R.id.ctrl_quiz_layout)
    RelativeLayout ctrlQuizLayout;
    @BindView(R.id.ctrl_instruction_image)
    ImageView ctrlInstructionImage;
    @BindView(R.id.ctrl_buttom_layout)
    RelativeLayout ctrlButtomLayout;
    @BindView(R.id.ctrl_betting_number_one)
    TextView ctrlBettingNumberOne;
    @BindView(R.id.ctrl_betting_number_two)
    TextView ctrlBettingNumberTwo;
    @BindView(R.id.ctrl_betting_number_layout)
    LinearLayout ctrlBettingNumberLayout;
    @BindView(R.id.ctrl_betting_winning)
    ImageButton ctrlBettingWinning;
    @BindView(R.id.ctrl_betting_fail)
    ImageButton ctrlBettingFail;
    @BindView(R.id.ctrl_confirm_layout)
    Button ctrlConfirmLayout;
    @BindView(R.id.ctrl_beting_layout)
    RelativeLayout ctrlBetingLayout;
    @BindView(R.id.player_layout)
    RelativeLayout playerLayout;
    @BindView(R.id.realplay_rl)
    RelativeLayout realplayRl;
    @BindView(R.id.money_image)
    ImageView moneyImage;
    @BindView(R.id.ctrl_betting_back_button)
    Button ctrlBettingBackButton;
    @BindView(R.id.startgame_text_imag)
    ImageView startgameTextImag;
    @BindView(R.id.ctrl_back_imag)
    ImageView ctrlBackImag;
    @BindView(R.id.ctrl_guessrecord_tv)
    TextView ctrlGuessrecordTv;
    @BindView(R.id.ctrl_betremark_tv)
    TextView ctrlBetremarkTv;
    @BindView(R.id.ctrl_betnum_five_tv)
    TextView ctrlBetnumFiveTv;
    @BindView(R.id.ctrl_betnum_six_tv)
    TextView ctrlBetnumSixTv;
    @BindView(R.id.ctrl_betnum_seven_tv)
    TextView ctrlBetnumSevenTv;
    @BindView(R.id.ctrl_betnum_eight_tv)
    TextView ctrlBetnumEightTv;
    @BindView(R.id.ctrl_betnum_nine_tv)
    TextView ctrlBetnumNineTv;
    @BindView(R.id.ctrl_betnum_zero_tv)
    TextView ctrlBetnumZeroTv;
    @BindView(R.id.ctrl_betnum_one_tv)
    TextView ctrlBetnumOneTv;
    @BindView(R.id.ctrl_betnum_two_tv)
    TextView ctrlBetnumTwoTv;
    @BindView(R.id.ctrl_betnum_three_tv)
    TextView ctrlBetnumThreeTv;
    @BindView(R.id.ctrl_betnum_four_tv)
    TextView ctrlBetnumFourTv;
    @BindView(R.id.ctrl_marqueeview)
    RoomMarqueeView ctrlMarqueeview;
    @BindView(R.id.ctrl_bet_tenflod_tv)
    TextView ctrlBetTenflodTv;
    @BindView(R.id.ctrl_bet_twentyfold_tv)
    TextView ctrlBetTwentyfoldTv;
    @BindView(R.id.ctrl_bet_fiftyfold_tv)
    TextView ctrlBetFiftyfoldTv;
    @BindView(R.id.ctrl_bet_hundredfold_tv)
    TextView ctrlBetHundredfoldTv;
    @BindView(R.id.ctrl_roomdetial_tv)
    TextView ctrlRoomdetialTv;
    @BindView(R.id.ctrl_nowtime_tv)
    TextView ctrlNowtimeTv;
    @BindView(R.id.ctrl_betpernum_tv)
    TextView ctrlBetpernumTv;
    @BindView(R.id.ctrl_bet_onepro_tv)
    TextView ctrlBetOneproTv;
    @BindView(R.id.ctrl_bet_fivepro_tv)
    TextView ctrlBetFiveproTv;
    @BindView(R.id.ctrl_bet_tenpro_tv)
    TextView ctrlBetTenproTv;
    @BindView(R.id.ctrl_bet_twentypro_tv)
    TextView ctrlBetTwentyproTv;
    @BindView(R.id.ctrl_betpernum_layout)
    RelativeLayout ctrlBetpernumLayout;


    private CtrlCompl ctrlCompl;
    private FillingCurrencyDialog fillingCurrencyDialog;

    private List<String> userInfos = new ArrayList<>();  //房屋内用户电话信息

    //2017/11/18 11：10 加入振动器
    public Vibrator vibrator; // 震动器
    private String dollName = "未知";
    private boolean isCurrentConnect = true;
    private String upTime;
    private String upFileName;
    private int money = 0;   //房间单次抓取金额
    private int betMoney;   //投注奖金
    private String state = "";
    private QuizInstrictionDialog quizInstrictionDialog;
    private String dollId;
    private String zt = "";
    //播放地址流
    private String playUrlMain = "";
    private String playUrlSecond = "";
    private String currentUrl;
    //用户操作和竞猜
    private boolean isStart = false;
    private boolean isLottery = false;
    private String periodsNum;
    private MediaPlayer mediaPlayer;
    private MediaPlayer btn_mediaPlayer;
    //显示的用户的name
    private String showName = "";
    private int prob;         //抓取概率
    private String reward = "";    //预计奖金
    private String showUserId = "";
    private int betFlodNum = 1;   //默认投注倍数1
    private List<TextView> betViewList;
    private List<TextView> betFoldList;
    private List<TextView> betProList;    //追投
    private List<Marquee> marquees = new ArrayList<>();
    private Handler handler = new Handler();
    private int betPro=0;   //追投期数   默认不追投

    static {
        System.loadLibrary("SmartPlayer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate();
        UMGameAgent.setDebugMode(true);   //设置输出运行时日志
        UMGameAgent.init(this);
        Glide.with(this).load(UserUtils.UserImage).asBitmap().
                transform(new GlideCircleTransform(this)).into(playerMainIv);
    }

    private int getLayoutId() {
        return R.layout.activity_ctrl;
    }

    private void afterCreate() {
        if (Utils.getIsOpenMusic(getApplicationContext())) {
            playBGMusic();   //播放房间背景音乐
        }

        LogUtils.loge("afterCreate", TAG);
        initView();
        initData();
        coinTv.setText("  " + UserUtils.UserBalance + " 充值");
        setVibrator();   //初始化振动器
        getGuesserlast10();
        handler.post(mRunnable);
    }

    protected void initView() {
        ButterKnife.bind(this);
        RxBus.get().register(this);
        LogUtils.loge("=====" + UserUtils.UserPhone, TAG);
        NettyUtils.sendRoomInCmd();
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlGifView.setEnabled(false);
        ctrlGifView.setMovieResource(R.raw.ctrl_video_loading);
        ctrlFailIv.setVisibility(View.GONE);
        if (Utils.connectStatus.equals(ConnectResultEvent.CONNECT_FAILURE)) {
            ctrlStatusIv.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        } else {
            ctrlStatusIv.setImageResource(R.drawable.point_green);
        }
        NettyUtils.pingRequest(); //判断连接
    }

    private void initData() {
        setBetView();
        ctrlCompl = new CtrlCompl(this, this);
        playUrlMain = getIntent().getStringExtra(Utils.TAG_URL_MASTER);
        playUrlSecond = getIntent().getStringExtra(Utils.TAG_URL_SECOND);
        dollName = getIntent().getStringExtra(Utils.TAG_ROOM_NAME);
        money = Integer.parseInt(getIntent().getStringExtra(Utils.TAG_DOLL_GOLD));
        dollId = getIntent().getStringExtra(Utils.TAG_DOLL_Id);
        prob = Integer.parseInt(getIntent().getStringExtra(Utils.TAG_ROOM_PROB));
        reward = getIntent().getStringExtra(Utils.TAG_ROOM_REWARD);
        //UserUtils.UserBetNum = YsdkUtils.loginResult.getData().getAppUser().getBET_NUM();
        if (!Utils.isEmpty(dollName)) {
            dollNameTv.setText(dollName);
        }
        betMoney = money * betFlodNum;
        ctrlDollgoldTv.setText(money + "/次");
        ctrlConfirmLayout.setText(betMoney + "/次");   //下注金额
        if (!Utils.isEmpty(reward)) {
            ctrlBetremarkTv.setText("预计奖金" + reward + "金币");
        } else {
            ctrlBetremarkTv.setText("预计奖金0金币");
        }
        playerNameTv.setText(UserUtils.NickName + "...");
        setStartMode(getIntent().getBooleanExtra(Utils.TAG_ROOM_STATUS, true));
        ctrlQuizLayout.setEnabled(false);
        currentUrl = playUrlMain;
        ctrlCompl.startPlayVideo(mRealPlaySv, currentUrl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtils.loge("onDestroy", TAG);
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.sendCmdCtrl(MoveType.CATCH);
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();
        ctrlCompl = null;
        RxBus.get().unregister(this);

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (btn_mediaPlayer != null && btn_mediaPlayer.isPlaying()) {
            btn_mediaPlayer.stop();
            btn_mediaPlayer.release();
            btn_mediaPlayer = null;
        }
        handler.removeCallbacks(mRunnable);
    }

    @Override
    public void getTime(int time) {
        timeCircleProgressView.setProgress(Utils.CATCH_TIME_OUT - time);
    }

    @Override
    public void getTimeFinish() {
        ctrlCompl.sendCmdCtrl(MoveType.CATCH);
        ctrlCompl.stopTimeCounter();
        timeCircleProgressView.setProgress(Utils.CATCH_TIME_OUT);
    }

    @Override
    public void getUserInfos(List<String> list, boolean is) {
        //当前房屋的人数
        userInfos = list;
        int counter = userInfos.size();
        if (counter > 0) {
            String s = counter + "人在线";
            playerCounterIv.setText(s);
            if (counter == 1) {
                //显示自己
                Glide.with(this).load(UserUtils.UserImage).asBitmap().
                        transform(new GlideCircleTransform(this)).into(playerSecondIv);
            } else {
                if (is) {
                    //显示另外一个人
                    for (int i = 0; i < counter; i++) {
                        if (!userInfos.get(i).equals(UserUtils.USER_ID)) {
                            showUserId = userInfos.get(i);
                            LogUtils.loge("显示观察者的userId::::" + showUserId, TAG);
                            getCtrlUserImage(showUserId);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getRecordErrCode(int code) {
        LogUtils.loge("录制视频失败::::::" + code, TAG);
    }

    @Override
    public void getRecordSuecss() {
        LogUtils.loge("录制视频完毕......", TAG);
    }

    @Override
    public void getRecordAttributetoNet(String time, String fileName) {
        LogUtils.loge("视频上传的时间::::" + time + "=====" + fileName, TAG);
        upTime = time;
        upFileName = fileName;
    }

    @Override
    public void getPlayerErcErrCode(int code) {
        LogUtils.loge("直播失败,错误码:::::" + code, TAG);
        uiHandler.sendEmptyMessage(0);
    }

    @Override
    public void getPlayerSucess() {
        LogUtils.loge("直播Sucess:::::", TAG);
        uiHandler.sendEmptyMessage(1);
    }

    @Override
    public void getVideoPlayConnect() {

    }

    @Override
    public void getVideoPlayStart() {

    }

    @Override
    public void getVideoStop() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        ctrlCompl.stopPlayVideo();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (btn_mediaPlayer != null && btn_mediaPlayer.isPlaying()) {
            btn_mediaPlayer.stop();
            btn_mediaPlayer.release();
            btn_mediaPlayer = null;
        }
        handler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.loge("onRestart", TAG);
        if (ctrlFailIv.getVisibility() == View.VISIBLE) {
            ctrlFailIv.setVisibility(View.GONE);
        }
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlCompl.startPlayVideo(mRealPlaySv, currentUrl);
        NettyUtils.pingRequest();
        handler.post(mRunnable);
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserDate(UserUtils.USER_ID);    //2秒后获取用户余额并更新UI
                }
            }, 2000);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ctrlGifView.setVisibility(View.GONE);
                    ctrlFailIv.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    ctrlGifView.setVisibility(View.GONE);
                    if (ctrlFailIv.getVisibility() == View.VISIBLE) {
                        ctrlFailIv.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.image_back, R.id.recharge_button, R.id.ctrl_back_imag,
            R.id.startgame_ll, R.id.ctrl_fail_iv, R.id.ctrl_quiz_layout,
            R.id.ctrl_instruction_image, R.id.ctrl_betting_winning, R.id.ctrl_betting_fail,
            R.id.ctrl_confirm_layout, R.id.ctrl_betting_back_button,
            R.id.ctrl_change_camera_iv, R.id.ctrl_guessrecord_tv, R.id.coin_tv,
            R.id.ctrl_betnum_zero_tv, R.id.ctrl_betnum_one_tv, R.id.ctrl_betnum_two_tv,
            R.id.ctrl_betnum_three_tv, R.id.ctrl_betnum_four_tv, R.id.ctrl_betnum_five_tv,
            R.id.ctrl_betnum_six_tv, R.id.ctrl_betnum_seven_tv, R.id.ctrl_betnum_eight_tv,
            R.id.ctrl_betnum_nine_tv, R.id.ctrl_bet_tenflod_tv, R.id.ctrl_bet_twentyfold_tv,
            R.id.ctrl_bet_fiftyfold_tv, R.id.ctrl_bet_hundredfold_tv, R.id.ctrl_roomdetial_tv,
            R.id.ctrl_betpernum_tv,R.id.ctrl_bet_onepro_tv,R.id.ctrl_bet_fivepro_tv,
            R.id.ctrl_bet_tenpro_tv,R.id.ctrl_bet_twentypro_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
            case R.id.ctrl_back_imag:
                finish();
                break;
            case R.id.recharge_button:
            case R.id.coin_tv:
                startActivity(new Intent(this, WeChatPayActivity.class));
                break;
            case R.id.startgame_ll:
                if (TextUtils.isEmpty(UserUtils.UserBalance)) {
                    getUserDate(UserUtils.USER_ID);
                    return;
                }
                //开始游戏按钮
                if (Integer.parseInt(UserUtils.UserBalance) >= money) {
                    if ((Utils.connectStatus.equals(ConnectResultEvent.CONNECT_SUCCESS))
                            && (isCurrentConnect)) {
                        ctrlCompl.sendCmdCtrl(MoveType.START);
                        coinTv.setText("  " + (Integer.parseInt(UserUtils.UserBalance) - money) + " 充值");
                        isStart = true;
                        upTime = Utils.getTime();
                    }
                    setVibratorTime(300, -1);
                    rechargeButton.setVisibility(View.GONE);
                } else {
                    MyToast.getToast(getApplicationContext(), "余额不足，请充值！").show();
                }
                break;
            case R.id.ctrl_fail_iv:
                ctrlFailIv.setVisibility(View.GONE);
                ctrlGifView.setVisibility(View.VISIBLE);
                break;
            case R.id.ctrl_quiz_layout:
                //竞猜
//                if (UserUtils.UserBetNum > 9) {
//                    MyToast.getToast(getApplicationContext(), "您今日猜中次数已达10次上限!").show();
//                    return;
//                }
                ctrlButtomLayout.setVisibility(View.GONE);
                ctrlBetingLayout.setVisibility(View.VISIBLE);
                getPond(periodsNum, dollId);
                ctrlBettingFail.setImageResource(R.drawable.ctrl_guess_unselect_bz);
                ctrlBettingWinning.setImageResource(R.drawable.ctrl_guess_unselect_z);
                ctrlBettingWinning.setEnabled(true);
                ctrlBettingFail.setEnabled(true);

                break;
            case R.id.ctrl_instruction_image:
                //说明
                startActivity(new Intent(this, NavigationPageActivity.class));
//                quizInstrictionDialog = new QuizInstrictionDialog(this, R.style.easy_dialog_style);
//                quizInstrictionDialog.show();
//                quizInstrictionDialog.setTitle("竞猜游戏说明");
//                quizInstrictionDialog.setContent(Utils.readAssetsTxt(this, "guessintroduce"));
                break;

            case R.id.ctrl_betting_winning:
                zt = "1";
                ctrlBettingFail.setImageResource(R.drawable.ctrl_guess_unselect_bz);
                ctrlBettingWinning.setImageResource(R.drawable.ctrl_guess_select_z);
                ctrlBettingFail.setEnabled(true);
                ctrlBettingWinning.setEnabled(false);
                //中
                break;
            case R.id.ctrl_betting_fail:
                zt = "0";
                ctrlBettingFail.setImageResource(R.drawable.ctrl_guess_select_bz);
                ctrlBettingWinning.setImageResource(R.drawable.ctrl_guess_unselect_z);
                ctrlBettingWinning.setEnabled(true);
                ctrlBettingFail.setEnabled(false);
                //不中
                break;
            case R.id.ctrl_confirm_layout:
                //下注
                ctrlBettingWinning.setEnabled(true);
                ctrlBettingWinning.setEnabled(true);
                if (TextUtils.isEmpty(UserUtils.UserBalance)) {
                    getUserDate(UserUtils.USER_ID);
                    return;
                }
                if (Integer.parseInt(UserUtils.UserBalance) >= betMoney) {
                    if (!zt.equals("") && !periodsNum.equals("")) {
                        getBets(UserUtils.USER_ID, Integer.valueOf(betMoney).intValue(), zt, periodsNum, dollId,betPro);
                        coinTv.setText("  " + (Integer.parseInt(UserUtils.UserBalance) - betMoney) + " 充值");
                        ctrlButtomLayout.setVisibility(View.VISIBLE);
                        ctrlBetingLayout.setVisibility(View.GONE);
                        ctrlQuizLayout.setEnabled(false);
                        moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
                        isLottery = true;
                        initBet();
                    } else {
                        if(zt.equals("")) {
                            MyToast.getToast(getApplicationContext(), "请下注！").show();
                        }else if(periodsNum.equals("")){
                            MyToast.getToast(getApplicationContext(), "期号为空，无法竞猜！").show();
                        }
                    }
                } else {
                    MyToast.getToast(getApplicationContext(), "请充值！").show();
                }
                break;
            case R.id.ctrl_betting_back_button:
                initBet();
                ctrlBetingLayout.setVisibility(View.GONE);
                ctrlButtomLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ctrl_change_camera_iv:
                currentUrl = currentUrl.equals(playUrlMain) ? playUrlSecond : playUrlMain;
                ctrlCompl.startPlaySwitchUrlVideo(currentUrl);
                break;
            case R.id.ctrl_guessrecord_tv:
                startActivity(new Intent(this, BetRecordActivity.class));
                break;
            case R.id.ctrl_betnum_zero_tv:
                zt = "0";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_one_tv:
                zt = "1";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_two_tv:
                zt = "2";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_three_tv:
                zt = "3";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_four_tv:
                zt = "4";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_five_tv:
                zt = "5";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_six_tv:
                zt = "6";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_seven_tv:
                zt = "7";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_eight_tv:
                zt = "8";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_betnum_nine_tv:
                zt = "9";
                setBetNumBg(zt);
                break;
            case R.id.ctrl_bet_tenflod_tv:
                if (betFlodNum == 10) {
                    ctrlBetTenflodTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetTenflodTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betFlodNum = 1;
                } else {
                    betFlodNum = 10;
                    setBetFoldBg(0);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_twentyfold_tv:
                if (betFlodNum == 20) {
                    ctrlBetTwentyfoldTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetTwentyfoldTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betFlodNum = 1;
                } else {
                    betFlodNum = 20;
                    setBetFoldBg(1);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_fiftyfold_tv:
                if (betFlodNum == 50) {
                    ctrlBetFiftyfoldTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetFiftyfoldTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betFlodNum = 1;
                } else {
                    betFlodNum = 50;
                    setBetFoldBg(2);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_hundredfold_tv:
                if (betFlodNum == 100) {
                    ctrlBetHundredfoldTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetHundredfoldTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betFlodNum = 1;
                } else {
                    betFlodNum = 100;
                    setBetFoldBg(3);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_roomdetial_tv:
                String url = getIntent().getStringExtra(Utils.TAG_ROOM_DOLLURL);
                showDetailDialog(url);
                break;
            case R.id.ctrl_betpernum_tv:
                //显示隐藏追投期数逻辑
                if(ctrlBetpernumLayout.getVisibility()==View.VISIBLE){
                    ctrlBetpernumLayout.setVisibility(View.GONE);
                    Drawable rightUp = getResources().getDrawable(R.drawable.ctrl_betpronum_up);
                    rightUp.setBounds(0, 0, rightUp.getMinimumWidth(), rightUp.getMinimumHeight());
                    ctrlBetpernumTv.setCompoundDrawables(null,null,rightUp,null);
                }else {
                    ctrlBetpernumLayout.setVisibility(View.VISIBLE);
                    Drawable rightDown = getResources().getDrawable(R.drawable.ctrl_betpronum_down);
                    rightDown.setBounds(0, 0, rightDown.getMinimumWidth(), rightDown.getMinimumHeight());
                    ctrlBetpernumTv.setCompoundDrawables(null,null,rightDown,null);
                }
                break;
            case R.id.ctrl_bet_onepro_tv:
                if(betPro==1){
                    ctrlBetOneproTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetOneproTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betPro=0;
                }else {
                    betPro = 1;
                    setBetProbg(0);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_fivepro_tv:
                if(betPro==5){
                    ctrlBetFiveproTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetFiveproTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betPro=0;
                }else {
                    betPro = 5;
                    setBetProbg(1);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_tenpro_tv:
                if(betPro==10){
                    ctrlBetTenproTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetTenproTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betPro=0;
                }else {
                    betPro = 10;
                    setBetProbg(2);
                }
                setBetRewardChange(betFlodNum);
                break;
            case R.id.ctrl_bet_twentypro_tv:
                if(betPro==20){
                    ctrlBetTwentyproTv.setTextColor(getResources().getColor(R.color.white));
                    ctrlBetTwentyproTv.setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
                    betPro=0;
                }else {
                    betPro = 20;
                    setBetProbg(3);
                }
                setBetRewardChange(betFlodNum);
                break;
            default:
                break;
        }
    }


    private void getWorkstation() {
        ctrlInstructionImage.setVisibility(View.GONE);
        ctrlQuizLayout.setVisibility(View.GONE);
        ctrlDollgoldTv.setVisibility(View.GONE);
        startgameLl.setVisibility(View.GONE);
        rechargeLl.setVisibility(View.GONE);
        catchLl.setVisibility(View.VISIBLE);
        operationRl.setVisibility(View.VISIBLE);
        ctrlRoomdetialTv.setVisibility(View.GONE);
        catchLl.setEnabled(true);
        ctrlCompl.startTimeCounter();
    }

    private void getStartstation() {
        ctrlInstructionImage.setVisibility(View.VISIBLE);
        ctrlQuizLayout.setVisibility(View.VISIBLE);
        ctrlDollgoldTv.setVisibility(View.VISIBLE);
        startgameLl.setVisibility(View.VISIBLE);
        rechargeLl.setVisibility(View.VISIBLE);
        catchLl.setVisibility(View.GONE);
        operationRl.setVisibility(View.GONE);
        ctrlRoomdetialTv.setVisibility(View.VISIBLE);
        catchLl.setEnabled(true);
    }

    private void getMoney() {
        fillingCurrencyDialog = new FillingCurrencyDialog(this, R.style.easy_dialog_style);
        fillingCurrencyDialog.show();
        fillingCurrencyDialog.setDialogClickListener(myDialogClick);
    }

    private FillingCurrencyDialog.MyDialogClick myDialogClick =
            new FillingCurrencyDialog.MyDialogClick() {
                @Override
                public void getMoney10(String money) {
                    Intent intent = new Intent(CtrlActivity.this, WeChatPayActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }

                @Override
                public void getMoney20(String money) {
                    Intent intent = new Intent(CtrlActivity.this, WeChatPayActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }

                @Override
                public void getMoney50(String money) {
                    Intent intent = new Intent(CtrlActivity.this, WeChatPayActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }

                @Override
                public void getMoney100(String money) {
                    Intent intent = new Intent(CtrlActivity.this, WeChatPayActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }
            };

    //摇杆操作
    @OnTouch({R.id.front_image, R.id.back_image, R.id.left_image, R.id.right_image, R.id.catch_ll})
    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        if ((!Utils.connectStatus.equals(ConnectResultEvent.CONNECT_SUCCESS))) {
            return false;
        }
        if (!isCurrentConnect) {
            return false;
        }
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.front_image:
                        setVibratorTime(3000, 1);
                        if (currentUrl.equals(playUrlMain)) {
                            ctrlCompl.sendCmdCtrl(MoveType.FRONT);
                        } else if (currentUrl.equals(playUrlSecond)) {
                            ctrlCompl.sendCmdCtrl(MoveType.LEFT);
                        } else {
                            ctrlCompl.sendCmdCtrl(MoveType.FRONT);
                        }
                        topImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_select_up_imag));
                        break;
                    case R.id.back_image:
                        setVibratorTime(3000, 1);
                        if (currentUrl.equals(playUrlMain)) {
                            ctrlCompl.sendCmdCtrl(MoveType.BACK);
                        } else if (currentUrl.equals(playUrlSecond)) {
                            ctrlCompl.sendCmdCtrl(MoveType.RIGHT);
                        } else {
                            ctrlCompl.sendCmdCtrl(MoveType.BACK);
                        }
                        belowImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_select_down_imag));
                        break;
                    case R.id.left_image:
                        setVibratorTime(3000, 1);
                        if (currentUrl.equals(playUrlMain)) {
                            ctrlCompl.sendCmdCtrl(MoveType.LEFT);
                        } else if (currentUrl.equals(playUrlSecond)) {
                            ctrlCompl.sendCmdCtrl(MoveType.BACK);
                        } else {
                            ctrlCompl.sendCmdCtrl(MoveType.LEFT);
                        }
                        leftImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_select_left_imag));
                        break;
                    case R.id.right_image:
                        setVibratorTime(3000, 1);
                        if (currentUrl.equals(playUrlMain)) {
                            ctrlCompl.sendCmdCtrl(MoveType.RIGHT);
                        } else if (currentUrl.equals(playUrlSecond)) {
                            ctrlCompl.sendCmdCtrl(MoveType.FRONT);
                        } else {
                            ctrlCompl.sendCmdCtrl(MoveType.RIGHT);
                        }
                        rightImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_select_right_imag));
                        break;
                    case R.id.catch_ll:
                        setVibratorTime(300, -1);
                        ctrlCompl.sendCmdCtrl(MoveType.CATCH);
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (view.getId()) {
                    case R.id.front_image:
                        if (vibrator != null)
                            vibrator.cancel();
                        ctrlCompl.sendCmdCtrl(MoveType.STOP);
                        topImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_up_imag));
                        break;
                    case R.id.back_image:
                        if (vibrator != null)
                            vibrator.cancel();
                        ctrlCompl.sendCmdCtrl(MoveType.STOP);
                        belowImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_down_imag));
                        break;
                    case R.id.left_image:
                        if (vibrator != null)
                            vibrator.cancel();
                        ctrlCompl.sendCmdCtrl(MoveType.STOP);
                        leftImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_left_imag));
                        break;
                    case R.id.right_image:
                        if (vibrator != null)
                            vibrator.cancel();
                        ctrlCompl.sendCmdCtrl(MoveType.STOP);
                        rightImage.setImageDrawable(getResources().getDrawable(R.drawable.ctrl_right_imag));
                        break;
                    case R.id.catch_ll:
                        ctrlCompl.stopTimeCounter();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    //初始化振动器   2017/11/18 11:11
    private void setVibrator() {
        vibrator = VibratorView.getVibrator(getApplicationContext());
    }

    //设置震动时间
    private void setVibratorTime(long time, int replay) {
        if (null != vibrator)
            vibrator.vibrate(new long[]{0, time, 0, 0}, replay);
    }

    private void setStartMode(boolean isFree) {
        startgameLl.setEnabled(isFree);
        if (isFree) {
            startgameLl.setBackgroundResource(R.drawable.ctrl_startgame_button);
            startgameTextImag.setImageResource(R.drawable.begin_game_text);
//            moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
//            ctrlQuizLayout.setEnabled(false);
            ctrlQuizLayout.setVisibility(View.VISIBLE);         //竞彩布局
            betChangeView(false);
            return;
        }
        startgameLl.setBackgroundResource(R.drawable.ctrl_startgame_button);
        startgameTextImag.setImageResource(R.drawable.ctrl_begin_loading);
        if (userInfos.size() > 1) {
            betChangeView(true);
        } else {
            betChangeView(false);
        }
    }

    private void betChangeView(boolean isBet) {
//        if (prob < 100) {
//            moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
//            ctrlQuizLayout.setEnabled(false);
//            return;
//        }
        if (isBet) {
            if (!isLottery) {
                moneyImage.setImageResource(R.drawable.ctrl_bet_button);
                ctrlQuizLayout.setEnabled(true);
            }
        } else {
            moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
            ctrlQuizLayout.setEnabled(false);
        }
    }

    /**
     * #########################  竞猜页面逻辑  ################################
     */
    private void setBetView() {
        betViewList = new ArrayList<>();
        betViewList.add(ctrlBetnumZeroTv);
        betViewList.add(ctrlBetnumOneTv);
        betViewList.add(ctrlBetnumTwoTv);
        betViewList.add(ctrlBetnumThreeTv);
        betViewList.add(ctrlBetnumFourTv);
        betViewList.add(ctrlBetnumFiveTv);
        betViewList.add(ctrlBetnumSixTv);
        betViewList.add(ctrlBetnumSevenTv);
        betViewList.add(ctrlBetnumEightTv);
        betViewList.add(ctrlBetnumNineTv);

        betFoldList = new ArrayList<>();
        betFoldList.add(ctrlBetTenflodTv);
        betFoldList.add(ctrlBetTwentyfoldTv);
        betFoldList.add(ctrlBetFiftyfoldTv);
        betFoldList.add(ctrlBetHundredfoldTv);

        betProList=new ArrayList<>();
        betProList.add(ctrlBetOneproTv);
        betProList.add(ctrlBetFiveproTv);
        betProList.add(ctrlBetTenproTv);
        betProList.add(ctrlBetTwentyproTv);
    }

    //竞猜投注UI变动
    private void setBetNumBg(String position) {
        if (position.equals("")) {
            for (int j = 0; j < betViewList.size(); j++) {
                betViewList.get(j).setTextColor(getResources().getColor(R.color.white));
                betViewList.get(j).setBackgroundResource(R.drawable.ctrl_betnum_unselect);
            }
            return;
        }
        int po = Integer.parseInt(position);
        for (int i = 0; i < betViewList.size(); i++) {
            if (po == i) {
                betViewList.get(i).setTextColor(getResources().getColor(R.color.ctrl_textcolor));
                betViewList.get(i).setBackgroundResource(R.drawable.ctrl_betnum_select);
            } else {
                betViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                betViewList.get(i).setBackgroundResource(R.drawable.ctrl_betnum_unselect);
            }
        }
    }

    //竞猜倍投UI变动
    private void setBetFoldBg(int betNum) {
        for (int i = 0; i < betFoldList.size(); i++) {
            if (i == betNum) {
                betFoldList.get(i).setTextColor(getResources().getColor(R.color.ctrl_textcolor));
                betFoldList.get(i).setBackgroundResource(R.drawable.ctrl_guess_betnum_bg);
            } else {
                betFoldList.get(i).setTextColor(getResources().getColor(R.color.white));
                betFoldList.get(i).setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
            }
        }
    }

    //竞猜追投UI变动
    private void setBetProbg(int proNum){
        for (int i = 0; i < betProList.size(); i++) {
            if (i == proNum) {
                betProList.get(i).setTextColor(getResources().getColor(R.color.ctrl_textcolor));
                betProList.get(i).setBackgroundResource(R.drawable.ctrl_guess_betnum_bg);
            } else {
                betProList.get(i).setTextColor(getResources().getColor(R.color.white));
                betProList.get(i).setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
            }
        }
    }


    //竞猜奖金和金额UI变动
    private void setBetRewardChange(int num) {
        int re = Integer.parseInt(reward) * num;
        int reward=re*(betPro+1);
        betMoney = money * num * (betPro+1);
        int showBetMoney= money*num;
        ctrlBetremarkTv.setText("预计奖金" + re + "金币");
        ctrlConfirmLayout.setText(showBetMoney + "/次");
    }

    //竞猜归零
    private void initBet() {
        zt = "";
        betFlodNum = 5;
        betPro=0;
        setBetNumBg(zt);
        setBetRewardChange(betFlodNum);
        ctrlBetpernumLayout.setVisibility(View.GONE);
        Drawable rightUp = getResources().getDrawable(R.drawable.ctrl_betpronum_up);
        rightUp.setBounds(0, 0, rightUp.getMinimumWidth(), rightUp.getMinimumHeight());
        ctrlBetpernumTv.setCompoundDrawables(null,null,rightUp,null);
        for (int i = 0; i < betFoldList.size(); i++) {
            betFoldList.get(i).setTextColor(getResources().getColor(R.color.white));
            betFoldList.get(i).setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
        }
        for(int i=0;i<betProList.size();i++){
            betProList.get(i).setTextColor(getResources().getColor(R.color.white));
            betProList.get(i).setBackgroundResource(R.drawable.ctrl_guess_unbetnum_bg);
        }
    }


    /**  #########################  竞彩页面逻辑  ################################  */

    /**************************************************
     * 控制状态区
     *****************************************************/
    //控制区与状态区
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_ROOM_IN),
            @Tag(Utils.TAG_ROOM_OUT),
            @Tag(Utils.TAG_MOVE_FAILE),
            @Tag(Utils.TAG_MOVE_RESPONSE)})
    public void getKnxConnectStates(Object response) {
        if (response instanceof MoveControlResponse) {
            MoveControlResponse moveControlResponse = (MoveControlResponse) response;
            if ((moveControlResponse.getSeq() == -2)) {
                if (moveControlResponse.getMoveType() == null) {
                    return;
                }
                if (moveControlResponse.getMoveType().name().equals(MoveType.START.name())) {
                    setStartMode(false);
                    //期号为空 竞猜流局
                    if (moveControlResponse.getRpcSuccess()) {
                        //获取期号
                        periodsNum = moveControlResponse.getPeriodsNum();
                    } else {
                        periodsNum = "";
                        moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
                        ctrlQuizLayout.setEnabled(false);
                    }
                } else if (moveControlResponse.getMoveType().name().equals(MoveType.CATCH.name())) {
                    //TODO 其他用户下爪了 观看者
                    LogUtils.loge("观看者观察到下爪了......", TAG);
                    moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
                    ctrlQuizLayout.setEnabled(false);
                    ctrlBetingLayout.setVisibility(View.GONE);
                    ctrlButtomLayout.setVisibility(View.VISIBLE);
                    initBet();
                }
            } else {
                if (moveControlResponse.getReturnCode() != ReturnCode.SUCCESS) {
                    return;
                }
                //本人点击start
                if (moveControlResponse.getMoveType().name()
                        .equals(MoveType.START.name())) {
                    getWorkstation();
                    ctrlCompl.startRecordVideo();
                    //TODO 竞猜按钮隐藏
                    periodsNum = moveControlResponse.getPeriodsNum();//为满足后台需求，这里也需要获取一下期数
                    ctrlQuizLayout.setVisibility(View.INVISIBLE);
                } else if (moveControlResponse.getMoveType().name()
                        .equals(MoveType.CATCH.name())) {
                    //TODO 本人点击下爪了 下爪成功
                    LogUtils.loge("本人点击下爪成功......", TAG);
                    moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
                    ctrlQuizLayout.setEnabled(false);
                    catchLl.setEnabled(false);
                }
            }
        } else if (response instanceof String) {
            Utils.showLogE(TAG, "move faile....");
        } else if (response instanceof AppOutRoomResponse) {
            AppOutRoomResponse appOutRoomResponse = (AppOutRoomResponse) response;
            Utils.showLogE(TAG, appOutRoomResponse.toString());
            long seq = appOutRoomResponse.getSeq();
            if (seq == -2) {
                userInfos.remove(appOutRoomResponse.getUserId());
                if (appOutRoomResponse.getUserId().equals(showUserId)) {
                    getUserInfos(userInfos, true);
                } else {
                    getUserInfos(userInfos, false);
                }
            }
        } else if (response instanceof AppInRoomResponse) {
            AppInRoomResponse appInRoomResponse = (AppInRoomResponse) response;
            Utils.showLogE(TAG, "=====" + appInRoomResponse.toString());
            String allUsers = appInRoomResponse.getAllUserInRoom(); //返回的UserId
            Boolean free = appInRoomResponse.getFree();
            long seq = appInRoomResponse.getSeq();
            if ((seq != -2) && (!Utils.isEmpty(allUsers))) {
                //TODO  我本人进来了
                ctrlCompl.sendGetUserInfos(allUsers, true);
                //是否能点击开始
                startgameLl.setEnabled(free);
                if (free) {
                    startgameLl.setBackgroundResource(R.drawable.ctrl_startgame_button);
                    startgameTextImag.setImageResource(R.drawable.begin_game_text);
                } else {
                    startgameLl.setBackgroundResource(R.drawable.ctrl_startgame_button);
                    startgameTextImag.setImageResource(R.drawable.ctrl_begin_loading);
                }
                ctrlQuizLayout.setVisibility(View.VISIBLE);
                moneyImage.setImageResource(R.drawable.ctrl_unbet_button);
                ctrlQuizLayout.setEnabled(false);
            } else {
                boolean is = false;
                if (userInfos.size() == 1) {
                    is = true;
                }
                userInfos.add(appInRoomResponse.getUserId());
                getUserInfos(userInfos, is);
            }
        }
    }

    //监控网关区
    //TODO 网关重连过后 需要前端主动去获取一次网关的状态来最终判断网关是否存在!!!
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_CONNECT_ERR),
            @Tag(Utils.TAG_CONNECT_SUCESS)})
    public void getConnectStates(String state) {
        if (state.equals(Utils.TAG_CONNECT_ERR)) {
            Utils.showLogE(TAG, "TAG_CONNECT_ERR");
            ctrlStatusIv.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        } else if (state.equals(Utils.TAG_CONNECT_SUCESS)) {
            Utils.showLogE(TAG, "TAG_CONNECT_SUCESS");
            ctrlStatusIv.setImageResource(R.drawable.point_green);
            NettyUtils.sendRoomInCmd();
            //TODO 后续修改获取网关状态接口
            NettyUtils.sendGetDeviceStatesCmd();
            isCurrentConnect = true;
        }
    }

    //监控单个网关连接区
    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAY_SINGLE_DISCONNECT)})
    public void getSingleGatwayDisConnect(String id) {
        Utils.showLogE(TAG, "getSingleGatwayDisConnect id" + id);
        if (id.equals(AppGlobal.getInstance().getUserInfo().getRoomid())) {
            ctrlStatusIv.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAY_SINGLE_CONNECT)})
    public void getSingleGatwayConnect(String id) {
        LogUtils.loge("getSingleGatwayDisConnect id" + id, TAG);
        if (id.equals(AppGlobal.getInstance().getUserInfo().getRoomid())) {
            ctrlStatusIv.setImageResource(R.drawable.point_green);
            isCurrentConnect = true;
        }
    }

    //设备故障
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Utils.TAG_DEVICE_ERR)})
    public void getDeviceErr(Object object) {
        if (object instanceof GatewayPoohStatusMessage) {
            //TODO 主板没有返回数据
            GatewayPoohStatusMessage message = (GatewayPoohStatusMessage) object;
            LogUtils.loge("主板没有返回数据" + message.toString(), TAG);
        } else if (object instanceof PoohAbnormalStatus) {
            //TODO 主板报错
            PoohAbnormalStatus status = (PoohAbnormalStatus) object;
            LogUtils.loge("主板报错 错误代码:::" + status.getValue(), TAG);
            //TODO 主板异常  UI返回用户金额
            if (isStart) {
                //TODO 返回玩家金额
                getUserDate(UserUtils.USER_ID);   //获取用户余额
            } else {
                //TODO 返回竞猜金额 如果用户竞猜
                if (isLottery) {
                    getUserDate(UserUtils.USER_ID);    //获取用户余额
                }
            }
            isStart = false;
            isLottery = false;
        }
    }

    //设备正常返回
    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_DEVICE_FREE)})
    public void getDeviceFree(GatewayPoohStatusMessage message) {
        String roomId = message.getRoomId();
        int number = message.getGifinumber();
        Utils.showLogE("getDeviceFree::::::" + roomId + "======" + number, TAG);
        if (roomId.equals(AppGlobal.getInstance().getUserInfo().getRoomid())) {
            getStartstation();
            setStartMode(true);
            getUserDate(UserUtils.USER_ID);    //再次获取用户余额并更新UI
            ctrlCompl.stopRecordView(); //录制完毕
            if (isStart) {
                if (number != 0) {
                    upFileName = "";
                    state = "1";
                    LogUtils.loge("抓取成功！", TAG);
                    updataTime(upTime, state);   //抓到娃娃  上传给后台
                    if (Utils.getIsOpenMusic(getApplicationContext())) {
                        playBtnMusic(R.raw.catch_success_music);
                    }
                    setCatchResultDialog(true);
                } else {
                    //删除本地视频
                    state = "0";
                    LogUtils.loge("抓取失败！", TAG);
                    if (Utils.getIsOpenMusic(getApplicationContext())) {
                        playBtnMusic(R.raw.catch_fail_music);
                    }
                    setCatchResultDialog(false);
                }
            }
            isStart = false;  //标志复位
            isLottery = false;
            upTime = "";
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_LOTTERY_DRAW)})
    public void getConnectStates(LotteryDrawAnnounceMessage message) {
        List<String> nickNameList = message.getBingoNickNameList();
        if (nickNameList != null) {
            StringBuffer sBuffer = new StringBuffer("恭喜:");
            int count = nickNameList.size();
            if (count > 3) {
                sBuffer.append(nickNameList.get(0) + "," + nickNameList.get(1) + "," + nickNameList.get(2));
            } else {
                for (String name : nickNameList) {
                    sBuffer.append(name);
                    sBuffer.append(",");
                }
                sBuffer.deleteCharAt(sBuffer.length() - 1);
            }
            sBuffer.append("猜中");
            MyToast.getToast(getApplicationContext(), sBuffer.toString()).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    /*************************************************
     * 网络请求区
     ***************************************************/
    //下注接口
    private void getBets(String userID, Integer wager, String guessKey, String guessId,
                         String dollID,int afterVoting) {
        HttpManager.getInstance().getBets(userID, wager, guessKey, guessId, dollID,afterVoting, new RequestSubscriber<Result<AppUserBean>>() {
            @Override
            public void _onSuccess(Result<AppUserBean> appUserBeanResult) {
                if (appUserBeanResult.getData().getAppUser() != null) {
                    String balance = appUserBeanResult.getData().getAppUser().getBALANCE();
                    if (TextUtils.isEmpty(balance)) {
                        coinTv.setText("  " + balance + " 充值");
                        UserUtils.UserBalance = balance;

                    }
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    //获取下注人数
    private void getPond(String playId, String dollId) {

        HttpManager.getInstance().getPond(playId, dollId, new RequestSubscriber<Result<PondResponseBean>>() {
            @Override
            public void _onSuccess(Result<PondResponseBean> loginInfoResult) {
                if (loginInfoResult.getData().getPond() != null) {
                    ctrlBettingNumberOne.setText(loginInfoResult.getData().getPond().getGUESS_Y() + "");
                    ctrlBettingNumberTwo.setText(loginInfoResult.getData().getPond().getGUESS_N() + "");
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    public void getCtrlUserImage(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode() == 0) {
                    UserBean bean = httpDataInfoResult.getData().getAppUser();
                    if ((bean != null) && (!Utils.isEmpty(bean.getIMAGE_URL()))) {
                        String showImage = UrlUtils.USERFACEIMAGEURL + bean.getIMAGE_URL();
                        Glide.with(getApplicationContext()).load(showImage)
                                .asBitmap().transform(new GlideCircleTransform(CtrlActivity.this)).into(playerSecondIv);
                    } else {
                        Glide.with(getApplicationContext()).load(R.drawable.ctrl_default_user_bg)
                                .asBitmap().transform(new GlideCircleTransform(CtrlActivity.this)).into(playerSecondIv);
                    }
                } else {
                    Glide.with(getApplicationContext()).load(R.drawable.ctrl_default_user_bg)
                            .asBitmap().transform(new GlideCircleTransform(CtrlActivity.this)).into(playerSecondIv);
                }
            }

            @Override
            public void _onError(Throwable e) {
                Glide.with(getApplicationContext()).load(R.drawable.ctrl_default_user_bg)
                        .asBitmap().transform(new GlideCircleTransform(CtrlActivity.this)).into(playerSecondIv);
            }
        });
    }

    private void updataTime(String time, String state) {
        LogUtils.loge("<<<<<<<<<<<<<", "userId2=" + UserUtils.USER_ID);
        HttpManager.getInstance().getRegPlayBack(time, UserUtils.USER_ID, state, dollId, periodsNum, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                LogUtils.loge("游戏记录上传结果=" + loginInfoResult.getMsg(), TAG);
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    //获取用户信息接口
    private void getUserDate(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG, "获取结果=" + loginInfoResult.getMsg());
                if (loginInfoResult.getMsg().equals("success")) {
                    UserBean bean = loginInfoResult.getData().getAppUser();
                    if (bean != null) {
                        String balance = bean.getBALANCE();
                        //UserUtils.UserBetNum = bean.getBET_NUM();
                        if (!TextUtils.isEmpty(balance)) {
                            coinTv.setText("  " + balance + " 充值");
                            UserUtils.UserBalance = balance;
                        }
//                        String showImage = UrlUtils.USERFACEIMAGEURL + bean.getIMAGE_URL();
//                        Glide.with(getApplicationContext()).load(showImage)
//                                .asBitmap().transform(new GlideCircleTransform(CtrlActivity.this)).into(playerSecondIv);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void getGuesserlast10() {
        HttpManager.getInstance().getGuesserlast10(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    HttpDataInfo httpDataInfo = httpDataInfoResult.getData();
                    if (httpDataInfo != null) {
                        List<GuessLastBean> list = httpDataInfo.getPondList();
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                Marquee marquee = new Marquee();
                                String nickname = list.get(i).getGUESSER_NAME();
                                if (nickname.length() > 12) {
                                    nickname.substring(0, 12);
                                }
                                String s = "恭喜" + "<font color='#fcf005'>" + nickname + "</font>"
                                        + "...在第" + list.get(i).getGUESS_ID() + "期成功猜中";
                                marquee.setTitle(s);
                                marquees.add(marquee);
                            }
                            ctrlMarqueeview.setImage(true);
                            ctrlMarqueeview.startWithList(marquees);
                        }
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }


    private void playBGMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.catchroom_bgm);
        // 设置音频流的类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        LogUtils.loge("房间背景音乐播放成功" + mediaPlayer.isPlaying(), TAG);
    }

    private void playBtnMusic(int file) {
        btn_mediaPlayer = MediaPlayer.create(this, file);
        btn_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btn_mediaPlayer.start();
    }

    private void setCatchResultDialog(boolean result) {
        final CatchDollResultDialog catchDollResultDialog = new CatchDollResultDialog(this, R.style.activitystyle);
        catchDollResultDialog.setCancelable(false);
        catchDollResultDialog.show();
        catchDollResultDialog.setStartAnimation();
        if (result) {
            catchDollResultDialog.setTitle("恭喜您！");
            catchDollResultDialog.setContent("本次抓娃娃成功啦。");
            catchDollResultDialog.setBackground(R.drawable.catchdialog_success_bg);
        } else {
            catchDollResultDialog.setTitle("太可惜了！");
            catchDollResultDialog.setContent("本次抓娃娃失败啦。");
            catchDollResultDialog.setBackground(R.drawable.catchdialog_fail_bg);
        }
        catchDollResultDialog.setDialogResultListener(new CatchDollResultDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if (resultCode == 1) {
                    //MyToast.getToast(getApplicationContext(), "二次抢抓功能还在完善中！").show();
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (catchDollResultDialog.isShowing()) {
                    Context context = ((ContextWrapper) catchDollResultDialog.getContext()).getBaseContext();
                    if (context instanceof Activity) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                            catchDollResultDialog.dismiss();
                    } else {
                        catchDollResultDialog.dismiss();
                    }
                }
            }
        }, 3000);
    }

    private void showDetailDialog(String url) {
        View view = getLayoutInflater().inflate(R.layout.roomdetail_dialog, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        final PopupWindow mPop = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.showAtLocation(realplayRl, Gravity.CENTER, 0, 0);//在屏幕居中，无偏移
        ImageView imageView = (ImageView) view.findViewById(R.id.roomdetail_dialog_iv);
        Glide.with(this)
                .load(UrlUtils.APPPICTERURL + url)
                .dontAnimate()
                .into(imageView);
        // 重写onKeyListener
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    //房间读秒
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ctrlNowtimeTv.setText(getTime());
            //每1毫秒重启一下线程
            handler.postDelayed(mRunnable, 1);
        }
    };

    //获得当前时间
    public String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
        String millisecond = String.valueOf(c.get(Calendar.MILLISECOND));  //毫秒
        return mHour + ":" + mMinute + ":" + mSecond + ":" + millisecond;
        //return mYear + "." + mMonth + "." + mDay+" "+mHour+":"+mMinute+":"+mSecond+":"+millisecond;
    }

}
