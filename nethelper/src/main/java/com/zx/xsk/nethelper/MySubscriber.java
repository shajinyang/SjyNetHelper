package com.zx.xsk.nethelper;

import android.content.Context;

import com.orhanobut.logger.Logger;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by sjy on 2017/6/12.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {
    private Context mConetxt;
    public abstract void onStartNet();
    public abstract void onErrorNet(Throwable e);
    public abstract void onCompletedNet();
    public abstract void onNextNet(T t);

    public MySubscriber(Context mConetxt) {
        this.mConetxt = mConetxt;
    }

    public MySubscriber(){

    }

    @Override
    public void onStart() {
        onStartNet();
    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e.getMessage());
        onErrorNet(e);
    }

    @Override
    public void onCompleted() {
        onCompletedNet();
    }


    @Override
    public void onNext(T t) {
         onNextNet(t);
    }
}
