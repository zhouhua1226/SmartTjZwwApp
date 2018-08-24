package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.view.CtrlActivity;
import com.game.smartremoteapp.activity.ctrl.view.PushCoinActivity;
import com.game.smartremoteapp.activity.home.GameCenterActivity;
import com.game.smartremoteapp.activity.home.IntegralActivity;
import com.game.smartremoteapp.adapter.ZWWAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.BannerBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Marquee;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.RoomBean;
import com.game.smartremoteapp.bean.RoomListBean;
import com.game.smartremoteapp.bean.ToyTypeBean;
import com.game.smartremoteapp.bean.VideoBackBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.EmptyLayout;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.ShareDialog;
import com.game.smartremoteapp.view.ZwwHeadView;
import com.game.smartremoteapp.view.reshrecyclerview.XRecyclerView;
import com.gatz.netty.utils.NettyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hongxiu on 2017/9/25.
 */
public class ZWWJFragment extends BaseFragment   {
    private static final String TAG = "ZWWJFragment";
    @BindView(R.id.zww_recyclerview)
    XRecyclerView zwwRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.zww_exshop_ibtn)
    ImageButton zwwExshopIBtn;
    private List<RoomBean> currentRoomBeens = new ArrayList<>();
    private ZWWAdapter zwwAdapter;
    private String sessionId;
    private EmptyLayout.OnClickReTryListener onClickReTryListener;
    private List<VideoBackBean> playBackBeanList = new ArrayList<>();
    private List<Marquee> marquees = new ArrayList<>();
    private List<BannerBean> bannerList = new ArrayList<>();//轮播图接口返回BannerBean
    private List<BannerBean> nBannerList = new ArrayList<>();//轮播图BannerBean
    private List<String> list = new ArrayList<>();//轮播图url
    //分类参数
    private int currentSumPage = 1;
    private int currentPage = 1;
    private List<ToyTypeBean> toyTypeBeanList;
    private String currentType = "";  //首页
    private ZwwHeadView mZwwHeadView;
    private EmptyLayout zwwEmptylayout;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zww;
    }
    @OnClick({R.id.zww_exshop_ibtn,R.id.zww_integral_ibtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zww_exshop_ibtn:
                startActivity(new Intent(getContext(),IntegralActivity.class));
                //shareApp();
//                if (!newsUrl.equals("")) {
//                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
//                    intent.putExtra("newsurl", newsUrl);
//                    intent.putExtra("newstitle", newsTitle);
//                    startActivity(intent);
//                }else {
//                    MyToast.getToast(getContext(),"暂无活动！").show();
//                }
                break;
            case R.id.zww_integral_ibtn:
                //startActivity(new Intent(getContext(), IntegralActivity.class));
                startActivity(new Intent(getContext(), GameCenterActivity.class));
                break;
        }
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        getBannerList();
        getToyType();
    }

    private void getUserList() {
        HttpManager.getInstance().getUserList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> listRankBeanResult) {
                playBackBeanList = listRankBeanResult.getData().getPlayback();
                for (int i = 0; i < playBackBeanList.size(); i++) {
                    Marquee marquee = new Marquee();
                    String nickname = playBackBeanList.get(i).getNICKNAME();
                    if (nickname.length() > 10) {
                        nickname.substring(0, 10);
                    }
                    String s = "恭喜" + "<font color='#FF0000'>" + nickname + "</font>"
                            + "抓中一个" + playBackBeanList.get(i).getDOLL_NAME();
                    marquee.setTitle(s);
                    marquee.setImgUrl(UrlUtils.APPPICTERURL + playBackBeanList.get(i).getIMAGE_URL());
                    marquees.add(marquee);
                }
                if(marquees.size()>0) {
                    mZwwHeadView.setMarqueeview(marquees);
                }
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }
    private void initData() {

        mZwwHeadView=new ZwwHeadView(getActivity());
        zwwEmptylayout=new EmptyLayout(getActivity());
        zwwAdapter = new ZWWAdapter(getActivity(), currentRoomBeens);
        zwwAdapter.setmOnItemClickListener(onItemClickListener);
        zwwRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        zwwRecyclerview.addHeaderView(mZwwHeadView);
        zwwRecyclerview.setAdapter(zwwAdapter);
        zwwRecyclerview.setPullRefreshEnabled(false);
        zwwRecyclerview.setLoadingListener(onPullListener);
        if (onClickReTryListener != null) {
            zwwEmptylayout.setOnClickReTryListener(onClickReTryListener);
        }
        if (mZwwHeadView.getTabLayout() != null) {
            mZwwHeadView.getTabLayout().addOnTabSelectedListener(tabSelectedListener);
        }
    }

    public void notifyAdapter(List<RoomBean> rooms, int page) {
        currentRoomBeens = rooms;
        currentSumPage = page;
        zwwAdapter.notify(currentRoomBeens);
    }

    public void showError() {
        if(zwwEmptylayout!=null) {
            zwwRecyclerview.addFootView(zwwEmptylayout);
            zwwEmptylayout.showEmpty();
        }
    }

    public void dismissEmptyLayout() {
        if(zwwEmptylayout!=null) {
            zwwEmptylayout.dismiss();
        }
    }

    public void setSessionId(String id, boolean isReconnect) {
        this.sessionId = id;
        UserUtils.setNettyInfo(sessionId, UserUtils.USER_ID, "", isReconnect);
        UserUtils.doNettyConnect(NettyUtils.LOGIN_TYPE_TENCENT);
    }

    public ZWWAdapter.OnItemClickListener onItemClickListener =
            new ZWWAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    jumpRoom(position);
                }
            };

    private void enterNext(String name, String camera1, String camera2, boolean status, String gold, String id, String prob, String reward, String dollUrl) {
        Intent intent = new Intent(getActivity(), CtrlActivity.class);
        intent.putExtra(Utils.TAG_ROOM_NAME, name);
        intent.putExtra(Utils.TAG_URL_MASTER, camera1);
        intent.putExtra(Utils.TAG_URL_SECOND, camera2);
        intent.putExtra(Utils.TAG_ROOM_STATUS, status);
        intent.putExtra(Utils.TAG_DOLL_GOLD, gold);
        intent.putExtra(Utils.TAG_DOLL_Id, id);
        intent.putExtra(Utils.TAG_ROOM_PROB, prob);
        intent.putExtra(Utils.TAG_ROOM_REWARD, reward);
        intent.putExtra(Utils.TAG_ROOM_DOLLURL, dollUrl);
        startActivity(intent);

    }

    /**
     * 房间跳转方法
     *
     * @param po
     */
    private void jumpRoom(int po) {
        if ((currentRoomBeens.size() > 0) && (!Utils.isEmpty(sessionId))) {
            String room_id = currentRoomBeens.get(po).getDollId();
            boolean room_status = false;
            UserUtils.setNettyInfo(sessionId, UserUtils.USER_ID, room_id, false);
            if (currentRoomBeens.get(po).getDollState().equals("0")) {
                room_status = true;
            } else if (currentRoomBeens.get(po).getDollState().equals("1")) {
                room_status = false;
            }
            String rtmpUrl1 = currentRoomBeens.get(po).getCameras().get(0).getRtmpUrl();
            String rtmpUrl2 = currentRoomBeens.get(po).getCameras().get(1).getRtmpUrl();
            String serviceName1 = currentRoomBeens.get(po).getCameras().get(0).getServerName();
            String serviceName2 = currentRoomBeens.get(po).getCameras().get(1).getServerName();
            String liveStream1 = currentRoomBeens.get(po).getCameras().get(0).getLivestream();
            String liveStream2 = currentRoomBeens.get(po).getCameras().get(1).getLivestream();
            String idToken = "?token=" + UserUtils.SRSToken.getToken()
                    + "&expire=" + UserUtils.SRSToken.getExpire()
                    + "&tid=" + UserUtils.SRSToken.getTid()
                    + "&time=" + UserUtils.SRSToken.getTime()
                    + "&type=" + UserUtils.SRSToken.getType()
                    + "/";
            String url1 = rtmpUrl1 + serviceName1 + idToken + liveStream1;
            String url2 = rtmpUrl2 + serviceName2 + idToken + liveStream2;
            LogUtils.loge("房间推流地址1=" + url1);
            LogUtils.loge("房间推流地址2=" + url2);
            if (!TextUtils.isEmpty(url2) && !TextUtils.isEmpty(url1)) {
                String type = currentRoomBeens.get(po).getDeviceType();
                if (!TextUtils.isEmpty(type) && type.equals("2")) {
                    //TODO 推币机处理
                    enterCoinNext(url1, url2);
                    return;
                }
                enterNext(currentRoomBeens.get(po).getDollName(),
                        url1, url2,
                        room_status,
                        String.valueOf(currentRoomBeens.get(po).getDollGold()),
                        currentRoomBeens.get(po).getDollId(),
                        currentRoomBeens.get(po).getProb(),
                        currentRoomBeens.get(po).getReward(),
                        currentRoomBeens.get(po).getDollUrl());
            } else {
                LogUtils.loge("当前设备没有配置摄像头!");
            }
        }
    }


    //TODO 正式环境统一处理
    private void enterCoinNext(String camera1, String camera2) {
        Intent intent = new Intent(getActivity(), PushCoinActivity.class);
        intent.putExtra(Utils.TAG_URL_MASTER, camera1);
        intent.putExtra(Utils.TAG_URL_SECOND, camera2);
        startActivity(intent);
    }

    private void getBannerList() {
        HttpManager.getInstance().getBannerList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    bannerList = loginInfoResult.getData().getRunImage();
                    if (bannerList.size() > 0) {
                        for (int i = 0; i < bannerList.size(); i++) {
                            int state = bannerList.get(i).getSTATE();
                            switch (state){
                                case 0:
                                        if(bannerList.get(i).getIMAGE_URL()!=null&&!bannerList.get(i).getIMAGE_URL().equals("")) {
                                             list.add(UrlUtils.APPPICTERURL+bannerList.get(i).getIMAGE_URL());
                                             nBannerList.add(bannerList.get(i));
                                        }
                                    break;
                                case 1:
                                    break;
                            }
                        }
                       mZwwHeadView.initBanner(list,nBannerList);
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.loge(e.getMessage());
            }
        });
    }

    private List<RoomBean> dealWithRoomStats(List<RoomBean> beens) {
        if (beens.size() == 0) {
            return beens;
        }
        for (int i = 0; i < beens.size(); i++) {
            RoomBean bean = beens.get(i);
            bean = UserUtils.dealWithRoomStatus(bean, bean.getDollState());
            beens.set(i, bean);
        }
        return beens;
    }
    /**
     * 获取娃娃机类型
     */
    private void getToyType() {
        HttpManager.getInstance().getToyType(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals("success")) {
                    if (result.getData() != null&& mZwwHeadView.getTabLayout()!=null) {
                        toyTypeBeanList = result.getData().getToyTypeList();
                        mZwwHeadView.getTabLayout().addTab( mZwwHeadView.getTabLayout().newTab().
                                setText("全部"), 0);  //保证一定会有全部按钮
                        if (toyTypeBeanList != null) {
                            for (int i = 0; i < toyTypeBeanList.size(); i++) {
                                mZwwHeadView.getTabLayout().addTab( mZwwHeadView.getTabLayout().newTab().
                                        setText(toyTypeBeanList.get(i).getTOY_TYPE()), i + 1);
                            }
                            if (toyTypeBeanList.size() > 5) {
                                mZwwHeadView.getTabLayout().setTabMode(TabLayout.MODE_SCROLLABLE);
                            } else {
                                mZwwHeadView.getTabLayout().setTabMode(TabLayout.MODE_FIXED);
                            }
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
    /**
     * 根据类型获取娃娃机
     */
    private void getToysByType(String type, int page) {
        dismissEmptyLayout();
        HttpManager.getInstance().getToyListByType(type, page, new RequestSubscriber<Result<RoomListBean>>() {
            @Override
            public void _onSuccess(Result<RoomListBean> loginInfoResult) {
                zwwRecyclerview.stopLoadMore();
                if (loginInfoResult.getMsg().equals("success")) {
                    if (loginInfoResult.getData() != null) {
                        List<RoomBean> roomBeens = dealWithRoomStats(loginInfoResult.getData().getDollList());
                        if (currentRoomBeens.size() == 0) {
                            currentRoomBeens = roomBeens;
                        } else {
                            //TODO 增加的
                            currentRoomBeens.addAll(roomBeens);
                        }
                        Collections.sort(currentRoomBeens, new Comparator<RoomBean>() {
                            @Override
                            public int compare(RoomBean t1, RoomBean t2) {
                                return t2.getDollState().compareTo(t1.getDollState());
                            }
                        });
                        zwwAdapter.notify(currentRoomBeens);
                        currentSumPage = loginInfoResult.getData().getPd().getTotalPage();

                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
                zwwRecyclerview.stopLoadMore();
            }
        });
    }
    private TabLayout.OnTabSelectedListener tabSelectedListener
            = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int pos = tab.getPosition();
            currentPage = 1;
            if (pos == 0) {
                currentType = "";
            } else {
                currentType = String.valueOf(toyTypeBeanList.get(pos - 1).getID());
            }
            currentRoomBeens.clear();
            getToysByType(currentType, currentPage);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    /**
     * 上拉加载、下拉刷新监听
     */
    private XRecyclerView.LoadingListener onPullListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {}
        @Override
        public void onLoadMore() {
            if (Utils.isNetworkAvailable(getContext())) {
                currentPage++;
                if (currentPage > currentSumPage) {
                    //TODO 无更多了
                 //   MyToast.getToast(getContext(), "没有更多啦！").show();
                  zwwRecyclerview.stopLoadMore();
                  //  zwwRecyclerview.noMoreLoading();
                    return;
                }
                getToysByType(currentType, currentPage);
            } else {
                MyToast.getToast(getContext(), "网络连接异常，请检查网络").show();
            }
        }
    };

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onResume() {
        super.onResume();
        getUserList();
    }
    //分享
    private void shareApp() {
        new ShareDialog(getContext(),new ShareDialog.OnShareSuccessOnClicker() {
            @Override
            public void onShareSuccess() {

            }
        });
    }

}

