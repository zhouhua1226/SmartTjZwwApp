package com.game.smartremoteapp.activity.home;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.AppInfo;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.model.http.download.DownLoadRunnable;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.LoadProgressView;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.UpdateDialog;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by hongxiu on 2017/9/25.
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.image_kf)
    ImageButton imageKf;
    @BindView(R.id.money_rl)
    RelativeLayout moneyRl;
    @BindView(R.id.record_rl)
    RelativeLayout recordRl;
    @BindView(R.id.invitation_rl)
    RelativeLayout invitationRl;
    @BindView(R.id.feedback_rl)
    RelativeLayout feedbackRl;
    @BindView(R.id.gywm_rl)
    RelativeLayout gywmRl;
    @BindView(R.id.bt_out)
    Button btOut;
    @BindView(R.id.vibrator_control_layout)
    RelativeLayout vibratorControlLayout;
    @BindView(R.id.vibrator_control_imag)
    ImageView vibratorControlImag;
    @BindView(R.id.setting_update_tv)
    TextView settingUpdateTv;
    @BindView(R.id.setting_update_layout)
    RelativeLayout settingUpdateLayout;
    @BindView(R.id.betrecord_rl)
    RelativeLayout betrecordRl;
    @BindView(R.id.setting_share_layout)
    RelativeLayout settingShareLayout;
    @BindView(R.id.roommusic_control_imag)
    ImageView roommusicControlImag;
    @BindView(R.id.roommusic_control_layout)
    RelativeLayout roommusicControlLayout;

    private String TAG = "SettingActivity";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Context context = SettingActivity.this;
    private LoadProgressView downloadDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        setIsVibrator();
        setIsOpenMusic();
        settingUpdateTv.setText("当前版本：" + Utils.getAppCodeOrName(this, 1));
        RxBus.get().register(this);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image_back, R.id.image_kf, R.id.money_rl,
            R.id.record_rl, R.id.invitation_rl, R.id.feedback_rl,
            R.id.gywm_rl, R.id.bt_out, R.id.vibrator_control_layout,
            R.id.vibrator_control_imag, R.id.setting_update_layout,
            R.id.betrecord_rl, R.id.setting_share_layout,R.id.roommusic_control_layout,
            R.id.roommusic_control_imag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_kf:
                startActivity(new Intent(this,ServiceActivity.class));
                break;
            case R.id.money_rl:
                //我的游戏币
                startActivity(new Intent(this, GameCurrencyActivity.class));
                break;
            case R.id.record_rl:
                //我的主娃娃记录
                startActivity(new Intent(this, RecordActivity.class));
                break;
            case R.id.betrecord_rl:
                //我的投注记录
                startActivity(new Intent(this, BetRecordActivity.class));
                break;
            case R.id.invitation_rl:
                //邀请码
                startActivity(new Intent(this, LnvitationCodeActivity.class));
                break;
            case R.id.feedback_rl:
                //问题反馈
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case R.id.gywm_rl:
                //关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.bt_out:
                getLogout(UserUtils.USER_ID);
                break;
            case R.id.vibrator_control_layout:
            case R.id.vibrator_control_imag:
                Utils.isVibrator = !Utils.isVibrator;
                editor.putBoolean("isVibrator", Utils.isVibrator);
                editor.commit();
                setBtnText(vibratorControlImag, Utils.isVibrator);
                break;
            case R.id.roommusic_control_layout:
            case R.id.roommusic_control_imag:
                boolean isMusic=(boolean)SPUtils.get(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
                if(isMusic){
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, false);
                }else {
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
                }
                setIsOpenMusic();
                break;
            case R.id.setting_update_layout:
               checkVersion();
                break;
            case R.id.setting_share_layout:
                Log.e(TAG,"分享参数userId="+UserUtils.USER_ID);
                MyToast.getToast(getApplicationContext(),"研发中！").show();
                //RobustApi.getInstance().shareWx(this, new ShareInfo(UserUtils.USER_ID));
                break;
        }
    }
    /**
     * 获取apk版本信息
     */
    private void checkVersion(){
        HttpManager.getInstance().checkVersion( new RequestSubscriber<Result<AppInfo>>() {
            @Override
            public void _onSuccess(Result<AppInfo> appInfoResult) {
                if(appInfoResult!=null){
                    String version= appInfoResult.getData().getVERSION();
                    if(!Utils.getAppCodeOrName(SettingActivity.this, 1).equals(version)){
                        updateApp(appInfoResult.getData().getDOWNLOAD_URL());
                    }else{
                       MyToast.getToast(SettingActivity.this,"当前已是最新版本").show();
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });

    }
    private void  updateApp(final String loadUri){
        UpdateDialog updateDialog=new UpdateDialog(this,R.style.easy_dialog_style);
        updateDialog.setCancelable(false);
        updateDialog.show();
        updateDialog.setDialogResultListener(new UpdateDialog.DialogResultListener() {
            @Override
            public void getResult(boolean result ) {
                if (result) {// 确定下载
                    showDialog();
                    new Thread(new DownLoadRunnable(SettingActivity.this, UrlUtils.APPPICTERURL+loadUri)).start();
                }
            }
        });
    }


    private void showDialog() {
        if(downloadDialog==null){
            downloadDialog = new LoadProgressView(this,R.style.easy_dialog_style);
        }
        if(!downloadDialog.isShowing()){
            downloadDialog.show();
        }
    }
    private void canceledDialog() {
        if(downloadDialog!=null&&downloadDialog.isShowing()){
            downloadDialog.dismiss();
        }
    }
    private void setIsVibrator() {
        settings = getSharedPreferences("app_user", 0);
        editor = settings.edit();
        if (settings.contains("isVibrator")) {
            Utils.isVibrator = settings.getBoolean("isVibrator", true);
        }

        if (!Utils.isVibrator)
            vibratorControlImag.setSelected(false);
        else
            vibratorControlImag.setSelected(true);
    }

    private void setBtnText(ImageView btn, boolean isOpen) {
        if (isOpen)
            btn.setSelected(true);
        else
            btn.setSelected(false);
    }

    private void setIsOpenMusic(){
        boolean isOpen=(boolean)SPUtils.get(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
        if(isOpen){
            roommusicControlImag.setSelected(true);
        }else {
            roommusicControlImag.setSelected(false);
        }
    }

    private void getLogout(String userId) {
        HttpManager.getInstance().getLogout(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG, "退出登录结果=" + loginInfoResult.getMsg());
                if (loginInfoResult.getMsg().equals("success")) {
//                    Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show();
//                    SPUtils.remove(context, UserUtils.SP_TAG_LOGIN);
//                    UserUtils.UserPhone = "";
                   // Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show();
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_ISLOGOUT, true);
                    SPUtils.put(getApplicationContext(), YsdkUtils.AUTH_TOKEN, "");
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
                    SPUtils.put(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false);
                    NettyUtils.destoryConnect();
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    finish();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
   /**
    * 下载ui更新通知
    */
   @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(Utils.TAG_DOWN_LOAD)})
   public void getDownLoadInfo(Object object) {
       if(object instanceof DownLoadRunnable.UpdateInfo){
           DownLoadRunnable.UpdateInfo info= (DownLoadRunnable.UpdateInfo) object;
           switch (info.getState()){
               case DownloadManager.STATUS_SUCCESSFUL:
                   downloadDialog.setProbarPercent(100);
                   canceledDialog();
                  // Toast.makeText(SettingActivity.this, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                   break;

               case DownloadManager.STATUS_RUNNING:
                   //int progress = (int) msg.obj;
                   downloadDialog.setProbarPercent((int) info.getProgress());
                   //canceledDialog();
                   break;
               case DownloadManager.STATUS_FAILED:
                   canceledDialog();
                   break;
               case DownloadManager.STATUS_PENDING:
                   showDialog();
                   break;
           }
       }
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}

