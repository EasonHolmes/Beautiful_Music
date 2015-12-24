package com.life.me.entity;

import android.os.Environment;

/**
 * Created by cuiyang on 15/9/22.
 */
public class ConfigTb {

    public static final String getBaseMusictResult = "http://client.ctmus.cn/iting2/imusic/";

    public static final String getWeatherResult = "http://api.map.baidu.com/telematics/v3/weather?location=";

    public static final String baidu_mcode = "&output=json&ak=FuyvSudm3jRYddk1Xq1yRI6B&mcode=30:30:0E:2E:96:D0:15:36:B9:27:4C:95:F7:28:7E:76:C1:23:28:33;com.life.me";

    public static final String SDCard = Environment.getExternalStorageDirectory() + "/life";
    public static final String Photo_Path = SDCard + "/life.png";
}