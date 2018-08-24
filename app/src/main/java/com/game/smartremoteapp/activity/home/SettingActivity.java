package com.game.smartremoteapp.activity.home;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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
import com.game.smartremoteapp.model.http.download.DownloadManagerUtil;
import com.game.smartremoteapp.utils.PermissionsUtils;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.utils.VersionUtils;
import com.game.smartremoteapp.utils.YsdkUtils;
import com.game.smartremoteapp.view.LoadProgressView;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.ShareDialog;
import com.game.smartremoteapp.view.UpdateDialog;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.game.smartremoteapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;

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

    private Context context = SettingActivity.this;
    private LoadProgressView downloadDialog;
    private DownloadManagerUtil downloadManagerUtil;
    long downloadId = 0;
    private String mVersion;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        setIsVibrator();
        setIsOpenMusic();
        mVersion=Utils.getAppCodeOrName(this, 1);
        settingUpdateTv.setText("当前版本：" +mVersion);
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
                SPUtils.putBoolean(getApplicationContext(),"isVibrator", Utils.isVibrator);
                setBtnText(vibratorControlImag, Utils.isVibrator);
                break;
            case R.id.roommusic_control_layout:
            case R.id.roommusic_control_imag:
                boolean isMusic= SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
                if(isMusic){
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, false);
                }else {
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
                }
                setIsOpenMusic();
                break;
            case R.id.setting_update_layout:

               checkVersion();  //汤姆抓娃娃
              //  MyToast.getToast(SettingActivity.this, "当前已是最新版本").show();//蘑菇抓娃娃
                break;
            case R.id.setting_share_layout:
                PermissionsUtils.checkPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                            @Override
                            public void onSuccessful() {
                                shareApp();
                            }
                            @Override
                            public void onFailure() {
                            }
                        });
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
                if(appInfoResult!=null) {
                    String version = appInfoResult.getData().getVERSION();
                    if (VersionUtils.validateVersion(mVersion, version)) {
                        updateApp(appInfoResult.getData());
                    } else {
                        MyToast.getToast(SettingActivity.this, "当前已是最新版本").show();
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });

    }
    private void  updateApp(final AppInfo mAppInfo){
        UpdateDialog updateDialog=new UpdateDialog(this,R.style.easy_dialog_style);
        updateDialog.setCancelable(false);
        updateDialog.show();
        updateDialog.setDialogTitle(mAppInfo.getVERSION());
        updateDialog.setDialogContext(mAppInfo.getCONTENT());
        updateDialog.setDialogResultListener(new UpdateDialog.DialogResultListener() {
         @Override
            public void getResult(boolean result ) {
               if (result) {// 确定下载
                    downloadManagerUtil=new DownloadManagerUtil(getApplicationContext());
                     if (downloadId != 0) {
                       downloadManagerUtil.clearCurrentTask(downloadId);
                     }
                   if(downloadManagerUtil.isDownloadManagerAvailable( )){
                       downloadId = downloadManagerUtil.download(UrlUtils.APPPICTERURL+mAppInfo.getDOWNLOAD_URL());
                   }
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

            Utils.isVibrator = SPUtils.getBoolean(getApplicationContext(),"isVibrator", true);
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
        boolean isOpen= SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_ISOPENMUSIC, true);
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
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_ISLOGOUT, true);
                    SPUtils.putString(getApplicationContext(), YsdkUtils.AUTH_TOKEN, "");
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false);
                    NettyUtils.destoryConnect();
                    MainActivity.mMainActivity.finish();//汤姆抓娃娃
                    startActivity(new Intent(SettingActivity.this, SplashActivity.class));
                   // HomeActivity.mMainActivity.finish();////蘑菇抓娃娃
                   // startActivity(new Intent(SettingActivity.this, Splash1Activity.class));
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
           if (info != null&&downloadDialog!=null) {
               switch (info.getState()) {
                   case DownloadManager.STATUS_SUCCESSFUL:
                       downloadDialog.setProbarPercent(100);
                       canceledDialog();
                       //Toast.makeText(MainActivity.this, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                       break;
                   case DownloadManager.STATUS_RUNNING:
                       downloadDialog.setProbarPercent(info.getProgress());
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
   }
    //分享
    private void shareApp() {
        new ShareDialog(this, new ShareDialog.OnShareSuccessOnClicker() {
            @Override
            public void onShareSuccess() {

            }
        });
    }

}

