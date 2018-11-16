package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartremoteapp.R;

/**
 * Created by mi on 2018/11/13.
 */

public class SelsectGenerDialog  implements View.OnClickListener {
    private final Dialog mAlertDialog;
    private TextView male_tv,cancel_tv,female_tv;
    private OnSelsectGenerOnClicker onSelsectGener;

    public SelsectGenerDialog(Context context,boolean isImage, OnSelsectGenerOnClicker onSelsectGener) {

        mAlertDialog = new Dialog(context, R.style.dialog);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.dialog_select_gener);
        male_tv = (TextView) mAlertDialog.findViewById(R.id.male_tv);
        female_tv = (TextView)mAlertDialog. findViewById(R.id.female_tv);
        cancel_tv = (TextView)mAlertDialog. findViewById(R.id.cancel_tv);
        if(isImage){
            male_tv.setText("拍摄");
            female_tv.setText("从手机相册选择");
        }else{
            male_tv.setText("男");
            female_tv.setText("女");
        }
        cancel_tv.setOnClickListener(this);
        female_tv.setOnClickListener(this);
        male_tv.setOnClickListener(this);
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setGravity(Gravity.BOTTOM);
        this.onSelsectGener=onSelsectGener;
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.male_tv:
                    if (null != this.onSelsectGener) {
                        onSelsectGener.onSelsectGener(true,"男");
                    }
                mAlertDialog.dismiss();
                break;
            case R.id.female_tv:
                if (null != this.onSelsectGener) {
                    onSelsectGener.onSelsectGener(false,"女");
                }
                mAlertDialog.dismiss();
                break;
            case R.id.cancel_tv:
                mAlertDialog.dismiss();
                break;
        }
    }


    //分享监听接口
    public interface  OnSelsectGenerOnClicker{
        void onSelsectGener(boolean option,String gener);
    }

}
