package com.zx.xsk.nethelper.inters;

import retrofit2.Call;
import retrofit2.Response;

public interface EnhancedCallback<T> {
    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call, Throwable t);

    void onGetCache(T t);
}
