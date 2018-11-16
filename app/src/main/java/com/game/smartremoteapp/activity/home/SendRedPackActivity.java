package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;

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

    @OnClick({R.id.image_back, R.id.btn_send_redwallet })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.btn_send_redwallet:
                this.finish();
                break;
        }
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
