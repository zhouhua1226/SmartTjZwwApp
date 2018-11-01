package com.game.smartremoteapp.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.PictureListBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.GlideCircleTransform;
import com.game.smartremoteapp.view.MyToast;
import com.game.smartremoteapp.view.ShareDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by mi on 2018/10/25.
 */

public class DollWallActivity extends BaseActivity {
    private static final String TAG = "DollWallActivity-------------";
    @BindView(R.id.image_back)
    ImageView image_back;
    @BindView(R.id.tv_doll_share)
    TextView doll_share;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.iv_wawa1)
    ImageView iv1;
    @BindView(R.id.iv_wawa2)
    ImageView iv2;
    @BindView(R.id.iv_wawa3)
    ImageView iv3;
    @BindView(R.id.iv_wawa4)
    ImageView iv4;
    @BindView(R.id.iv_wawa5)
    ImageView iv5;
    @BindView(R.id.iv_wawa6)
    ImageView iv6;
    @BindView(R.id.iv_wawa7)
    ImageView iv7;
    @BindView(R.id.iv_wawa8)
    ImageView iv8;
    @BindView(R.id.iv_wawa9)
    ImageView iv9;
    @BindView(R.id.iv_wawa10)
    ImageView iv10;
    @BindView(R.id.iv_wawa11)
    ImageView iv11;
    @BindView(R.id.iv_wawa12)
    ImageView iv12;
    @BindView(R.id.iv_wawa13)
    ImageView iv13;
    @BindView(R.id.iv_wawa14)
    ImageView iv14;
    @BindView(R.id.iv_wawa15)
    ImageView iv15;
    @BindView(R.id.iv_wawa16)
    ImageView iv16;
    private List<ImageView> dollImageList;
    private File file;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_doll_wall;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
    }
  @OnClick({R.id.image_back,R.id.tv_doll_share})
  public  void onClickView(View v){
      switch (v.getId()){
          case R.id.image_back:
              finish();
              break;
          case R.id.tv_doll_share:
              initFile();
              setShareFuil(true);
              screenshot();
              break;
      }
  }

    @Override
    protected void onResume() {
        super.onResume();
        setShareFuil(false);
    }

    @Override
    protected void initView() {
        if (!UserUtils.NickName.equals("")) {
            userName.setText(UserUtils.NickName);
        } else {
            userName.setText("暂无昵称");
        }
        Glide.with(this)
                .load(UserUtils.UserImage)
                .error(R.mipmap.app_mm_icon)
                .dontAnimate()
                .transform(new GlideCircleTransform(this))
                .into(userImage);
        dollImageView();
        getUserDollPicture(UserUtils.USER_ID);
    }
    private void dollImageView() {
        dollImageList = new ArrayList<>();
        dollImageList.add(iv1);
        dollImageList.add(iv2);
        dollImageList.add(iv3);
        dollImageList.add(iv4);
        dollImageList.add(iv5);
        dollImageList.add(iv6);
        dollImageList.add(iv7);
        dollImageList.add(iv8);
        dollImageList.add(iv9);
        dollImageList.add(iv10);
        dollImageList.add(iv11);
        dollImageList.add(iv12);
        dollImageList.add(iv13);
        dollImageList.add(iv14);
        dollImageList.add(iv15);
        dollImageList.add(iv16);
    }
    private void getUserDollPicture(String userId) {
        HttpManager.getInstance().getUserDollPicture(userId, new RequestSubscriber<Result<PictureListBean>>() {
            @Override
            public void _onSuccess(Result<PictureListBean> result) {
               if(result.getMsg().equals(Utils.HTTP_OK)){
                   if(result.getData().getPictureList()!=null){
                       initdata(result.getData().getPictureList());
                   }
               }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void initdata(List<PictureListBean.PictureBean> pictureList) {
            for (int i=0;i<16;i++){
                if(i<pictureList.size()){
                    Glide.with(this).load(UrlUtils.APPPICTERURL + pictureList.get(i).getDOLL_URL())
                            .error(R.drawable.loading)
                            .into(dollImageList.get(i));
                }else{
                    dollImageList.get(i).setBackgroundResource(R.drawable.logo_share);
                }
        }
    }

    private void screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null&&file!=null)  {
            try {
                // 获取内置SD卡路径
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                if(file!=null){
                    shareApp(file);
                }
            } catch (Exception e) {
            }
        }else{
            MyToast.getToast(this,"分享失败").show();
        }
         setShareFuil(false);
    }
    private void setShareFuil(boolean isShare){
        if(image_back!=null) {
            if(isShare){
                image_back.setVisibility(View.GONE);
                doll_share.setVisibility(View.GONE);
            }else{
                image_back.setVisibility(View.VISIBLE);
                doll_share.setVisibility(View.VISIBLE);
            }
        }
    }
    private void initFile() {
        String files= null;
        try {
            files = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } catch (NullPointerException e) {
            files= Environment.getExternalStorageDirectory()+"/temp/";
        }
        Utils.deleteAll(files);//清空
        file = new File(files + "/"+System.currentTimeMillis() + ".png");
      if (file.isFile() && file.exists()) {
        file.delete(); //如果存在删除
     }
   }

    //分享
    private void shareApp(File file) {
        ShareDialog mShareDialog=  new ShareDialog(this, new ShareDialog.OnShareSuccessOnClicker() {
            @Override
            public void onShareSuccess() {
                setShareFuil(false);
            }
        });
        mShareDialog.setImage(file);
    }
}