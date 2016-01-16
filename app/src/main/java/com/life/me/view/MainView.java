package com.life.me.view;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.life.me.entity.SearchWeather_Bean;

/**
 * Created by cuiyang on 16/1/5.
 */
public interface MainView {
    Toolbar setToolbar(Toolbar toolbar);

    /**
     * 获取天气信息
     * @param Longitude
     * @param Latitude
     */
    void getWeatherResult(String Longitude, String Latitude);

    void setDrawerOpen(DrawerLayout drawerOpen);

    /**
     * 设置背景图片
     * @param Localpath
     */
    void setBackground(String Localpath);

    /**
     * 保存天气信息更新ui
     * @param searchWeatherBean
     */
    void saveDataAndUpdateUi(SearchWeather_Bean searchWeatherBean);

    /**
     * 设置侧边栏内容
     * @param drawerContent
     */
    void setPushContent(String drawerContent);



}
