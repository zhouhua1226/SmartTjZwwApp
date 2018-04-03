package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.fragment.BetRecordFragment;
import com.game.smartremoteapp.fragment.MyCenterFragment;
import com.game.smartremoteapp.fragment.PlayRecordFragment;
import com.game.smartremoteapp.fragment.RankFragmentTwo;
import com.game.smartremoteapp.fragment.ZWWJFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/4/3 13:28
 * 修改人：
 * 修改时间：
 * 类描述：房间游戏记录
 */
public class RoomPlayRecordActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.ranktwo_catchtitle_tv)
    TextView ranktwoCatchtitleTv;
    @BindView(R.id.ranktwo_guesstitle_tv)
    TextView ranktwoGuesstitleTv;

    private String TAG="RoomPlayRecord---";
    private BetRecordFragment betRecordFragment;//竞猜记录
    private PlayRecordFragment playRecordFragment;//游戏记录
    private Fragment fragmentAll;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_roomplayrecord;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        showBetFg();  //默认展示竞猜
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        fragmentAll = getSupportFragmentManager().findFragmentById(
                R.id.main_center);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image_back, R.id.ranktwo_catchtitle_tv, R.id.ranktwo_guesstitle_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.ranktwo_catchtitle_tv:
                showPlayFg();
                String roomId=getIntent().getStringExtra("roomId");
                playRecordFragment.setRoomId(roomId);
                break;
            case R.id.ranktwo_guesstitle_tv:
                showBetFg();
                break;
            default:
                break;
        }
    }

    private void setShowChangeView(int i){
        if(i==2){
            ranktwoGuesstitleTv.setBackgroundResource(R.drawable.white_circleborder);
            ranktwoGuesstitleTv.setTextColor(getResources().getColor(R.color.apptheme_bg));
            ranktwoCatchtitleTv.setBackgroundResource(R.color.apptheme_bg);
            ranktwoCatchtitleTv.setTextColor(getResources().getColor(R.color.white));

        }else {
            ranktwoCatchtitleTv.setBackgroundResource(R.drawable.white_circleborder);
            ranktwoCatchtitleTv.setTextColor(getResources().getColor(R.color.apptheme_bg));
            ranktwoGuesstitleTv.setBackgroundResource(R.color.apptheme_bg);
            ranktwoGuesstitleTv.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void showBetFg() {
        if (!(fragmentAll instanceof BetRecordFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            //如果所有的fragment都不为空的话，把所有的fragment都进行隐藏。最开始进入应用程序，fragment为空时，此方法不执行
            hideFragment(fragmentTransaction);
            //如果这个fragment为空的话，就创建一个fragment，并且把它加到ft中去.如果不为空，就把它直接给显示出来
            if(betRecordFragment == null){
                betRecordFragment = new BetRecordFragment();
                fragmentTransaction.add(R.id.main_center, betRecordFragment);
            }else {
                fragmentTransaction.show(betRecordFragment);
            }
            setShowChangeView(2);
            fragmentTransaction.commitAllowingStateLoss();
        }




    }

    private void showPlayFg() {
        if (!(fragmentAll instanceof PlayRecordFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            hideFragment(fragmentTransaction);
            if(playRecordFragment==null) {
                playRecordFragment = new PlayRecordFragment();
                fragmentTransaction.add(R.id.main_center, playRecordFragment);
            }else {
                fragmentTransaction.show(playRecordFragment);
            }
            setShowChangeView(1);
            //一定要记得提交
            fragmentTransaction.commitAllowingStateLoss();
        }

    }


    //隐藏fragment
    public void hideFragment(FragmentTransaction fragmentTransaction){
        if(betRecordFragment != null){
            fragmentTransaction.hide(betRecordFragment);
        }
        if(playRecordFragment != null){
            fragmentTransaction.hide(playRecordFragment);
        }
    }


}
