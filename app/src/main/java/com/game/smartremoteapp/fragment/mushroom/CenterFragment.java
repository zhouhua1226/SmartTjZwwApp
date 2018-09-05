package com.game.smartremoteapp.fragment.mushroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.AccountWalletActivity;
import com.game.smartremoteapp.activity.home.BetRecordActivity;
import com.game.smartremoteapp.activity.home.GameCurrencyActivity;
import com.game.smartremoteapp.activity.home.InformationActivity;
import com.game.smartremoteapp.activity.home.LnvitationCodeActivity;
import com.game.smartremoteapp.activity.home.MainActivity;
import com.game.smartremoteapp.activity.home.MyCtachRecordActivity;
import com.game.smartremoteapp.activity.home.MyLogisticsOrderActivity;
import com.game.smartremoteapp.activity.home.ServiceActivity;
import com.game.smartremoteapp.activity.home.SettingActivity;
import com.game.smartremoteapp.activity.mushroom.Splash1Activity;
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
import butterknife.OnClick;


/**
 * Created by mi on 2018/8/8.
 */

public class CenterFragment extends BaseFragment {

    @BindView(R.id.user_center_image)
    ImageView userImage;
    @BindView(R.id.user_center_name)
    TextView userName;
    @BindView(R.id.tv_center_golds)
    TextView userGolds;

    private String TAG = "MyCenterActivity";
    private List<VideoBackBean> videoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_munshroom_center;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            getAppUserInf(UserUtils.USER_ID);
        }else{
            Utils.toActivity(getActivity(), Splash1Activity.class);
            MainActivity.mMainActivity.finish();
      }
    }


    private void getUserImageAndName() {
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            if (!UserUtils.NickName.equals("")) {
                userName.setText(UserUtils.NickName);
            } else {
                userName.setText("暂无昵称");
            }
            userGolds.setText("游戏币:" + UserUtils.UserBalance );
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


    @OnClick({R.id.rl_center_order, R.id.rl_center_gold_record,
            R.id.rl_center_bet_record, R.id.rl_center_wawa_mail,
            R.id.rl_center_wawa_exchange, R.id.rl_center_invite_code,
            R.id.rl_center_customer_service, R.id.rl_center_set,
            R.id.user_center_image, R.id.tv_center_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_center_customer_service:
                startActivity(new Intent(getContext(), ServiceActivity.class));
                break;
            case R.id.rl_center_set:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.user_center_image:
                startActivity(new Intent(getContext(), InformationActivity.class));
                break;
            case R.id.tv_center_recharge:
                startActivity(new Intent(getContext(), AccountWalletActivity.class));
                break;
            case R.id.rl_center_wawa_mail:
                Intent intent=new Intent(getContext(),MyCtachRecordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.rl_center_invite_code:// 邀请码
                startActivity(new Intent(getContext(), LnvitationCodeActivity.class));
                break;

            case R.id.rl_center_gold_record:  //金币记录
                startActivity(new Intent(getContext(), GameCurrencyActivity.class));
                break;
            case R.id.rl_center_bet_record://竞猜记录
                startActivity(new Intent(getContext(), BetRecordActivity.class));
                break;
            case R.id.rl_center_order://物流订单查询类
                startActivity(new Intent(getContext(), MyLogisticsOrderActivity.class));
                break;

            case R.id.rl_center_wawa_exchange:
                Intent intent2=new Intent(getContext(),MyCtachRecordActivity.class);
                intent2.putExtra("type","2");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void getAppUserInf(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if(result.getData().getAppUser()==null){
                    return;
                }
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                UserUtils.UserWeekDay=result.getData().getAppUser().getWEEKS_CARD();
                UserUtils.UserMouthDay=result.getData().getAppUser().getMONTH_CARD();

                UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                String name = result.getData().getAppUser().getCNEE_NAME();
                String phone = result.getData().getAppUser().getCNEE_PHONE();
                String address = result.getData().getAppUser().getCNEE_ADDRESS();
                if(name!=null && phone!=null && address!=null) {
                    UserUtils.UserAddress = name + " " + phone + " " + address;
                }
                UserUtils.IsBankInf = result.getData().getIsBankInf();
                UserUtils.BankBean = result.getData().getBankCard();
                if(!UserUtils.IsBankInf.equals("0")){
                    UserUtils.UserPhone=result.getData().getBankCard().getBANK_PHONE();
                }else{
                    if(result.getData().getAppUser().getBDPHONE().equals("")&&result.getData().getAppUser().getBDPHONE()!=null){
                        UserUtils.UserPhone=result.getData().getAppUser().getPHONE();
                    }else{
                        UserUtils.UserPhone=result.getData().getAppUser().getBDPHONE();
                    }
                }

                getUserImageAndName();
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getContext(), "网络异常！").show();
            }
        });
    }


}