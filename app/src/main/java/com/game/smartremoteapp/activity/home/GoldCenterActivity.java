package com.game.smartremoteapp.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;

import java.lang.reflect.Method;

/**
 * Created by mi on 2018/9/20.
 */

public class GoldCenterActivity extends BaseActivity {

    private String TAG="GameCenterActivity";
    private Context context=GoldCenterActivity.this;
    private WebView webView;
    private GifView newswebGifView;
    private RelativeLayout layout;
    private WebSettings s;
    private String urlPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected void initView(){
        layout= (RelativeLayout) findViewById(R.id.lv_malayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        layout.addView(webView);

        newswebGifView = new GifView(getApplicationContext());
        newswebGifView.setLayoutParams(params);
        layout.addView(newswebGifView);
        newswebGifView.setEnabled(false);
        newswebGifView.setMovieResource(R.raw.waitloadinggif);
        newswebGifView.setVisibility(View.VISIBLE);

        if(Utils.isNetworkAvailable(context)){
            getGoldUrl();
        }else {
            MyToast.getToast(context,"请检查网络").show();
        }
    }

    //加载数据
    public void loadUrl() {
        if(urlPath!=null) {
            LogUtils.loge( urlPath,TAG);
            webView.loadUrl(urlPath);
            webView.reload();
        }else {
            MyToast.getToast(context,"请登录！").show();
            Intent intent=new Intent(context,LoginCodeActivity.class);
            startActivity(intent);
            finish();
        }
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
        // s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new  JsInterface(), "AndroidWebView");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                s.setBlockNetworkImage(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB&&webView!=null) {
                    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
                super.onPageFinished(view,url);
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
                if(progress == 100){
                    newswebGifView.setVisibility(View.GONE);
                    layout.removeView(newswebGifView);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.requestFocus();
        webView.reload();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
    //JS调用Android JAVA方法名和HTML中的按钮 onclick后的别名后面的名字对应
    private class JsInterface {
        public JsInterface() {
        }

        @JavascriptInterface
        public void onBack() {
            finish();
        }
    }
    /**
     * 获取虚拟按键的高度
     * @param context
     * @return
     */
    public  int getBottomStatusHeight(Context context){
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight(context);
        return totalHeight  - contentHeight;
    }

    //设置状态栏
    private  void setStatusBarColor(){
        Window window =getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.main_red));
        }

    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public int getDpi(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private void getGoldUrl(){
        HttpManager.getInstance().getGoldMainUrl(UserUtils.USER_ID, new RequestSubscriber<Result<String>>() {
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
}
