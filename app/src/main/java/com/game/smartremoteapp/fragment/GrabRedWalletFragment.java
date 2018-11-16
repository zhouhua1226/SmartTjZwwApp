package com.game.smartremoteapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.GrabRedWalletAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.RoomBean;
import com.game.smartremoteapp.view.reshrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by mi on 2018/11/14.
 */

public class GrabRedWalletFragment extends BaseFragment {

    @BindView(R.id.grw_recyclerview)
    XRecyclerView mXRecyclerView;
    private String TAG="GrabRedWalletFragment";
    private GrabRedWalletAdapter playRecordAdapter;
    private List<RoomBean> list=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grab_redwallet;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initDate();
    }

    private void initDate() {
        playRecordAdapter = new GrabRedWalletAdapter(getContext(), list);
        mXRecyclerView.setLayoutManager( new GridLayoutManager(getContext(), 2));
        mXRecyclerView.setAdapter(playRecordAdapter);
        mXRecyclerView.setLoadingListener(onPullListener);
        mXRecyclerView.setPageSize(8);
        mXRecyclerView.setPullRefreshEnabled(true);
    }
    XRecyclerView.LoadingListener onPullListener =new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
        }
        @Override
        public void onLoadMore() {
        }
    };

}
