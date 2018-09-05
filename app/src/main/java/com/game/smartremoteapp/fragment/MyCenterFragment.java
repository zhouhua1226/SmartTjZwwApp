package com.game.smartremoteapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.AccountDetailActivity;
import com.game.smartremoteapp.activity.home.AccountInformationActivity;
import com.game.smartremoteapp.activity.home.AccountWalletActivity;
import com.game.smartremoteapp.activity.home.AgencyActivity;
import com.game.smartremoteapp.activity.home.BetRecordActivity;
import com.game.smartremoteapp.activity.home.DrawMoneyActivity;
import com.game.smartremoteapp.activity.home.InformationActivity;
import com.game.smartremoteapp.activity.home.IntegralActivity;
import com.game.smartremoteapp.activity.home.IntegralTaskActivity;
import com.game.smartremoteapp.activity.home.LnvitationCodeActivity;
import com.game.smartremoteapp.activity.home.LoginCodeActivity;
import com.game.smartremoteapp.activity.home.MainActivity;
import com.game.smartremoteapp.activity.home.MyCtachRecordActivity;
import com.game.smartremoteapp.activity.home.MyJoinCodeActivity;
import com.game.smartremoteapp.activity.home.MyLogisticsOrderActivity;
import com.game.smartremoteapp.activity.home.RechargeActivity;
import com.game.smartremoteapp.activity.home.ServiceActivity;
import com.game.smartremoteapp.activity.home.SettingActivity;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.VideoBackBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.game.smartremoteapp.R.id.mycenter_mymoney_tv;
import static com.game.smartremoteapp.utils.UserUtils.UserAmount;

/**
 * Created by hongxiu on 2017/9/25.
 */
public class MyCenterFragment extends BaseFragment {

    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_number)
    TextView userNumber;
    @BindView(R.id.mycenter_golds_tv)
    TextView mycenter_golds;

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
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            //getUserDate(UserUtils.USER_ID);
           // getUserAccBalCount(UserUtils.USER_ID);
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
            mycenter_golds.setText("游戏币:" + UserUtils.UserBalance );
            //mycenterMymoneyTv.setText(Html.fromHtml("余额   "+"<font color='#ff9700'>0</font>"));
            userNumber.setText("累积抓中" + UserUtils.UserCatchNum + "次");
            Glide.with(getContext())
                    .load(UserUtils.UserImage)
                    .error(R.mipmap.app_mm_icon)
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
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                    UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                    UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                    UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                    String name = result.getData().getAppUser().getCNEE_NAME();
                    String phone = result.getData().getAppUser().getCNEE_PHONE();
                    String address = result.getData().getAppUser().getCNEE_ADDRESS();
                    UserUtils.UserAddress = name + " " + phone + " " + address;
                    LogUtils.loge("个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                            + " 抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                            + " 昵称=" + result.getData().getAppUser().getNICKNAME()
                            + " 头像=" + UserUtils.UserImage
                            + " 发货地址=" + UserUtils.UserAddress, TAG);
                    getUserImageAndName();
                }
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
            R.id.mycenter_agency_tv, R.id.mycenter_excenter_tv,
            R.id.mycenter_withdraw_layout,R.id.mycenter_accinfo_layout,
            R.id.mycenter_mymoney_tv,R.id.mycenter_qianbao,
            R.id.mycenter_integral,R.id.ll_integral_task,R.id.imb_center_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                startActivity(new Intent(getContext(), RechargeActivity.class));
                break;
            case R.id.mycenter_catchrecord_layout:
                Intent intent=new Intent(getContext(),MyCtachRecordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.mycenter_joincode_layout:
                //加盟码
                startActivity(new Intent(getContext(), MyJoinCodeActivity.class));
                break;
            case R.id.user_name:
                //此处添加登录dialog
                break;
            case R.id.mycenter_currencyrecord_tv:
                //充值中心
                //MyToast.getToast(getContext(),"金币记录").show();
                startActivity(new Intent(getContext(), RechargeActivity.class));
                break;
            case R.id.mycenter_guessrecord_tv://竞猜记录
                startActivity(new Intent(getContext(), BetRecordActivity.class));
                break;
            case R.id.mycenter_logisticsorder_tv://物流订单查询类
                startActivity(new Intent(getContext(), MyLogisticsOrderActivity.class));
                break;
            case R.id.mycenter_lnvitationcode_layout:
                startActivity(new Intent(getContext(), LnvitationCodeActivity.class));
                break;
            case R.id.mycenter_exshop_layout:
                Intent intent2=new Intent(getContext(),MyCtachRecordActivity.class);
                intent2.putExtra("type","2");
                startActivity(intent2);
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
                if(UserUtils.IsBankInf.equals("0")||UserUtils.BankBean==null){
                    MyToast.getToast(getContext(), "请先完善账户信息！").show();
                    startActivity(new Intent(getContext(),AccountInformationActivity.class));
                }else {
                    startActivity(new Intent(getContext(),DrawMoneyActivity.class));
                }
                break;
            case R.id.mycenter_accinfo_layout:
                startActivity(new Intent(getContext(),AccountInformationActivity.class));
                break;
            case mycenter_mymoney_tv:
                startActivity(new Intent(getContext(),AccountDetailActivity.class));
                break;
            case  R.id.mycenter_qianbao:
                Utils.toActivity(getContext(),AccountWalletActivity.class);
                break;
            case  R.id.mycenter_integral:
                Utils.toActivity(getContext(),IntegralActivity.class);
                break;
            case R.id.ll_integral_task:
                Utils.toActivity(getContext(),IntegralTaskActivity.class);
                break;
            case R.id.imb_center_sign:
                MainActivity.mMainActivity.getUserSign(UserUtils.USER_ID, "0",true); //签到请求
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
                    UserAmount=amount;
                   //  mycenterMymoneyTv.setText(Html.fromHtml("余额   " + "<font color='#ff9700'>" + amount + "</font>"));
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
            Utils.toActivity(getActivity(), LoginCodeActivity.class);
            MainActivity.mMainActivity.finish();
            return;
        }
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    if (result.getData().getAppUser() == null) {
                        return;
                    }
                    UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                    UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                    UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                    UserUtils.UserWeekDay = result.getData().getAppUser().getWEEKS_CARD();
                    UserUtils.UserMouthDay = result.getData().getAppUser().getMONTH_CARD();

                    UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                    String name = result.getData().getAppUser().getCNEE_NAME();
                    String phone = result.getData().getAppUser().getCNEE_PHONE();
                    String address = result.getData().getAppUser().getCNEE_ADDRESS();
                    if (name != null && phone != null && address != null) {
                        UserUtils.UserAddress = name + " " + phone + " " + address;
                    }
                    UserUtils.IsBankInf = result.getData().getIsBankInf();
                    UserUtils.BankBean = result.getData().getBankCard();
                    if (!UserUtils.IsBankInf.equals("0")) {
                        UserUtils.UserPhone = result.getData().getBankCard().getBANK_PHONE();
                    } else {
                        if (result.getData().getAppUser().getBDPHONE().equals("") && result.getData().getAppUser().getBDPHONE() != null) {
                            UserUtils.UserPhone = result.getData().getAppUser().getPHONE();
                        } else {
                            UserUtils.UserPhone = result.getData().getAppUser().getBDPHONE();
                        }
                    }
                    LogUtils.loge("个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                            + " 抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                            + " 昵称=" + result.getData().getAppUser().getNICKNAME()
                            + " 头像=" + UserUtils.UserImage
                            + " 发货地址=" + UserUtils.UserAddress, TAG);
                    getUserImageAndName();
                }
            }
            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getContext(), "网络异常！").show();
            }
        });
    }


}
