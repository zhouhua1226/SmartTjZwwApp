package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.AccountDetailActivity;
import com.game.smartremoteapp.activity.home.AccountInformationActivity;
import com.game.smartremoteapp.activity.home.AgencyActivity;
import com.game.smartremoteapp.activity.home.BetRecordActivity;
import com.game.smartremoteapp.activity.home.DrawMoneyActivity;
import com.game.smartremoteapp.activity.home.ExChangeShopActivity;
import com.game.smartremoteapp.activity.home.GameCurrencyActivity;
import com.game.smartremoteapp.activity.home.InformationActivity;
import com.game.smartremoteapp.activity.home.LnvitationCodeActivity;
import com.game.smartremoteapp.activity.home.MyCtachRecordActivity;
import com.game.smartremoteapp.activity.home.MyJoinCodeActivity;
import com.game.smartremoteapp.activity.home.MyLogisticsOrderActivity;
import com.game.smartremoteapp.activity.home.RecordActivity;
import com.game.smartremoteapp.activity.home.ServiceActivity;
import com.game.smartremoteapp.activity.home.SettingActivity;
import com.game.smartremoteapp.activity.wechat.WeChatPayActivity;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.VideoBackBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;

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
    @BindView(R.id.mycenter_none_tv)
    TextView mycenterNoneTv;
    @BindView(R.id.mycenter_pay_layout)
    RelativeLayout mycenterPayLayout;
    @BindView(R.id.mycenter_catchrecord_layout)
    RelativeLayout mycenterCatchrecordLayout;
    @BindView(R.id.mycenter_setting_layout)
    RelativeLayout mycenterSettingLayout;
    @BindView(R.id.mycenter_kefu_layout)
    RelativeLayout mycenterKefuLayout;
    @BindView(R.id.mycenter_mycurrency_tv)
    TextView mycenterMycurrencyTv;
    @BindView(R.id.mycenter_exshop_layout)
    RelativeLayout mycenterExshopLayout;
    @BindView(R.id.mycenter_agency_tv)
    TextView mycenterAgencyTv;
    @BindView(R.id.mycenter_excenter_tv)
    TextView mycenterExcenterTv;
    @BindView(R.id.mycenter_withdraw_layout)
    RelativeLayout mycenterWithdrawLayout;
    @BindView(R.id.mycenter_mymoney_tv)
    TextView mycenterMymoneyTv;
    @BindView(R.id.mycenter_currencyrecord_tv)
    TextView mycenterCurrencyrecordTv;
    @BindView(R.id.mycenter_logisticsorder_tv)
    TextView mycenterLogisticsorderTv;
    @BindView(R.id.mycenter_guessrecord_tv)
    TextView mycenterGuessrecordTv;
    @BindView(R.id.mycenter_joincode_layout)
    RelativeLayout mycenterJoincodeLayout;
    @BindView(R.id.mycenter_lnvitationcode_layout)
    RelativeLayout mycenterLnvitationcodeLayout;
    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.mycenter_accinfo_layout)
    RelativeLayout mycenterAccinfoLayout;

    private String TAG = "MyCenterActivity";
    private List<VideoBackBean> videoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_center;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "个人中心userId=" + UserUtils.USER_ID);
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            //getUserDate(UserUtils.USER_ID);
            getUserAccBalCount(UserUtils.USER_ID);
            getAppUserInf(UserUtils.USER_ID);
        }
    }


    private void getUserImageAndName() {
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            if (!UserUtils.NickName.equals("")) {
                userName.setText(UserUtils.NickName);
            } else {
                userName.setText("暂无昵称");
            }
            mycenterMycurrencyTv.setText(Html.fromHtml("游戏币   " + "<font color='#ff9700'>" + UserUtils.UserBalance + "</font>"));
            //mycenterMymoneyTv.setText(Html.fromHtml("余额   "+"<font color='#ff9700'>0</font>"));
            userNumber.setText("累积抓中" + UserUtils.UserCatchNum + "次");
            Glide.with(getContext())
                    .load(UserUtils.UserImage)
                    .error(R.drawable.ctrl_default_user_bg)
                    .dontAnimate()
                    .transform(new GlideCircleTransform(getContext()))
                    .into(userImage);
        } else {
            userName.setText("请登录");
            videoList.clear();
            userImage.setImageResource(R.drawable.round);
        }
    }

    private void getUserDate(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                String name = result.getData().getAppUser().getCNEE_NAME();
                String phone = result.getData().getAppUser().getCNEE_PHONE();
                String address = result.getData().getAppUser().getCNEE_ADDRESS();
                UserUtils.UserAddress = name + " " + phone + " " + address;
                Log.e(TAG, "个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                        + " 抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                        + " 昵称=" + result.getData().getAppUser().getNICKNAME()
                        + " 头像=" + UserUtils.UserImage
                        + " 发货地址=" + UserUtils.UserAddress);
                getUserImageAndName();
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    @OnClick({R.id.mycenter_kefu_layout, R.id.mycenter_setting_layout, R.id.user_image,
            R.id.mycenter_pay_layout, R.id.user_name, R.id.mycenter_catchrecord_layout,
            R.id.mycenter_joincode_layout, R.id.mycenter_currencyrecord_tv,
            R.id.mycenter_guessrecord_tv, R.id.mycenter_logisticsorder_tv,
            R.id.mycenter_lnvitationcode_layout, R.id.mycenter_exshop_layout,
            R.id.mycenter_agency_tv, R.id.mycenter_excenter_tv, R.id.image_back,
            R.id.mycenter_withdraw_layout,R.id.mycenter_accinfo_layout,
            R.id.mycenter_mymoney_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:

                break;
            case R.id.mycenter_kefu_layout:
                startActivity(new Intent(getContext(), ServiceActivity.class));
                break;
            case R.id.mycenter_setting_layout:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.user_image:
                startActivity(new Intent(getContext(), InformationActivity.class));
                break;
            case R.id.mycenter_pay_layout:
//                startActivity(new Intent(getContext(), SelectRechargeTypeActiivty.class));
                startActivity(new Intent(getContext(), WeChatPayActivity.class));
                break;
            case R.id.mycenter_catchrecord_layout:
                startActivity(new Intent(getContext(), MyCtachRecordActivity.class));
                break;
            case R.id.mycenter_joincode_layout:
                //加盟码
                startActivity(new Intent(getContext(), MyJoinCodeActivity.class));
                break;
            case R.id.user_name:
                //此处添加登录dialog
                break;
            case R.id.mycenter_currencyrecord_tv:
                //金币记录
                //MyToast.getToast(getContext(),"金币记录").show();
                startActivity(new Intent(getContext(), GameCurrencyActivity.class));
                break;
            case R.id.mycenter_guessrecord_tv:
                startActivity(new Intent(getContext(), BetRecordActivity.class));
                break;
            case R.id.mycenter_logisticsorder_tv:
                startActivity(new Intent(getContext(), MyLogisticsOrderActivity.class));
                break;
            case R.id.mycenter_lnvitationcode_layout:
                startActivity(new Intent(getContext(), LnvitationCodeActivity.class));
                break;
            case R.id.mycenter_exshop_layout:
                startActivity(new Intent(getContext(), ExChangeShopActivity.class));
                //startActivity(new Intent(context, MyCtachRecordActivity.class));
                break;
            case R.id.mycenter_agency_tv:
                //MyToast.getToast(context,"功能开发中！").show();
                startActivity(new Intent(getContext(), AgencyActivity.class));
                break;
            case R.id.mycenter_excenter_tv:
                //MyToast.getToast(getContext(),"功能开发中！").show();
                //startActivity(new Intent(getContext(), ExChangeCenterActivity.class));
                break;
            case R.id.mycenter_withdraw_layout:
                if(UserUtils.IsBankInf.equals("0")){
                    MyToast.getToast(getContext(), "请先完善账户信息！").show();
                    startActivity(new Intent(getContext(),AccountInformationActivity.class));
                }else {
                    startActivity(new Intent(getContext(),DrawMoneyActivity.class));
                }
                break;
            case R.id.mycenter_accinfo_layout:
                startActivity(new Intent(getContext(),AccountInformationActivity.class));
                break;
            case R.id.mycenter_mymoney_tv:
                startActivity(new Intent(getContext(),AccountDetailActivity.class));
                break;
            default:
                break;
        }
    }

    private void getUserAccBalCount(String userId) {
        HttpManager.getInstance().getUserAccBalCount(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    String amount = httpDataInfoResult.getData().getAccBal();
                    UserUtils.UserAmount=amount;
                    mycenterMymoneyTv.setText(Html.fromHtml("余额   " + "<font color='#ff9700'>" + amount + "</font>"));
                }
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getContext(), "网络异常！").show();
            }
        });
    }

    private void getAppUserInf(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                String name = result.getData().getAppUser().getCNEE_NAME();
                String phone = result.getData().getAppUser().getCNEE_PHONE();
                String address = result.getData().getAppUser().getCNEE_ADDRESS();
                UserUtils.UserAddress = name + " " + phone + " " + address;
                UserUtils.IsBankInf = result.getData().getIsBankInf();
                UserUtils.BankBean = result.getData().getBankCard();
                if(!UserUtils.IsBankInf.equals("0")){
                    UserUtils.UserPhone=result.getData().getBankCard().getBANK_PHONE();
                }
                Log.e(TAG, "个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                        + " 抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                        + " 昵称=" + result.getData().getAppUser().getNICKNAME()
                        + " 头像=" + UserUtils.UserImage
                        + " 发货地址=" + UserUtils.UserAddress);
                getUserImageAndName();
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getContext(), "网络异常！").show();
            }
        });
    }


}
