package com.life.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.model.Main_Model;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleRequestQueue;
import com.life.me.presenter.Main_presenter;
import com.life.me.view.SystemBarTintManager;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Main_Model, SwipeRefreshLayout.OnRefreshListener {


    @InjectView(R.id.mToolBar)
    Toolbar mToolBar;
    @InjectView(R.id.main_Wimg)
    ImageSwitcher mainWimg;
    @InjectView(R.id.main_Wcity)
    TextView mainWcity;
    @InjectView(R.id.main_Wweather)
    TextView mainWweather;
    @InjectView(R.id.txt_nuan)
    TextSwitcher txtNuan;
    @InjectView(R.id.img_one)
    ImageSwitcher imgOne;
    @InjectView(R.id.img_two)
    ImageSwitcher imgTwo;
    @InjectView(R.id.img_three)
    ImageSwitcher imgThree;
    @InjectView(R.id.img_one_fu)
    ImageSwitcher imgOneFu;
    @InjectView(R.id.img_two_fu)
    ImageSwitcher imgTwoFu;
    @InjectView(R.id.img_three_fu)
    ImageSwitcher imgThreeFu;
    @InjectView(R.id.txt_tommor)
    TextView txtTommor;
    @InjectView(R.id.txt_tommorandtommor)
    TextView txtTommorandtommor;
    @InjectView(R.id.txt_threetommor)
    TextView txtThreetommor;
    @InjectView(R.id.honey_txt)
    ShimmerTextView honeyTxt;
    @InjectView(R.id.date_one)
    TextView dateOne;
    @InjectView(R.id.date_two)
    TextView dateTwo;
    @InjectView(R.id.date_three)
    TextView datethree;
    @InjectView(R.id.drawer_view)
    LinearLayout drawerView;
    @InjectView(R.id.mDrawerlayout)
    DrawerLayout mDrawerlayout;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.img_music)
    ImageButton imgMusic;


    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private Main_presenter presenter;
    private final int UPDATE_IMG = 0;
    private Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_IMG:
                    refreshLayout.setRefreshing(false);
                    updateUI((List<WeatherDao>) msg.obj);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mContext = this;
        initToolbar("宝贝", false);
        new Shimmer().setDuration(1000).start(honeyTxt);//侧边栏的字
        presenter = new Main_presenter();
        initView();
    }

    private void initView() {
        drawerView.setOnClickListener(this);//设置一个空的键听要不侧边栏会透过去影响后面的手势
        imgMusic.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(R.color.toolbar_background2);
        refreshLayout.setOnRefreshListener(this);
        if (!HttpUtils.getSingleton().hasNetwork(mContext)) {//未联网时拿缓存
            hand.obtainMessage(UPDATE_IMG, DataSupport.order("id asc").find(WeatherDao.class)).sendToTarget();
        } else {
            presenter.getWeather(mContext, this);
        }
    }

    private void updateUI(@NonNull List<WeatherDao> list) {
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

    private void initToolbar(String title, boolean or) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.statc_color);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_music:
                startActivity(new Intent(mContext,Music_Activity.class));
                overridePendingTransition(R.anim.small_2_big,R.anim.fade_out);
                break;
        }
    }

    @Override
    public void weatherCallback(List<WeatherDao> weatherList) {
        if (weatherList != null) {
            hand.obtainMessage(UPDATE_IMG, weatherList).sendToTarget();
        }
    }

    @Override
    public void onRefresh() {
        if (!HttpUtils.getSingleton().hasNetwork(mContext)) {
            HttpUtils.getSingleton().showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
            refreshLayout.setRefreshing(false);
            return;
        }
        presenter.getWeather(mContext, this);
    }
}
