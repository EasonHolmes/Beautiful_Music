package com.life.me.presenter.iml;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
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

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.life.me.R;
import com.life.me.app.Myapplication;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.SearchWeather_Bean;
import com.life.me.entity.bmobentity.LocationTb;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.Utils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.mutils.imageloder.ImageLoader;
import com.life.me.presenter.IMain_Presenter;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by cuiyang on 15/9/23.
 */
public abstract class Main_presenter extends AppCompatActivity implements IMain_Presenter,View.OnClickListener {
    @InjectView(R.id.mToolBar)
    protected Toolbar mToolBar;
    @InjectView(R.id.main_Wimg)
    protected ImageSwitcher mainWimg;
    @InjectView(R.id.main_Wcity)
    protected TextView mainWcity;
    @InjectView(R.id.main_Wweather)
    protected TextView mainWweather;
    @InjectView(R.id.txt_nuan)
    protected TextSwitcher txtNuan;
    @InjectView(R.id.img_one)
    protected ImageSwitcher imgOne;
    @InjectView(R.id.img_two)
    protected ImageSwitcher imgTwo;
    @InjectView(R.id.img_three)
    protected ImageSwitcher imgThree;
    @InjectView(R.id.img_one_fu)
    protected ImageSwitcher imgOneFu;
    @InjectView(R.id.img_two_fu)
    protected ImageSwitcher imgTwoFu;
    @InjectView(R.id.img_three_fu)
    protected ImageSwitcher imgThreeFu;
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
    //BaiduLocation Servers
    protected final LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    protected final String tempcoor = "bd09ll";
    protected Vibrator mVibrator;
    protected LocationClient mLocationClient;
    //BaiduLocation Servers

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        httpUtils = HttpUtils.getSingleton();
        //初始化百度client
        mLocationClient = new LocationClient(this);
        if (Utils.hasFile())
            ImageLoader.getInstance().loadImage(ConfigTb.Photo_Path, mainBackground, false);
        new Shimmer().setDuration(1500).start(honeyTxt);
        onViewCreated(savedInstanceState);
    }

    @Override
    public boolean getWeatherFilter(SearchWeather_Bean bean) {
        if (bean.getResults() != null && bean.getResults().size() > 0) {
            return true;
        } else {
            Widget_Utils.showDialog(this, "没有查询到天气信息");
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
                    setUI(list.get(i), imgOne, imgOneFu, txtTommor, dateOne);
                    break;
                case 2:
                    setUI(list.get(i), imgTwo, imgTwoFu, txtTommorandtommor, dateTwo);
                    break;
                case 3:
                    setUI(list.get(i), imgThree, imgThreeFu, txtThreetommor, datethree);
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
        if (weather.getNightPictureUrl().contains("leizhenyu.png")) {
            setimg(v2, R.mipmap.leizhenyu);
        } else if (weather.getNightPictureUrl().contains("xiaoyu.png")) {
            setimg(v2, R.mipmap.xiaoyu);
        } else if (weather.getNightPictureUrl().contains("yu.png")) {
            setimg(v2, R.mipmap.zhongyu);
        } else if (weather.getNightPictureUrl().contains("duoyun.png")) {
            setimg(v2, R.mipmap.duoyun);
        } else if (weather.getNightPictureUrl().contains("qing.png")) {
            setimg(v2, R.mipmap.qingtian);
        } else if (weather.getNightPictureUrl().contains("wu.png")) {
            setimg(v2, R.mipmap.youwu);
        } else {
            setimg(v2, R.mipmap.yintian);
        }
        nuan.setText(weather.getTemperature());
        date.setText(weather.getDate());
    }

    /**
     * 初始化定位信息
     *
     * @param mContext
     * @param mLocationClient
     * @param listener
     */
    public void startLocation(Context mContext, LocationClient mLocationClient, BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);

        mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    @Override
    public void setOnclickListener(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void uploadLocation() {
        BmobQuery<TokenTb> query = new BmobQuery<TokenTb>();
        query.addWhereEqualTo("deviceId", ((Myapplication) getApplication()).getToken(Main_presenter.this));
        query.findObjects(Main_presenter.this, new FindListener<TokenTb>() {
            @Override
            public void onSuccess(List<TokenTb> object) {
                LocationTb location = new LocationTb();
                location.setLocName(CacheBean.addr);
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
