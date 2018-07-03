package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.alipay.AlipayRequest;
import com.game.smartremoteapp.alipay.PayCallback;
import com.game.smartremoteapp.alipay.PayResult;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.AlipayBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.EZUtils;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/5/2.
 */

public class PayNowActivity extends BaseActivity {

    @BindView(R.id.ll_rightBtn)
    RelativeLayout ll_rightBtn;
    @BindView(R.id.iv_leftBtn)
    ImageView ivRightBtn;

    @BindView(R.id.et_pay_amount)
    EditText etAmount;
    @BindView(R.id.tv_user_blance)
    TextView userBlance;
    @BindView(R.id.tv_user_account)
    TextView userAccount;
    @BindView(R.id.rb_aliapy_pay)
    RadioButton rb_aliapy;
    private  int payType=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_now;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void initView() {
        setLeftBtnDefaultOnClickListener();
        setTitle("充值");
        ivRightBtn.setVisibility(View.VISIBLE);
        ivRightBtn.setBackgroundResource(R.drawable.kf1);
        ll_rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Utils.toActivity(PayNowActivity.this,ServiceActivity.class);
            }
        });

        userAccount.setText(UserUtils.UserName);
        userBlance.setText(UserUtils.UserBalance);
        rb_aliapy.setChecked(true);
    }

    @OnClick({ R.id.rl_pay_alipay,R.id.bt_sure_pay })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_alipay:
                rb_aliapy.setChecked(!rb_aliapy.isChecked());
                break;
            case R.id.bt_sure_pay:
                String amount= etAmount.getText().toString();
                if(amount.isEmpty()){
                    MyToast.getToast(PayNowActivity.this,"请输入充值金额").show();
                    return;
                }
                if(Long.parseLong(amount)<10){
                MyToast.getToast(PayNowActivity.this,"充值金额最少10元").show();
                return;
               }
                if(!rb_aliapy.isChecked()){
                    MyToast.getToast(PayNowActivity.this,"请选择充值方式").show();
                    return;
                }
                getOrderInfo(amount);
                break;
        }
    }

    /**
     * 获取支付宝支付信息
     * @param amount
     */
    private void getOrderInfo(String amount) {
        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID, amount, new RequestSubscriber<Result<AlipayBean>>() {
            @Override
            public void _onSuccess(Result<AlipayBean> result) {
                if (result.getCode() == 0) {
                    startApliyPay(result.getData().getAlipay()); //调用支付宝支付接口
                }
            }

            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

    /**
     * 调取支付支付界面
     */
    private void startApliyPay(String orderInfo) {
        AlipayRequest.StartAlipay(this, orderInfo, new PayCallback() {
            @Override
            public void payResult(Map<String, String> result) {
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                Message msg = new Message();
                msg.what = EZUtils.PLAYER_APLIY_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EZUtils.PLAYER_APLIY_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //  notifyPayResult("支付成功");
                        MyToast.getToast(PayNowActivity.this, "支付成功").show();
                        getAppUserInf();
                    } else if (TextUtils.equals(resultStatus, "8000") || TextUtils.equals(resultStatus, "6004")) {
                        //正在处理中，支付结果未知
                        // notifyPayResult("正在支付中，请与客服联系");
                        MyToast.getToast(PayNowActivity.this, "正在支付中，请与客服联系").show();
                    } else {
                        //   notifyPayResult("支付失败");
                        MyToast.getToast(PayNowActivity.this, "支付失败").show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    /**
     * 刷新游戏币
     */
    private void getAppUserInf( ) {
        HttpManager.getInstance().getAppUserInf(UserUtils.USER_ID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if(result.getData().getAppUser()==null){
                    return;
                }
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                userBlance.setText(UserUtils.UserBalance);
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

}

