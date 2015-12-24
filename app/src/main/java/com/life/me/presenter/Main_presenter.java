package com.life.me.presenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.life.me.R;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.SearchWeather_Bean;
import com.life.me.mutils.Widget_Utils;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cuiyang on 15/9/23.
 */
public abstract class Main_presenter extends AppCompatActivity {
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initToolbar(getResources().getString(R.string.main_act_title), false);

        onViewCreated();
    }

    protected abstract void onViewCreated();

    protected void initToolbar(String title, boolean or) {
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//这个直接连半透明都没有了
        }
        mToolBar.setTitle(title);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!or) {
            mDrawerlayout = (DrawerLayout) findViewById(R.id.mDrawerlayout);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolBar, R.string.open, R.string.close);
            mDrawerToggle.syncState();
            mDrawerlayout.setDrawerListener(mDrawerToggle);
        }
    }

    /**
     * 设置侧边栏内容
     *
     * @param drawerContent
     */
    protected void setPushContent(String drawerContent) {
        SharedPreferences share = Main_presenter.this.getSharedPreferences(getResources().getString(R.string.drawer_content_file), MODE_PRIVATE);
        if (drawerContent != null) {//是否有推送内容
            share.edit().putString(getResources().getString(R.string.push_content), drawerContent).apply();
            honeyTxt.setText(drawerContent + "");
        } else {
            honeyTxt.setText(share.getString(getResources().getString(R.string.push_content), "I love you my Lover"));
        }
    }

    protected boolean getWeatherFilter(SearchWeather_Bean bean) {
        if (bean.getResults() != null && bean.getResults().size() > 0) {
            return true;
        } else {
            Widget_Utils.showSnacker(linearLayout, "没有查询到天气信息");
            return false;
        }
    }


    protected void updateUI(@NonNull List<WeatherDao> list) {
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


//    /**
//     * 查询天气信息
//     *
//     * @param mContext
//     * @param callback
//     */
//    public void getWeather(Context mContext, Main_CallBack callback) {
//        if (CacheBean.Longitude == null || CacheBean.Latitude == null) {//有可能定位延迟
//            callback.weatherCallback(null);
//            return;
//        }
//        HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
//                ConfigTb.sdfsd + CacheBean.Longitude + "," + CacheBean.Latitude + "&output=json&ak=FuyvSudm3jRYddk1Xq1yRI6B&mcode="+ConfigTb.baidu_mcode
//                , new HttpUtils.RequestCallBack() {
//                    @Override
//                    public void success(String result) {
//                        try {
//                            SearchWeatherBean tb = new Gson().fromJson(result, SearchWeatherBean.class);
//                            if (tb.getError() == 0) {
//                               Log.e(getClass().getName(),"sdfsdf========"+result);
//                                List<WeatherDao> data = tb.getResults().get(0).getWeather_data();
//                                WeatherDao.deleteAll(WeatherDao.class);// 删除所有
//                                for (WeatherDao dao : data) {
//                                    //放入天气返回的城市因为没开网百度定不到位就会为null..后面再取地址就nullexception不过没开网可以拿到经纬度
//                                    dao.setCity(tb.getResults().get(0).getCurrentCity());
//                                    dao.save();
//                                }
//                                List<WeatherDao> list = DataSupport.order("id asc").find(WeatherDao.class);
//                                callback.weatherCallback(list);
//                            } else {
//                                callback.weatherCallback(null);
//                            }
//                        } catch (Exception e) {
//                            callback.weatherCallback(null);
//                            Log.e(getClass().getName(), "eeee==" + e.getMessage());
//                        }
//                    }
//                });
//    }

}
