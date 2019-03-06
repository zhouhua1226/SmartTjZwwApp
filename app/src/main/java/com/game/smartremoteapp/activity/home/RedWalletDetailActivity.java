package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.RecordRedWalletAdapter;
import com.game.smartremoteapp.adapter.ZWWAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.RedInfoBean;
import com.game.smartremoteapp.bean.RedPackageBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.UserInfoBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/11/16.
 */

public class RedWalletDetailActivity extends BaseActivity {
    @BindView(R.id.mv_view)
    View mView;
    @BindView(R.id.user_image)
    RoundedImageView user_image;
    @BindView(R.id.tv_user_name)
    TextView user_name;
    @BindView(R.id.tv_redwallet_num)
    TextView redwallet_num;
    @BindView(R.id.tv_reward_num)
    TextView reward_num;
    @BindView(R.id.tv_redwallet_desc)
    TextView redwallet_desc;

    @BindView(R.id.rv_redwallet)
    RecyclerView mRecyclerView;
    private List<UserInfoBean> mUserInfoBeans = new ArrayList<>();
    private RecordRedWalletAdapter mRecordRedWalletAdapter;
    private boolean  isHit=false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redwallet_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        ButterKnife.bind(this);
        Utils.setViewLayoutParams(this, mView);
        initView();
    }


    @Override
    protected void initView() {
        String redId = getIntent().getStringExtra(UrlUtils.REDID);
        isHit=getIntent().getBooleanExtra(UrlUtils.ISHIT,false);
        redwallet_desc.setVisibility(View.GONE);
        initRecyclerView();
        getRedPackdetail(redId);
    }

    private void getRedPackdetail(String redId) {
        HttpManager.getInstance().getRedPackdetail(redId, new RequestSubscriber<Result<RedInfoBean>>() {
            @Override
            public void _onSuccess(Result<RedInfoBean> loginInfoResult) {
                if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    if(loginInfoResult.getData().getRedInfo()!=null){
                      initData(loginInfoResult.getData().getRedInfo());
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void initData(List<RedPackageBean> redInfo) {
        if(redInfo.size()>0) {
            Glide.with(this).load(UrlUtils.APPPICTERURL + redInfo.get(0).getImgurl())
                    .error(R.mipmap.app_mm_icon)
                    .into(user_image);
            user_name.setText(redInfo.get(0).getNickName()+"的红包");
            redwallet_num.setText(redInfo.get(0).getRedGold());
            if(redInfo.get(0).getUserInfo().size()>0) {
                if(!redInfo.get(0).getUserInfo().get(0).getGold().equals("0")){
                    reward_num.setText("领取" + redInfo.get(0).getUserInfo().size() + "/" + redInfo.get(0).getRedNum());
                    mUserInfoBeans.addAll(redInfo.get(0).getUserInfo());
                    mRecordRedWalletAdapter.notify(mUserInfoBeans);
                }else{
                    reward_num.setText("领取0/" + redInfo.get(0).getRedNum());
                }
            }
        }else{
            finish();
        }
    }

    @OnClick({R.id.tv_back})
    public void onViewClicked() {
        this.finish();
    }

    private void initRecyclerView() {
        mRecordRedWalletAdapter = new RecordRedWalletAdapter(this, mUserInfoBeans);
        mRecyclerView.setAdapter(mRecordRedWalletAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecordRedWalletAdapter.setmOnItemClickListener(new ZWWAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(isHit) {
                    Intent intent = new Intent(RedWalletDetailActivity.this, UserInfoActivity.class);
                    intent.putExtra(UrlUtils.USERID, mUserInfoBeans.get(position).getUserId());
                    startActivity(intent);
                }
            }
        });
    }
}
