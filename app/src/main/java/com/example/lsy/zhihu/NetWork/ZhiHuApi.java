package com.example.lsy.zhihu.NetWork;

import com.example.lsy.zhihu.bean.DailyNews;
import com.example.lsy.zhihu.bean.NewApi;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lsy on 16-10-29.
 */

public interface ZhiHuApi {
    @GET("api/4/news/latest")
    Observable<DailyNews> getDailyNews();

    @GET("api/4/news/{id}")
    Observable<NewApi> getNew(@Path("id") String newId);
}
