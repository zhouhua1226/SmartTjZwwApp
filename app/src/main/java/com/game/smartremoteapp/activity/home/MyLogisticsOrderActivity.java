package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.LogisticsAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.LogisticsBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/1/18 11:38
 * 修改人：
 * 修改时间：
 * 类描述：物流订单查询类
 */
public class MyLogisticsOrderActivity extends BaseActivity {

    @BindView(R.id.back_image_bt)
    ImageButton backImageBt;
    @BindView(R.id.logisticsorder_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.logisticsorder_fail_tv)
    TextView logisticsorderFailTv;

    private String TAG="MyLogisticsOrderActivity";
    private LogisticsAdapter logisticsAdapter;
    private List<LogisticsBean> list=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logisticsorder;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initdata();
        getLogisticsOrder(UserUtils.USER_ID);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initdata() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(0));
        logisticsAdapter = new LogisticsAdapter(this,list);
        recyclerView.setAdapter(logisticsAdapter);

    }

    @OnClick({R.id.back_image_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image_bt:
                finish();
                break;
            default:
                break;
        }
    }

    private void getLogisticsOrder(String userId){
        if(Utils.isEmpty(userId)){
            recyclerView.setVisibility(View.GONE);
            logisticsorderFailTv.setVisibility(View.VISIBLE);
            return;
        }
        HttpManager.getInstance().getLogisticsOrder(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if(loginInfoResult.getMsg().equals("success")){
                    list=loginInfoResult.getData().getLogistics();
                    if(list.size()>0){
                        logisticsAdapter.notify(list);
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        logisticsorderFailTv.setVisibility(View.VISIBLE);
                    }
                }else {
                    recyclerView.setVisibility(View.GONE);
                    logisticsorderFailTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {
                recyclerView.setVisibility(View.GONE);
                logisticsorderFailTv.setVisibility(View.VISIBLE);
            }
        });
    }


}
