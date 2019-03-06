package com.game.smartremoteapp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.LoginCodeActivity;
import com.game.smartremoteapp.activity.home.MainActivity;
import com.game.smartremoteapp.base.BaseFragment;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.view.GifView;
import com.game.smartremoteapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mi on 2018/12/3.
 */

public class HomeFragment extends BaseFragment{
    @BindView(R.id.lv_malayout)
    RelativeLayout mLayout;
    private WebView webView;

    private GifView newswebGifView;
    private String TAG="HomeFragment----------";
    private WebSettings s;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getActivity().getApplicationContext());
        webView.setLayoutParams(params);
        mLayout.addView(webView);

        newswebGifView = new GifView(getActivity());
        newswebGifView.setLayoutParams(params);
        mLayout.addView(newswebGifView);
        newswebGifView.setEnabled(false);
        newswebGifView.setMovieResource(R.raw.waitloadinggif);
        newswebGifView.setVisibility(View.VISIBLE);
        fresh();
    }
    //加载数据
    public void loadUrl() {
        if (!TextUtils.isEmpty(UserUtils.USER_ID)) {
            String  FIRST_CHARGE=  SPUtils.getString(getActivity().getApplicationContext(), UserUtils.SP_FIRET_CHARGE,"0");
            String data = "userID="+UserUtils.USER_ID+"&sessionID="+UserUtils.sessionID+"&FIRST_CHARGE="+FIRST_CHARGE;
            LogUtils.loge(JCUtils.HOMEURL + "?" + data, TAG);
            webView.loadUrl(JCUtils.HOMEURL + "?" + data);
            webView.reload();
        } else {
            MainActivity.mMainActivity.finish();//汤姆抓娃娃
            Intent intent = new Intent(getActivity(), LoginCodeActivity.class);
            startActivity(intent);
        }
    }
    //设置属性
    @SuppressLint("JavascriptInterface")
    private void fresh(){

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
        loadUrl();
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
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                LogUtils.loge(s,TAG);
                webView.loadUrl(s);
                return true;
            }
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                newswebGifView.setVisibility(View.GONE);
                MyToast.getToast(getActivity().getApplicationContext(),"加载出错，请重试！");
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
    public void onDestroyView() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }

}
