package com.game.smartremoteapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.view.reshrecyclerview.BaseMoreFooter;


/**
 * Created by cx on 2017/11/16
 */

public class EmptyLayout extends RelativeLayout implements View.OnClickListener,BaseMoreFooter {

    private View mView;
    private ImageView iv_empty;//空页
    private ImageView iv_loading;//加载页面
    private ImageView iv_error;//错误页面
    private int mState = 0;
    private final int  STATE_DISMISS = 0,//不需要页面
                        STATE_EMPTY = 1,//空页
                        STATE_LOADING = 2,//加载页面
                        STATE_ERROR = 3;//错误页面
    private OnClickReTryListener onClickReTryListener;

    public EmptyLayout(Context context) {
        super(context);
        init(context);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_emptyview, null);
        iv_empty = (ImageView) mView.findViewById(R.id.iv_empty);
        iv_loading = (ImageView) mView.findViewById(R.id.iv_loading);
        iv_error = (ImageView) mView.findViewById(R.id.iv_error);
        setListener();
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mView.setLayoutParams(layoutParams);
        addView(mView);
    }

    private void setListener() {
        iv_empty.setOnClickListener(this);
//        iv_loading.setOnClickListener(this);
        iv_error.setOnClickListener(this);
    }

    private void refreshView() {
        switch (mState) {
            case STATE_DISMISS:
                cleanState();
                break;
            case STATE_EMPTY:
                cleanState();
                if (iv_empty.getVisibility() != VISIBLE) {
                    iv_empty.setVisibility(VISIBLE);
                }
                break;
            case STATE_LOADING:
                cleanState();
                if (iv_loading.getVisibility() != VISIBLE) {
                    iv_loading.setVisibility(VISIBLE);
                }
                break;
            case STATE_ERROR:
                cleanState();
                if (iv_error.getVisibility() != VISIBLE) {
                    iv_error.setVisibility(VISIBLE);
                }
                break;
            default:
                break;

        }
    }

    private void cleanState() {
        if (iv_empty.getVisibility() != GONE) {
            iv_empty.setVisibility(GONE);
        }
        if (iv_loading.getVisibility() != GONE) {
            iv_loading.setVisibility(GONE);
        }
        if (iv_error.getVisibility() != GONE) {
            iv_error.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (onClickReTryListener != null) {
            onClickReTryListener.onClickReTry(v);
        }
    }
    /**
     * 加载中
     */
    @Override
    public void loading() {

    }
    /**
     * 加载完成
     */
    @Override
    public void complete() {

    }
    /**
     * 没有更多数据了
     */
    @Override
    public void noMore() {
        mView.setVisibility(GONE);
    }

    @Override
    public void clickLoadMore() {

    }

    @Override
    public boolean isClickLoadMore() {
        return false;
    }
    /**
     * 当前是否是加载中
     * @return true 是 false 不是
     */
    @Override
    public boolean isLoading() {
        return false;
    }

    /**
     * 这个在子类掉用view 的 {@link android.view.View#setVisibility(int visibility) setVisibility}
     */
    @Override
    public void setViewVisibility(int visibility) {

    }

    public static interface OnClickReTryListener {
        void onClickReTry(View view);
    }

    public void setOnClickReTryListener(OnClickReTryListener onClickReTryListener) {
        this.onClickReTryListener = onClickReTryListener;
    }

    public void dismiss() {
        mState = STATE_DISMISS;
        refreshView();
    }

    public void showEmpty() {
        mState = STATE_EMPTY;
        refreshView();
    }

    public void showLoading() {
        mState = STATE_LOADING;
        refreshView();
    }

    public void showError() {
        mState = STATE_ERROR;
        refreshView();
    }
}
