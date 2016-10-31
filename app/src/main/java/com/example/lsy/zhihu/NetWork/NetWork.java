package com.example.lsy.zhihu.NetWork;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lsy on 16-10-29.
 */

public class NetWork {
    private static ZhiHuApi zhiHuApi;
    private static OkHttpClient okHttpClient=new OkHttpClient();
    private static GsonConverterFactory gsonConverterFactory=GsonConverterFactory.create();
    private static CallAdapter.Factory rxAdapter= RxJavaCallAdapterFactory.create();

    public static ZhiHuApi getZhiHuApi() {
        if(zhiHuApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .addCallAdapterFactory(rxAdapter)
                    .addConverterFactory(gsonConverterFactory)
                    .client(okHttpClient)
                    .baseUrl("http://news-at.zhihu.com/")
                    .build();
            zhiHuApi=retrofit.create(ZhiHuApi.class);
        }
        return zhiHuApi;
    }
}
