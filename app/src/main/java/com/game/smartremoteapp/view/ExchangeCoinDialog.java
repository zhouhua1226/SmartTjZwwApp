package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.utils.Utils;

/**
 * Created by mi on 2018/8/27.
 */

public class ExchangeCoinDialog extends Dialog implements View.OnClickListener{

    private Button sureBtn,cancleBtn;
    private TextView goldnumView,exchange_rate;
    private EditText beannumView;
    private int mGold=0;
    private  Context mtx;
    private int mBean=0;
    public ExchangeCoinDialog(Context context) {
        super(context);
        mtx=context;
    }

    public ExchangeCoinDialog(Context context, int themeResId) {
        super(context, themeResId);
        mtx=context;
    }

    protected ExchangeCoinDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mtx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exbeangogold);
        findView();
        setListner();
    }

    public void findView() {
        beannumView= (EditText) findViewById(R.id.exgoldenbean_dialog_beannum_tv);
        goldnumView= (TextView) findViewById(R.id.exgoldenbean_dialog_goldnum_tv);
        exchange_rate= (TextView) findViewById(R.id.tv_exchange_rate);
        cancleBtn= (Button) findViewById(R.id.exgoldenbean_dialog_cancle_btn);
        sureBtn= (Button) findViewById(R.id.exgoldenbean_dialog_sure_btn);
        exchange_rate.setText("*兑换比列100金豆==1金币");
    }

    public void setText(String num){
        if(Utils.isNumeric(num)) {
            mBean=(Integer.parseInt(num)/100*100);
            beannumView.setText(num);
            beannumView.setSelection(beannumView.getText().length());
            mGold=mBean/100;
            goldnumView.setText(mGold+"金币");
        }
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        sureBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        beannumView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                 String beans=editable.toString();
                if(Utils.isNumeric(beans)) {
                    if (Integer.parseInt(beans)>=100&&Integer.parseInt(beans)<=mBean) {
                        mGold = Integer.parseInt(beans) / 100;
                        goldnumView.setText(mGold + "金币");
                    }else if(Integer.parseInt(beans)>mBean){
                        mGold=mBean/100;
                        goldnumView.setText(mGold+"金币");
                        beannumView.setText(mBean+"");
                    } else {
                        mGold = 0;
                        goldnumView.setText("");
                    }
                    beannumView.setSelection(beannumView.getText().length());
                }
            }
        });
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exgoldenbean_dialog_cancle_btn:
                if (null != this.listener) {
                    listener.getResult(false,"0");
                }
                this.dismiss();
                break;
            case R.id.exgoldenbean_dialog_sure_btn:
                String mBeans=beannumView.getText().toString();
                if(mGold>0&&!mBeans.isEmpty()){
                    if (null != this.listener) {
                        listener.getResult(true,mBeans);
                    }
                    this.dismiss();
                }else{
                    MyToast.getToast(mtx,"兑换金币最少为1金币！").show();
                }
                break;
            default:
                break;

        }
    }

    private   DialogResultListener listener;

    public void setDialogResultListener( DialogResultListener listener) {
        this.listener = listener;
    }

    public interface DialogResultListener {
        /**
         * 获取结果的方法
         *
         * @param resultCode 0取消  1确定
         */
        void getResult(boolean resultCode,String beans);
    }

}

