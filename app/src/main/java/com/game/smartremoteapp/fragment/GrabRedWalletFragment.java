package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.PerfectInformationActivity;
import com.game.smartremoteapp.activity.home.RedWalletDetailActivity;
import com.game.smartremoteapp.adapter.GrabRedWalletAdapter;
import com.game.smartremoteapp.adapter.ZWWAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Marquee;
import com.game.smartremoteapp.bean.RedPackageBean;
import com.game.smartremoteapp.bean.RedPackageListBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.SlideRedPackageBean;
import com.game.smartremoteapp.bean.UserInfoBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.EmptyView;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.NoticeDialog;
import com.game.smartremoteapp.view.RedWalletHeaderView;
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
    private GrabRedWalletAdapter mAdapter;
    private List<RedPackageBean> list=new ArrayList<>();
    private RedWalletHeaderView mRedWalletHeaderView;
    private List<Marquee> marquees = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grab_redwallet;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initDate();
    }

    private void initDate() {
        mRedWalletHeaderView=new RedWalletHeaderView(getActivity());
        mAdapter = new GrabRedWalletAdapter(getContext(), list);
        mXRecyclerView.addHeaderView(mRedWalletHeaderView);
        mXRecyclerView.setEmptyView(new EmptyView(getContext(),"暂无红包可抢！"));
        mXRecyclerView.setLayoutManager( new GridLayoutManager(getContext(), 2));
        mXRecyclerView.setAdapter(mAdapter);
        mXRecyclerView.setLoadingListener(onPullListener);
        mXRecyclerView.setPageSize(8);
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);

        mAdapter.setmOnItemClickListener(new ZWWAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RedPackageBean mRedPackageBean=list.get(position);
                getRedPackage(mRedPackageBean);
            }
        });
        showRedPackage(UserUtils.USER_ID);
        getSlideShowList();
    }
  private void getRedPackage(RedPackageBean mRedPackageBean){
      if(UserUtils.AGE>0&&!UserUtils.WXACCOUNT.equals("")){
          if(UserUtils.GENDER.equals(UserUtils.GENDER_FEMALE)){//女性
              if(UserUtils.USER_ID.equals(mRedPackageBean.getUserId())||isReword(mRedPackageBean.getUserInfo())){
                  setToRedWalletDetailActivity(mRedPackageBean.getId());
              }else{
                  gradRedWalletnotice(UserUtils.USER_ID,mRedPackageBean.getUserId(),mRedPackageBean.getId(),mRedPackageBean.getNickName());

              }
          }else{
              MyToast.getToast(getContext(), "抱歉，只限制女性抢红包").show();
              setToRedWalletDetailActivity(mRedPackageBean.getId());
          }
      }else{
          startActivity(new Intent(getContext(), PerfectInformationActivity.class));
      }
  }

//    @Override
//    public void onResume() {
//        super.onResume();
//        LogUtils.loge("游戏记录查询userId="+ UserUtils.USER_ID,TAG);
//        if(list!=null&&list.size()>0&&mAdapter!=null){
//            list.clear();
//            mAdapter.notifyDataSetChanged();
//        }
//        showRedPackage(UserUtils.USER_ID);
//        getSlideShowList();
//    }

    private  void setToRedWalletDetailActivity(String redId){
      Intent intent=new Intent(getActivity(), RedWalletDetailActivity.class);
      intent.putExtra(UrlUtils.REDID,redId);
      Utils.toActivity(getActivity(), intent);
  }
    private boolean isReword(List<UserInfoBean> userInfo){
        boolean isReword=false;
        for (UserInfoBean mUserInfoBean:userInfo) {
            if(UserUtils.USER_ID.equals(mUserInfoBean.getUserId())){
                isReword= true;
            }
        }
        return isReword;
    }
    XRecyclerView.LoadingListener onPullListener =new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
             list.clear();
             mAdapter.notifyDataSetChanged();
             showRedPackage(UserUtils.USER_ID);
        }
        @Override
        public void onLoadMore() {
        }
    };


 private void showRedPackage(String userId){
     HttpManager.getInstance().showRedPackage(userId,new RequestSubscriber<Result<RedPackageListBean>>() {
         @Override
         public void _onSuccess(Result<RedPackageListBean> loginInfoResult) {
             setZwwRecyclerview();
             if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                 if(loginInfoResult.getData().getList()!=null){
                     list.addAll(loginInfoResult.getData().getList());
                 }
                 mAdapter.notify(list);
             }
         }
         @Override
         public void _onError(Throwable e) {
             setZwwRecyclerview();
         }
     });
 }
    private void setZwwRecyclerview(){
        if(mXRecyclerView!=null) {
            mXRecyclerView.refreshComplete();
        }
    }
    private void gradRedWalletnotice(final String userId, final String redUserId, final String redId, String nickName){
          NoticeDialog mNoticeDialog = new NoticeDialog(getActivity(), R.style.activitystyle);
          mNoticeDialog.show();
          mNoticeDialog.setDialogTitle(nickName);
          mNoticeDialog.setDialogResultListener(new NoticeDialog.DialogResultListener() {
            @Override
            public void result() {
                getRedPackage(userId,redUserId,redId);
            }
        });
    }
    private void getRedPackage(String userId, String redUserId, final String redId){
        HttpManager.getInstance().getRedPackage(userId,redUserId,redId,new RequestSubscriber<Result<Void>>() {
            @Override
            public void _onSuccess(Result<Void> loginInfoResult) {
                if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    MyToast.getToast(getActivity(),"恭喜你红包金币领取成功！").show();
                }else{
                    MyToast.getToast(getActivity(),"红包金币已经抢完了！").show();
                }
                Intent intent=new Intent(getActivity(), RedWalletDetailActivity.class);
                intent.putExtra(UrlUtils.REDID,redId);
                Utils.toActivity(getActivity(), intent);
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void getSlideShowList() {
        HttpManager.getInstance().getSlideShowList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> listRankBeanResult) {
                if (listRankBeanResult.getMsg().equals(Utils.HTTP_OK)) {
                    List<SlideRedPackageBean>   playBackBeanList = listRankBeanResult.getData().getList();
                    for (int i = 0; i < playBackBeanList.size(); i++) {
                        Marquee marquee = new Marquee();
                        String nickname = playBackBeanList.get(i).getNICKNAME();
                        if (nickname.length() > 10) {
                            nickname.substring(0, 10);
                        }
                        String s = "恭喜" + "<font color='#FFAE00'>" + nickname + "</font>"
                                + "抢红包获得" +"<font color='#FFAE00'>" + playBackBeanList.get(i).getGOLD() +"金币"+ "</font>";
                        marquee.setTitle(s);
                        marquees.add(marquee);
                    }
                    if (marquees.size() > 0) {
                        mRedWalletHeaderView.setMarqueeview(marquees,false);
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }
}
