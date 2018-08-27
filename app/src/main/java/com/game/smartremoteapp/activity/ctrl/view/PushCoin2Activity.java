package com.game.smartremoteapp.activity.ctrl.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.gatz.netty.global.ConnectResultEvent;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iot.game.pooh.server.entity.json.Coin2ControlResponse;
import com.iot.game.pooh.server.entity.json.CoinControlResponse;
import com.iot.game.pooh.server.entity.json.app.AppInRoomResponse;
import com.iot.game.pooh.server.entity.json.app.AppOutRoomResponse;
import com.iot.game.pooh.server.entity.json.enums.CoinStatusType;
import com.iot.game.pooh.server.entity.json.enums.ReturnCode;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PushCoin2Activity extends Activity implements IctrlView{
    @BindView(R.id.coin2_play_video_sv)
    SurfaceView mPlaySv;

    @BindView(R.id.coin2_start_btn)
    Button startBtn;

    @BindView(R.id.coin2_ctrl_rl)
    RelativeLayout ctrlRl;
    @BindView(R.id.coin2_play_btn)
    Button playBtn;
    @BindView(R.id.coin2_swap_btn)
    Button swapBtn;

    @BindView(R.id.coin2_info_rl)
    RelativeLayout infoRl;
    @BindView(R.id.coin2_time_tv)
    TextView timeTv;
    @BindView(R.id.coin2_bingo_tv)
    TextView bingoTv;

    @BindView(R.id.coin2_state_tv)
    TextView stateTv;

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

    static class UiHandler extends Handler {
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
            int what = msg.what;
            if (what == 0) {
                activity.timeTv.setText(String.valueOf(activity.timeCount));
                activity.timeCount --;
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
        ctrlCompl.stopRecordView();
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();
        ctrlCompl = null;
    }

    private void initData() {
        RxBus.get().register(this);
        NettyUtils.sendRoomInCmd("coin2push");
        NettyUtils.pingRequest(); //判断连接
        playUrlMain = getIntent().getStringExtra(Utils.TAG_URL_MASTER);
        ctrlCompl = new CtrlCompl(this, this);
        currentUrl = playUrlMain;
        ctrlCompl.startPlayVideo(mPlaySv, currentUrl);
        stateTv.setText("TAG_CONNECT_SUCESS");
        if (Utils.connectStatus.equals(ConnectResultEvent.CONNECT_FAILURE)) {
            stateTv.setText("网关异常");
        }
    }

    @OnClick({R.id.coin2_start_btn, R.id.coin2_play_btn, R.id.coin2_swap_btn})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.coin2_start_btn) {
            NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_START);
        } else if (id == R.id.coin2_play_btn) {
            NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_PLAY);
        } else if (id == R.id.coin2_swap_btn) {
            NettyUtils.sendPushCoin2Cmd(NettyUtils.USER_PUSH_COIN_SWAP);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_ROOM_IN),
            @Tag(Utils.TAG_ROOM_OUT),
            @Tag(Utils.TAG_COIN_RESPONSE)})
    public void getCoinDeviceResponse(Object response){
        if (response instanceof Coin2ControlResponse){
            Coin2ControlResponse coinControlResponse = (Coin2ControlResponse) response;
            Log.e(TAG, "====" + coinControlResponse.toString());
            dealWithResponse(coinControlResponse);
        } else if (response instanceof AppInRoomResponse) {
            AppInRoomResponse appInRoomResponse = (AppInRoomResponse) response;
            Log.e(TAG, "appInRoomResponse........" + appInRoomResponse.toString());
            Boolean free = appInRoomResponse.getFree();
            if (!free) {
                stateTv.setText("网关使用中!");
                isOwnerUse(TAG_OTHER_START);
            }
        } else if (response instanceof AppOutRoomResponse) {
            AppOutRoomResponse appOutRoomResponse = (AppOutRoomResponse) response;
            Log.e(TAG, "====" + appOutRoomResponse.toString());
        }
    }

    private void dealWithResponse(Coin2ControlResponse response) {
        ReturnCode code = response.getReturnCode();
        if (!code.name().equals(ReturnCode.SUCCESS.name())) {
            return;
        }
        CoinStatusType statusType = response.getCoinStatusType();
        if (statusType.name().equals(CoinStatusType.BINGO.name())) {
            bingCount = bingCount + response.getBingo();
            bingoTv.setText(String.valueOf(bingCount));
            //刷新定时器
            timeCount = TIME_OUT;
        } else if (statusType.name().equals(CoinStatusType.START.name())) {
            String uId = response.getUserId();
            if (TextUtils.isEmpty(uId)) {
                return;
            }
            if (uId.equals(UserUtils.USER_ID)) {
                isOwnerUse(TAG_MY_START);
            } else {
                isOwnerUse(TAG_OTHER_START);
            }
        } else if (statusType.name().equals(CoinStatusType.SWAY.name())) {
            timeCount = TIME_OUT;
        } else if (statusType.name().equals(CoinStatusType.PLAY.name())) {
            timeCount = TIME_OUT;
        } else if (statusType.name().equals(CoinStatusType.END.name())) {
            isOwnerUse(TAG_DEVICE_FREE);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAT_USING),
                    @Tag(Utils.TAG_CONNECT_ERR),
                    @Tag(Utils.TAG_CONNECT_SUCESS)})
    public void getGatwayStates(String tag) {
        //TODO 发送命令 网关在使用中
        Log.e(TAG, "网关........" + tag);
        stateTv.setText(tag);
    }


    private void isOwnerUse(int is) {
        bingCount = 0;
        timeCount = TIME_OUT;
        if (is == TAG_MY_START) {
            ctrlRl.setVisibility(View.VISIBLE);
            infoRl.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.GONE);
            startTimer();
        } else if (is == TAG_OTHER_START) {
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setEnabled(false);
            ctrlRl.setVisibility(View.GONE);
            infoRl.setVisibility(View.GONE);
            stopTimer();
        } else if (is == TAG_DEVICE_FREE) {
            timeTv.setText(String.valueOf(timeCount));
            bingoTv.setText(String.valueOf(bingCount));
            ctrlRl.setVisibility(View.GONE);
            infoRl.setVisibility(View.GONE);
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setEnabled(true);
            stopTimer();
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

    }

    @Override
    public void getUserInfos(List<String> list, boolean is) {

    }

    @Override
    public void getRecordErrCode(int code) {

    }

    @Override
    public void getRecordSuecss() {

    }

    @Override
    public void getRecordAttributetoNet(String time, String name) {

    }

    @Override
    public void getPlayerErcErrCode(int code) {

    }

    @Override
    public void getPlayerSucess() {

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
}
