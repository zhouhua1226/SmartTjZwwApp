package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.view.View;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.base.MyApplication;
import com.game.smartremoteapp.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/8/3.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }
    @OnClick({R.id.tv_login_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_password:
                Utils.toActivity(this, LoginCodeActivity.class);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }
    @Override
    public void onBackPressed() {
        MyApplication.getInstance().exit();
    }
}
