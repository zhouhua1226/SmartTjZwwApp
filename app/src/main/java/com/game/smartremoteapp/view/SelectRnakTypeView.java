package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartremoteapp.R;

/**
 * Created by mi on 2018/9/6.
 */

public class SelectRnakTypeView   implements View.OnClickListener {
    private TextView mDayBank, mWeekBank, mMouthBank;
    private Context mCtx;
    private int checkBrandIndex = -1;
    private OnDismissListListener onDismissListListener;
    private Dialog mAlertDialog;


    public SelectRnakTypeView(Context context,  OnDismissListListener _onDismissListListener) {
        this.mCtx = context;
        this.onDismissListListener=_onDismissListListener;
        mAlertDialog = new Dialog(context, R.style.dialog);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.view_selsect_rank_type);

        mDayBank = (TextView)mAlertDialog. findViewById(R.id.tv_day_bank);
        mWeekBank = (TextView)mAlertDialog. findViewById(R.id.tv_week_bank);
        mMouthBank = (TextView)mAlertDialog. findViewById(R.id.tv_mouth_bank);

        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setGravity(Gravity.TOP|Gravity.RIGHT);

        setListner();
    }


    private void setListner() {
        mDayBank.setOnClickListener(this);
        mWeekBank.setOnClickListener(this);
        mMouthBank.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_day_bank:
                checkBrandIndex=1;
                break;
            case R.id.tv_week_bank:
                checkBrandIndex=2;
                break;
            case R.id.tv_mouth_bank:
                checkBrandIndex=3;
                break;
        }
        if(onDismissListListener!=null&&checkBrandIndex>0){
            onDismissListListener.onnDismissList(checkBrandIndex);
        }
    }

    //接口日期选择接口
    public interface OnDismissListListener {
        void onnDismissList(int index);
    }


}