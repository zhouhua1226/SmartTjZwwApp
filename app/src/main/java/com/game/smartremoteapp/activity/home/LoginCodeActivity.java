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
import com.game.smartremoteapp.utils.ChannelUtils;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.game.smartremoteapp.utils.UserUtils.SP_TAG_PHONE;


/**
 * Created by chen on 2018/3/21.
 */
public class LoginCodeActivity extends BaseActivity {
    private static final String TAG = "LoginCodeActivity---";
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.login_loading_gv)
    GifView loginLoadingGv;
    private UMShareAPI mUMShareAPI;

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
        String phone = SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_PHONE, null);
        if (phone != null) {
            et_phone.setText(phone);
            et_password.requestFocus();
        }
        loginLoadingGv.setEnabled(false);
        loginLoadingGv.setMovieResource(R.raw.login_loadinggif);
        mUMShareAPI = UMShareAPI.get(this);
    }

    /**
     * 是否展示预加载
     *
     * @param isVisible
     */
    private void setGifView(boolean isVisible) {
        if (loginLoadingGv == null) {
            return;
        }
        if (isVisible) {
            loginLoadingGv.setVisibility(View.VISIBLE);
        } else {
            loginLoadingGv.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.bt_sure_login, R.id.tv_forget_password, R.id.tv_register,
            R.id.login_qq_iv, R.id.login_wx_iv})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_sure_login:
                login();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, FindBackPassActivity.class));
                break;
            case R.id.tv_register:
                Utils.toActivity(this, RegisterActivity.class);
                break;
            case R.id.login_qq_iv:
                if(Utils.isQQClientAvailable(this)){
                    mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, authListener);//QQ授权
                    setGifView(true);
                }else{
                    MyToast.getToast(this,"你还未安装QQ客户端!").show();
                }
                break;
            case R.id.login_wx_iv:
                if(Utils.isWeixinAvilible(this)) {
                    mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);//微信授权
                    setGifView(true);
                }else{
                    MyToast.getToast(this,"你还未安装微信客户端!").show();
                }
                break;
        }
    }

    private void login() {
        String phone = et_phone.getText().toString();
        String pass = et_password.getText().toString();
        if (phone.isEmpty()) {
            MyToast.getToast(this, "请输入手机号").show();
            return;
        }
        if (!Utils.checkPhoneREX(phone)) {
            MyToast.getToast(this, "手机号格式有误").show();
            return;
        }
        if (pass.isEmpty()) {
            MyToast.getToast(this, "请输入密码").show();
            return;
        }
        if (!Utils.isTextPassword(pass)) {
            MyToast.getToast(this, "密码6-20位数字和字母组合").show();
            return;
        }
        loginTask(phone, pass);
    }

    private void loginTask(final String phone, String pass) {
        HttpManager.getInstance().getLoginPassword(phone, pass, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode() == 0) {
                    YsdkUtils.loginResult = httpDataInfoResult;
                    UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, httpDataInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                    SPUtils.putString(getApplicationContext(), SP_TAG_PHONE, phone);
                    UserUtils.AGE = httpDataInfoResult.getData().getAppUser().getAGE();
                    MyToast.getToast(getApplicationContext(), "登录成功！").show();
                    Utils.toActivity(LoginCodeActivity.this, MainActivity.class);
                    finish();
                } else {
                    MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage(),TAG);
            }
        });
    }
    /**
     * 应用授权
     * @param platform
     */
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if(data==null){
                MyToast.getToast(getApplicationContext(),"登录失败！").show();
                setGifView(false);
                return;
            }
            getPlatformInfo(platform);
        }
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            setGifView(false);
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            // Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
            setGifView(false);
        }
    };


    /**
     * 获取用户信息
     * @param platform
     */
    private void getPlatformInfo(SHARE_MEDIA platform) {
         mUMShareAPI.getPlatformInfo(LoginCodeActivity.this, platform, new UMAuthListener() {
             @Override
             public void onStart(SHARE_MEDIA share_media) {
             }
             @Override
             public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> map) {
                 LogUtils.loge(map.toString(),TAG);
                 if(map==null){
                     MyToast.getToast(getApplicationContext(),"登录失败！").show();
                     setGifView(false);
                     return;
                 }
                 String uid = null;
                 String regChannel = null;
                 if(platform.equals(SHARE_MEDIA.WEIXIN)){
                     uid=map.get("unionid");
                     regChannel="WEIXIN";
                 }else if(platform.equals(SHARE_MEDIA.QQ)){
                     uid=map.get("openid");
                     regChannel="QQ";
                 }
                 String name= map.get("screen_name");
                 String gender= map.get("gender");
                 String profile_image_url= map.get("profile_image_url");
                 LogUtils.loge("个人信息刷新结果==uid=" + uid
                         + " regChannel=" + regChannel
                         + " name=" + name
                         + " profile_image_url=" + profile_image_url
                         + " gender=" + gender,TAG);
                 wxLoginTask(uid,name,gender,profile_image_url,regChannel);
             }
             @Override
             public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                 setGifView(false);
             }
             @Override
             public void onCancel(SHARE_MEDIA share_media, int i) {
                 setGifView(false);
             }
         });
    }
    /**
     * 第三方登录
     */
    private void wxLoginTask( String uid, String name, String gender, String profile_image_url,String regChannel) {
        HttpManager.getInstance().wxRegister(uid,name,gender,profile_image_url,regChannel, ChannelUtils.getChannelNum(),
          new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                setGifView(false);
                if (httpDataInfoResult.getCode() == 0) {
                    YsdkUtils.loginResult = httpDataInfoResult;
                    UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, httpDataInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                    MyToast.getToast(getApplicationContext(), "登录成功！").show();
                    UserUtils.AGE = httpDataInfoResult.getData().getAppUser().getAGE();
                    Utils.toActivity(LoginCodeActivity.this, MainActivity.class);
                    finish();
                } else {
                    MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                setGifView(false);
                LogUtils.logi(e.getMessage(),TAG);
            }
        });

    }

}
