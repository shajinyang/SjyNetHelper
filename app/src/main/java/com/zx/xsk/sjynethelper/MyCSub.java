package com.zx.xsk.sjynethelper;

import com.zx.xsk.nethelper.BaseSubscriber;

/**
 * Created by sjy on 2017/9/29.
 */

public abstract class MyCSub<T> extends BaseSubscriber<T> {

    abstract void onCompeteReq();
    abstract void onNextReq(T t);

       @Override
    public void onCompletedNet() {
        onCompeteReq();
    }

    @Override
    public void onNextNet(T t) {
        onNextReq(t);
    }
}
