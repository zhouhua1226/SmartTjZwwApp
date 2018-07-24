package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.AppManager;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/7/19.
 */

public class WelcomeActivity extends BaseActivity{
    private String uid;
    @BindView(R.id.ll_welcome)
    LinearLayout ll_welcome;
    @BindView(R.id.rl_welcome)
    RelativeLayout Viewwelcome;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getAppVersion();
        initWelcome();
    }
    private void getAppVersion() {
        Utils.appVersion = Utils.getAppCodeOrName(this, 0);
        Utils.osVersion = Utils.getSystemVersion();
        Utils.deviceType = Utils.getDeviceBrand();
        Utils.IMEI = Utils.getIMEI(this);
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initWelcome() {
//        if(isFirstInto()){
//            startActivity(new Intent(this,NavigationPageActivity.class));
//        }
        if (SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false)) {
            //用户已经注册
            uid =  SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
            if (Utils.isEmpty(uid)) {
                return;
            }
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                getAuthLogin(uid);
            } else {
                MyToast.getToast(getApplicationContext(), "请查看你的网络！").show();
                initCreatView();
            }
        } else {
            initCreatView();
        }

    }

    private void initCreatView() {
        ll_welcome.setBackgroundResource(R.drawable.login_mm_bg);
        Viewwelcome.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_login_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_password:
                Utils.toActivity(this, LoginCodeActivity.class);
                break;
            default:
                break;
        }
    }

    private void getAuthLogin(String userId) {
        HttpManager.getInstance().getAuthLogin(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult == null || loginInfoResult.getData() == null
                        || loginInfoResult.getData().getAppUser() == null) {
                    initCreatView();
                    return;
                }
                if (loginInfoResult.getMsg().equals("success")) {
                 //   MyToast.getToast(getApplicationContext(), "自动登录成功！").show();
                    YsdkUtils.loginResult = loginInfoResult;
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, loginInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    UserUtils.USER_ID = loginInfoResult.getData().getAppUser().getUSER_ID();
                    UserUtils.isUserChanger = true;
                    Utils.toActivity(WelcomeActivity.this, MainActivity.class);
                } else {
                    initCreatView();
                }
            }

            @Override
            public void _onError(Throwable e) {
                initCreatView();
            }
        });
    }

    private boolean isFirstInto() {
        // 判断是否第一次打开app
        boolean user_first = SPUtils.getBoolean(getApplicationContext(),"FIRST", true);
        if (user_first) {
            SPUtils.putBoolean(getApplicationContext(),"FIRST", false);
        }
        return user_first;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishAllActivity();
    }
}
