package com.life.me.http;

import com.life.me.entity.Contains_keyWord_bean;
import com.life.me.entity.Post_Get_Search;
import com.life.me.entity.SearchWeather_Bean;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by cuiyang on 15/12/21.
 */
public interface ApiService {

    @GET
    Observable<SearchWeather_Bean> getWeather(@Url String url);

    @POST("V2")
    Observable<Contains_keyWord_bean> getContains_key_MusicName(@Body Post_Get_Search search);

}
