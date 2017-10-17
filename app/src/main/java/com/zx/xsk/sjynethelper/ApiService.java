package com.zx.xsk.sjynethelper;

import com.zx.xsk.nethelper.HttpResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sjy on 2017/9/25.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("index/login/login")
    Observable<HttpResult> login(@FieldMap Map<String,Object> parms);


    /**
     * 知乎日报
     */
    @GET("http://news-at.zhihu.com/api/4/news/latest")
    Observable<DailyListBean> getDailyList();

}
