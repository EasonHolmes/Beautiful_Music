package com.life.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.ConfigTb;
import com.life.me.model.Main_Model;
import com.life.me.mutils.Commutils;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.Utils;
import com.life.me.presenter.Main_presenter;
import com.life.me.view.ProgressWheel;
import com.life.me.view.SystemBarTintManager;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

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
    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;
    @InjectView(R.id.img_music)
    ImageView imgMusic;
    @InjectView(R.id.main_background)
    ImageView mainBackground;


    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext;
    private Main_presenter presenter;
    private final int UPDATE_IMG = 0;
    private final int GET_FORM_GALLERY = 1;


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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mContext = this;

        initToolbar(getResources().getString(R.string.main_act_title), false);
        new Shimmer().setDuration(1500).start(honeyTxt);//侧边栏的字

        String drawerContent = getIntent().getStringExtra(getResources().getString(R.string.push_content));//拿推送传过来的内容
        SharedPreferences share = mContext.getSharedPreferences(getResources().getString(R.string.drawer_content_file), MODE_PRIVATE);
        if (drawerContent != null) {//如果是推送进来的就保存内容并显示否则拿之前保存的内容
            share.edit().putString(getResources().getString(R.string.push_content), drawerContent).apply();
            honeyTxt.setText(drawerContent + "");
        } else {
            honeyTxt.setText(share.getString(getResources().getString(R.string.push_content), "I love you my Lover"));
        }

        presenter = new Main_presenter();//初始化控制器
        initView();
        setBackground();

    }

    private void initView() {
        drawerView.setOnClickListener(this);//设置一个空的键听要不侧边栏会透过去影响后面的手势
        imgMusic.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(R.color.toolbar_background2);
        refreshLayout.setOnRefreshListener(this);
        if (!HttpUtils.getSingleton().hasNetwork(mContext)) {
            hand.obtainMessage(UPDATE_IMG, DataSupport.order("id asc").find(WeatherDao.class)).sendToTarget();
        } else {
            presenter.getWeather(mContext, this);
        }
    }

    private void setBackground() {
        //有文件再去加载
        if (Utils.hasFile()) {
            mainBackground.setImageBitmap(BitmapFactory.decodeFile(ConfigTb.PhotoName));
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
                startActivity(new Intent(mContext, Music_Activity.class));
                overridePendingTransition(R.anim.small_2_big, R.anim.fade_out);
                break;
        }
    }

    @Override
    public void weatherCallback(List<WeatherDao> weatherList) {
        if (weatherList != null) {
            hand.obtainMessage(UPDATE_IMG, weatherList).sendToTarget();
        } else {
            refreshLayout.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(mContext, Setting_Activity.class));
                break;
            case R.id.background:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_FORM_GALLERY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FORM_GALLERY && data != null) {
            //复制存入图片
            Utils.fileChannelCopy(new File(new Commutils().getPath(mContext, data.getData())), new File(ConfigTb.PhotoName));
            setBackground();
        } else {
            Snackbar.make(linearLayout, "选择图片失败", Snackbar.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(MainActivity.this);//是做用户统计的
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(MainActivity.this);//是做用户统计的
    }

}
