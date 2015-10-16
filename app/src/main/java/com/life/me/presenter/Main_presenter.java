package com.life.me.presenter;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.SearchWeatherBean;
import com.life.me.entity.bmobentity.LocationTb;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.model.Main_Model;
import com.life.me.mutils.DeviceInfo;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleRequestQueue;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by cuiyang on 15/9/23.
 */
public class Main_presenter {
    /**
     * 查询天气信息
     *
     * @param mContext
     * @param callback
     */
    public void getWeather(Context mContext, Main_Model callback) {
        if (CacheBean.Longitude == null || CacheBean.Latitude == null) {//有可能定位延迟
            callback.weatherCallback(null);
            return;
        }
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
                                    //放入天气返回的城市因为没开网百度定不到位就会为null..后面再取地址就nullexception不过没开网可以拿到经纬度
                                    dao.setCity(tb.getResults().get(0).getCurrentCity());
                                    dao.save();
                                }
                                List<WeatherDao> list = DataSupport.order("id asc").find(WeatherDao.class);
                                callback.weatherCallback(list);
                            } else {
                                callback.weatherCallback(null);
                            }
                        } catch (Exception e) {
                            callback.weatherCallback(null);
                            Log.e(getClass().getName(), "eeee==" + e.getMessage());
                        }
                    }
                });
    }

    /**
     * 如果有token也就是打开过应用就使用
     * 如果没有就生成上传
     */
    public String getToken(Context mContext) {
        SharedPreferences share = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokens = share.getString("token", "");
        if (tokens.equals("") || tokens.length() < 1) {
            String tem = DeviceInfo.getInstance(mContext).makeDeviceIdForToken();
            String device = tem.substring(9, tem.length());
            share.edit().putString("token", device).apply();
            TokenTb tb = new TokenTb();
            tb.setDeviceId(device);
            tb.save(mContext);
            return device;
        } else {
            return tokens;
        }
    }

    /**
     * 上传地理位置
     */
    public void upLocation(Context mContext) {
        BmobQuery<TokenTb> query = new BmobQuery<TokenTb>();
        query.addWhereEqualTo("deviceId", getToken(mContext));
        query.findObjects(mContext, new FindListener<TokenTb>() {
            @Override
            public void onSuccess(List<TokenTb> object) {
                LocationTb location = new LocationTb();
                location.setLocName(CacheBean.addr);
                if (object != null && object.size() > 0) {
                    location.setDeviceId((TokenTb) object.get(0));
                }
                location.save(mContext, null);
            }

            @Override
            public void onError(int code, String msg) {
            }
        });
    }
}
