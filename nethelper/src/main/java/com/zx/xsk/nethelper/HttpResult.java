package com.zx.xsk.nethelper;

/**
 * 网络请求接口返回泛型
 * Created by sjy on 2017/6/12.
 */

public class HttpResult<T> {
   public String error;
   public String info;
   public T data;
}
