package com.game.smartremoteapp.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.PlayRecordAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.GameListBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yincong on 2018/4/3 13:17
 * 修改人：
 * 修改时间：
 * 类描述：房间游戏记录
 */
public class PlayRecordFragment extends BaseFragment {

    @BindView(R.id.playrecord_recyclerview)
    RecyclerView playrecordRecyclerview;
    @BindView(R.id.fail_tv)
    TextView failTv;

    private String TAG="PlayRecordFragment";
    private PlayRecordAdapter playRecordAdapter;
    private List<GameListBean> list=new ArrayList<>();
    public String roomId="";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playrecord;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initDate();
        getRoomGamelist(roomId);
    }

    private void initDate() {
        playRecordAdapter = new PlayRecordAdapter(getContext(), list);
        playrecordRecyclerview.setAdapter(playRecordAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        playrecordRecyclerview.setLayoutManager(linearLayoutManager);
        playrecordRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setRoomId(String roId){
        this.roomId=roId;
    }

    private void getRoomGamelist(String roomId){
        if(roomId.equals("")){
            MyToast.getToast(getContext(),"房间ID为空！");
            return;
        }
        HttpManager.getInstance().getRoomGamelist(roomId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)){
                    list=httpDataInfoResult.getData().getGameList();
                    if(list.size()>0){
                        playrecordRecyclerview.setVisibility(View.VISIBLE);
                        failTv.setVisibility(View.GONE);
                        playRecordAdapter.notify(list);
                    }else {
                        playrecordRecyclerview.setVisibility(View.GONE);
                        failTv.setVisibility(View.VISIBLE);
                    }
                }else {
                    if(playrecordRecyclerview!=null) {
                        playrecordRecyclerview.setVisibility(View.GONE);
                        failTv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
                if(playrecordRecyclerview!=null) {
                    playrecordRecyclerview.setVisibility(View.GONE);
                    failTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
