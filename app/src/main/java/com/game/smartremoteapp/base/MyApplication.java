package com.game.smartremoteapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.game.smartremoteapp.service.SmartRemoteService;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by zhouh on 2017/9/7.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static List<Activity> activities = new LinkedList<Activity>();
    private String TAG="MyApplication--";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        startCoreService();
        getPushAgent();
        setCrashHandler();
       // LogUtils.logInit(true);//初始化logger
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());
    }
    private void setCrashHandler() {

    }
    private void getPushAgent() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e(TAG,"友盟deviceToken="+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setDisplayNotificationNumber(2);
        mPushAgent.setDebugMode(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void startCoreService() {
        Intent it = new Intent();
        it.setClass(getApplicationContext(), SmartRemoteService.class);
        startService(it);
    }

    public static MyApplication getInstance() {
        return myApplication;
    }


    public void finishProcess() {
        System.exit(0);
    }

    public void exit() {
        if (activities != null) {
            Activity activity;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                if (activity != null) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    activity = null;
                }
                activities.remove(i);
                i--;
            }
        }
    }

}
