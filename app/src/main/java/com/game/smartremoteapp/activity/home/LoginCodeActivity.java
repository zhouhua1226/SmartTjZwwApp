package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.MyToast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by chenw on 2018/3/21.
 */
public class LoginCodeActivity extends BaseActivity{
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_code;
    }
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        setLeftBtnDefaultOnClickListener();
        setTitle("登录");
    }
    @OnClick({R.id.bt_sure_login,R.id.tv_forget_password,R.id.tv_register})
    public void onViewClicked(View v){
         switch (v.getId()){
             case R.id.bt_sure_login:
                 login();
                 break;
             case R.id.tv_forget_password:
                startActivity(new Intent(this,FindBackPassActivity.class));
                 break;
             case R.id.tv_register:
                 Utils.toActivity(this,RegisterActivity.class);
                 break;
         }
    }

    private void login() {
        String phone=et_phone.getText().toString();
        String pass=et_password.getText().toString();
       if(phone.isEmpty()){
           MyToast.getToast(this,"请输入手机号").show();
           return;
       }if(!Utils.checkPhoneREX(phone)){
            MyToast.getToast(this,"手机号格式有误").show();
            return;
        }
        if(pass.isEmpty()){
            MyToast.getToast(this,"请输入密码").show();
            return;
        }if(!Utils.isTextPassword(pass)){
            MyToast.getToast(this,"密码6-20位数字和字母组合").show();
            return;
        }
        loginTask(phone,pass);
    }

    private void loginTask(String phone, String pass) {
        HttpManager.getInstance().getLoginPassword(phone, pass, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getCode()==0){
                    YsdkUtils.loginResult = httpDataInfoResult;
                    UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                    MyToast.getToast(getApplicationContext(), "登录成功！").show();
                    Intent intent = new Intent(LoginCodeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }
}
