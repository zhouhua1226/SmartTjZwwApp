package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.LoginInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hongxiu on 2017/10/23.
 */
public class NewAddressActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.preserve_button)
    Button preserveButton;
    @BindView(R.id.newaddress_name_et)
    EditText newaddressNameEt;
    @BindView(R.id.newaddress_phone_et)
    EditText newaddressPhoneEt;
    @BindView(R.id.newaddress_dq_et)
    EditText newaddressDqEt;
    @BindView(R.id.newaddress_detail_et)
    EditText newaddressDetailEt;

    private String TAG="NewAddressActivity--";
    private String name="";
    private String phone="";
    private String address="";
    private String information="";

    @Override
    protected int getLayoutId() {
        //新增地址页面
        return R.layout.activity_newaddress;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void initData(){
        name=newaddressNameEt.getText().toString();
        phone=newaddressPhoneEt.getText().toString();
        address=newaddressDetailEt.getText().toString();
    }



    @OnClick({R.id.image_back, R.id.preserve_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.preserve_button:
                initData();
                if(Utils.isEmpty(name)||Utils.isEmpty(phone)||Utils.isEmpty(address)){
                    MyToast.getToast(this, "请将信息填写完整！").show();
                }else {
                    information=name+"  "+phone+"  "+address;
                    UserUtils.UserAddress=information;
                    getConsignee(name,phone,address,UserUtils.USER_ID);

                    //finish();
                }


                break;
        }
    }

    private void getConsignee(String name,String phone,String address,String userID){
        HttpManager.getInstance().getConsignee(name, phone, address, userID, new RequestSubscriber<Result<LoginInfo>>() {
            @Override
            public void _onSuccess(Result<LoginInfo> loginInfoResult) {
                Log.e(TAG,"收货信息结果="+loginInfoResult.getMsg());
                String name=loginInfoResult.getData().getAppUser().getCNEE_NAME();
                String phone=loginInfoResult.getData().getAppUser().getCNEE_PHONE();
                String address=loginInfoResult.getData().getAppUser().getCNEE_ADDRESS();
                UserUtils.UserAddress=name+" "+phone+" "+address;
                MyToast.getToast(getApplicationContext(), "保存成功！").show();
                finish();
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }


}
