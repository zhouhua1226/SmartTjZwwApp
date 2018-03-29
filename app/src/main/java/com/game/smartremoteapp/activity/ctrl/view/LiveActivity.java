package com.game.smartremoteapp.activity.ctrl.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartremoteapp.adapter.MessageAdapter;
import com.game.smartremoteapp.adapter.MessageLoopAdapter;
import com.game.smartremoteapp.bean.MessageBean;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GifView;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.iot.game.pooh.server.entity.json.enums.MoveType;
import com.umeng.analytics.game.UMGameAgent;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/3/27.
 */

public class LiveActivity extends Activity implements IctrlView {
    private static final String TAG = "ControlActivity---";
    @BindView(R.id.realplay_sv)
    SurfaceView mRealPlaySv;
    @BindView(R.id.ctrl_gif_view)
    GifView ctrlGifView;
    @BindView(R.id.ctrl_fail_iv)
    ImageView ctrlFailIv;
    @BindView(R.id.tv_event_notification)
    TextView notification;//活动通知时间
    @BindView(R.id.tv_war_record)
    TextView war_record;//战绩
    @BindView(R.id.rl_send_message)
    RelativeLayout editMessageView;//发消息
    @BindView(R.id.iv_send_message)
    ImageView send_message;//发消息
    @BindView(R.id.et_massage)
    EditText etMassage;//编辑消息
    @BindView(R.id.ll_rolling_view)
    LinearLayout rollingView;//消息轮播View
    @BindView(R.id.lvchat)
    ListView mListView;//消息轮播

    private CtrlCompl ctrlCompl;
    private String currentUrl;
    private List<MessageBean> messageBeanList;
    private MessageLoopAdapter mAdapter;
    private MessageAdapter mAdapters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate();
        UMGameAgent.setDebugMode(true);   //设置输出运行时日志
        UMGameAgent.init(this);
    }
    private void afterCreate() {
        initView();
        initData();
        ininListview();
    }
    private void ininListview() {
        messageBeanList = new ArrayList<>();
        mAdapters = new MessageAdapter(this, messageBeanList);
        mListView.setAdapter(mAdapters);
    }
    private void initData() {
        currentUrl = getIntent().getStringExtra(Utils.TAG_LIVE_DURL);
        ctrlCompl = new CtrlCompl(this, this);
        ctrlCompl.startPlayVideo(mRealPlaySv, currentUrl);
    }

    private void initView() {
        NettyUtils.sendRoomInCmd();
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlGifView.setEnabled(false);
        ctrlGifView.setMovieResource(R.raw.ctrl_video_loading);
        ctrlFailIv.setVisibility(View.GONE);
        NettyUtils.pingRequest(); //判断连接
    }
    @OnClick({R.id.iv_send_message})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.iv_send_message:
                String  message=etMassage.getText().toString();
                if(!message.isEmpty()){
                    mAdapters.notifyDataChanged(new MessageBean("1", "YoYo", message));
                }
                    break;
        }
    }
    private int getLayoutId() {
        return R.layout.activity_live;
    }
    @Override
    public void getTime(int time) {
    }
    @Override
    public void getTimeFinish() {
        ctrlCompl.sendCmdCtrl(MoveType.CATCH);
        ctrlCompl.stopTimeCounter();
    }
    @Override
    public void getUserInfos(List<String> list, boolean is) {
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
    }

    @Override
    public void getPlayerErcErrCode(int code) {
        LogUtils.loge("直播失败,错误码:::::" + code, TAG);
    }

    @Override
    public void getPlayerSucess() {
        LogUtils.loge("直播Sucess:::::", TAG);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}


