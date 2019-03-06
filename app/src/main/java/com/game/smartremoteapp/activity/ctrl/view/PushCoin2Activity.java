package com.game.smartremoteapp.activity.ctrl.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartremoteapp.activity.home.GameInstrcutionActivity;
import com.game.smartremoteapp.activity.home.RechargeActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.UserBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.CatchDollResultDialog;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.gatz.netty.global.ConnectResultEvent;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iot.game.pooh.server.entity.json.Coin2ControlResponse;
import com.iot.game.pooh.server.entity.json.app.AppInRoomResponse;
import com.iot.game.pooh.server.entity.json.app.AppOutRoomResponse;
import com.iot.game.pooh.server.entity.json.enums.CoinStatusType;
import com.iot.game.pooh.server.entity.json.enums.ReturnCode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PushCoin2Activity extends Activity implements IctrlView{
    @BindView(R.id.coin2_play_video_sv)
    SurfaceView mPlaySv;

    @BindView(R.id.player_iv)
    ImageView player_iv;
    @BindView(R.id.player_name_tv)
    TextView player_name;
    @BindView(R.id.tv_room_peoples)
    TextView room_peoples;

    @BindView(R.id.startgame_ll)
    LinearLayout startBtn;
    @BindView(R.id.ctrl_dollgold_tv)
    TextView ctrl_dollgold;
    @BindView(R.id.ctrl_dollstate_tv)
    TextView ctrl_dollstate;

    @BindView(R.id.playgame_rl)
    RelativeLayout playgame_rl;
    @BindView(R.id.ctrl_play_dollgold_tv)
    TextView dollgold_tv;
    @BindView(R.id.ll_wiper)
    LinearLayout ll_wiper;
    @BindView(R.id.iv_add_recharge)
    ImageView iv_add_recharge;
    @BindView(R.id.tv_user_golds)
    TextView user_golds;
    @BindView(R.id.tv_time_count)
    TextView time_count;
    @BindView(R.id.coin2_state_tv)
    TextView stateTv;
    @BindView(R.id.ctrl_gif_view)
    GifView ctrlGifView;
    @BindView(R.id.ctrl_fail_iv)
    ImageView ctrlFailIv;
    @BindView(R.id.recharge_ll)
    RelativeLayout recharge_ll;
    @BindView(R.id.coin_gif_view)
    GifView coinGif;
    @BindView(R.id.tv_add_coin_animant)
    TextView add_coin_animant;

    private static final String TAG = "PushCoin2Activity-";
    private String currentUrl;
    private String playUrlMain;
    private CtrlCompl ctrlCompl;

    private int timeCount = TIME_OUT;
    private int bingCount = 0;
    private Timer timer;
    private Task task;
    private UiHandler uiHandler = new UiHandler(PushCoin2Activity.this);

    private static final int TAG_MY_START = 0x10f0;
    private static final int TAG_OTHER_START = 0x10f1;
    private static final int TAG_DEVICE_FREE = 0x10ff;
    private static final int TIME_OUT = 30;

    private String money="0";
    private String userMoney="0";
    private List<String> userInfos = new ArrayList<>();  //房屋内用户电话信息

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
     private MediaPlayer mediaPlayer3;
    private boolean isMePlay=false;

    static   class UiHandler extends Handler {
           WeakReference<PushCoin2Activity> ac;
          private UiHandler(PushCoin2Activity pushCoin2Activity) {
               ac = new WeakReference<>(pushCoin2Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PushCoin2Activity activity = ac.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    activity.time_count.setText(activity.timeCount+"s");
                    activity.timeCount--;
                    break;
                case 1:
                    activity.ctrlGifView.setVisibility(View.GONE);
                    activity.ctrlFailIv.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    activity.ctrlGifView.setVisibility(View.GONE);
                    if ( activity.ctrlFailIv.getVisibility() == View.VISIBLE) {
                        activity.ctrlFailIv.setVisibility(View.GONE);
                    }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_coin2);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer3 != null && mediaPlayer3.isPlaying()) {
            mediaPlayer3.stop();
            mediaPlayer3.release();
            mediaPlayer3 = null;
        }
        release();
    }
    private void release(){
        ctrlCompl.stopRecordView();
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();

        if (mediaPlayer1 != null && mediaPlayer1.isPlaying()) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        if (mediaPlayer2 != null && mediaPlayer2.isPlaying()) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }
    private void initData() {

        RxBus.get().register(this);
        NettyUtils.sendRoomInCmd("coin2push");
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlGifView.setEnabled(false);
        ctrlGifView.setMovieResource(R.raw.ctrl_video_loading);
        ctrlFailIv.setVisibility(View.GONE);
        coinGif.setVisibility(View.GONE);
        coinGif.setEnabled(false);

        playUrlMain = getIntent().getStringExtra(Utils.TAG_URL_MASTER);
        money=getIntent().getStringExtra(Utils.TAG_DOLL_GOLD);

        ctrlCompl = new CtrlCompl(this, this);
        currentUrl = playUrlMain;
        ctrlCompl.startPlayVideo(mPlaySv, currentUrl);

        stateTv.setText("TAG_CONNECT_SUCESS");
        if (Utils.connectStatus.equals(ConnectResultEvent.CONNECT_FAILURE)) {
            stateTv.setText("网关异常");
        }

        player_name.setText(UserUtils.NickName);
        Glide.with(this).load(UserUtils.UserImage).asBitmap().
                error(R.mipmap.app_mm_icon).
                transform(new GlideCircleTransform(this)).into(player_iv);
        ctrl_dollgold.setText(money + "/次");
        dollgold_tv.setText(money);
        userMoney=UserUtils.UserBalance;
        user_golds.setText(userMoney);

        getUserDate(UserUtils.USER_ID);
        mediaPlayer();
        if (Utils.getIsOpenMusic(getApplicationContext())) {
            playRoomMusic();   //播放房间背景音乐
        }
    }
    private void mediaPlayer(){
        mediaPlayer2 = MediaPlayer.create(this, R.raw.down_coin);
        // 设置音频流的类型
        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer2.setLooping(false);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.push_coin);
        // 设置音频流的类型
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer1.setLooping(false);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.loge("onRestart", TAG);
        if (ctrlFailIv.getVisibility() == View.VISIBLE) {
            ctrlFailIv.setVisibility(View.GONE);
        }
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlCompl.startPlayVideo(mPlaySv, currentUrl);
        NettyUtils.sendRoomInCmd("coin2push");
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            getgetUserDate();
        }
        if (Utils.getIsOpenMusic(getApplicationContext())) {
            playRoomMusic();   //播放房间背景音乐
        }
    }
  private void getgetUserDate(){
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              getUserDate(UserUtils.USER_ID);    //2秒后获取用户余额并更新UI
          }
      }, 2000);
  }
    private void playPushMusic() {if(mediaPlayer1!=null){mediaPlayer1.start();}}
    private void playDownMusic(){if(mediaPlayer2!=null){mediaPlayer2.start();}}
    private void playRoomMusic(){
        if(mediaPlayer3==null){
            mediaPlayer3 = MediaPlayer.create(this, R.raw.push_coin_music);
            // 设置音频流的类型
            mediaPlayer3.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer3.setLooping(true);
        }
        mediaPlayer3.start();
    }
    private void  pushCoinAnimat(){
        Movie mMovie = Movie.decodeStream(getResources().openRawResource(
                R.raw.coin_push_out));
        coinGif.setVisibility(View.VISIBLE);
        coinGif.setMovie(mMovie);
        int dur=mMovie.duration();
        //gif动画播放一次
        coinGif.setPaused(false);
        coinGif.postDelayed(new Runnable() {
            @Override
            public void run() {
                coinGif.setPaused(true);
                coinGif.setVisibility(View.GONE);
            }
        },dur);
    }

    @OnClick({R.id.startgame_ll, R.id.playgame_rl, R.id.ll_wiper,
              R.id.coin2_back, R.id.coin2_why, R.id.ll_message,R.id.recharge_ll})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startgame_ll:
                if (judgeMoney()) {
                    NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_START);
                } else {
                    setCatchResultDialog(0);
                }
                break;
            case R.id.playgame_rl:
                if (judgeMoney()) {
                    NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_PLAY);
                } else {
                    setCatchResultDialog(0);
                }
                break;
            case R.id.ll_wiper:
                NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_SWAP);
                break;
            case R.id.coin2_back:
                finish();
                break;
            case R.id.coin2_why:
                startActivity(new Intent(this, GameInstrcutionActivity.class));
                break;
            case R.id.recharge_ll:
                startActivity(new Intent(this, RechargeActivity.class));
                break;
        }

    }
    private boolean judgeMoney() {
        if(!userMoney.isEmpty()&&!money.isEmpty()) {
            if (Integer.parseInt(userMoney) >= Integer.parseInt(money)) {
                return true;
            }
        }
        return false;
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_ROOM_IN),
            @Tag(Utils.TAG_ROOM_OUT),
            @Tag(Utils.TAG_COIN_RESPONSE)})
    public void getCoinDeviceResponse(Object response){
        if (response instanceof Coin2ControlResponse){
            Coin2ControlResponse coinControlResponse = (Coin2ControlResponse) response;
            LogUtils.loge( "====" + coinControlResponse.toString(),TAG);
            dealWithResponse(coinControlResponse);
        } else if (response instanceof AppInRoomResponse) {
            AppInRoomResponse appInRoomResponse = (AppInRoomResponse) response;
            LogUtils.loge("appInRoomResponse........" + appInRoomResponse.toString(),TAG);
            Boolean free = appInRoomResponse.getFree();
            String allUsers = appInRoomResponse.getAllUserInRoom(); //返回的UserId
            long seq = appInRoomResponse.getSeq();
            if ((seq != -2) && (!Utils.isEmpty(allUsers))) {
                //TODO  我本人进来了
                ctrlCompl.sendGetUserInfos(allUsers, true);
                //是否玩家正在游戏中
               if (!free) {
                  isOwnerUse(TAG_OTHER_START);
                    stateTv.setText("网关使用中!");
                }
            } else {
                boolean is = false;
                if (userInfos.size() == 1) {
                    is = true;
                }
                userInfos.add(appInRoomResponse.getUserId());
                getUserInfos(userInfos, is);
            }

        } else if (response instanceof AppOutRoomResponse) {
            AppOutRoomResponse appOutRoomResponse = (AppOutRoomResponse) response;
            LogUtils.loge("====" + appOutRoomResponse.toString(),TAG);
            long seq = appOutRoomResponse.getSeq();
            if (seq == -2) {
                userInfos.remove(appOutRoomResponse.getUserId());
                if (appOutRoomResponse.getUserId().equals(UserUtils.USER_ID)) {
                     getUserInfos(userInfos, true);
                } else {
                     getUserInfos(userInfos, false);
                }
            }
        }
    }

    private void dealWithResponse(Coin2ControlResponse response) {
        ReturnCode code = response.getReturnCode();
        if (!code.name().equals(ReturnCode.SUCCESS.name())) {
            return;
        }
        CoinStatusType statusType = response.getCoinStatusType();
        if (statusType.name().equals(CoinStatusType.BINGO.name())) {
            //刷新定时器
            timeCount = TIME_OUT;
            if(!Utils.isNumeric(response.getBingo()+"")){
                return;
            }
            if(isMePlay){ //自己玩
                bingCount = bingCount + response.getBingo();
                if(response.getBingo()!=null&&response.getBingo()>0) {
                    if (!userMoney.isEmpty() && !money.isEmpty()) {
                        userMoney = Integer.parseInt(userMoney) + response.getBingo() + "";
                        user_golds.setText(userMoney);
                        setAnimant(response.getBingo());
                    }
                    playDownMusic();
                }
            }
            //  bingoTv.setText(String.valueOf(bingCount));
        } else if (statusType.name().equals(CoinStatusType.START.name())) {

            String uId = response.getUserId();
            if (TextUtils.isEmpty(uId)) {
                return;
            }
            if (uId.equals(UserUtils.USER_ID)) {
                isOwnerUse(TAG_MY_START);//自己玩
            } else {
                isOwnerUse(TAG_OTHER_START);//他人玩
            }
        } else if (statusType.name().equals(CoinStatusType.SWAY.name())) {
          //  timeCount = TIME_OUT;
        } else if (statusType.name().equals(CoinStatusType.PLAY.name())) {
            timeCount = TIME_OUT;
            if(isMePlay) {//自己玩
                if (!userMoney.isEmpty() && !money.isEmpty()) {
                    userMoney = Integer.parseInt(userMoney) - Integer.parseInt(money) + "";
                    user_golds.setText(userMoney);
                }
                playPushMusic();
            }

        } else if (statusType.name().equals(CoinStatusType.END.name())) {//游戏结束
            LogUtils.loge( "bingCount====" + bingCount,TAG);
            if(isMePlay) { //自己玩
                if(bingCount>0) {
                    pushCoinAnimat();
                }
                getgetUserDate();
            }
            isOwnerUse(TAG_DEVICE_FREE);
        }
    }
    //监控网关区
    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAT_USING),
                    @Tag(Utils.TAG_CONNECT_ERR),
                    @Tag(Utils.TAG_CONNECT_SUCESS)})
    public void getGatwayStates(String tag) {
        //TODO 发送命令 网关在使用中
        LogUtils.loge( "网关........" + tag,TAG);
        stateTv.setText(tag);
    }
    //设备状态
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_COIN_DEVICE_STATE)})
    public void getCoinDeviceState(String state) {
        LogUtils.loge( "当前游戏机状态::::" + state,TAG);
    }

    private void isOwnerUse(int is) {
        bingCount = 0;
        timeCount = TIME_OUT;
        isMePlay=false;
        switch (is){
            case TAG_MY_START:
                isMePlay=true;
                playgame_rl.setVisibility(View.VISIBLE);
                ll_wiper.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.INVISIBLE);
                iv_add_recharge.setVisibility(View.INVISIBLE);
                recharge_ll.setEnabled(false);
                time_count.setVisibility(View.VISIBLE);
                startTimer();
                break;
            case TAG_OTHER_START:
                startBtn.setVisibility(View.VISIBLE);
                startBtn.setEnabled(false);
                playgame_rl.setVisibility(View.GONE);
                ll_wiper.setVisibility(View.INVISIBLE);
                iv_add_recharge.setVisibility(View.VISIBLE);
                recharge_ll.setEnabled(true);
                time_count.setVisibility(View.GONE);
                ctrl_dollstate.setText("排队");
                stopTimer();
                break;
            case TAG_DEVICE_FREE:
                //  time_count.setText(String.valueOf(timeCount));
                time_count.setVisibility(View.GONE);
                //   bingoTv.setText(String.valueOf(bingCount));
                playgame_rl.setVisibility(View.GONE);
                ll_wiper.setVisibility(View.INVISIBLE);
                iv_add_recharge.setVisibility(View.VISIBLE);
                recharge_ll.setEnabled(true);
                startBtn.setVisibility(View.VISIBLE);
                ctrl_dollstate.setText("上机");
                startBtn.setEnabled(true);
                stopTimer();
                break;
        }
    }

    public void startTimer() {
        if (timer != null) {
            timer = null;
        }
        timer = new Timer();
        task = new Task();
        timer.schedule(task, 0, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            task.cancel();
            task = null;
            timer = null;
        }
    }

    class Task extends TimerTask {
        @Override
        public void run() {
            uiHandler.sendEmptyMessage(0);
            if (timeCount == 0) {
                stopTimer();
            }
        }
    }

    @Override
    public void getTime(int time) {
    }
    @Override
    public void getTimeFinish() {
        ctrlCompl.stopTimeCounter();
    }

    @Override
    public void getUserInfos(List<String> list, boolean is) {
        //当前房屋的人数
        userInfos = list;
        int counter = userInfos.size();
        LogUtils.loge("当前房屋的人数::::" + counter, TAG);
        if (counter > 0) {
            String s = (counter + 20) + "人围观";//线人数 默认20个
            room_peoples.setText(s);
        }
    }
    @Override
    public void getRecordErrCode(int code) {}
    @Override
    public void getRecordSuecss() {}
    @Override
    public void getRecordAttributetoNet(String time, String name) {}
    @Override
    public void getPlayerErcErrCode(int code) {
        LogUtils.loge("直播失败,错误码:::::" + code, TAG);
        uiHandler.sendEmptyMessage(1);
    }
    @Override
    public void getPlayerSucess() {
        LogUtils.loge("直播Sucess:::::", TAG);
        uiHandler.sendEmptyMessage(2);
    }
    @Override
    public void getVideoPlayConnect() {}
    @Override
    public void getVideoPlayStart() {}
    @Override
    public void getVideoStop() {}
    //获取用户信息接口
    private void getUserDate(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    UserBean bean = loginInfoResult.getData().getAppUser();
                    if (bean != null) {
                        String balance = bean.getBALANCE();
                        if (!TextUtils.isEmpty(balance)) {
                            UserUtils.UserBalance = balance;
                            userMoney=UserUtils.UserBalance;
                            user_golds.setText(userMoney);
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void setCatchResultDialog(final int index  ) {
        final CatchDollResultDialog catchDollResultDialog = new CatchDollResultDialog(this, R.style.activitystyle);
        catchDollResultDialog.setCancelable(false);
        catchDollResultDialog.show();
        catchDollResultDialog.setTitle("您的金币余额不足");
        catchDollResultDialog.setRechargeContent();
        catchDollResultDialog.setFail("取消充值");
        catchDollResultDialog.setSuccess("前往充值");
        catchDollResultDialog.setBackground(R.drawable.catchdialog_success_bg);
        catchDollResultDialog.setDialogResultListener(new CatchDollResultDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if(resultCode>0) {
                    switch (index) {
                        case 0:
                            Utils.toActivity(PushCoin2Activity.this, RechargeActivity.class);
                            break;
                    }
                }
                catchDollResultDialog.dismiss();
            }
        });
    }

    private void setAnimant(int bingCount){
        add_coin_animant.setText(" + "+bingCount);
        add_coin_animant.setVisibility(View.VISIBLE);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(add_coin_animant, "alpha", 1f, 0.2f);
        ObjectAnimator animator = ObjectAnimator.ofFloat(add_coin_animant, "translationY", 0f,500f);
        AnimatorSet animatorSet=new AnimatorSet();
        //同时沿Y轴移动，且改变透明度
        animatorSet.play(animator).with(alphaAnimator) ;
        //每个动画都设置成3s，也可以分别设置
        animatorSet.setDuration(1500);
        animatorSet.start();
        add_coin_animant.postDelayed(new Runnable() {
            @Override
            public void run() {
                add_coin_animant.setVisibility(View.GONE);
            }
        },1500);
    }
}
