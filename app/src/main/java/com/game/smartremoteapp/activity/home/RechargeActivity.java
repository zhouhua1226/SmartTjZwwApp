package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.RechargeAdapter;
import com.game.smartremoteapp.alipay.AlipayUtils;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.AlipayBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.NowPayBean;
import com.game.smartremoteapp.bean.OrderBean;
import com.game.smartremoteapp.bean.PayCardBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyLoading;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.PayTypeDialog;
import com.game.smartremoteapp.view.SpaceItemDecoration;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by chenw on 2018/7/17.
 */

public class RechargeActivity extends BaseActivity implements ReceivePayResult {
    private static final String TAG = "RechargeActivity----------";
    @BindView(R.id.tv_account_money)
    TextView userBlance;
    @BindView(R.id.recharge_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_recharge_car)
    LinearLayout rechargeCarView;
    @BindView(R.id.ll_week_car)
    LinearLayout weekCar;
    @BindView(R.id.ll_mouth_car)
    LinearLayout mouthCar;
    @BindView(R.id.view_car_line)
    View carLine;

    @BindView(R.id.tv_week_send_rate)
    TextView week_send_rate;
    @BindView(R.id.tv_week_send_money)
    TextView week_send_money;
    @BindView(R.id.tv_week_recharge_money)
    TextView week_recharge_money;
    @BindView(R.id.tv_week_summoney)
    TextView week_summoney;
    @BindView(R.id.tv_week_car_nocity)
    TextView week_car_nocity;

    @BindView(R.id.tv_mouth_send_rate)
    TextView mouth_send_rate;
    @BindView(R.id.tv_mouth_send_money)
    TextView mouth_send_money;
    @BindView(R.id.tv_mouth_recharge_money)
    TextView mouth_recharge_money;
    @BindView(R.id.tv_mouth_summoney)
    TextView mouth_summoney;
    @BindView(R.id.tv_mouth_car_nocity)
    TextView mouth_car_nocity;

    private RechargeAdapter mRechargeAdapter;
    private List<PayCardBean> mPayCardBeans= new ArrayList<>();
    private PayCardBean mWeek=null;
    private  PayCardBean  mMouth= null;
    private static final String mAppID = "151615975028920";//参数只支持微信支付
    private static final String mKey = "hX4uxxSsQVTWo3honWarbUkpqu5cA31A";
    private String payOutType; //wc 周卡 mc 月卡 首冲 fc  正常 nm
    private String isFirst;
    private IpaynowPlugin mIpaynowplugin;
    private MyLoading mLoadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        isFirst=  SPUtils.getString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE,"0");
        initView();
        getAppUserInf();
        getPayCardList();
        mIpaynowplugin = IpaynowPlugin.getInstance().init(this);// 1.插件初始化
        mLoadingDialog = new MyLoading(mIpaynowplugin.getDefaultLoading());
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(16));
        mRecyclerView.setFocusable(false);

        mRecyclerView.setAdapter(mRechargeAdapter = new RechargeAdapter(this,isFirst, mPayCardBeans, new RechargeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PayCardBean mPayCardBean=mPayCardBeans.get(position);
                String  isFirst=  SPUtils.getString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE,"0");
                          if(isFirst.equals("0")){
                              payOutType="fc";
                              getPayTypeDialog(mPayCardBean.getAMOUNT(),mPayCardBean.getFIRSTAWARD_GOLD());
                           }else{
                              payOutType="nm";
                              getPayTypeDialog(mPayCardBean.getAMOUNT(),mPayCardBean.getGOLD());
                }
            }
        }));
    }

    @OnClick({R.id.image_back, R.id.tv_recharge_detail,
            R.id.ll_mouth_car, R.id.ll_week_car})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_recharge_detail:
                Utils.toActivity(this, GameCurrencyActivity.class);
                break;
            case R.id.ll_mouth_car:
                if(mMouth!=null){
                    payOutType="mc";
                    getPayTypeDialog(mMouth.getAMOUNT(),mMouth.getRECHARE());
                }
                break;
            case R.id.ll_week_car:
                if(mWeek!=null){
                    payOutType="wc";
                    getPayTypeDialog(mWeek.getAMOUNT(), mWeek.getRECHARE());
                }
                break;
        }
    }

    /**
     * 刷新游戏币
     */
    private void getAppUserInf() {
        HttpManager.getInstance().getAppUserInf(UserUtils.USER_ID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getData().getAppUser() == null) {
                    return;
                }
                UserUtils.UserBalance =result.getData().getAppUser().getBALANCE();
                SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, result.getData().getAppUser().getFIRST_CHARGE());
                isFirst=result.getData().getAppUser().getFIRST_CHARGE();
                userBlance.setText(UserUtils.UserBalance);
               if(mPayCardBeans.size()>0) {
                   mRechargeAdapter.setIsFirstReacher(isFirst);
                   mRechargeAdapter.notifyDataSetChanged();
               }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }
    //获取充值卡列表
    public void getPayCardList() {

        HttpManager.getInstance().getPayCardList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getCode()==0) {
                    List<PayCardBean>   mylist = loginInfoResult.getData().getPaycard();
                    for (PayCardBean mPayCardBean:mylist) {
                         if(mPayCardBean.getID()==8){//周卡
                             mWeek=mPayCardBean;
                         }else if(mPayCardBean.getID()==9){//月卡
                            mMouth=mPayCardBean;
                         }else{
                             mPayCardBeans.add(mPayCardBean);
                         }
                    }
                 initData();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void initData() {
        mRechargeAdapter.setDataSetChanged(mPayCardBeans);//刷新列表
        if(mMouth!=null){
            mouth_recharge_money.setText("￥"+mMouth.getAMOUNT());
            mouth_summoney.setText("共 "+mMouth.getGOLD()+" 币");
            mouth_send_money.setText("送 "+mMouth.getAWARD()+" 币");
        }
        if(mWeek!=null){
            week_recharge_money.setText("￥"+mWeek.getAMOUNT());
            week_summoney.setText("共 "+mWeek.getGOLD()+" 币");
            week_send_money.setText("送 "+mWeek.getAWARD()+" 币");
        }
    }
    /**
     * 调取支付支付界面
     */
    private void startPay(String orderInfo) {
         AlipayUtils.startApliyPay(this, orderInfo, new AlipayUtils.OnApliyPayResultListenter() {
             @Override
            public void OnApliyPayResult(boolean isSuccess) {
                 if (isSuccess) {
                     getAppUserInf();
                 }
            }
        });
    }

    /**
     *选择支付方式
     * @param amount
     *
     */
    private void getPayTypeDialog(final String amount, final String reGold) {
        PayTypeDialog mPayTypeDialog = new PayTypeDialog(this, R.style.activitystyle);
        mPayTypeDialog.show();
        mPayTypeDialog.setDialogResultListener(new PayTypeDialog.DialogResultListener() {
            @Override
            public void getResult( boolean payChannelType) {
                 if(payChannelType ){
                     getNowPayOrder(amount,reGold, "13");
                 }else{
                     getOrderInfo(amount,reGold );
                 }
            }
        });
    }

    /**
     *获取微信订单信息
     *
     * @param amount
     * @param reGold
     */
    private void getNowPayOrder( String amount, String reGold,String payChannelType) {
        HttpManager.getInstance().getNowPayOrder(UserUtils.USER_ID,  amount,payChannelType,
                 reGold,payOutType, new RequestSubscriber<NowPayBean<OrderBean>>() {
                    @Override
                    public void _onSuccess(NowPayBean<OrderBean> result) {
                        if (result.getCode() == 0) {
                            mIpaynowplugin.setCustomLoading(mLoadingDialog).setCallResultReceiver(RechargeActivity.this).pay(result.getNowpayData());
                        }
                    }
                    @Override
                    public void _onError(Throwable e) {
                        LogUtils.logi(e.getMessage());
                    }
                });
    }


    /**
     *获取支付宝订单信息
     * @param amount
     * @param reGold
     */
    private void getOrderInfo(String amount, String reGold) {
        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID, amount,
                reGold,payOutType, new RequestSubscriber<Result<AlipayBean>>() {
            @Override
            public void _onSuccess(Result<AlipayBean> result) {
                if (result.getCode() == 0) {
                    startPay(result.getData().getAlipay()); //调用支付宝支付接口
                }
            }

            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

    @Override
    public void onIpaynowTransResult(ResponseParams responseParams) {
            String respCode = responseParams.respCode;
            String errorCode = responseParams.errorCode;
            String errorMsg = responseParams.respMsg;
            StringBuilder temp = new StringBuilder();
            if (respCode.equals("00")) {
                getAppUserInf();
                MyToast.getToast(getApplicationContext(),"支付成功!").show();
            } else if (respCode.equals("02")) {
                MyToast.getToast(getApplicationContext(),"支付取消!").show();
            } else if (respCode.equals("01")) {
                MyToast.getToast(getApplicationContext(),"支付失败!").show();
                Log.e( TAG,"respCode:" + respCode+"respMsg:"+errorMsg);
            }  else {
                Log.e( TAG,"respCode:" + respCode+"respMsg:"+errorMsg);
            }
        }
}