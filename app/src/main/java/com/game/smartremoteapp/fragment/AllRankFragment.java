package com.game.smartremoteapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.ListRankAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.ListRankBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.UserBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.RankHeadView;
import com.game.smartremoteapp.view.SelectRnakTypeView;
import com.game.smartremoteapp.view.reshrecyclerview.XRecyclerView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
/**
 * Created by mi on 2018/9/6.
 */

public class AllRankFragment extends BaseFragment {
   @BindView(R.id.tv_bank_order)
    TextView bank_order;
    @BindView(R.id.rg_bank)
    RadioGroup mRadioGroup;
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;
    @BindView(R.id.bank_recyclerview)
    XRecyclerView mXRecyclerView;

    private RankHeadView mBankHeadView;
    private ListRankAdapter listRankAdapter;
    private List<UserBean> rankList = new ArrayList<>();
    private int selectIndex=1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_rank;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initaddTab();
        initData();
    }
    private void initaddTab() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
            }
        });
        bank_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SelectRnakTypeView(getActivity(), new SelectRnakTypeView.OnDismissListListener() {
                    @Override
                    public void onnDismissList(int index) {
                        selectIndex= index;
                    }
                });
            }
        });
    }

    private void initData() {
        mBankHeadView=new RankHeadView(getActivity());
        listRankAdapter = new ListRankAdapter(getContext(), rankList,1);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mXRecyclerView.addHeaderView(mBankHeadView);
        mXRecyclerView.setAdapter(listRankAdapter);
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setLoadingListener(onPullListener);
    }


    /**
     * 上拉加载、下拉刷新监听
     */
    private XRecyclerView.LoadingListener onPullListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {

        }
        @Override
        public void onLoadMore() {}
    };
    private void getRankBetTodayList(String userId){
        HttpManager.getInstance().getRankBetTodayList(userId, new RequestSubscriber<Result<ListRankBean>>() {
            @Override
            public void _onSuccess(Result<ListRankBean> result) {
                if(result.getMsg().equals(Utils.HTTP_OK)){
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void getRankDollTodayList(String userId){
        HttpManager.getInstance().getRankDollTodayList(userId, new RequestSubscriber<Result<ListRankBean>>() {
            @Override
            public void _onSuccess(Result<ListRankBean> result) {
                if(result.getMsg().equals(Utils.HTTP_OK)) {
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
}
