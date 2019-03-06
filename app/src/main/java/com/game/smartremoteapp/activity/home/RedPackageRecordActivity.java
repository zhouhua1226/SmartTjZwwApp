package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.RedWalletRecordAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.RedPackageBean;
import com.game.smartremoteapp.bean.RedWalletlistInfoBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.UserSumInfoBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.SelectRecordTimeView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2019/1/8.
 */

public class RedPackageRecordActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.tv_title_right)
    TextView title_right;
    @BindView(R.id.user_image)
    RoundedImageView user_image;
    @BindView(R.id.tv_user_name)
    TextView user_name;
    @BindView(R.id.tv_redwallet_gold)
    TextView redwallet_gold;
    @BindView(R.id.tv_redwallet_number)
    TextView redwallet_number;
    @BindView(R.id.tv_redwallet_time)
    TextView redwallet_time;
    @BindView(R.id.ll_redwallet_time)
    LinearLayout redwalletimetView;

    @BindView(R.id.rv_redwallet)
    RecyclerView mRecyclerView;
    private List<RedWalletlistInfoBean> mRedWalletInfoBeans = new ArrayList<>();
    private RedWalletRecordAdapter mAdapter;
    private String yearTime;
    private boolean isShow = false;
    private boolean isSend = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_redwallet_record;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        Calendar calendar = Calendar.getInstance();
        yearTime = calendar.get(Calendar.YEAR) + "";
        if(yearTime.equals("2019")){
            redwalletimetView.setVisibility(View.GONE);
        }
        redwallet_time.setText(yearTime);
        initRecyclerView();
        getResiveRedPackageInfo(UserUtils.USER_ID, yearTime);
    }

    @OnClick({R.id.image_back, R.id.tv_title_right, R.id.ll_redwallet_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_title_right:
                mRecyclerView.setVisibility(View.VISIBLE);
                mRedWalletInfoBeans.clear();
                mAdapter.notifyDataSetChanged();
                isSend=!isSend;
                if(isSend){
                    title.setText("发出的红包");
                    title_right.setText("收到红包");
                    getSendRedPackageInfo(UserUtils.USER_ID, yearTime);
                 }else{
                    title.setText("收到的红包");
                    title_right.setText("发出红包");
                    getResiveRedPackageInfo(UserUtils.USER_ID, yearTime);
                 }
                break;
            case R.id.ll_redwallet_time:
                new SelectRecordTimeView(this, new SelectRecordTimeView.OnDismissListListener() {
                    @Override
                    public void onnDismissList(int index) {

                    }
                });
                break;
        }
    }

    private void getResiveRedPackageInfo(String userId, String yearTime) {
        HttpManager.getInstance().getResiveRedPackageInfo(userId, yearTime, new RequestSubscriber<Result<RedPackageBean>>() {
            @Override
            public void _onSuccess(Result<RedPackageBean> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    getUserImageAndName(result.getData().getUserSumInfo());
                    if(result.getData().getReceiveListInfo().size()>0){
                        mRedWalletInfoBeans.addAll(result.getData().getReceiveListInfo());
                        mAdapter.notify(mRedWalletInfoBeans,false);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });

    }

    private void getSendRedPackageInfo(String userId, String year) {
        HttpManager.getInstance().getSendRedPackageInfo(userId, year, new RequestSubscriber<Result<RedPackageBean>>() {
            @Override
            public void _onSuccess(Result<RedPackageBean> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    getUserImageAndName(result.getData().getUserSumInfo());
                    if(result.getData().getSendlistInfo().size()>0){
                        mRedWalletInfoBeans.addAll(result.getData().getSendlistInfo());
                        mAdapter.notify(mRedWalletInfoBeans,true);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new RedWalletRecordAdapter(this, mRedWalletInfoBeans, isSend);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setmOnItemClickListener(new RedWalletRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               if(isSend){
                   Intent intent=new Intent(RedPackageRecordActivity.this, RedWalletDetailActivity.class);
                   intent.putExtra(UrlUtils.REDID,mRedWalletInfoBeans.get(position).getREDPACKAGE_ID());
                   intent.putExtra(UrlUtils.ISHIT,true);
                   startActivity(intent);
               }
            }
        });
    }

    private void getUserImageAndName(UserSumInfoBean userSumInfo) {
        if (!isShow) {
            Glide.with(this)
                    .load(UserUtils.UserImage)
                    .error(R.mipmap.app_mm_icon)
                    .dontAnimate()
                    .into(user_image);
            isShow = true;
        }
        if (isSend) {
            user_name.setText(UserUtils.NickName + "一共发出");
            if(userSumInfo.getCOUNT().equals("0")){
                redwallet_gold.setText("0");
            }else{
                redwallet_gold.setText(userSumInfo.getSUMGOLD());
            }
            redwallet_number.setVisibility(View.VISIBLE);
            redwallet_number.setText("发出红包" + userSumInfo.getCOUNT() + "个");
        } else {
            user_name.setText(UserUtils.NickName + "一共收到");
            redwallet_number.setVisibility(View.GONE);
            if(userSumInfo.getSUMGOLD()==null){
                redwallet_gold.setText("0");
            }else{
                redwallet_gold.setText(userSumInfo.getSUMGOLD());
            }
        }
    }
}
