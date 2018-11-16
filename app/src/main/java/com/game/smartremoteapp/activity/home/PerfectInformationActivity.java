package com.game.smartremoteapp.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.HttpDataInfo;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.SelsectGenerDialog;
import com.jianguo.timedialog.TimePickerDialog;
import com.jianguo.timedialog.data.Type;
import com.jianguo.timedialog.listener.OnDateSetListener;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/11/15.
 */

public class PerfectInformationActivity extends BaseActivity{
    @BindView(R.id.iv_add_deader)
    ImageView user_image;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.tv_user_gener)
    TextView user_gener;
    @BindView(R.id.tv_user_age)
    TextView user_age;

    private int age=0;
    private String gender;
    private String nickname;
    private TimePickerDialog mDialogAll;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_information;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
        getAppUserInf(UserUtils.USER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this)
                .load(UserUtils.UserImage)
                .error(R.mipmap.app_mm_icon)
                .dontAnimate()
                .transform(new GlideCircleTransform(this))
                .into(user_image);
    }

    @Override
    protected void initView() {
        initTimeDialog();
    }

    @OnClick({R.id.image_back, R.id.iv_add_deader,
            R.id.rl_user_gener, R.id.rl_user_age,
            R.id.btn_sure_infor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.iv_add_deader:
                Utils.toActivity(this,TakePhotoActivity.class);
                break;
            case R.id.rl_user_gener:
                setGener();
                break;
            case R.id.rl_user_age:
                if(!mDialogAll.isAdded()){
                    mDialogAll.show(getSupportFragmentManager(),"year_month_day");
                }
                break;
            case R.id.btn_sure_infor:
                nickname=et_nickname.getText().toString();
                gender=user_gener.getText().toString();
                if (nickname.isEmpty()) {
                    MyToast.getToast(this, "请输入手昵称").show();
                    return;
                }
                if (gender.isEmpty()) {
                    MyToast.getToast(this, "请确认你的性别").show();
                    return;
                }
                if (age==0) {
                    MyToast.getToast(this, "请确认你的年龄").show();
                    return;
                }
                setPerfectInformation(UserUtils.USER_ID);
                break;

        }
    }

    private  void setGener(){
        new SelsectGenerDialog(this,false, new SelsectGenerDialog.OnSelsectGenerOnClicker() {
            @Override
            public void onSelsectGener(boolean option,String gener) {
                if (gener!=null){
                    user_gener.setText(gener);
                }
            }
        });
    }

    private void initTimeDialog() {
        long fiveYears = 50L * 365 * 1000 * 60 * 60 * 24L;
        long tweenYears = 20L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder( )
                .setCyclic(false)
                .setTitleStringId("出生日期")
                .setMinMillseconds(System.currentTimeMillis()-fiveYears)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis()-tweenYears)
                .setThemeColor(getResources().getColor(R.color.apptheme_bg))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.apptheme_bg))
                .setWheelItemTextSize(14)
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        try {
                            age = Utils.getAge(new Date(millseconds));
                            user_age.setText(age+"");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();
    }
  private  void  setPerfectInformation(String userId){
      HttpManager.getInstance().perfectInformation(nickname, gender, age, userId, new RequestSubscriber<Result<HttpDataInfo>>() {
          @Override
          public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
              if(loginInfoResult.getMsg().equals("success")) {
                  MyToast.getToast(PerfectInformationActivity.this,"信息已保存").show();
                  UserUtils.AGE= loginInfoResult.getData().getAppUser().getAGE();
                  UserUtils.GENDER=loginInfoResult.getData().getAppUser().getGENDER();
                  UserUtils.NickName = loginInfoResult.getData().getAppUser().getNICKNAME();
                  UserUtils.UserImage = UrlUtils.APPPICTERURL + loginInfoResult.getData().getAppUser().getIMAGE_URL();
                  finish();
              }
          }
          @Override
          public void _onError(Throwable e) {
          }
      });
  }

    private void getAppUserInf(String userId) {
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    if (result.getData().getAppUser() == null) {
                        return;
                    }
                    UserUtils.AGE= result.getData().getAppUser().getAGE();
                    UserUtils.GENDER=result.getData().getAppUser().getGENDER();
                    UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                    UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                    getUserImageAndName();
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void getUserImageAndName() {
        if (!UserUtils.NickName.equals("")) {
            et_nickname.setText(UserUtils.NickName);
        }
        user_gener.setText(UserUtils.GENDER);
    }
}