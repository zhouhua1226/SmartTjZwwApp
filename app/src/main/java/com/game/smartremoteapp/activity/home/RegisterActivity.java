package com.game.smartremoteapp.activity.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.bean.Token;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/3/21.
 */
public class RegisterActivity extends BaseActivity{
    @BindView(R.id.et_register_phone)
    EditText et_phone;
    @BindView(R.id.et_register_password)
    EditText et_password;
    @BindView(R.id.et_register_sure_password)
    EditText sure_password;
    @BindView(R.id.et_register_code)
    EditText register_code;
    @BindView(R.id.tv_get_code)
    TextView  getCode;
   private MyCountDownTimer myCountDownTimer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
        myCountDownTimer =new MyCountDownTimer(60000,1000);
    }

    @Override
    protected void initView() {
        setLeftBtnDefaultOnClickListener();
        setTitle("注册");
    }

    @OnClick({R.id.btn_sure_register,R.id.tv_get_code})
    public void onViewClicked(View v){
          switch (v.getId()){
              case R.id.btn_sure_register:
                  registerTask();
                  break;
              case R.id.tv_get_code:
                  getCode();
                  break;
          }
    }

    private void registerTask() {
        String phone=et_phone.getText().toString();
        String password=et_password.getText().toString();
        String pass=sure_password.getText().toString();
        String code=register_code.getText().toString();
        if(phone.isEmpty()){
            MyToast.getToast(this,"请输入手机号").show();
            return;
        }if(!Utils.checkPhoneREX(phone)){
            MyToast.getToast(this,"手机号格式有误").show();
            return;
        }
        if(password.isEmpty()){
            MyToast.getToast(this,"请输入密码").show();
            return;
        }if(!Utils.isTextPassword(pass)){
            MyToast.getToast(this,"密码6-20位数字和字母组合").show();
            return;
        }
        if(!password.equals(pass)){
            MyToast.getToast(this,"两次输入的密码不一致").show();
            return;
        }
        if(code.isEmpty()){
            MyToast.getToast(this,"请输入短信验证码").show();
            return;
        }
        regiterTask(phone,pass,code);
    }

    private void regiterTask(String phone, String pass, String code) {
        HttpManager.getInstance().getRegiter(phone,pass,code, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                MyToast.getToast(RegisterActivity.this,"短信验证码一下发").show();
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void getCode() {
        String phone=et_phone.getText().toString();
        if(phone.isEmpty()){
            MyToast.getToast(this,"请输入手机号").show();
            return;
        }if(!Utils.checkPhoneREX(phone)){
            MyToast.getToast(this,"手机号格式有误").show();
            return;
        }
        myCountDownTimer.start();
        getCodeTask(phone);
    }

    private void getCodeTask(String phone) {
        String str = Base64.encodeToString(phone.getBytes(), Base64.DEFAULT);
        HttpManager.getInstance().getCode(str, new RequestSubscriber<Result<Token>>() {
            @Override
            public void _onSuccess(Result<Token> token) {
                MyToast.getToast(RegisterActivity.this,"短信验证码一下发").show();
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    //倒计时
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setTextColor(Color.RED);
            getCode.setText("倒计时("+millisUntilFinished/1000+")");
            getCode.setEnabled(false);
        }
        @Override
        public void onFinish() {
            getCode.setTextColor(Color.rgb(141,188,246));
            getCode.setText("重新获取");
            getCode.setEnabled(true);
        }
    }
}
