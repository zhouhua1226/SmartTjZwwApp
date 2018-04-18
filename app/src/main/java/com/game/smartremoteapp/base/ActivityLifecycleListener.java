package com.game.smartremoteapp.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UserUtils;
/**
 * Created by mi on 2018/4/8.
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private int refCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        refCount++;
        LogUtils.loge("ActivityLifecycleCallbacks-------onActivityCreated" );
    }
    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtils.loge("ActivityLifecycleCallbacks-------onActivityDestroyed" );
        refCount--;
        if (refCount == 0) {
            exit();
        }
    }

    private void exit( ) {
        LogUtils.loge("App退出了");
        if(UserUtils.USER_ID!=null){
            getLogout(UserUtils.USER_ID);
        }
    }
    private void getLogout(String userId) {
        HttpManager.getInstance().getLogout(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
}



