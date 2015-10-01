package com.life.me.model;

import com.life.me.dao.WeatherDao;

import java.util.List;

/**
 * Created by cuiyang on 15/9/23.
 */
public interface Main_Model {

    void weatherCallback(List<WeatherDao> weatherList);
}
