package com.life.me.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.SearchWeatherBean;
import com.life.me.model.Main_Model;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleRequestQueue;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by cuiyang on 15/9/23.
 */
public class Main_presenter {

    public void getWeather(Context mContext, Main_Model callback) {
        HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
                ConfigTb.WeatherHttpUrl + CacheBean.Longitude + "," + CacheBean.Latitude + "&output=json&ak=FuyvSudm3jRYddk1Xq1yRI6B&mcode=30:30:0E:2E:96:D0:15:36:B9:27:4C:95:F7:28:7E:76:C1:23:28:33;com.life.me"
                , new HttpUtils.RequestCallBack() {
                    @Override
                    public void success(String result) {
                        try {
                            SearchWeatherBean tb = new Gson().fromJson(result, SearchWeatherBean.class);
                            if (tb.getError() == 0) {
                                List<WeatherDao> data = tb.getResults().get(0).getWeather_data();
                                WeatherDao.deleteAll(WeatherDao.class);// 删除所有
                                for (WeatherDao dao : data) {
                                    //放入天气返回的城市因为没开网百度定不到位就会为null
                                    // 后面再取地址就nullexception不过没开网可以拿到经纬度
                                    dao.setCity(tb.getResults().get(0).getCurrentCity());
                                    dao.save();
                                }
                                List<WeatherDao> list = DataSupport.order("id asc").find(WeatherDao.class);
                                callback.weatherCallback(list);
                            }
                        } catch (Exception e) {
                            Log.e(getClass().getName(), "eeee==" + e.getMessage());
                        }
                    }
                });
    }
}
