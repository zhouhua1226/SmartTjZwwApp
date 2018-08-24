package com.game.smartremoteapp.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.protocol.JCResult;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.protocol.MD5;
import com.game.smartremoteapp.protocol.MyBase64;
import com.game.smartremoteapp.protocol.RspBodyBaseBean;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;

import java.lang.reflect.Method;

/**
 * Created by yincong on 2018/8/15 10:30
 * 修改人：
 * 修改时间：
 * 类描述：游戏中心
 */
public class GameCenterActivity extends Activity {

    private String TAG="GameCenterActivity";
    private Context context=GameCenterActivity.this;
    private WebView webView;
    private   GifView newswebGifView;
    private RelativeLayout layout;
    private WebSettings s;
    private String data,uid="459";
    private static final String gameCenterPath="http://60.55.47.172:8000/IntegralMall/IM_game.html";  //游戏中心 60.55.47.172:8000(测)  106.75.143.0(正)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamecenter);
        setStatusBarColor();
        initView();
        if(Utils.isNetworkAvailable(context)){
            fresh();
        }else {
            MyToast.getToast(context,"请检查网络").show();
        }

    }

    private void initView(){
        layout= (RelativeLayout) findViewById(R.id.gamecenter_rl);
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
    }


    //加载数据
    public void loadUrl() {
        if(!TextUtils.isEmpty(UserUtils.USER_ID)) {
            String auth = RspBodyBaseBean.getAuth(context, JCUtils.UID);
            try {
                String jiami=java.net.URLEncoder.encode(auth, "utf-8").toLowerCase();
                data = MyBase64.toHexString(MyBase64.encrypt(jiami, MyBase64.BAkey)).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("游戏中心--------------"+JCUtils.UID,JCUtils.GAMECENTERPATH+"?"+data);  //+"&phone="+ Build.BRAND+"&height="+getBottomStatusHeight(context)
            webView.loadUrl(JCUtils.GAMECENTERPATH+"?"+data);
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
        s.setAppCacheEnabled(true);  // 开启H5(APPCache)缓存功能
        s.setDatabaseEnabled(true);  // 设置数据库
        s.setAllowFileAccess(true);  // 可以读取文件缓存(manifest生效)
        if (Utils.isNetworkAvailable(context)) {  // 根据网络连接情况，设置缓存模式，
            s.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }

        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(this,"Native");

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

//        webView.loadUrl("javascript:alerts(Native.alerts())");
//        webView.loadUrl("javascript:alerts(Native.back())");
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
    @JavascriptInterface
    public void back(String data){
        this.finish();
    }

    @JavascriptInterface
    public void money(String data){
        Intent intent=new Intent(this,RechargeActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void goExbean(String bean){
        startActivity(new Intent(this,EXGoldenBeanActivity.class));
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

    private void getGameBeans(){
        String auth = RspBodyBaseBean.getAuth(context, JCUtils.UID);
        String opt="117";
        HttpManager.getInstance().getGameBeans(opt, auth, new RequestSubscriber<JCResult>() {
            @Override
            public void _onSuccess(JCResult jcResult) {
                String error=jcResult.getError();
                if(error.equals("0")){
                    JCUtils.GAMEJD=jcResult.getJD();
                    MyToast.getToast(getApplicationContext(),"我的金豆为"+JCUtils.GAMEJD).show();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }


}
