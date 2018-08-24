package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.protocol.JCUtils;
import com.game.smartremoteapp.utils.Utils;

/**
 * Created by yincong on 2018/8/23 17:35
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class EXGoldenBeanDialog extends Dialog implements View.OnClickListener{

    private Button sureBtn,cancleBtn;
    private TextView textView;

    public EXGoldenBeanDialog(Context context) {
        super(context);
    }

    public EXGoldenBeanDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EXGoldenBeanDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exgoldenean);
        findView();
        setListner();
    }

    public void findView() {
        textView= (TextView) findViewById(R.id.exgoldenbean_dialog_goldnum_tv);
        cancleBtn= (Button) findViewById(R.id.exgoldenbean_dialog_cancle_btn);
        sureBtn= (Button) findViewById(R.id.exgoldenbean_dialog_sure_btn);
    }

    public void setText(String num){
        textView.setText(num+ JCUtils.JDNAME);
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        sureBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exgoldenbean_dialog_cancle_btn:
                if (null != this.listener) {
                    listener.getResult(0);
                }
                this.dismiss();
                break;
            case R.id.exgoldenbean_dialog_sure_btn:
                if (null != this.listener) {
                    listener.getResult(1);
                }
                this.dismiss();
                break;
            default:
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
         * @param resultCode 0取消  1确定
         */
        void getResult(int resultCode);
    }

}
