package com.life.me.entity;

import android.os.Environment;

/**
 * Created by cuiyang on 15/9/22.
 */
public class ConfigTb {

    public static final String WeatherHttpUrl = "http://api.map.baidu.com/telematics/v3/weather?location=";

    public static final String preference_Name = "config";
    public static final String preference_BKey = "background";


    public static final String SDCard = Environment.getExternalStorageDirectory() + "/life";
    public static final String PhotoName = SDCard+"/life.jpg";
}