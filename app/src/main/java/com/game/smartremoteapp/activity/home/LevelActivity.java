package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.JoinEarnAdapter;
import com.game.smartremoteapp.adapter.LevelPowerAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.LevelPowerBean;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.LevelPowerDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/11/1.
 */

public class LevelActivity  extends BaseActivity{
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_image)
    RoundedImageView user_image;
    @BindView(R.id.user_level_value)
    TextView level_value;
    @BindView(R.id.user_level_desc)
    TextView level_desc;
    @BindView(R.id.progress_level)
    ProgressBar progress_level;
    @BindView(R.id.tv_progress_level_value)
    TextView progress_level_value;
    @BindView(R.id.tv_progress_level_percent)
    TextView progress_level_percent;
    @BindView(R.id.rcv_level)
    RecyclerView mRecyclerView;

    private LevelPowerAdapter mAdapter;
    private   List<LevelPowerBean> mLevelPowers=new ArrayList<>();
    private LevelPowerDialog mDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_level;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }
    @OnClick({R.id.image_back})
    public  void onClickView(View v){
        switch (v.getId()){
            case R.id.image_back:
                finish();
                break;
        }
    }
    @Override
    protected void initView() {
        if (!UserUtils.NickName.equals("")) {
            user_name.setText(UserUtils.NickName);
        } else {
            user_name.setText("暂无昵称");
        }
        Glide.with(this)
                .load(UserUtils.UserImage)
                .error(R.mipmap.app_mm_icon)
                .dontAnimate()
                .transform(new GlideCircleTransform(this))
                .into(user_image);
       initData();

    }

    private void initData() {
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_post,"随心邮寄","LV13开启"));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_gift,"精美礼品","LV16开启"));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_treasure_chest,"神秘宝箱","LV18开启"));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_recharge,"充值特惠","LV31开启"));
        mAdapter = new LevelPowerAdapter(this, mLevelPowers);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter.setmOnItemClickListener(new JoinEarnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
             int iocn =R.drawable.icon_level_post_desc;
              switch (position){
                  case 0:
                      iocn=R.drawable.icon_level_post_desc;
                      break;
                  case 1:
                      iocn=R.drawable.icon_level_gift_desc;
                      break;
                  case 2:
                      iocn=R.drawable.icon_level_treasure_chest_desc;
                      break;
                  case 3:
                      iocn=R.drawable.icon_level_recharge_desc;
                      break;
              }
                if(mDialog==null){
                    mDialog = new LevelPowerDialog(LevelActivity.this, R.style.easy_dialog_style);
                }
                mDialog.show();
                mDialog.setLevelPowerBg(iocn);
            }
        });
    }

}
