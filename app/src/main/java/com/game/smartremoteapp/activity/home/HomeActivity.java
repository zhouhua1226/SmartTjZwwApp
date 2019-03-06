package com.game.smartremoteapp.activity.home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.base.MyApplication;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mi on 2018/12/3.
 */

public class HomeActivity extends BaseActivity{
    @BindView(R.id.lv_malayout)
    RelativeLayout mLayout;
    private WebView webView;

    private GifView newswebGifView;
    private String TAG="HomeActivity----------";
    private WebSettings s;
    private long mExitTime;
    private boolean isFirst=false;
    @Override
    protected int getLayoutId() {
      return R.layout.activity_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView( getApplicationContext());
        webView.setLayoutParams(params);
        mLayout.addView(webView);

        newswebGifView = new GifView(getApplicationContext());
        newswebGifView.setLayoutParams(params);
        mLayout.addView(newswebGifView);
        newswebGifView.setEnabled(false);
        newswebGifView.setMovieResource(R.raw.waitloadinggif);
        newswebGifView.setVisibility(View.VISIBLE);
        if(Utils.isNetworkAvailable(this)){
            fresh();
        }else {
            MyToast.getToast(this,"请检查网络").show();
        }
    }

    //加载数据
    public void loadUrl() {
        if (!TextUtils.isEmpty(UserUtils.USER_ID)) {
            String  FIRST_CHARGE=  SPUtils.getString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE,"0");
            String data = "userID="+UserUtils.USER_ID+"&sessionID="+UserUtils.sessionID+"&FIRST_CHARGE="+FIRST_CHARGE;
            LogUtils.loge(JCUtils.HOMEURL + "?" + data, TAG);
            webView.loadUrl(JCUtils.HOMEURL + "?" + data);
            webView.reload();
        } else {
            MainActivity.mMainActivity.finish();//汤姆抓娃娃
            Intent intent = new Intent(this, LoginCodeActivity.class);
            startActivity(intent);
        }
    }

    //设置属性
    @SuppressLint("JavascriptInterface")
    private void fresh(){
        s= webView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);   //WebSettings.LayoutAlgorithm.NARROW_COLUMNS设置布局，会引起WebView的重新布局（relayout）,默认值NARROW_COLUMNS
        s.setUseWideViewPort(true);        //WebView是否支持HTML的“viewport”标签或者使用wide viewport
        s.setLoadWithOverviewMode(true);  //是否允许WebView度超出以概览的方式载入页面，默认false
        s.setJavaScriptEnabled(true);   //设置WebView是否允许执行JavaScript脚本，默认false
        s.setGeolocationEnabled(true);  //定位是否可用，默认为true
        s.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        s.setBlockNetworkImage(true);  //使把图片加载放在最后来加载渲染
        s.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        s.setDefaultTextEncodingName("utf-8");
        s.setLoadsImagesAutomatically(true);
        if (Utils.isNetworkAvailable(this)) {  // 根据网络连接情况，设置缓存模式，
            s.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new JsInterface(), "AndroidWebView");
        loadUrl();
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                isFirst=false;
            }
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                s.setBlockNetworkImage(false);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                LogUtils.loge(url,TAG);
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                }

                return true;
            }
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                newswebGifView.setVisibility(View.GONE);
                MyToast.getToast(getApplicationContext(),"加载出错，请重试！");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if(progress == 100){
                    newswebGifView.setVisibility(View.GONE);
                    mLayout.removeView(newswebGifView);
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
        }
    }

    private class JsInterface {
        public JsInterface() {
        }
        @JavascriptInterface
        public void Back() {
            getLogout(UserUtils.sessionID);
        }
        @JavascriptInterface
        public void  index() {
            isFirst=true;
        }
        @JavascriptInterface
        public void my() {
            isFirst=true;
        }
    }
    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if(isFirst){
                exit();
            }else{
                webView.goBack();
            }
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLogout(String userId) {
        HttpManager.getInstance().getLogout(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                LogUtils.loge( "退出登录结果=" + loginInfoResult.getMsg(),TAG);
                if (loginInfoResult.getMsg().equals("success")) {
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_ISLOGOUT, true);
                    SPUtils.putString(getApplicationContext(), YsdkUtils.AUTH_TOKEN, "");
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false);
                    startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                    finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
}
