package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.utils.SPUtils;
import com.game.smartremoteapp.utils.UserUtils;


/**
 * Created by yincong on 2017/12/13 16:58
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class CatchDollResultDialog extends Dialog implements View.OnClickListener{

    private final static String TAG = "CatchDollResultDialog";
    private Context mContext;
    private TextView fail_tv,success_tv,title,content,txdesc;
    private RelativeLayout bg_layout;
    private ImageView imageView;
    private AnimationDrawable animation;

    public CatchDollResultDialog(Context context) {
        super(context);
    }

    public CatchDollResultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public CatchDollResultDialog(Context context, int theme) {
        super(context, theme);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_catchdoll_result);
        findView();
        setListner();

    }


    public void findView() {
        fail_tv= (TextView) findViewById(R.id.catchdialog_fail_tv);
        success_tv= (TextView) findViewById(R.id.catchdialog_success_tv);
        title= (TextView) findViewById(R.id.catchdialog_title1_tv);
        txdesc= (TextView) findViewById(R.id.catchdialog_desc_tv);
        content= (TextView) findViewById(R.id.catchdialog_content_tv);
        bg_layout= (RelativeLayout) findViewById(R.id.catchdialog_layout);
        imageView= (ImageView) findViewById(R.id.catchdialog_anim_imag);
    }

    public void setTitle(String titles){
        title.setText(titles);
    }

    public void setContent(String contents){
        content.setText(Html.fromHtml(contents));
    }
    public void setDesc(String desc){
        if(desc!=null){
            txdesc.setVisibility(View.VISIBLE);
            txdesc.setText(desc);
        }
    }
    public void setRechargeContent(){
        String isFirst=  SPUtils.getString(mContext, UserUtils.SP_FIRET_CHARGE,"0");
        if(isFirst.equals("0")){
            String s = "首次充值任意金额可获得" + "<font color='#FF0000'>" + "双倍" + "</font>"
                    + "金币";
            content.setText(Html.fromHtml(s));
        }else{
            content.setText("请充值");
        }
    }

    public void setFail(String titles){
        fail_tv.setText(titles);
    }

    public void setSuccess(String contents){
        success_tv.setText(contents);
    }

    public void setBackground(int picter){
        bg_layout.setBackgroundResource(picter);
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        fail_tv.setOnClickListener(this);
        success_tv.setOnClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.catchdialog_fail_tv:
                if (null != this.listener) {
                    listener.getResult(0);
                }
                if(animation!=null&&animation.isRunning()){
                    animation.stop();
                }
                CatchDollResultDialog.this.dismiss();
                break;
            case R.id.catchdialog_success_tv:
                if (null != this.listener) {
                    listener.getResult(1);
                }
                if(animation!=null&&animation.isRunning()){
                    animation.stop();
                }
                CatchDollResultDialog.this.dismiss();
                break;
        }
    }

    private DialogResultListener listener;

    public void setDialogResultListener(DialogResultListener listener) {
        this.listener = listener;
    }

    public interface DialogResultListener {
        /**
         * 获取结果的方法
         *
         * @param resultCode 0.取消 1.再试一次
         */
        void getResult(int resultCode);
    }

    public void setStartAnimation() {
        animation = new AnimationDrawable();
        animation.addFrame(mContext.getResources().getDrawable(R.drawable.catchdialogresult_three_bg), 1000);
        animation.addFrame(mContext.getResources().getDrawable(R.drawable.catchdialogresult_two_bg), 1000);
        animation.addFrame(mContext.getResources().getDrawable(R.drawable.catchdialogresult_one_bg), 1000);
        animation.setOneShot(false);
        imageView.setBackgroundDrawable(animation);
        // start the animation!
        animation.start();
    }

}

