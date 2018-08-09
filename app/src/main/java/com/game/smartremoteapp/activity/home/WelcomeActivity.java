package com.game.smartremoteapp.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.base.MyApplication;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.PermissionsUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.game.smartremoteapp.R.id.webview;
import static com.game.smartremoteapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;

/**
 * Created by chenw on 2018/7/19.
 */

public class WelcomeActivity extends BaseActivity{

    private static final String TAG ="WelcomeActivity-----" ;
    private String uid;
    @BindView(webview)
    WebView mWebView;
    @BindView(R.id.btn_timer)
    Button btn_timer;
    private WebSettings s;

    private boolean isTimeFinsh=false;
    private boolean isLogin=false;

    private CountDownTimer myCountDownTimer;
    private boolean isTime=true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume---");
        isTime=true;
        if(isTimeFinsh){
            toActivity();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e(TAG,"onStart()---");
//        isTime=true;
//        if(isTimeFinsh){
//            toActivity();
//        }
//    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getAppVersion();
        initWelcome();
    }
    private void getAppVersion() {
        Utils.appVersion = Utils.getAppCodeOrName(this, 0);
        Utils.osVersion = Utils.getSystemVersion();
        Utils.deviceType = Utils.getDeviceBrand();
        Utils.IMEI = Utils.getIMEI(this);
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initWelcome() {
         fresh();
        if (SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false)) {
            //用户已经注册
            uid =  SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
            if (Utils.isEmpty(uid)) {
                return;
            }
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                getAuthLogin(uid);
            } else {
                MyToast.getToast(getApplicationContext(), "请查看你的网络！").show();
            }
        }

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCountDownTimer.cancel();
                if(isLogin){
                    Utils.toActivity(WelcomeActivity.this,MainActivity.class);
                    finish();
                }else{
                   Utils.toActivity(WelcomeActivity.this,SplashActivity.class);
                    finish();
                }
            }
        });
        myCountDownTimer=  new  MyCountDownTimer(5000,1000).start();
    }
   private void  setPermissions(){
       PermissionsUtils.checkPermissions(this, new String[]{
               Manifest.permission.READ_PHONE_STATE},
               PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                   @Override
                   public void onSuccessful() {
                       getAppVersion();
                   }
                   @Override
                   public void onFailure() {

                   }
               });
       }


    /**
     *
     * 自动登录
     * @param userId
     */
    private void getAuthLogin(String userId) {
        HttpManager.getInstance().getAuthLogin(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult == null || loginInfoResult.getData() == null
                        || loginInfoResult.getData().getAppUser() == null) {
                    return;
                }
                if (loginInfoResult.getMsg().equals("success")) {
                    isLogin=true;
                    YsdkUtils.loginResult = loginInfoResult;
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, loginInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    UserUtils.USER_ID = loginInfoResult.getData().getAppUser().getUSER_ID();
                    UserUtils.isUserChanger = true;
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private boolean isFirstInto() {
        // 判断是否第一次打开app
        boolean user_first = SPUtils.getBoolean(getApplicationContext(),"FIRST", true);
        if (user_first) {
            SPUtils.putBoolean(getApplicationContext(),"FIRST", false);
        }
        return user_first;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

 //倒计时
class MyCountDownTimer extends CountDownTimer {

     public MyCountDownTimer(long millisInFuture, long countDownInterval) {
         super(millisInFuture, countDownInterval);
     }

     @Override
     public void onTick(long millisUntilFinished) {
         btn_timer.setText("跳过" + millisUntilFinished / 1000 + "s");
     }

     @Override
     public void onFinish() {
         isTimeFinsh=true;
         toActivity();
     }
 }

    private void  toActivity(){
        myCountDownTimer.cancel();
        if( isTimeFinsh&&isTime) {
            if (isLogin ) {
                Utils.toActivity(WelcomeActivity.this, MainActivity.class);
                finish();
            } else {
                Utils.toActivity(WelcomeActivity.this, SplashActivity.class);
                finish();
            }
        }
    }
    private void loadUrl(){
        mWebView.loadUrl(UrlUtils.ADVERTYURL);
        mWebView.reload();
    }
    //设置属性
    @SuppressLint("JavascriptInterface")
    private void fresh(){
        loadUrl();
        s= mWebView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBlockNetworkImage(true);
        s.setRenderPriority(WebSettings.RenderPriority.HIGH);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.addJavascriptInterface(this,"Native");
        mWebView.setWebViewClient(new WebViewClient()
        {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                s.setBlockNetworkImage(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.equals(UrlUtils.ADVERTYURL)){
                    view.loadUrl(url);
                    return true;
                }else{
                   isTime=false;
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    WelcomeActivity.this.startActivity(intent);
                    return true;
                }
          }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog

            }
        });

    }

    //android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            if(mWebView.canGoBack()){
                mWebView.goBack();// 返回前一个页面
                return true;
              }else{
                MyApplication.getInstance().exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
