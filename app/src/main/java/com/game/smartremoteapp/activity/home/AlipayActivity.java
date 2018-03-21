package com.game.smartremoteapp.activity.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flamigo.jsdk.tools.ToastUtil;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.alipay.AlipayRequest;
import com.game.smartremoteapp.alipay.PayCallback;
import com.game.smartremoteapp.alipay.PayResult;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.EZUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 2018/3/12.
 */

public class AlipayActivity extends BaseActivity {
    @BindView(R.id.select_account_tv)
    TextView account_tv;
    @BindView(R.id.select_money_tv)
    TextView money_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apliy;
    }
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image_back,R.id.image_service,R.id.layout_wechat,R.id.layout_apliy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.image_service:
                Utils.toActivity(this,ServiceActivity.class);
                break;
            case R.id.layout_wechat:
                ToastUtil.show("正在维护中...");
                break;
            case R.id.layout_apliy:
                 getOrderInfo();
                break;
        }
    }
    /**
     * 调取支付支付界面
     */
    private void startApliyPay(String orderInfo){
        AlipayRequest.StartAlipay(this, orderInfo, new PayCallback() {
            @Override
            public void payResult(	Map< String, String> result) {
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
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EZUtils.PLAYER_APLIY_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map< String, String>)msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //  notifyPayResult("支付成功");
                    }else if(TextUtils.equals(resultStatus, "8000")||TextUtils.equals(resultStatus, "6004")){
                        //正在处理中，支付结果未知
                       // notifyPayResult("正在支付中，请与客服联系");
                    }else{
                     //   notifyPayResult("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    /**
     * 获取支付宝支付信息
     */
    private void  getOrderInfo(){
        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID,"7", new RequestSubscriber<Result<String>>() {
            @Override
            public void _onSuccess(Result<String> result) {
                  if(result!=null){
                      startApliyPay(result.getData()); //调用支付宝支付接口
                  }
            }
            @Override
            public void _onError(Throwable e) {
               // Utils.showLogE(TAG, "orderinfo::::" + e.getMessage());
            }
        });
    }

}
