package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.alipay.AlipayRequest;
import com.game.smartremoteapp.alipay.PayCallback;
import com.game.smartremoteapp.alipay.PayResult;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.PayCardBean;
import com.game.smartremoteapp.utils.EZUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/3/23.
 */

public class PayActivity extends BaseActivity  {
    @BindView(R.id.tv_order_card_bean)
    TextView card_bean;
    @BindView(R.id.tv_order_card_money)
    TextView card_money;
    @BindView(R.id.tv_order_pay_money)
    TextView pay_money;
    @BindView(R.id.tv_account_blance)
    TextView accountBlance;
    @BindView(R.id.cb_balance)
    CheckBox cbBalance;

    private PayCardBean mPayCardBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void initView() {

        mPayCardBean = (PayCardBean) getIntent().getSerializableExtra("PayCardBean");
        if (mPayCardBean == null) {
            finish();
        }
        card_bean.setText("订单名称：购买" + mPayCardBean.getGOLD() + "金币");
        card_money.setText("订单金额： ￥" + mPayCardBean.getAMOUNT());
        pay_money.setText("￥" + mPayCardBean.getAMOUNT());
        cbBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.getToast(PayActivity.this, "暂不支持余额支付").show();
                cbBalance.setChecked(false);
            }
        });

    }

    @OnClick({R.id.image_back, R.id.image_service,R.id.rl_pay_alipay,R.id.rl_pay_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_service:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
            case R.id.rl_pay_alipay:
               // getOrderInfo();
                break;
            case R.id.rl_pay_weixin:
                Intent intent=new Intent(this,PayNowActivity.class);
                intent.putExtra("PayCardBean",mPayCardBean);
                Utils.toActivity(this,intent);
              //  getOrderWinxinInfo();
                break;
        }
    }

    /**
     * 获取支付宝支付信息
     */
//    private void getOrderInfo() {
//        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID, amount, UserUtils.USER_ID, "0.1", new RequestSubscriber<Result<AlipayBean>>() {
//            @Override
//            public void _onSuccess(Result<AlipayBean> result) {
//                if (result.getCode() == 0) {
//                    startApliyPay(result.getData().getAlipay()); //调用支付宝支付接口
//                }
//            }
//
//            @Override
//            public void _onError(Throwable e) {
//                LogUtils.logi(e.getMessage());
//            }
//        });
//    }

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
                        MyToast.getToast(PayActivity.this, "支付成功").show();
                    } else if (TextUtils.equals(resultStatus, "8000") || TextUtils.equals(resultStatus, "6004")) {
                        //正在处理中，支付结果未知
                        // notifyPayResult("正在支付中，请与客服联系");
                        MyToast.getToast(PayActivity.this, "正在支付中，请与客服联系").show();
                    } else {
                        //   notifyPayResult("支付失败");
                        MyToast.getToast(PayActivity.this, "支付失败").show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
