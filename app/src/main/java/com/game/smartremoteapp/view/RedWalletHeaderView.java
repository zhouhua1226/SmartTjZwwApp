package com.game.smartremoteapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.Marquee;

import java.util.List;

/**
 * Created by mi on 2019/1/8.
 */

public class RedWalletHeaderView extends LinearLayout {
    private  MarqueeView marqueeview;
    private Context mtx;
    public RedWalletHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public RedWalletHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mtx=context;
        View view =  LayoutInflater.from(mtx).inflate(R.layout.item_redwallet_header, (ViewGroup)findViewById(android.R.id.content),false);
        marqueeview = (MarqueeView) view.findViewById(R.id.marqueeview);
        addView(view);
    }

    /**
     * Marqueeview
     */
    public  void setMarqueeview(List<Marquee> marquees,boolean isImage ){
        marqueeview.setImage(isImage);
        marqueeview.startWithList(marquees);
    }


}
