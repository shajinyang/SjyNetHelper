package com.zx.xsk.nethelper;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.orhanobut.logger.Logger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求类
 * Created by sjy on 2017/6/12.
 */

public class NetHelper {
    private static Application application;
    private static Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT = 5;//超时时间
    public static OkHttpClient okHttpClient;
    public static NetHelper Instance;
    public static String BaseUrl="";
    private MySubscriber mySubscriber;
    private boolean isCache=false;
    private StringBuffer messages=new StringBuffer();//存储日志消息

    public NetHelper() {
        initOkHttpClient();
    }


    public static NetHelper getInstance(){
        if(Instance==null){
            Instance=new NetHelper();
        }
        return Instance;
    }

    public static void init(Application application,String baseUrl){
        setApplication(application);
        BaseUrl=baseUrl;
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        NetHelper.application = application;
    }

    /**
     * 基础url
     * @param url
     * @return
     */
    public NetHelper baseUrl(String url){
        BaseUrl=url;
        return this;
    }

    /**
     * 设置缓存时间
     * @param time
     * @return
     */
    public NetHelper cacheExpire(long time){
        return this;
    }

    /**
     * 是否缓存
     * @param isCache
     * @return
     */
    public NetHelper isCache(boolean isCache){
        this.isCache=isCache;
        return this;
    }


    /**
     * 创建retrofit实例
     * @param apiService
     * @return
     */
    public <T> T create(Class<T> apiService){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return  retrofit.create(apiService);
    }





    /**
     * 初始化okhttp配置
     */
    private void initOkHttpClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //添加日志打印过滤器
        builder.addInterceptor(getLogInterceptor());
        okHttpClient = builder.build();
    }

    /**
     * http日志拦截过滤器
     * @return
     */
    private Interceptor getLogInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger(){
            @Override
            public void log(String message) {
                if(message!=null&&message.contains("END HTTP")){
                    Logger.i(messages.toString());
                    messages=new StringBuffer();
                }else if(message.contains("{")){
                    Logger.i(message);
                    messages.append(message+"\n");
                }
                else {
                    messages.append(message+"\n");
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }













}
