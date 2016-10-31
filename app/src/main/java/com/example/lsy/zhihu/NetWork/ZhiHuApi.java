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
    //获取最新文章，即今日热文
    @GET("api/4/news/latest")
    Observable<DailyNews> getDailyNews();

    //通过id查看新闻
    @GET("api/4/news/{id}")
    Observable<NewApi> getNew(@Path("id") String newId);

    //获取以往消息,date前一天的消息
    @GET("api/4/news/before/{date}")
    Observable<DailyNews> getBeforData(@Path("date") String date);
}
