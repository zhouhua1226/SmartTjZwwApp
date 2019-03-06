package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.CatchDollResultDialog;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/11/14.
 */

public class SendRedPackActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_gold_number)
    EditText et_gold_number;
    @BindView(R.id.et_redwallet_number)
    EditText et_redwallet_number;
    @BindView(R.id.btn_send_redwallet)
    Button send_redwallet;
    private String gold_number;
    private String redwallet_number;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_redpack;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        et_gold_number.addTextChangedListener(this);
        et_redwallet_number.addTextChangedListener(this);
    }

    @OnClick({R.id.image_back, R.id.btn_send_redwallet,R.id.right_title_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.right_title_tv:
               Utils.toActivity(this,RedPackageRecordActivity.class);
                break;
            case R.id.btn_send_redwallet:
                String redGold=et_gold_number.getText().toString();
                String redNumber=et_redwallet_number.getText().toString();

              if(redGold.isEmpty()){
                  MyToast.getToast(this,"请输入红包金币数！").show();
                  return;
              }
                if(Integer.parseInt(redGold)>Integer.parseInt(UserUtils.UserBalance)){
                    setCatchResultDialog(0);
                    return;
                }
                if(Integer.parseInt(redGold)<10){
                    MyToast.getToast(this,"请红包金币数不少于10个！").show();
                    return;
                }
                if(redNumber.isEmpty()){
                    MyToast.getToast(this,"请输入红包数量！").show();
                    return;
                }
                if(Integer.parseInt(redNumber)<2){
                    MyToast.getToast(this,"请红包数不少于2个！").show();
                    return;
                }
                sendRedWallet(redGold,redNumber);
                break;
        }
    }
    private void setCatchResultDialog(final int index  ) {
        final CatchDollResultDialog catchDollResultDialog = new CatchDollResultDialog(this, R.style.activitystyle);
        catchDollResultDialog.setCancelable(false);
        catchDollResultDialog.show();
        catchDollResultDialog.setTitle("您的金币余额不足");
        catchDollResultDialog.setRechargeContent();
        catchDollResultDialog.setFail("取消充值");
        catchDollResultDialog.setSuccess("前往充值");
        catchDollResultDialog.setBackground(R.drawable.catchdialog_success_bg);
        catchDollResultDialog.setDialogResultListener(new CatchDollResultDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if(resultCode>0) {
                    switch (index) {
                        case 0:
                            Utils.toActivity(SendRedPackActivity.this, RechargeActivity.class);
                            break;
                    }
                }
                catchDollResultDialog.dismiss();
            }
        });
    }
    private void sendRedWallet(String redGold, String redNumber) {
        HttpManager.getInstance().shareRedPackage(redGold,redNumber, UserUtils.USER_ID,new RequestSubscriber<Result<Void >>() {
            @Override
            public void _onSuccess(Result<Void> loginInfoResult) {
                if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    MyToast.getToast(SendRedPackActivity.this,"红包金币发送成功！").show();
                     finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
            gold_number=et_gold_number.getText().toString();
            redwallet_number=et_redwallet_number.getText().toString();
           if(!gold_number.isEmpty()&&!redwallet_number.isEmpty()){
                send_redwallet.setEnabled(true);
            }else{
               send_redwallet.setEnabled(false);
           }
    }


}
