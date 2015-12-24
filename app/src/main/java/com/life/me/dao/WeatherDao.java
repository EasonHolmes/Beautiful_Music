package com.life.me.dao;

import org.litepal.crud.DataSupport;

/**
 * Created by cuiyang on 15/9/24.
 */
public class WeatherDao extends DataSupport {


    /**
     * date : 周四 09月24日 (实时：21℃)
     * dayPictureUrl : http://api.map.baidu.com/images/weather/day/yin.png
     * nightPictureUrl : http://api.map.baidu.com/images/weather/night/leizhenyu.png
     * weather : 阴转雷阵雨
     * wind : 微风
     * temperature : 24 ~ 16℃
     */

    private String date;
    private String dayPictureUrl;
    private String nightPictureUrl;
    private String weather;
    private String wind;
    private String temperature;
    private String city;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDayPictureUrl(String dayPictureUrl) {
        this.dayPictureUrl = dayPictureUrl;
    }

    public void setNightPictureUrl(String nightPictureUrl) {
        this.nightPictureUrl = nightPictureUrl;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getDayPictureUrl() {
        return dayPictureUrl;
    }

    public String getNightPictureUrl() {
        return nightPictureUrl;
    }

    public String getWeather() {
        return weather;
    }

    public String getWind() {
        return wind;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "WeatherDao{" +
                "date='" + date + '\'' +
                ", dayPictureUrl='" + dayPictureUrl + '\'' +
                ", nightPictureUrl='" + nightPictureUrl + '\'' +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}
