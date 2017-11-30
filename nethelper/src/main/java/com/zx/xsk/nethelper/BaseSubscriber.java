package com.zx.xsk.nethelper;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zx.xsk.nethelper.icontext.IApplication;
import com.zx.xsk.nethelper.util.SettingsUtil;

import rx.Subscriber;

/**
 * Created by sjy on 2017/6/12.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private Context mConetxt;
    public abstract void onStartNet();
    public abstract void onErrorNet(Throwable e);
    public abstract void onCompletedNet();
    public abstract void onNextNet(T t);

    public BaseSubscriber(Context mConetxt) {
        this.mConetxt = mConetxt;
    }

    public BaseSubscriber(){

    }


    @Override
    public void onStart() {

        if(SettingsUtil.isNetworkAvailable(IApplication.application.getApplicationContext())){
            onStartNet();
        }else {
            Toast.makeText(IApplication.application.getApplicationContext(),"网络连接已断开",Toast.LENGTH_SHORT).show();
            onStartNet();
        }
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
