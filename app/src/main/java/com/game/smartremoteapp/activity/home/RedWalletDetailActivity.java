package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.RecordRedWalletAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.UserBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/11/16.
 */

public class RedWalletDetailActivity extends BaseActivity{
    @BindView(R.id.rv_redwallet)
    RecyclerView mRecyclerView;
    @BindView(R.id.user_image)
    RoundedImageView user_image;
    @BindView(R.id.tv_user_name)
    TextView  user_name;
    @BindView(R.id.tv_redwallet_num)
    TextView redwallet_num;
    @BindView(R.id.tv_reward_num)
    TextView reward_num;

    private List<UserBean>  mUserBeans=new ArrayList<>();
    private RecordRedWalletAdapter mRecordRedWalletAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_redwallet_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
       initRecyclerView();
    }
    @OnClick({R.id.tv_back })
    public void onViewClicked() {
                this.finish();
    }
    private void initRecyclerView() {
        mRecordRedWalletAdapter = new RecordRedWalletAdapter(this, mUserBeans);
        mRecyclerView.setAdapter(mRecordRedWalletAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
