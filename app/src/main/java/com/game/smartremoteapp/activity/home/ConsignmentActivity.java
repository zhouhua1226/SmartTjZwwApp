package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.ConsignmentAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.VideoBackBean;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hongxiu on 2017/10/18.
 */
public class ConsignmentActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.consignment_rl)
    RelativeLayout consignmentRl;
    @BindView(R.id.shipping_button)
    Button shippingButton;
    @BindView(R.id.title_img)
    ImageView titleImg;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.times_tv)
    TextView timesTv;
    @BindView(R.id.information_tv)
    TextView informationTv;
    @BindView(R.id.text_tv)
    TextView textTv;
    @BindView(R.id.remark_et)
    EditText remarkEt;
    @BindView(R.id.consignment_recyclerview)
    RecyclerView consignmentRecyclerview;

    @BindView(R.id.consignment_singleyj_layout)
    LinearLayout  SingleyjLayout;
    @BindView(R.id.consignment_hdfk_cb)
    CheckBox hdfk_cb;
    @BindView(R.id.consignment_wwbdkyf_cb)
    CheckBox wwbdkyf_cb;

    private String TAG = "ConsignmentActivity--";
    private VideoBackBean videoBackBean;
    private ConsignmentAdapter consignmentAdapter;
    private List<VideoBackBean> list = new ArrayList<>();
    private String information = "";
    private String fhType = "0";  //0 免邮：1  金币抵扣 ：2  货到付款 暂不不支持：3 用户13级2件包邮
    private StringBuffer stringId = new StringBuffer("");
    private StringBuffer stringDollId = new StringBuffer("");

    @Override
    protected int getLayoutId() {
        //申请发货页面
        return R.layout.activity_consignment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAddressData();
    }

    private void initData() {
        list = (List<VideoBackBean>) getIntent().getSerializableExtra("record");//获取list方式
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        consignmentRecyclerview.setLayoutManager(linearLayoutManager);
        consignmentRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        consignmentAdapter = new ConsignmentAdapter(this, list);
        consignmentRecyclerview.setAdapter(consignmentAdapter);
        if (list.size() >= 3) {
            fhType="0";//免邮：0
            SingleyjLayout.setVisibility(View.GONE);
        } else if(UserUtils.LEVEL>=13&&list.size()>1) {
            fhType="3";//满13级2件包邮
            SingleyjLayout.setVisibility(View.VISIBLE);
            wwbdkyf_cb.setEnabled(false);
            wwbdkyf_cb.setText("  13级特权2件包邮");
          }else{
            fhType="1";//1  金币抵扣
            wwbdkyf_cb.setEnabled(false);
            SingleyjLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initAddressData() {
        if (!Utils.isEmpty(UserUtils.UserAddress)) {
             informationTv.setText(UserUtils.UserAddress);
            if(fhType.equals("1")){
                wwbdkyf_cb.setText( "  "+Utils.getJBDKNum(UserUtils.UserAddress)+"娃娃币抵扣邮费");
            }
        } else {
             informationTv.setText("新增收货地址");
             if(fhType.equals("1")){
                wwbdkyf_cb.setText("  娃娃币抵扣邮费");
            }
        }
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }


    @OnClick({R.id.image_back, R.id.consignment_rl, R.id.shipping_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.consignment_rl:
                //新增地址
                startActivity(new Intent(this, NewAddressActivity.class));
                break;
            case R.id.shipping_button:
                if(!Utils.isEmpty(UserUtils.UserAddress)){
                    information = UserUtils.UserAddress.replace(" ", ",");
                }
                setgetSendGoods();
                break;
        }
    }

private void  setgetSendGoods(){
    String remark = remarkEt.getText().toString();
     int length = list.size();
    if(length<1){
        MyToast.getToast(this, "请选择邮寄娃娃！").show();
        return;
    }
    if (Utils.isEmpty(information)) {
        MyToast.getToast(this, "请设置收货信息！").show();
        return;
    }
    if(!isFormalAddress()) {
        MyToast.getToast(getApplicationContext(), "暂不支持您所填写的地区！").show();
        return;
    }
    if (fhType.equals("1") || fhType.equals("2")) {
        if(!isEnough()) {
            MyToast.getToast(getApplicationContext(), "您的余额不足！").show();
            return;
        }
    }
    String dollID=list.get(0).getID() + ",";
    if(length > 1){
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                stringId.append(list.get(i).getID());
                stringDollId.append(list.get(i).getDOLLID());
            } else {
                stringId.append("," + list.get(i).getID());
                stringDollId.append(list.get(i).getDOLLID());
            }
        }
        dollID=String.valueOf(stringId);
    }
      getSendGoods(dollID, length + "", information, remark, UserUtils.USER_ID,fhType, Utils.getProvinceNum(UserUtils.UserAddress));
    }
    private void getSendGoods(String dollID, String number, String consignee, String remark, String userID, String mode,String costNum) {
        //Log.e(TAG, "发货参数=" + costNum);
        HttpManager.getInstance().getSendGoods(dollID, number, consignee, remark, userID, mode,costNum,null, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    list = loginInfoResult.getData().getPlayback();
                    MyToast.getToast(getApplicationContext(), "发货成功，请耐心等待！").show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("record", Utils.HTTP_OK);
                    intent.putExtras(bundle);
                    setResult(2, intent);
                    finish();
                }else{
                    MyToast.getToast(getApplicationContext(), loginInfoResult.getMsg()).show();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    /**
     * 判断用户余额是否够付邮费
     * @return
     */
    private boolean isEnough(){
        if (!Utils.getJBDKNum(UserUtils.UserAddress).equals("")) {
            int yf = Integer.parseInt(Utils.getJBDKNum(UserUtils.UserAddress));
            int ye = Integer.parseInt(UserUtils.UserBalance);
            if (ye >= yf) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean isFormalAddress(){
        if (!Utils.getJBDKNum(UserUtils.UserAddress).equals("")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
