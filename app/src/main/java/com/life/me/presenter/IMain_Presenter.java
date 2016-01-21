package com.life.me.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import com.life.me.dao.WeatherDao;
import com.life.me.entity.resultentity.SearchWeather_Bean;

import java.util.List;

/**
 * Created by cuiyang on 16/1/5.
 */
public interface IMain_Presenter extends IPresenter {

    void setOnclickListener(View... views);

    /**
     * 检查是否为空
     * @param bean
     * @return
     */
    boolean getWeatherFilter(SearchWeather_Bean bean);

    /**
     * 更新ui
     * @param list
     */
    void updateUI(@NonNull List<WeatherDao> list);

    /**
     * 上传位置
     */
    void uploadLocation(String addr);
}
