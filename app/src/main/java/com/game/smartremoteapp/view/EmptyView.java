package com.game.smartremoteapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;

/**
 * Created by mi on 2019/1/2.
 */

public class EmptyView    extends RelativeLayout{


    private String mDesc;

    public EmptyView(Context context) {
        super(context);
        init(context);
    }
    public EmptyView(Context context, String desc) {
        super(context);
        mDesc=desc;
        init(context);
    }

    private void init(Context context) {
        View mView= LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null);
        TextView   empty = (TextView) mView.findViewById(R.id.tv_empty);
       RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       mView.setLayoutParams(layoutParams);
        addView(mView);
        empty.setText(mDesc);
    }
}
