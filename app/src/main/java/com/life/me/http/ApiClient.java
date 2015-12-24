package com.life.me.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.life.me.entity.ConfigTb;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by cuiyang on 15/12/21.
 */
public final class ApiClient {

    private ApiClient() {
    }

    private static final String API_HOST = ConfigTb.getBaseMusictResult;//因为使用的api都不是一家的所有这个base不定

    private static final Gson gson = new GsonBuilder().create();


    private static final Retrofit retrofit_str = new Retrofit.Builder()
            .baseUrl(API_HOST)
            .client(OkHttpClientSingle.getHttpClientSingle())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    /**
     * 使用Rxjava不能全用okHttp做拦截器
     */
    private static final Retrofit retrofit_rx = new Retrofit.Builder()
            .baseUrl(API_HOST)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static final ApiService SERVICE_str = retrofit_str.create(ApiService.class);

    public static final ApiService SERVICE_rx = retrofit_rx.create(ApiService.class);
}
