package com.life.me.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import com.life.me.dao.WeatherDao;
import com.life.me.entity.SearchWeather_Bean;

import java.util.List;

/**
 * Created by cuiyang on 16/1/5.
 */
public interface IMain_Presenter extends IPresenter {
    void setOnclickListener(View... views);

    boolean getWeatherFilter(SearchWeather_Bean bean);

    void updateUI(@NonNull List<WeatherDao> list);

    void uploadLocation();
}
