package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2019/1/8.
 */
public class UserInfoActivity extends BaseActivity{
    @BindView(R.id.iv_user_header)
    RoundedImageView user_header;
    @BindView(R.id.tv_user_name)
    TextView user_name;
    @BindView(R.id.tv_gender)
    TextView user_gender;
    @BindView(R.id.tv_user_age)
    TextView user_age;
    @BindView(R.id.tv_qq_number)
    TextView qq_number;
    @BindView(R.id.tv_weixin_number)
    TextView weixin_number;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        String userId=getIntent().getStringExtra(UrlUtils.USERID);
        getAppUserInf(userId);
    }
    @OnClick({R.id.image_back})
    public void onViewClicked() {
        this.finish();
    }

    private void getAppUserInf(String userId) {
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    Glide.with(UserInfoActivity.this)
                            .load(UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL())
                            .error(R.mipmap.app_mm_icon)
                            .dontAnimate()
                            .into(user_header);
                    user_name.setText(result.getData().getAppUser().getNICKNAME());
                    user_gender.setText(result.getData().getAppUser().getGENDER());
                    user_age.setText(result.getData().getAppUser().getAGE()+"");
                    qq_number.setText(result.getData().getAppUser().getQQACCOUNT());
                    weixin_number.setText(result.getData().getAppUser().getWXACCOUNT());
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
}
