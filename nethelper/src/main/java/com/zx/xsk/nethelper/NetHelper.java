package com.zx.xsk.nethelper;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.zx.xsk.nethelper.dbbeans.ResponseBean;
import com.zx.xsk.nethelper.icontext.IApplication;
import com.zx.xsk.nethelper.util.CacheManager;
import com.zx.xsk.sutil.SettingsUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 网络请求类
 * Created by sjy on 2017/6/12.
 */

public class NetHelper {
    private static Application application;
    private static Realm realm;
    private static RealmConfiguration realmConfig;
    private static Retrofit retrofit;
    public static OkHttpClient okHttpClient;
    public static NetHelper Instance;
    public static String BaseUrl = "";
    private static boolean isCache = false;//是否缓存
    private static long overtime = 60 * 1000;//缓存有效时间，默认1分钟
    private static final int DEFAULT_TIMEOUT = 5;//超时时间
    private static StringBuffer messages = new StringBuffer();//存储日志消息

    public NetHelper() {
        if (okHttpClient == null) {
            initOkHttpClient();
        }
    }


    public static NetHelper getInstance() {
        if (Instance == null) {
            Instance = new NetHelper();
        }
        return Instance;
    }

    public static void init(Application application, String baseUrl) {
        if (okHttpClient == null) {
            initOkHttpClient();
        }
        setApplication(application);
        IApplication.setApplication(application);

        //初始化数据库
        realmConfig = new RealmConfiguration
                .Builder(application)
                .build();
        BaseUrl = baseUrl;
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
     *
     * @param url
     * @return
     */
    public NetHelper baseUrl(String url) {
        BaseUrl = url;
        return this;
    }

    /**
     * 设置缓存时间
     *
     * @param time
     * @return
     */
    public NetHelper cacheExpire(long time) {
        overtime = time;
        return this;
    }

    /**
     * 是否缓存
     *
     * @param isCache
     * @return
     */
    public NetHelper isCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }


    /**
     * 创建retrofit实例
     *
     * @param apiService
     * @return
     */
    public <T> T create(Class<T> apiService) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(apiService);
    }


    /**
     * 初始化okhttp配置
     */
    private static void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//连接超时设置
                .retryOnConnectionFailure(true)//错误重连
                .addInterceptor(getLogInterceptor()) //添加日志打印过滤器
                .addInterceptor(getCacheInterceptor()); //添加缓存过滤
        okHttpClient = builder.build();
    }

    /**
     * http日志拦截过滤器
     *
     * @return
     */
    private static Interceptor getLogInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (message != null && message.contains("END HTTP")) {
                    Logger.i(messages.toString());
                    messages = new StringBuffer();
                } else if (message.contains("{")) {
                    Logger.i(message);
                    messages.append(message + "\n");
                } else {
                    messages.append(message + "\n");
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


    /**
     * cacheInterceptor缓存拦截
     *
     * @return
     */
    private static Interceptor getCacheInterceptor() {
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                String params = buffer.readString(Charset.forName("UTF-8")); //获取请求参数
                final String key;
                if (request.method().equals("POST")) {
                    key = CacheManager.encryptMD5(request.url().toString() + params);
                } else {
                    key = CacheManager.encryptMD5(request.url().toString());
                }
                Response response = null;
                if (SettingsUtil.isNetworkAvailable(application.getApplicationContext())) {
                    //缓存模式开启，有网络状态下也会访问本地缓存
                    if (isCache == true) {
                        //读取数据库缓存
                        realm = Realm.getInstance(realmConfig);
                        ResponseBean responseBean = realm
                                .where(ResponseBean.class)
                                .equalTo("key", key)
                                .findFirst();
                        String bean = "";
                        //判断数据是否过期
                        if (responseBean.getOvertime() + responseBean.getUpdateTime() > System.currentTimeMillis()) {
                            bean = responseBean.getResponseContent();
                            if (bean != null && !bean.isEmpty()) {
                                response = getCacheResponse(request, bean);
                            } else {
                                response = getNetResponse(chain, request, key);
                            }
                        } else {
                            response = getNetResponse(chain, request, key);
                        }
                    } else {
                        //访问网络数据不缓存到本地
                        response = getNetResponseUnCache(chain, request, key);
                    }
                } else {
                    //没有网络的时候,调用chain.proceed(request),会抛出异常（504）
                    //读出响应
                    String bean = "";
                    //缓存模式，直接读取本地缓存
                    if(isCache==true) {
                        //读取数据库缓存
                        realm = Realm.getInstance(realmConfig);
                        ResponseBean responseBean = realm
                                .where(ResponseBean.class)
                                .equalTo("key", key)
                                .findFirst();
                        bean = responseBean.getResponseContent();
                        if (bean == null) {
                            bean = "";
                        }
                    }
                    response = getCacheResponse(request, bean);
                }
                realm.close();
                return response;
            }
        };
        return cacheInterceptor;
    }

    /**
     * 构建缓存response
     *
     * @param request
     * @param bean
     * @return
     */
    private static Response getCacheResponse(Request request, String bean) {
        Response response;
        int maxStale = 60 * 60 * 24 * 28;
        //构建一个新的response响应结果
        response = new Response.Builder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .body(ResponseBody.create(MediaType.parse("application/json"), bean.getBytes()))
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .build();
        return response;
    }

    /**
     * 请求网络并缓存数据
     *
     * @param chain
     * @param request
     * @param key
     * @return
     * @throws IOException
     */
    private static Response getNetResponse(Interceptor.Chain chain, Request request, final String key) throws IOException {
        Response response;
        Response originresponse = chain.proceed(request);
        //获取MediaType，用于重新构建ResponseBody
        MediaType type = originresponse.body().contentType();
        //获取body字节即响应，用于存入数据库和重新构建ResponseBody
        final byte[] bs = originresponse.body().bytes();
        response = originresponse.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + 0)
                //重新构建body，原因在于body只能调用一次，之后就关闭了。
                .body(ResponseBody.create(type, bs))
                .build();
        //缓存数据
        //硬盘缓存
        //CacheManager.getInstance().setCache(key,new String(bs,"utf-8"));
        //数据库缓存
        realm = Realm.getInstance(realmConfig);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ResponseBean responseBean = realm.where(ResponseBean.class).equalTo("key", key).findFirst();
                if (responseBean != null) {
                    responseBean.setUpdateTime(System.currentTimeMillis());
                    try {
                        responseBean.setResponseContent(new String(bs, "utf-8"));
                    } catch (UnsupportedEncodingException e) {

                    }
                } else {
                    ResponseBean responseBean2 = realm.createObject(ResponseBean.class);
                    responseBean2.setId(System.currentTimeMillis());
                    responseBean2.setKey(key);
                    responseBean2.setUpdateTime(System.currentTimeMillis());
                    try {
                        responseBean2.setResponseContent(new String(bs, "utf-8"));
                    } catch (UnsupportedEncodingException e) {

                    }
                    responseBean2.setOvertime(overtime);
                }
            }
        });
        return response;
    }
    /**
     * 请求网络不缓存数据
     *
     * @param chain
     * @param request
     * @param key
     * @return
     * @throws IOException
     */
    private static Response getNetResponseUnCache(Interceptor.Chain chain, Request request, final String key) throws IOException {
        Response response;
        Response originresponse = chain.proceed(request);
        //获取MediaType，用于重新构建ResponseBody
        MediaType type = originresponse.body().contentType();
        //获取body字节即响应，用于存入数据库和重新构建ResponseBody
        final byte[] bs = originresponse.body().bytes();
        response = originresponse.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + 0)
                //重新构建body，原因在于body只能调用一次，之后就关闭了。
                .body(ResponseBody.create(type, bs))
                .build();
        return response;
    }


}
