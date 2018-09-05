package com.game.smartremoteapp.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

import static com.game.smartremoteapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;

/**
 * Created by chenw on 2018/7/19.
 */
public class WelcomeActivity extends BaseActivity{
    private static final String TAG ="WelcomeActivity-----";
    private String uid;
    @BindView(R.id.btn_timer)
    Button btn_timer;
    @BindView(R.id.rl_mlayout)
    RelativeLayout mlayout;
    private WebSettings s;
    private boolean isTimeFinsh=false;
    private boolean isLogin=false;
    private CountDownTimer myCountDownTimer;
    private boolean isTime=true;
    private WebView mWebView;
    private ImageView mImageView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTime=true;
        if(isTimeFinsh){
            toActivity();
        }
    }
  private void  setSelectServer(){
      AlertDialog dialog = new AlertDialog.Builder(this)
              .setMessage("设置应用类型")
              .setCancelable(false)
              .setPositiveButton("真实服", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      HttpManager.setBaseUrl(UrlUtils.URL);
                      SPUtils.putBoolean(getApplicationContext(),SPUtils.ISTEST,false);
                      getAuthLogin(uid);

                  }
              })
              .setNegativeButton("测试服", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      HttpManager.setBaseUrl(UrlUtils.URL_TEST);
                      SPUtils.putBoolean(getApplicationContext(),SPUtils.ISTEST,true);
                      getAuthLogin(uid);

                  }
              }).create();
      dialog.show();
  }
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mlayout.addView(mWebView);

        mImageView = new ImageView(getApplicationContext());
        mImageView.setLayoutParams(params);
        mlayout.addView(mImageView);
        mImageView.setBackgroundResource(R.drawable.app_yd_naviage);
    }

    private void initWelcome() {
         fresh();
        //默认真实服
        HttpManager.setBaseUrl(UrlUtils.URL);
        SPUtils.putBoolean(getApplicationContext(),SPUtils.ISTEST,false);

        if (SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false)) {
            //用户已经注册
            uid =  SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
            if (Utils.isEmpty(uid)) {
                return;
            }
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                 getAuthLogin(uid); //真实发布
            } else {
                MyToast.getToast(getApplicationContext(), "请查看你的网络！").show();
            }
        }
     //  setSelectServer();//测试

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCountDownTimer.cancel();
                setActivity();
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
        if (Utils.isEmpty(userId)) {
            return;
        }
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
            setActivity();
        }
    }
    private void setActivity(){
        if (isLogin ) {
            Utils.toActivity(WelcomeActivity.this, MainActivity.class);
            finish();
        } else {
            Utils.toActivity(WelcomeActivity.this, SplashActivity.class);//汤姆抓娃娃
             finish();
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
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.addJavascriptInterface(this,"Native");
        mWebView.setWebViewClient(new WebViewClient() {
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
                if(progress == 100){
                    mlayout.removeView(mImageView);
                }
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }else{
            myCountDownTimer.cancel();
            MyApplication.getInstance().exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
