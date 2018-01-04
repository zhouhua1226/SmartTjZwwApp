package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.InformationActivity;
import com.game.smartremoteapp.activity.home.RecordGameActivty;
import com.game.smartremoteapp.activity.home.RecordGameTwoActivity;
import com.game.smartremoteapp.activity.home.SelectRechargeTypeActiivty;
import com.game.smartremoteapp.activity.home.SettingActivity;
import com.game.smartremoteapp.activity.wechat.WeChatPayActivity;
import com.game.smartremoteapp.adapter.MyCenterAdapter;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.LoginInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.VideoBackBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.FillingCurrencyDialog;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by hongxiu on 2017/9/25.
 */
public class MyCenterFragment extends BaseFragment {

    private static final String TAG = "MyCenterFragment";
    Unbinder unbinder;
    @BindView(R.id.image_kefu)
    ImageButton imageKefu;
    @BindView(R.id.image_setting)
    ImageButton imageSetting;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_number)
    TextView userNumber;
    @BindView(R.id.user_filling)
    TextView userFilling;
    @BindView(R.id.mycenter_recyclerview)
    RecyclerView mycenterRecyclerview;
    @BindView(R.id.header)
    RecyclerViewHeader header;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.mycenter_none_tv)
    TextView mycenterNoneTv;
    Unbinder unbinder1;
    private FillingCurrencyDialog fillingCurrencyDialog;
    private MyCenterAdapter myCenterAdapter;
    private List<String> list;
    private List<VideoBackBean> videoList = new ArrayList<>();
    private List<VideoBackBean> videoReList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_center;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();
        initData();
        onClick();
    }

    private void onClick() {
        myCenterAdapter.setOnItemClickListener(new MyCenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), RecordGameActivty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("record", videoList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                startActivity(new Intent(getContext(), RecordGameTwoActivity.class));
            }
        });

//        下拉刷新
        swiperefresh.setColorSchemeResources(R.color.pink);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserImageAndName();
                        myCenterAdapter.notifyDataSetChanged();
                        swiperefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

    private void initData() {
        myCenterAdapter = new MyCenterAdapter(getContext(), videoList);
        mycenterRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mycenterRecyclerview.addItemDecoration(new SpaceItemDecoration(15));
        mycenterRecyclerview.setAdapter(myCenterAdapter);
        header.attachTo(mycenterRecyclerview);

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDate(UserUtils.USER_ID);
        getVideoBackList(UserUtils.USER_ID);
    }

    private void getUserImageAndName() {
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            if (!UserUtils.NickName.equals("")) {
                userName.setText(UserUtils.NickName);
            } else {
                userName.setText(UserUtils.UserPhone);
            }
            Glide.with(getContext())
                    .load(UserUtils.UserImage)
                    .dontAnimate()
                    .transform(new GlideCircleTransform(getContext()))
                    .into(userImage);
        } else {
            userName.setText("请登录");
            userImage.setImageResource(R.drawable.round);
        }
    }

    @OnClick({R.id.image_kefu, R.id.image_setting, R.id.user_image, R.id.user_filling, R.id.user_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_kefu:
                MyToast.getToast(getContext(), "我是客服").show();
                break;
            case R.id.image_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.user_image:
                startActivity(new Intent(getContext(), InformationActivity.class));
                break;
            case R.id.user_filling:
                startActivity(new Intent(getContext(), SelectRechargeTypeActiivty.class));
//                getMoney();
                break;
            case R.id.user_name:
                //此处添加登录dialog
                break;
            default:
                break;
        }
    }

    private void getMoney() {
        fillingCurrencyDialog = new FillingCurrencyDialog(getContext(), R.style.easy_dialog_style);
        fillingCurrencyDialog.show();
        fillingCurrencyDialog.setDialogClickListener(myDialogClick);
    }

    private FillingCurrencyDialog.MyDialogClick myDialogClick = new FillingCurrencyDialog.MyDialogClick() {
        @Override
        public void getMoney10(String money) {
            Intent intent = new Intent(getContext(), WeChatPayActivity.class);
            intent.putExtra("money", money);
            getContext().startActivity(intent);
        }

        @Override
        public void getMoney20(String money) {
            Intent intent = new Intent(getContext(), WeChatPayActivity.class);
            intent.putExtra("money", money);
            getContext().startActivity(intent);
        }

        @Override
        public void getMoney50(String money) {
            Intent intent = new Intent(getContext(), WeChatPayActivity.class);
            intent.putExtra("money", money);
            getContext().startActivity(intent);
        }

        @Override
        public void getMoney100(String money) {
            Intent intent = new Intent(getContext(), WeChatPayActivity.class);
            intent.putExtra("money", money);
            getContext().startActivity(intent);
        }
    };

    private void getUserDate(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<LoginInfo>>() {
            @Override
            public void _onSuccess(Result<LoginInfo> result) {
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                UserUtils.UserImage = UrlUtils.USERFACEIMAGEURL + result.getData().getAppUser().getIMAGE_URL();
                Log.e(TAG, "个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                        + "抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                        + "昵称=" + result.getData().getAppUser().getNICKNAME()
                        + "头像=" + UserUtils.UserImage);
                getUserImageAndName();
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void getVideoBackList(String userId) {
        HttpManager.getInstance().getVideoBackList(userId, new RequestSubscriber<Result<LoginInfo>>() {
            @Override
            public void _onSuccess(Result<LoginInfo> result) {
                videoList= result.getData().getPlayback();
                Utils.showLogE("video记录列表", "result=" + result.getMsg()+"="+videoList.size());
                if(videoList.size()!=0) {
                    mycenterNoneTv.setVisibility(View.GONE);
                    myCenterAdapter.notify(videoList);
                }else {
                    mycenterRecyclerview.setVisibility(View.GONE);
                    mycenterNoneTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    //记录数据重组   11/28 17:55
    private List<VideoBackBean> getCatchNum(List<VideoBackBean> list, List<VideoBackBean> reList) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDOLL_NAME().equals(reList.get(i).getDOLL_NAME())) {
                list.get(i).setCOUNT(reList.get(i).getCOUNT());
            } else {
                for (int j = 0; j < reList.size(); j++) {
                    if (reList.get(j).getDOLL_NAME().equals(list.get(i).getDOLL_NAME())) {
                        list.get(i).setCOUNT(reList.get(j).getCOUNT());
                    }
                }
            }
        }
        return list;
    }

    //记录重复赛选
    public List<VideoBackBean> removeDuplicate(List<VideoBackBean> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getDOLL_NAME().equals(list.get(i).getDOLL_NAME())) {
                    list.remove(j);
                }
            }
        }

        return list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }
}
