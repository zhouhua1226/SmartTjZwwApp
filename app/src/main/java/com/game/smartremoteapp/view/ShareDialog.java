package com.game.smartremoteapp.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.adapter.ShareAdapter;
import com.game.smartremoteapp.base.BaseActivity;
import com.game.smartremoteapp.bean.PlateBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22077 on 2017/6/15.
 */

public class ShareDialog {
    private final TextView cancel;
    private final RecyclerView mRecyclerView;
    private Dialog mAlertDialog;
    private View mView;
    private Context mContext;
    private List<PlateBean> mPlate=new ArrayList<>();
    private UMWeb web;
    private SHARE_MEDIA shareMedia;
    private OnShareSuccessOnClicker onShareSuccessOnClicker;
    private boolean isImage=false;
    private UMImage image;
    public ShareDialog(Context context, OnShareSuccessOnClicker onShareSuccessOnClicker) {
        this.mContext = context;
        mAlertDialog = new Dialog(context, R.style.dialog);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.widget_share_dialog);

        cancel = (TextView) mAlertDialog.findViewById(R.id.tv_option_cancel);
        mRecyclerView = (RecyclerView) mAlertDialog.findViewById(R.id.share_recyclerview);
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setGravity(Gravity.BOTTOM);
        this.onShareSuccessOnClicker=onShareSuccessOnClicker;
        initMedia();
        setInitView();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }
    private void setInitView() {
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_qq ,null,"QQ"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_qzone ,null,"QQ空间"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_wechat ,null,"微信"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_wxcircle,null,"朋友圈"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(8));
        mRecyclerView.setAdapter(new ShareAdapter(mContext, mPlate, new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mAlertDialog.dismiss();
                switch (position) {
                    //QQ好友
                    case 0:
                        if(!Utils.isQQClientAvailable(mContext)){
                            MyToast.getToast(mContext,"你还未安装QQ客户端!").show();
                            return;
                        }
                        shareMedia = SHARE_MEDIA.QQ;
                        break;
                    case 1://QQ朋友圈
                        if(!Utils.isQQClientAvailable(mContext)){
                            MyToast.getToast(mContext,"你还未安装QQ客户端!").show();
                            return;
                        }
                        shareMedia = SHARE_MEDIA.QZONE;
                        break;
                    case 2://微信好友
                        if(!Utils.isWeixinAvilible(mContext)){
                            MyToast.getToast(mContext,"你还未安装QQ客户端!").show();
                            return;
                        }
                        shareMedia = SHARE_MEDIA.WEIXIN;
                        break;
                    case 3://微信朋友圈
                        if(!Utils.isWeixinAvilible(mContext)){
                            MyToast.getToast(mContext,"你还未安装QQ客户端!").show();
                            return;
                        }
                        shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                        break;
                }
                shareAction();
            }  }));
    }

    private void shareAction() {
        if(isImage&&image!=null){
            new ShareAction((Activity) mContext)
                    .withMedia(image)
                    .setPlatform(shareMedia)
                    .setCallback(shareListener)
                    .share();
        }else {
            new ShareAction((BaseActivity) mContext)
                    .withMedia(web)
                    .setPlatform(shareMedia)
                    .setCallback(shareListener).
                     share();
        }
    }
   public  void setImage(File file){
         this.isImage=true;
         image = new UMImage(mContext, file);//本地文件
         image.setTitle(mContext.getString(R.string.app_name));
         image.setDescription(mContext.getString(R.string.app_description));
         image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
   }

    private void initMedia(){
        web = new UMWeb(mContext.getString(R.string.load_web));
        web.setTitle(mContext.getString(R.string.app_name));
        web.setDescription(mContext.getString(R.string.app_description));
        web.setThumb(new UMImage(mContext,BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_mm_icon)));
    }

    //分享监听接口
    public interface  OnShareSuccessOnClicker{
          void onShareSuccess();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            shareGame();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,"分享失败"+t.getMessage(),Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext,"分享取消",Toast.LENGTH_SHORT).show();
        }
    };

    private void shareGame(){
        HttpManager.getInstance().shareGame(UserUtils.USER_ID ,new RequestSubscriber<Result<Void>>() {
            @Override
            public void _onSuccess(Result<Void> loginInfoResult) {
                if(loginInfoResult.getCode()==0){
                    Toast.makeText(mContext,"分享成功",Toast.LENGTH_SHORT).show();
                     if(onShareSuccessOnClicker!=null){
                         onShareSuccessOnClicker.onShareSuccess();}
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

}
