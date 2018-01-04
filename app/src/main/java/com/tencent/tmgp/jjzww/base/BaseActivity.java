package com.tencent.tmgp.jjzww.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iot.game.pooh.server.entity.json.announce.LotteryDrawAnnounceMessage;
import com.tencent.tmgp.jjzww.utils.SPUtils;
import com.tencent.tmgp.jjzww.utils.UserUtils;
import com.tencent.tmgp.jjzww.utils.Utils;
import com.umeng.message.PushAgent;


/**
 * Created by zhouh on 2017/9/7.
 */
public abstract class BaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity---";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        afterCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        RxBus.get().register(this);
        initDialog();
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    private void initDialog() {

    }

    //监控开奖信息
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_LOTTERY_DRAW)})
    public void getConnectStates(LotteryDrawAnnounceMessage message) {
        String roomId = message.getRoomId();
        String pId = message.getPeriodsNum();
        Utils.showLogE(TAG, "房间号:" + roomId + "第" + pId + "期开奖了.......");
    }
}
