package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.protocol.JCResult;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.protocol.RspBodyBaseBean;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.EXGoldenBeanDialog;
import com.game.smartremoteapp.view.ExchangeCoinDialog;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/8/23 10:38
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class EXGoldenBeanActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.exgoldenbean_gdnum_tv)
    TextView exgoldenbeanGdnumTv;
    @BindView(R.id.exgoldenbean_10_iv)
    ImageView exgoldenbean10Iv;
    @BindView(R.id.exgoldenbean_20_iv)
    ImageView exgoldenbean20Iv;
    @BindView(R.id.exgoldenbean_50_iv)
    ImageView exgoldenbean50Iv;
    @BindView(R.id.exgoldenbean_100_iv)
    ImageView exgoldenbean100Iv;
    @BindView(R.id.exgoldenbean_200_iv)
    ImageView exgoldenbean200Iv;
    @BindView(R.id.exgoldenbean_500_iv)
    ImageView exgoldenbean500Iv;
    @BindView(R.id.exgoldenbean_gogame_tv)
    ImageButton exgoldenbeanGogameTv;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_exgoldenbean;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        exgoldenbeanGdnumTv.setText(JCUtils.GAMEJD + JCUtils.JDNAME);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getGameBeans();
    }

    @OnClick({R.id.image_back, R.id.exgoldenbean_10_iv, R.id.exgoldenbean_20_iv,
            R.id.exgoldenbean_50_iv, R.id.exgoldenbean_100_iv, R.id.exgoldenbean_200_iv,
            R.id.exgoldenbean_500_iv,R.id.exgoldenbean_gogame_tv,R.id.exgoldenbean_exchange_coin_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.exgoldenbean_10_iv:
                showEXDialog(1000);
                break;
            case R.id.exgoldenbean_20_iv:
                showEXDialog(2000);
                break;
            case R.id.exgoldenbean_50_iv:
                showEXDialog(5000);
                break;
            case R.id.exgoldenbean_100_iv:
                showEXDialog(10000);
                break;
            case R.id.exgoldenbean_200_iv:
                showEXDialog(20000);
                break;
            case R.id.exgoldenbean_500_iv:
                showEXDialog(50000);
                break;
            case R.id.exgoldenbean_gogame_tv:
                startActivity(new Intent(this,GameCenterActivity.class));
                break;
            case R.id.exgoldenbean_exchange_coin_tv:
                if(Utils.isNumeric(JCUtils.GAMEJD)){
                    if(Integer.parseInt(JCUtils.GAMEJD)>=100){
                        showEXBtGDialog(JCUtils.GAMEJD);
                    }else{
                        MyToast.getToast(this,"你的金豆不足！").show();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showEXBtGDialog(String beanNum) {
        ExchangeCoinDialog exGoldenBeanDialog = new ExchangeCoinDialog(this, R.style.easy_dialog_style);
        exGoldenBeanDialog.setCancelable(true);
        exGoldenBeanDialog.show();
        exGoldenBeanDialog.setText(beanNum + "");
        exGoldenBeanDialog.setDialogResultListener(new  ExchangeCoinDialog.DialogResultListener() {
            @Override
            public void getResult(boolean resultCode,String bean) {
                if (resultCode) {
                    getBeanEXGold(bean);
                }
            }
        });
    }

    private void getBeanEXGold(String beanNum) {
        HttpManager.getInstance().getBeanEXGold(UserUtils.USER_ID,JCUtils.UID,  beanNum,  new RequestSubscriber<JCResult>() {
            @Override
            public void _onSuccess(JCResult jcResult) {
                String error=jcResult.getError();
                if(error.equals("0")){
                    JCUtils.GAMEJD=jcResult.getJD();
                    exgoldenbeanGdnumTv.setText(JCUtils.GAMEJD + JCUtils.JDNAME);
                    MyToast.getToast(getApplicationContext(),"兑换成功").show();
                }
            }
            @Override
            public void _onError(Throwable e) {

            }
        });

    }

    private void showEXDialog(final int beanNum) {
        EXGoldenBeanDialog exGoldenBeanDialog = new EXGoldenBeanDialog(this, R.style.easy_dialog_style);
        exGoldenBeanDialog.setCancelable(true);
        exGoldenBeanDialog.show();
        exGoldenBeanDialog.setText(beanNum + "");
        exGoldenBeanDialog.setDialogResultListener(new EXGoldenBeanDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if (resultCode == 1) {
                    getCoinEXGoldenbean(beanNum);
                }
            }
        });
    }

    private void getCoinEXGoldenbean(final int beanNum) {
        if (Integer.parseInt(UserUtils.UserBalance) < (beanNum / 100)) {
            MyToast.getToast(getApplicationContext(), "您的余额不足,请充值！").show();
            startActivity(new Intent(this, RechargeActivity.class));
            return;
        }
        HttpManager.getInstance().getCoinEXGoldenbean(UserUtils.USER_ID, String.valueOf(beanNum), new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpInfoResult) {
                if (httpInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    UserUtils.UserBalance = httpInfoResult.getData().getAppUser().getBALANCE();
                    JCUtils.GAMEJD = httpInfoResult.getData().getAppUser().getJDNUM();
                    exgoldenbeanGdnumTv.setText(JCUtils.GAMEJD + JCUtils.JDNAME);
                    MyToast.getToast(getApplicationContext(), "兑换成功,您已获得" + beanNum + JCUtils.JDNAME).show();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void getGameBeans() {
        String auth = RspBodyBaseBean.getAuth(this, JCUtils.UID);
        String opt = "117";
        HttpManager.getInstance().getGameBeans(opt, auth, new RequestSubscriber<JCResult>() {
            @Override
            public void _onSuccess(JCResult jcResult) {
                String error = jcResult.getError();
                if (error.equals("0")) {
                    JCUtils.GAMEJD = jcResult.getJD();
                    exgoldenbeanGdnumTv.setText(JCUtils.GAMEJD + JCUtils.JDNAME);
                    //MyToast.getToast(getApplicationContext(),"我的金豆为"+JCUtils.GAMEJD).show();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
