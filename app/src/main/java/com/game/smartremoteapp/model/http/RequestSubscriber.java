package com.game.smartremoteapp.model.http;

import rx.Subscriber;
import rx.exceptions.OnErrorFailedException;
/**
 * Created by zhouh on 2017/7/5.
 */
public abstract class RequestSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }
    @Override
    public void onError(Throwable e) {
        try {
            if(e!=null){
                _onError(e);
               // LogUtils.loge(e.getMessage(), "onError");
            }
        } catch (OnErrorFailedException e1) {
            if(e1.getMessage()!=null) {
              //  LogUtils.loge(e1.getMessage(),  "OnErrorFailedException");
            }
        }
    }

    @Override
    public void onNext(T t) {
        _onSuccess(t);
    }

    public abstract void _onSuccess(T t);

    public abstract void _onError(Throwable e);
}
