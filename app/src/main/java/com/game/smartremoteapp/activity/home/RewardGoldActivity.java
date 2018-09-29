package com.game.smartremoteapp.activity.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.RewardGoldAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.LoginRewardGoldBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.QuizInstrictionDialog;
import com.game.smartremoteapp.view.reshrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/9/27.
 */

public class RewardGoldActivity extends BaseActivity{
    @BindView(R.id.tv_today_receive)
    TextView today_receive;
    @BindView(R.id.tv_yesday_catch)
    TextView yesday_catch;
    @BindView(R.id.btn_reward_receive)
    Button reward_receive;
    @BindView(R.id.regold_recyclerview)
    XRecyclerView mXRecyclerView;
    private RewardGoldAdapter mAdater;
    private List<LoginRewardGoldBean.LoginRewardGold> mLoginRewardGoldBeans=new ArrayList<>();
    private QuizInstrictionDialog instrictionDialog;
    private  LoginRewardGoldBean.LoginRewardGold mLoginRewardGold;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward_gold;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
//        mAdater=new RewardGoldAdapter(this,mLoginRewardGoldBeans);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mXRecyclerView.setPullRefreshEnabled(false);
//        mXRecyclerView.setLoadingMoreEnabled(false);
//        mXRecyclerView.setAdapter(mAdater);
//        mAdater.setmOnItemClickListener(new ZWWAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                doRewardInfo(UserUtils.USER_ID,mLoginRewardGoldBeans.get(position));
//            }
//        });
        getRewardInfo(UserUtils.USER_ID);
    }
    @OnClick({R.id.image_back,R.id.image_why,R.id.btn_reward_receive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_why:
                //邮寄说明对话框
                instrictionDialog = new QuizInstrictionDialog(this, R.style.easy_dialog_style);
                instrictionDialog.show();
                instrictionDialog.setTitle("金币领取说明");
                instrictionDialog.setContent(Utils.readAssetsTxt(this, "rewardintroduce"));
                break;
            case R.id.btn_reward_receive:
                if(mLoginRewardGold!=null){
                    doRewardInfo(UserUtils.USER_ID,mLoginRewardGold);
                }
                break;
        }
    }

    private void getRewardInfo(String userId) {
        HttpManager.getInstance().getRewardInfo(userId, new RequestSubscriber<Result<LoginRewardGoldBean>>() {
            @Override
            public void _onSuccess(Result<LoginRewardGoldBean> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    if(result.getData()!=null){
                        if(result.getData().getLoginRewardGold().size()>0){
                            mLoginRewardGold=result.getData().getLoginRewardGold().get(0);
                            initdata(mLoginRewardGold);
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void initdata(LoginRewardGoldBean.LoginRewardGold mLoginRewardGold) {
        if(Utils.getDateOver(mLoginRewardGold.getCreateTime())){
            today_receive.setText(mLoginRewardGold.getGold());
            yesday_catch.setText(mLoginRewardGold.getGold());
            reward_receive.setVisibility(View.VISIBLE);
            if(mLoginRewardGold.getTag().equals("Y")){
                reward_receive.setBackgroundResource(R.drawable.bg_reward_gold_nomal);
                reward_receive.setTextColor(Color.parseColor("#19180f"));
                reward_receive.setEnabled(false);
                reward_receive.setText("已领取");
            }else{
                reward_receive.setBackgroundResource(R.drawable.bg_reward_gold_checked);
                reward_receive.setTextColor(Color.WHITE);
                reward_receive.setEnabled(true);
                reward_receive.setText("领取赠币");
            }
        }
    }

    private void doRewardInfo(String userId, final LoginRewardGoldBean.LoginRewardGold loginRewardGold) {
        HttpManager.getInstance().doReward(userId, new RequestSubscriber<Result<String>>() {
            @Override
            public void _onSuccess(Result<String> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    MyToast.getToast(getApplicationContext(), "金币领取成功！").show();
                    loginRewardGold.setTag("Y");
                    initdata(loginRewardGold);
                }else{
                    MyToast.getToast(getApplicationContext(), "金币领取失败！").show();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
}
