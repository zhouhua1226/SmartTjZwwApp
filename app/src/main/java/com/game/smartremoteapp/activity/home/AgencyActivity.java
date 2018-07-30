package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.AgencyAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.PromoteEarnBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/3/16 10:40
 * 修改人：
 * 修改时间：
 * 类描述：代理收益明细
 */
public class AgencyActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.agence_recyclerview)
    RecyclerView agenceRecyclerview;
    @BindView(R.id.agence_fail_tv)
    TextView agenceFailTv;

    private String TAG = "AgencyActivity--";
    private List<PromoteEarnBean> list = new ArrayList<>();
    private AgencyAdapter agencyAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agence;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getUserPromoteList(UserUtils.USER_ID);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        agencyAdapter = new AgencyAdapter(this, list);
        agenceRecyclerview.setAdapter(agencyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        agenceRecyclerview.setLayoutManager(linearLayoutManager);
        agenceRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getUserPromoteList(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        HttpManager.getInstance().getUserPromoteList(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    list = httpDataInfoResult.getData().getLogList();
                    if (list.size() > 0) {
                        agenceRecyclerview.setVisibility(View.VISIBLE);
                        agenceFailTv.setVisibility(View.GONE);
                        agencyAdapter.notify(list);
                    }else {
                        agenceRecyclerview.setVisibility(View.GONE);
                        agenceFailTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    agenceRecyclerview.setVisibility(View.GONE);
                    agenceFailTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {
                agenceRecyclerview.setVisibility(View.GONE);
                agenceFailTv.setVisibility(View.VISIBLE);
                MyToast.getToast(getApplicationContext(), "网络异常！").show();
            }
        });
    }

}
