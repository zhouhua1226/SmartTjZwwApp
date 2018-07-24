package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.alipay.AlipayUtils;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.AlipayBean;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.PayCardBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/7/19.
 */

public class CardDetailActivity extends BaseActivity {

    private static final String TAG ="CardDetailActivity==========" ;
    @BindView(R.id.card_detail_radiogroup )
    RadioGroup radiogroup;
    @BindView(R.id.radio_week_card )
    RadioButton radio_week;
    @BindView(R.id.radio_mouth_card )
    RadioButton radio_mouth;
    @BindView(R.id.iv_card_bg )
    ImageView card_bg;
    @BindView(R.id.tv_card_money )
    TextView card_money;
    @BindView(R.id.tv_card_time )
    TextView card_time;
    @BindView(R.id.iv_card_buy )
    Button card_buy;

    @BindView(R.id.tv_detail_one )
    TextView detail_one;
    @BindView(R.id.tv_detail_two )
    TextView detail_two;
    @BindView(R.id.tv_detail_three )
    TextView detail_three;
    private PayCardBean mWeek=null;
    private  PayCardBean  mMouth= null;
    private int type=1;
    private  String payOutType;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_card_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.white);
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        type=getIntent().getIntExtra(UrlUtils.CARD_TYPE,0);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case  R.id.radio_mouth_card:
                        initMouthView();
                        break;
                    case  R.id.radio_week_card:
                        initWeekView();
                        break;
                }
    }
          });
        getPayCardList();
        if(type>1){
            radio_mouth.setChecked(true);
            initMouthView();
        }else{
            radio_week.setChecked(true);
            initWeekView();
        }
    }

    private void initWeekView() {
        card_bg.setBackgroundResource(R.drawable.icon_week_detail);
        card_time.setText(getResources().getString(R.string.week_take_effect));
        String s1 = "购买周卡后一共可获得" + "<font color='#FF0000'>" + "420" + "</font>"+"游戏币,可立即获得"+ "<font color='#FF0000'>" + "280" + "</font>"+"游戏币";
        String s2 = "有效期内每天赠送" + "<font color='#FF0000'>" + "20" + "</font>"+"游戏币，7天内共赠送"+ "<font color='#FF0000'>" + "140" + "</font>"+"游戏币";
        String s3 = "重复购买周卡，奖励不会叠加，而是会延顺到下一个周期";
        detail_one.setText(Html.fromHtml(s1));
        detail_two.setText(Html.fromHtml(s2));
        detail_three.setText(Html.fromHtml(s3));
        if(mWeek!=null) {
            card_money.setText(mWeek.getAMOUNT());
        }
        payOutType="wc";
    }

    private void initMouthView() {
        card_bg.setBackgroundResource(R.drawable.icon_mouth_detail);
        card_time.setText(getResources().getString(R.string.mouth_take_effect));
        String s1 = "购买周卡后一共可获得" + "<font color='#FF0000'>" + "1980" + "</font>"+"游戏币,可立即获得"+ "<font color='#FF0000'>" + "980" + "</font>"+"游戏币";
        String s2 = "有效期内每天赠送" + "<font color='#FF0000'>" + "33" + "</font>"+"游戏币"
                + "\n30天内共赠送"+ "<font color='#FF0000'>" + "990" + "</font>"+"游戏币";
        String s3 = "重复购买月卡，奖励不会叠加，而是会延顺到下一个周期";
        detail_one.setText(Html.fromHtml(s1));
        detail_two.setText(Html.fromHtml(s2));
        detail_three.setText(Html.fromHtml(s3));
        if(mMouth!=null) {
            card_money.setText(mMouth.getAMOUNT());
        }
        payOutType="mc";
    }

    @OnClick({R.id.image_back,R.id.iv_card_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.iv_card_buy:
                if(type>1){
                    getOrderInfo(mMouth.getAMOUNT(),mMouth.getRECHARE(),payOutType);
                }else{
                    getOrderInfo(mWeek.getAMOUNT(),mWeek.getRECHARE(),payOutType);
                }
                break;
        }
    }
    /**
     *获取订单信息
     */
    private void getOrderInfo(String amount,String reGold,String payOutType) {
        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID, amount,
                reGold,payOutType,
                new RequestSubscriber<Result<AlipayBean>>() {
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


    /**
     * 调取支付支付界面
     */
    private void startPay(String orderInfo) {
        AlipayUtils.startApliyPay(this, orderInfo, new AlipayUtils.OnApliyPayResultListenter() {
            @Override
            public void OnApliyPayResult(boolean isSuccess) {
                if(isSuccess){

                }
            }
        });
    }

    //获取充值卡列表
    public void getPayCardList() {
        HttpManager.getInstance().getPayCardList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode()==0) {
                    List<PayCardBean> mylist = httpDataInfoResult.getData().getPaycard();
                    for (PayCardBean mPayCardBean:mylist) {
                        if(mPayCardBean.getID()==8){//周卡
                            mWeek=mPayCardBean;
                        }else if(mPayCardBean.getID()==9){//月卡
                            mMouth=mPayCardBean;
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
        if(type>1){
            if(mMouth!=null){
                card_money.setText(mWeek.getAMOUNT());
            }
        }else{
            if(mWeek!=null){
                card_money.setText(mWeek.getAMOUNT());
            }
        }

    }
}
