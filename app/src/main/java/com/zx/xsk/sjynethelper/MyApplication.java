package com.zx.xsk.sjynethelper;

import android.app.Application;

import com.zx.xsk.nethelper.BaseSubscriber;
import com.zx.xsk.nethelper.NetHelper;
import com.zx.xsk.sutil.DateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sjy on 2017/9/26.
 */

public class MyApplication extends Application {
    private String key="shhasd123asd123s";
    String val;
    @Override
    public void onCreate() {
        super.onCreate();
        NetHelper.init(this,"http://api.85zx.cn/index.php/",interceptor);
    }


    Interceptor interceptor=new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String json="{\"time\":\""+ DateUtil.timestamp()+"\"}";
            try {
                val=AESCipher.encrypt(key,json).trim();
                String res= AESCipher.decrypt(key,val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Request request= chain.request();
            Request.Builder builder=request.newBuilder();
            builder.addHeader("sign",val);
            return chain.proceed(builder.build());
        }
    };
}
