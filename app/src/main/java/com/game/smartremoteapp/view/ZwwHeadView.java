package com.game.smartremoteapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.activity.home.NewsWebActivity;
import com.game.smartremoteapp.bean.BannerBean;
import com.game.smartremoteapp.bean.Marquee;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;
/**
 * Created by mi on 2018/8/22.
 */
public class ZwwHeadView   extends LinearLayout {
    private  MarqueeView marqueeview;
    private  TabLayout typeTabLayout;
    private  RelativeLayout mArqueeView;
    private  Banner zwwBanner;
    private Context mtx;
    public ZwwHeadView(Context context) {
        super(context);
        initView(context);
    }

    public ZwwHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mtx=context;
        View view =  LayoutInflater.from(mtx).inflate(R.layout.item_zww_header, (ViewGroup)findViewById(android.R.id.content),false);
        zwwBanner = (Banner) view.findViewById(R.id.zww_banner);
        marqueeview = (MarqueeView) view.findViewById(R.id.marqueeview);
        typeTabLayout = (TabLayout) view.findViewById(R.id.type_tly);
        mArqueeView = (RelativeLayout) view.findViewById(R.id.rl_marqueeview);
        mArqueeView.getBackground().setAlpha(120);
        addView(view);
    }
    /**
     * /banner轮播
     */
    public void initBanner(List<String> lists, final List<BannerBean> nBannerList) {

        //设置Banner样式
        zwwBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        zwwBanner.setImageLoader(new GlideImageLoader());
        zwwBanner.setImages(lists);
        //设置Banner动画效果
        zwwBanner.setBannerAnimation(Transformer.DepthPage);
        //设置轮播时间
        zwwBanner.setDelayTime(3000);
        //设置指示器位置(当banner模式中有指示器时)
        zwwBanner.setIndicatorGravity(BannerConfig.CENTER);
        //Banner设置方法全部调用完毕时最后调用
        zwwBanner.start();
        zwwBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerBean  mBannerBean=  nBannerList.get(position);
                if (!mBannerBean.getHREF_ST().equals("")) {
                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
                    intent.putExtra("newsurl",  mBannerBean.getHREF_ST());
                    intent.putExtra("newstitle", mBannerBean.getRUN_NAME());
                    mtx.startActivity(intent);
                }
            }
        });
    }
    /**
     * Marqueeview
     */
    public  void setMarqueeview(List<Marquee> marquees){
        mArqueeView.setVisibility(View.VISIBLE);
        marqueeview.setImage(true);
        marqueeview.startWithList(marquees);
    }

    public TabLayout getTabLayout(){
        return typeTabLayout;
    }
}