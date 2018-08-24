package com.game.smartremoteapp.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.PermissionsUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.ShareDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.game.smartremoteapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;

/**
 * Created by mi on 2018/8/13.
 */

public class IntegralTaskActivity extends BaseActivity {
    @BindView(R.id.lv_malayout)
    RelativeLayout mLayout;
    private  WebView webView;

    private GifView newswebGifView;
    private String TAG="IntegralActivity";
    private WebSettings s;
    private String urlPath;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getPointsMallUrl();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        mLayout.addView(webView);

        newswebGifView = new GifView(getApplicationContext());
        newswebGifView.setLayoutParams(params);
        mLayout.addView(newswebGifView);
        newswebGifView.setEnabled(false);
        newswebGifView.setMovieResource(R.raw.waitloadinggif);
        newswebGifView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null&&urlPath!=null){
            fresh();
        }
    }

    private void loadUrl(){
        Log.e(TAG,"图片新闻url="+urlPath);
        webView.loadUrl(urlPath.replace("\"", "/"));
        webView.reload();
    }
    //设置属性
    @SuppressLint("JavascriptInterface")
    private void fresh(){

        loadUrl();
        s= webView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBlockNetworkImage(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new  JsInterface(), "AndroidWebView");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                s.setBlockNetworkImage(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
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
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    /**
     *积分商城
     */
    private void getPointsMallUrl(){
        HttpManager.getInstance().getPointsMallTask(UserUtils.USER_ID ,new RequestSubscriber<Result<String>>() {
            @Override
            public void _onSuccess(Result<String> loginInfoResult) {
                if(loginInfoResult.getData()!=null){
                    urlPath=loginInfoResult.getData();
                    fresh();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
    private class JsInterface {
        public JsInterface( ) {
        }
        @JavascriptInterface
        public void onBacktask() {
            finish();
        }
        //分享
        @JavascriptInterface
        public void share() {
            PermissionsUtils.checkPermissions(IntegralTaskActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                        @Override
                        public void onSuccessful() {
                            shareApp();
                        }
                        @Override
                        public void onFailure() {
                        }
                    });
        }
        //邀请好友
        @JavascriptInterface
        public void  invite() {
            Utils.toActivity(IntegralTaskActivity.this,LnvitationCodeActivity.class);
        }
        //抓娃娃
        @JavascriptInterface
        public void  fist() {
            MainActivity.mMainActivity.finish();
            Intent intent=new Intent(IntegralTaskActivity.this,MainActivity.class);
            intent.putExtra("mainIndex",0);
            Utils.toActivity(IntegralTaskActivity.this,intent);
            finish();
        }
        //充值
        @JavascriptInterface
        public void recharge() {
            Utils.toActivity(IntegralTaskActivity.this,RechargeActivity.class);
        }
    }
    //分享
    private void shareApp() {
            new ShareDialog(this, new ShareDialog.OnShareSuccessOnClicker() {
                @Override
                public void onShareSuccess() {
                    fresh();
                }
            });

    }
}
