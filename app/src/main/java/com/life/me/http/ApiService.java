package com.life.me.http;

import com.life.me.entity.postentity.Post_Get_Ring;
import com.life.me.entity.postentity.Post_Get_Search;
import com.life.me.entity.resultentity.Contains_keyWord_bean;
import com.life.me.entity.resultentity.MusicAndImgResult_bean;
import com.life.me.entity.resultentity.SearchWeather_Bean;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by cuiyang on 15/12/21.
 */
public interface ApiService {

    /**
     * 天气
     *
     * @param url
     * @return
     */
    @GET
    Observable<SearchWeather_Bean> getWeather(@Url String url);

    /**
     * 关键字搜索结果
     *
     * @param search
     * @return
     */
    @POST("V2")
    Observable<Contains_keyWord_bean> getContains_key_MusicName(@Body Post_Get_Search search);

    /**
     * 获取音乐相关信息
     *
     * @param ring
     * @return
     */
    @POST("V2")
    Observable<MusicAndImgResult_bean> getMusicResult(@Body Post_Get_Ring ring);
}
