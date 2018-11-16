package com.game.smartremoteapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.LevelPowerAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.LevelBean;
import com.game.smartremoteapp.bean.LevelPowerBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.LevelPowerDialog;
import com.game.smartremoteapp.view.MyToast;
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
    TextView levelValue;
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
        getgetUserLevel(UserUtils.USER_ID);
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

    private void getgetUserLevel(String userId) {
        HttpManager.getInstance().getgetUserLevel(userId, new RequestSubscriber<Result<LevelBean>>() {
            @Override
            public void _onSuccess(Result<LevelBean> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    UserUtils.LEVEL=result.getData().getLevel();
                    levelValue.setText("LV"+ UserUtils.LEVEL);
                    progress_level_value.setText("LV"+result.getData().getLevel());
                    String s = "还需要" + "<font color='#FF8400'>" + ((int)result.getData().getResidualValue()) + "</font>"
                            + "经验值到达" +"<font color='#FF8400'>" +"LV"+(UserUtils.LEVEL+1) + "</font>" ;
                    level_desc.setText(Html.fromHtml(s));
                    int mPercent=(int) (result.getData().getPercentage()*100);
                    progress_level.setProgress(mPercent);
                    progress_level_percent.setText(mPercent+"%");
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void initData() {
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_post,"随心邮寄", 13));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_gift,"精美礼品",16));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_treasure_chest,"神秘宝箱",18));
        mLevelPowers.add(new LevelPowerBean(R.drawable.icon_level_recharge,"充值特惠",31));
        mAdapter = new LevelPowerAdapter(this, mLevelPowers);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter.setmOnItemClickListener(new LevelPowerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(boolean isHeader, int position) {
                switch (position){
                    case 0:
                        showLevelPowerDialog(R.drawable.icon_level_post_desc);
                        break;
                    case 1:
                        if(!isHeader&&UserUtils.LEVEL>=16){
                            getGift(16);
                        }else{
                            showLevelPowerDialog(R.drawable.icon_level_gift_desc);
                        }
                        break;
                    case 2:
                        if(!isHeader&&UserUtils.LEVEL>=18){
                            getGift(18);
                        }else{
                            showLevelPowerDialog(R.drawable.icon_level_treasure_chest_desc);
                        }
                        break;
                    case 3:
                        showLevelPowerDialog(R.drawable.icon_level_recharge_desc);
                        break;
                }

            }

        });
    }
  private void showLevelPowerDialog(int iocn){
      if(mDialog==null){
          mDialog = new LevelPowerDialog(LevelActivity.this, R.style.easy_dialog_style);
      }
      mDialog.show();
      mDialog.setLevelPowerBg(iocn);
  }

   private void  getGift(int index){
       if (Utils.isEmpty(UserUtils.UserAddress)) {
           startActivity(new Intent(this, NewAddressActivity.class));
           return;
       }

      String information = UserUtils.UserAddress.replace(" ", ",");

       if (index == 16) {
            if(!UserUtils.LEVEL_16_TAG.equals("0")){
                startActivity(new Intent(this, MyLogisticsOrderActivity.class));
            }else{
                getSendGoods(null, null, information, "16级精美礼品", UserUtils.USER_ID,"0", Utils.getProvinceNum(UserUtils.UserAddress),"16");
            }
         }else if(index==18){
           if(!UserUtils.LEVEL_18_TAG.equals("0")){
               startActivity(new Intent(this, MyLogisticsOrderActivity.class));
           }else{
               getSendGoods(null, null, information, "18级神秘宝箱", UserUtils.USER_ID,"0", Utils.getProvinceNum(UserUtils.UserAddress),"18");
           }
       }
   }


    private void getSendGoods(String dollID, String number, String consignee, String remark, String userID, String mode, String costNum, final String level) {
        //Log.e(TAG, "发货参数=" + costNum);
        HttpManager.getInstance().getSendGoods(dollID, number, consignee, remark, userID, mode,costNum,level, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    MyToast.getToast(getApplicationContext(), "发货成功，请耐心等待！").show();
                    if (level.equals("16")) {
                        UserUtils.LEVEL_16_TAG = "1";
                    } else if (level.equals("18")) {
                        UserUtils.LEVEL_18_TAG = "1";
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

}
