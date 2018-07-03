package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartremoteapp.activity.ctrl.view.CtrlActivity;
import com.game.smartremoteapp.activity.ctrl.view.IctrlView;
import com.game.smartremoteapp.activity.ctrl.view.PushCoinActivity;
import com.game.smartremoteapp.activity.home.JoinEarnActivity;
import com.game.smartremoteapp.activity.home.NewsWebActivity;
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
import com.game.smartremoteapp.view.GlideImageLoader;
import com.game.smartremoteapp.view.MarqueeView;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.PullToRefreshView;
import com.game.smartremoteapp.view.SpaceItemDecoration;
import com.gatz.netty.utils.NettyUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.attr.name;

/**
 * Created by hongxiu on 2017/9/25.
 */
public class ZWWJFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener,IctrlView {
    private static final String TAG = "ZWWJFragment";
    @BindView(R.id.zww_recyclerview)
    RecyclerView zwwRecyclerview;
    @BindView(R.id.zww_emptylayout)
    EmptyLayout zwwEmptylayout;
    @BindView(R.id.marqueeview)
    MarqueeView marqueeview;
    @BindView(R.id.type_tly)
    TabLayout typeTabLayout;
    Unbinder unbinder;
    @BindView(R.id.mPullToRefreshView)
    PullToRefreshView mPullToRefreshView;
    @BindView(R.id.zww_guess_btn)
    ImageButton zwwGuessBtn;
    Unbinder unbinder1;
    @BindView(R.id.zww_exshop_ibtn)
    ImageButton zwwExshopIBtn;
    @BindView(R.id.zww_earnmoney_ibtn)
    ImageButton zwwEarnmoneyIBtn;
//    @BindView(R.id.sfv_player)
//    MySurfaceVivew mPlayerView;
    @BindView(R.id.rl_marqueeview)
    RelativeLayout  mArqueeView;
    @BindView(R.id.zww_banner)
    Banner zwwBanner;
    @BindView(R.id.zww_news_tv)
    TextView zwwNewsTv;
    private List<RoomBean> currentRoomBeens = new ArrayList<>();
    private ZWWAdapter zwwAdapter;
    private String sessionId;
    private EmptyLayout.OnClickReTryListener onClickReTryListener;
    private List<VideoBackBean> playBackBeanList = new ArrayList<>();
    private List<Marquee> marquees = new ArrayList<>();
    private List<BannerBean> bannerList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    //分类参数
    private int currentSumPage = 1;
    private int currentPage = 1;
    private List<ToyTypeBean> toyTypeBeanList;
    private String currentType = "";  //首页
    private CtrlCompl ctrlCompl=null;
    private String url1=null;
    private String newsUrl="";
    private String newsTitle="";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zww;
    }
    @OnClick({R.id.zww_exshop_ibtn, R.id.zww_guess_btn,R.id.zww_earnmoney_ibtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zww_exshop_ibtn:
                //startActivity(new Intent(getContext(), ExChangeShopActivity.class));
                if (!newsUrl.equals("")) {
                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
                    intent.putExtra("newsurl", newsUrl.replace("\"", "/"));
                    intent.putExtra("newstitle", newsTitle);
                    startActivity(intent);
                }else {
                    MyToast.getToast(getContext(),"暂无活动！").show();
                }
                break;
            case R.id.zww_guess_btn:
                jumpRoom(0);
                break;
            case R.id.zww_earnmoney_ibtn:
                //MyToast.getToast(getContext(),"功能研发中！").show();
                startActivity(new Intent(getContext(), JoinEarnActivity.class));
                break;
            case R.id.zww_news_tv:
                if (!newsUrl.equals("")) {
                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
                    intent.putExtra("newsurl", newsUrl.replace("\"", "/"));
                    intent.putExtra("newstitle", newsTitle);
                    startActivity(intent);
                }else {
                    MyToast.getToast(getContext(),"暂无活动！").show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        onClick();
        NettyUtils.sendRoomInCmd();
        getBannerList();
        getToyType();
        getUserList();
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
                    mArqueeView.setVisibility(View.VISIBLE);
                    marqueeview.setImage(true);
                    marqueeview.startWithList(marquees);
                }
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }
    private void initData() {
        dismissEmptyLayout();
        zwwAdapter = new ZWWAdapter(getActivity(), currentRoomBeens);
        zwwAdapter.setmOnItemClickListener(onItemClickListener);
        zwwRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        if (Utils.getWidthSize(getContext()) < 720) {
            zwwRecyclerview.addItemDecoration(new SpaceItemDecoration(6));
        } else {
            zwwRecyclerview.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.PX_10)));
        }
        zwwRecyclerview.setHasFixedSize(true);
        zwwRecyclerview.setNestedScrollingEnabled(false);
        zwwRecyclerview.setAdapter(zwwAdapter);
        if (onClickReTryListener != null) {
            zwwEmptylayout.setOnClickReTryListener(onClickReTryListener);
        }
        typeTabLayout.addOnTabSelectedListener(tabSelectedListener);
        mArqueeView.getBackground().setAlpha(120);

    }
    private void onClick() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }
    public void notifyAdapter(List<RoomBean> rooms, int page) {
        currentRoomBeens = rooms;
        currentSumPage = page;
        zwwAdapter.notify(currentRoomBeens);
    }

    public void showError() {
        zwwEmptylayout.showEmpty();
    }

    public void dismissEmptyLayout() {
        zwwEmptylayout.dismiss();
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


    //TODO 正式环境统一处理
    private void enterCoinNext(String dollName,  String camera1, String camera2,String roomId) {
        Intent intent = new Intent(getActivity(), PushCoinActivity.class);
        intent.putExtra(Utils.TAG_URL_MASTER, camera1);
        intent.putExtra(Utils.TAG_URL_SECOND, camera2);
        intent.putExtra(Utils.TAG_ROOM_NAME, name);
        intent.putExtra(Utils.TAG_DOLL_Id, roomId);
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

    //banner轮播
    private void initBanner(List<String> lists) {

        //设置Banner样式
        zwwBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
         //设置图片加载器
        zwwBanner.setImageLoader(new GlideImageLoader());
         zwwBanner.setImages(lists);
        //设置Banner动画效果
       zwwBanner.setBannerAnimation(Transformer.DepthPage);
       //设置轮播时间
        zwwBanner.setDelayTime(2000);
        //设置指示器位置(当banner模式中有指示器时)
        zwwBanner.setIndicatorGravity(BannerConfig.CENTER);
       //Banner设置方法全部调用完毕时最后调用
       zwwBanner.start();
        zwwBanner.setOnBannerListener(new OnBannerListener() {
            @Override
           public void OnBannerClick(int position) {
                //MyToast.getToast(getContext(), "您点击了第" + (position + 1) + "张图片").show();
//                if (!bannerList.get(position).getHREF_ST().equals("")) {
//                   Intent intent = new Intent(getContext(), NewsWebActivity.class);
//                    intent.putExtra("newsurl", bannerList.get(position).getHREF_ST().replace("\"", "/"));
//                    intent.putExtra("newstitle", bannerList.get(position).getRUN_NAME());
//                    startActivity(intent);
//              }
           }
        });
    }

    private void getBannerList() {
        ctrlCompl = new CtrlCompl(this, getActivity());
        NettyUtils.pingRequest(); //判断连接
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
                                        if(bannerList.get(i).getHREF_ST()==null||bannerList.get(i).getHREF_ST().equals("")) {
                                             list.add(UrlUtils.APPPICTERURL+bannerList.get(i).getIMAGE_URL());
                                        }else{
                                            newsUrl =bannerList.get(i).getHREF_ST() ;
                                            newsTitle = bannerList.get(i).getRUN_NAME();
                                        }
                                    break;
                                case 1:
 //                                   if (bannerList.get(i).getDEVICE_STATE().equals("0") && bannerList.get(i).getRTMP_URL() != null) {
//                                      setSessionId(UserUtils.sessionID,false);
//                                    initSurface(bannerList.get(i));
//                                 }
                                    break;
                            }
                        }
                        initBanner(list);
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.loge(e.getMessage());
            }
        });
    }
//    private void initSurface(BannerBean mBannerBean) {
//        mPlayerView.setZOrderMediaOverlay(true);
//        mPlayerView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        String rtmpUrl = mBannerBean.getRTMP_URL();
//        String serviceName = mBannerBean.getSERVER_NAME();
//        String liveStream = mBannerBean.getLIVESTREAM();
//        url1 = rtmpUrl + serviceName + "/"+liveStream;
//        LogUtils.loge("url1===="+url1);
//        ctrlCompl.startPlayVideo(mPlayerView, url1);
//        mPlayerView.getHolder().addCallback(this);
//        mPlayerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//
//    }
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
    private void getToyType() {
        HttpManager.getInstance().getToyType(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals("success")) {
                    if (result.getData() != null) {
                        toyTypeBeanList = result.getData().getToyTypeList();
                        typeTabLayout.addTab(typeTabLayout.newTab().
                                setText("全部"), 0);  //保证一定会有全部按钮
                        if (toyTypeBeanList != null) {
                            for (int i = 0; i < toyTypeBeanList.size(); i++) {
                                typeTabLayout.addTab(typeTabLayout.newTab().
                                        setText(toyTypeBeanList.get(i).getTOY_TYPE()), i + 1);
                            }
                            if (toyTypeBeanList.size() > 5) {
                                typeTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            } else {
                                typeTabLayout.setTabMode(TabLayout.MODE_FIXED);
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
    private void getToysByType(String type, int page) {
        HttpManager.getInstance().getToyListByType(type, page, new RequestSubscriber<Result<RoomListBean>>() {
            @Override
            public void _onSuccess(Result<RoomListBean> loginInfoResult) {
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
                        if (currentRoomBeens.size() > 2) {
                            mPullToRefreshView.setIsFooterView(true);
                        } else {
                            mPullToRefreshView.setIsFooterView(false);
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {

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

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkAvailable(getContext())) {
                    currentPage++;
                    if (currentPage > currentSumPage) {
                        //TODO 无更多了
                        MyToast.getToast(getContext(), "没有更多啦！").show();
                        mPullToRefreshView.onFooterRefreshComplete();
                        return;
                    }
                    getToysByType(currentType, currentPage);
                } else {
                    MyToast.getToast(getContext(), "网络连接异常，请检查网络").show();
                }
                mPullToRefreshView.onFooterRefreshComplete();
            }
        }, 1500);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkAvailable(getContext())) {

                } else {
                    MyToast.getToast(getContext(), "网络连接异常，请检查网络").show();
                }
                mPullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1500);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void getTime(int time) {
    }
    @Override
    public void getTimeFinish() {
    }
    @Override
    public void getUserInfos(List<String> list, boolean is) {

    }
    @Override
    public void getRecordErrCode(int code) {
        LogUtils.loge("录制视频失败::::::" + code, TAG);
    }

    @Override
    public void getRecordSuecss() {
        LogUtils.loge("录制视频完毕......", TAG);
    }

    @Override
    public void getRecordAttributetoNet(String time, String fileName) {
        LogUtils.loge("视频上传的时间::::" + time + "=====" + fileName, TAG);
    }

    @Override
    public void getPlayerErcErrCode(int code) {
       // LogUtils.loge("直播失败,错误码:::::" + code, TAG);
    }
    @Override
    public void getPlayerSucess() {
        LogUtils.loge("直播Sucess:::::", TAG);
    }
    @Override
    public void getVideoPlayConnect() {
    }
    @Override
    public void getVideoPlayStart() {
    }
    @Override
    public void getVideoStop() {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();
        ctrlCompl = null;
        unbinder1.unbind();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onResume() {
        super.onResume();
        getUserList();
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        LogUtils.loge("surfaceCreated=== ------",TAG);
//        if(ctrlCompl!=null&&url1!=null){
//            ctrlCompl.startPlayVideo(mPlayerView,url1);
//        }
//    }
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//        LogUtils.loge("surfaceChanged=== ----",TAG);
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        ctrlCompl.stopPlayVideo();
//    }

}

