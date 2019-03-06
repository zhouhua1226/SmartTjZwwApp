package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartremoteapp.R;

/**
 * Created by mi on 2019/1/18.
 */

public class NoticeDialog extends Dialog implements View.OnClickListener {

    private final static String TAG = "UpdateDialog";
    private Context mtx;
    private TextView tv_title, sure_btn,cancel_btn;//提示

    public NoticeDialog(Context context) {
        super(context);
    }

    public NoticeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public NoticeDialog(Context context, int theme) {
        super(context, theme);
        mtx = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_dialog);
        findView();
        setStyle();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setDialogTitle(String title) {
        tv_title.setText(Html.fromHtml("抢红包后" + "<font color='#FFAE00'>" + title + "</font>" + "会获取到您的微信和QQ号码,继续抢红包？"));
    }

    public void findView() {
        tv_title = (TextView) findViewById(R.id.notice_title);
        cancel_btn = (TextView) findViewById(R.id.cancel_btn);
        sure_btn = (TextView) findViewById(R.id.sure_btn);
        cancel_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
    }

    private void setStyle() {
        //设置对话框不可取消
        this.setCancelable(false);
        //设置触摸对话框外面不可取消
        this.setCanceledOnTouchOutside(false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //获得应用窗口大小
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        //设置对话框居中显示
        layoutParams.gravity = Gravity.CENTER;
        //设置对话框宽度为屏幕的4/5
        layoutParams.width = (displaymetrics.widthPixels / 5) * 4;
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                if(listener!=null){
                    listener.result();
                }   this.dismiss();
                break;
            case R.id.cancel_btn:
                this.dismiss();
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
         */
        void result( );
    }
}
