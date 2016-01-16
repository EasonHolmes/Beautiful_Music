package com.life.me.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


import com.life.me.entity.resultentity.SearchWeather_Bean;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

/**
 * Created by cuiyang on 15/12/22.
 */
public class ApiDao {

    private ApiDao() {
    }

    private static ApiDao single = null;

    public static synchronized ApiDao getInstance() {
        if (single == null) {
            single = new ApiDao();
        }
        return single;
    }

    public static SearchWeather_Bean saveWeather(SearchWeather_Bean data) {
        DataSupport.deleteAll(WeatherDao.class);
        SQLiteDatabase db = Connector.getWritableDatabase();
        db.beginTransaction();
        for (WeatherDao bean : data.getResults().get(0).getWeather_data()) {
            ContentValues values = new ContentValues();
            values.put("date", bean.getDate());
            values.put("dayPictureUrl", bean.getDayPictureUrl());
            values.put("nightPictureUrl", bean.getNightPictureUrl());
            values.put("weather", bean.getWeather());
            values.put("wind", bean.getWind());
            values.put("temperature", bean.getTemperature());
            values.put("city", data.getResults().get(0).getCurrentCity());
            db.insert(WeatherDao.class.getSimpleName(), "", values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return data;
    }
}
