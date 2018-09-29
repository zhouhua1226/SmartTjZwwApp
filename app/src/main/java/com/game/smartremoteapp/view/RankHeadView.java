package com.game.smartremoteapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.utils.UserUtils;

/**
 * Created by mi on 2018/9/6.
 */

public class RankHeadView extends LinearLayout {
    private ImageView  mIcon;
    private TextView mName,mqualifying,mMqualiy,mWhy,mUp,mtype,mIsBank;
    private Context mtx;
    public RankHeadView(Context context) {
        super(context);
        initView(context);
    }

    public RankHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.mtx=context;
        View view =  LayoutInflater.from(mtx).inflate(R.layout.item_rank_header, (ViewGroup)findViewById(android.R.id.content),false);
        mIcon = (ImageView) view.findViewById(R.id.iv_user_header);
        mName = (TextView) view.findViewById(R.id.tv_is_bank);
        mqualifying = (TextView) view.findViewById(R.id.tv_user_oder);
        mMqualiy = (TextView) view.findViewById(R.id.tv_user_numer);
        mWhy = (TextView) view.findViewById(R.id.tv_bank_why);
        mUp = (TextView) view.findViewById(R.id.tv_bank_up);
        mtype = (TextView) view.findViewById(R.id.tv_bank_type_num);
        mIsBank = (TextView) view.findViewById(R.id.tv_is_bank);
        addView(view);
        setUserIcon();
    }

    public void setUserName(String name) {
        mName.setText(name);
    }
    public void setUserIcon() {
        Glide.with(mtx).load(UserUtils.UserImage).asBitmap().
                error(R.mipmap.app_mm_icon).
                transform(new GlideCircleTransform(mtx)).into(mIcon);
    }
    public void setUserMqualifying(String  qualifying) {
        mqualifying.setText(qualifying);
    }

    public void setUserWhy(String why) {
        mWhy.setText("");
    }
    public void setUserMqualiy(String name) {
        mMqualiy.setText("  我的抓取数：10个");
    }

    public void setUserType(String type) {
        mtype.setText(type);
    }
    public void setUserIsBank(String isBnak) {
        mIsBank.setText(isBnak);
    }
}
