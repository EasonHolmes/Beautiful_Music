package com.life.me.presenter.iml;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.life.me.R;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.bmobentity.LocationTb;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.entity.resultentity.SearchWeather_Bean;
import com.life.me.mutils.DeviceInfo;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.LogUtils;
import com.life.me.mutils.Utils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.mutils.imageloder.ImageLoader;
import com.life.me.presenter.IMain_Presenter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by cuiyang on 15/9/23.
 */
public abstract class Main_presenter extends AppCompatActivity implements IMain_Presenter, View.OnClickListener {
    @InjectView(R.id.mToolBar)
    protected Toolbar mToolBar;
    @InjectView(R.id.main_Wimg)
    protected ImageSwitcher mainWimg;
    @InjectView(R.id.main_Wcity)
    protected TextView mainWcity;
    @InjectView(R.id.main_Wweather)
    protected TextView mainWweather;
    @InjectView(R.id.txt_nuan)
    protected TextView txtNuan;
    @InjectView(R.id.img_one)
    protected ImageSwitcher imgOne;
    @InjectView(R.id.img_two)
    protected ImageSwitcher imgTwo;
    @InjectView(R.id.img_three)
    protected ImageSwitcher imgThree;
//    @InjectView(R.id.img_one_fu)
//    protected ImageSwitcher imgOneFu;
//    @InjectView(R.id.img_two_fu)
//    protected ImageSwitcher imgTwoFu;
//    @InjectView(R.id.img_three_fu)
//    protected ImageSwitcher imgThreeFu;
    @InjectView(R.id.txt_tommor)
    protected TextView txtTommor;
    @InjectView(R.id.txt_tommorandtommor)
    protected TextView txtTommorandtommor;
    @InjectView(R.id.txt_threetommor)
    protected TextView txtThreetommor;
    @InjectView(R.id.honey_txt)
    protected ShimmerTextView honeyTxt;
    @InjectView(R.id.date_one)
    protected TextView dateOne;
    @InjectView(R.id.date_two)
    protected TextView dateTwo;
    @InjectView(R.id.date_three)
    protected TextView datethree;
    @InjectView(R.id.drawer_view)
    protected LinearLayout drawerView;
    @InjectView(R.id.mDrawerlayout)
    protected DrawerLayout mDrawerlayout;
    @InjectView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.linearLayout)
    protected LinearLayout linearLayout;
    @InjectView(R.id.img_music)
    protected ImageView imgMusic;
    @InjectView(R.id.main_background)
    protected ImageView mainBackground;


    protected ActionBarDrawerToggle mDrawerToggle;
    protected HttpUtils httpUtils;

    //高德start声明AMapLocationClient类对象
    public AMapLocationClient locationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption locationOption = null;
    //高德end


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        httpUtils = HttpUtils.getSingleton();
        if (Utils.hasFile())
            ImageLoader.getInstance().loadImage(ConfigTb.Photo_Path, mainBackground, false);

        new Shimmer().setDuration(1500).start(honeyTxt);

        onViewCreated(savedInstanceState);
    }

    protected AMapLocationClientOption setLocationOption() {
        locationOption = new AMapLocationClientOption();
        ////设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        return locationOption;
    }

    @Override
    public boolean getWeatherFilter(SearchWeather_Bean bean) {
        if (bean.getResults() != null && bean.getResults().size() > 0) {
            return true;
        } else {
            Widget_Utils.showSnackbar(linearLayout, "没有查询到天气信息");
            return false;
        }
    }

    @Override
    public void updateUI(@NonNull List<WeatherDao> list) {
        for (int i = 0; i < list.size(); i++) {
            switch (i) {
                case 0:
                    setTitleWeather(list.get(i), mainWimg);
                    break;
                case 1:
                    setUI(list.get(i), imgOne, null, txtTommor, dateOne);
                    break;
                case 2:
                    setUI(list.get(i), imgTwo, null, txtTommorandtommor, dateTwo);
                    break;
                case 3:
                    setUI(list.get(i), imgThree, null, txtThreetommor, datethree);
                    break;
            }
        }
    }

    private void setTitleWeather(WeatherDao weather, ImageSwitcher v) {
        if (weather.getWeather().contains("阵雨")) {
            setimg(v, R.mipmap.leizhenyu);
        } else if (weather.getWeather().contains("小雨")) {
            setimg(v, R.mipmap.xiaoyu);
        } else if (weather.getWeather().contains("雨")) {
            setimg(v, R.mipmap.zhongyu);
        } else if (weather.getWeather().contains("多云")) {
            setimg(v, R.mipmap.duoyun);
        } else if (weather.getWeather().contains("晴")) {
            setimg(v, R.mipmap.qingtian);
        } else if (weather.getWeather().contains("雾")) {
            setimg(v, R.mipmap.youwu);
        } else {
            setimg(v, R.mipmap.yintian);
        }
        mainWcity.setText(weather.getCity());
        mainWweather.setText(weather.getWeather() + "");
        txtNuan.setText(weather.getDate().substring(14, weather.getDate().length() - 1));
    }

    private void setimg(ImageSwitcher v, int imgId) {
        v.setImageResource(imgId);
    }

    private void setUI(WeatherDao weather, ImageSwitcher v, ImageSwitcher v2, TextView nuan, TextView date) {
        if (weather.getDayPictureUrl().contains("zhenyu.png")) {
            setimg(v, R.mipmap.leizhenyu);
        } else if (weather.getDayPictureUrl().contains("xiaoyu.png")) {
            setimg(v, R.mipmap.xiaoyu);
        } else if (weather.getDayPictureUrl().contains("yu.png")) {
            setimg(v, R.mipmap.zhongyu);
        } else if (weather.getDayPictureUrl().contains("duoyun.png")) {
            setimg(v, R.mipmap.duoyun);
        } else if (weather.getDayPictureUrl().contains("qing.png")) {
            setimg(v, R.mipmap.qingtian);
        } else if (weather.getDayPictureUrl().contains("wu.png")) {
            setimg(v, R.mipmap.youwu);
        } else {
            setimg(v, R.mipmap.yintian);
        }
//        if (weather.getNightPictureUrl().contains("leizhenyu.png")) {
//            setimg(v2, R.mipmap.leizhenyu);
//        } else if (weather.getNightPictureUrl().contains("xiaoyu.png")) {
//            setimg(v2, R.mipmap.xiaoyu);
//        } else if (weather.getNightPictureUrl().contains("yu.png")) {
//            setimg(v2, R.mipmap.zhongyu);
//        } else if (weather.getNightPictureUrl().contains("duoyun.png")) {
//            setimg(v2, R.mipmap.duoyun);
//        } else if (weather.getNightPictureUrl().contains("qing.png")) {
//            setimg(v2, R.mipmap.qingtian);
//        } else if (weather.getNightPictureUrl().contains("wu.png")) {
//            setimg(v2, R.mipmap.youwu);
//        } else {
//            setimg(v2, R.mipmap.yintian);
//        }
        nuan.setText(weather.getTemperature());
        date.setText(weather.getDate());
    }

    @Override
    public void setOnclickListener(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void uploadLocation(String addr) {
        BmobQuery<TokenTb> query = new BmobQuery<TokenTb>();
        query.addWhereEqualTo("deviceId", DeviceInfo.getInstance(this).getToken());
        query.findObjects(Main_presenter.this, new FindListener<TokenTb>() {
            @Override
            public void onSuccess(List<TokenTb> object) {
                LocationTb location = new LocationTb();
                location.setLocName(addr);
                if (object != null && object.size() > 0) {
                    location.setDeviceId((TokenTb) object.get(0));
                }
                location.save(Main_presenter.this, null);
            }

            @Override
            public void onError(int code, String msg) {
            }
        });
    }
}
