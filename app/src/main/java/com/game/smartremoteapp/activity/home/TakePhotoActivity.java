package com.game.smartremoteapp.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.AppUserBean;
import com.game.smartremoteapp.bean.Result;
import com.game.smartremoteapp.model.http.HttpManager;
import com.game.smartremoteapp.model.http.RequestSubscriber;
import com.game.smartremoteapp.utils.Base64;
import com.game.smartremoteapp.utils.BitmapUtils;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.utils.Utils;
import com.game.smartremoteapp.view.MyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by hongxiu on 2017/9/26.
 */
public class TakePhotoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "TakePhotoActivity-";
    public static final int GALLERY_REQUEST_CODE = 2;    // 相册选图标记
    public static final int CAMERA_REQUEST_CODE = 3;    // 相机拍照标记
    public static final int GALLERY_PICTURE_CUT =4 ;// 图片裁剪
    private TextView shootTv;
    private TextView photoTv;
    private TextView cancelTv;
    private File file;
    private String base64;
    private Uri outputUri;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_picture);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
         shootTv = (TextView) findViewById(R.id.shoot_tv);
        photoTv = (TextView) findViewById(R.id.photo_tv);
        cancelTv = (TextView) findViewById(R.id.cancel_tv);
        shootTv.setOnClickListener(this);
         photoTv.setOnClickListener(this);
         cancelTv.setOnClickListener(this);

         String files= this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Utils.deleteAll(files);//清空
       file = new File(files + "/"+System.currentTimeMillis() + ".jpg");
        if (file.isFile() && file.exists()) {
             file.delete(); //如果存在删除
       }
    }

//    @Override
//    protected int getLayoutId() {
//        return R.layout.dialog_picture;
//    }
//
//    @Override
//    protected void afterCreate(Bundle savedInstanceState) {
//
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//
//        shootTv = (TextView) findViewById(R.id.shoot_tv);
//        photoTv = (TextView) findViewById(R.id.photo_tv);
//        cancelTv = (TextView) findViewById(R.id.cancel_tv);
//        shootTv.setOnClickListener(this);
//        photoTv.setOnClickListener(this);
//        cancelTv.setOnClickListener(this);
//
//        String files= this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        Utils.deleteAll(files);//清空
//        file = new File(files + "/"+System.currentTimeMillis() + ".jpg");
//        if (file.isFile() && file.exists()) {
//            file.delete(); //如果存在删除
//        }
//    }
//
//    @Override
//    protected void initView() {
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shoot_tv:
                takePhone();
                break;
            case R.id.photo_tv:
                selectPicture();
                break;
            case R.id.cancel_tv:
                finish();
                break;
        }
    }


    private void getFaceImage(String userId,String faceImage){
        HttpManager.getInstance().getFaceImage(userId, faceImage, new RequestSubscriber<Result<AppUserBean>>() {
            @Override
            public void _onSuccess(Result<AppUserBean> result) {
                if(result.getCode()==0) {
                    UserUtils.UserImage= UrlUtils.USERFACEIMAGEURL+result.getData().getAppUser().getIMAGE_URL();
                    MyToast.getToast(getApplicationContext(),"修改成功！").show();
                     finish();
                }else{
                    MyToast.getToast(getApplicationContext(),"修改失败!").show();
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.loge("getFaceImage::::" + e.getMessage(),TAG);
            }
        });
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     *
     */
    private void takePhone() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent,  CAMERA_REQUEST_CODE);
    }
    /**
     *
     */
    private void selectPicture() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE); // 打开相册
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case   GALLERY_REQUEST_CODE: //选择图片
                    if(data.getData()!=null){
                        cropPhoto(data.getData());
                    }
                    break;
                case   CAMERA_REQUEST_CODE: //照相图片
                    //  updateusrimg(file.getPath());
                    cropPhoto(Uri.fromFile(file));
                    break;
                case  GALLERY_PICTURE_CUT://裁剪
                     updateusrimg(outputUri.getPath());
                    break;
            }
        }
    }

    private void updateusrimg(String path) {
        Log.i("com.jianguo.aoaocar",path);
        Bitmap bitmap= BitmapUtils.compressImageFromFile(path);
        base64= Base64.encode(BitmapUtils.compressBmpFromBmp(bitmap));
        getFaceImage(UserUtils.USER_ID,base64);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent,  GALLERY_PICTURE_CUT);
    }
}

