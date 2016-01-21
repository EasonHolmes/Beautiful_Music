package com.life.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.life.me.dao.ApiDao;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.resultentity.SearchWeather_Bean;
import com.life.me.http.ApiClient;
import com.life.me.mutils.Commutils;
import com.life.me.mutils.LogUtils;
import com.life.me.mutils.Utils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.presenter.iml.Main_presenter;
import com.life.me.view.MainView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Main_presenter implements MainView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private Context mContext;
    private final int UPDATE_HEADER_WEATHCER = 0;
    private final int GET_FORM_GALLERY = 1;
    private final int REFRESH = 2;
    private Subscription subscription;
    private boolean isOne = true;

    private Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_HEADER_WEATHCER:
                    refreshLayout.setRefreshing(false);
                    updateUI((List<WeatherDao>) msg.obj);
                    break;
                case REFRESH:
                    refreshLayout.setRefreshing(false);
                    break;
            }
            return false;
        }
    });

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        mContext = this;
        setToolbar(mToolBar);
        setPushContent(getIntent().getStringExtra(getResources().getString(R.string.push_content)));//推送传过来的内容
        setOnclickListener(drawerView, imgMusic);//设置一个空的键听要不侧边栏会透过去影响后面的手势

        //初始化定位
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationClient.setLocationListener(new MapLocationListener());
        locationClient.setLocationOption(setLocationOption());
        //初始化定位

        refreshLayout.setColorSchemeResources(R.color.toolbar_background2);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
        this.onRefresh();
    }


    @Override
    public Toolbar setToolbar(Toolbar toolbar) {
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//这个直接连半透明都没有了
        }
//        mToolBar.setPadding(0, ScreenUtils.dip2px(mContext, 26), 0, 0);
        mToolBar.setTitle(getResources().getString(R.string.main_act_title));
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.mDrawerlayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolBar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerlayout.setDrawerListener(mDrawerToggle);
        return mToolBar;
    }

    @Override
    public void getWeatherResult(String Longitude, String Latitude) {
        if (TextUtils.isEmpty(Longitude) || TextUtils.isEmpty(Longitude)) {
            Widget_Utils.showSnackbar(linearLayout, getResources().getString(R.string.not_have_location));
            return;
        }
        Action1<SearchWeather_Bean> action1 = searchWeatherBean -> saveDataAndUpdateUi((SearchWeather_Bean) searchWeatherBean);
        Action1<Throwable> onError = error -> Widget_Utils.setSnackbarClicklistener(linearLayout, "哎哟喂,没有拿到数据", "再来一次", v -> onRefresh()).show();
        subscription = ApiClient.SERVICE_rx.getWeather(ConfigTb.getWeatherResult + Longitude + "," + Latitude + ConfigTb.baidu_mcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, onError);
    }

    @Override
    public void setDrawerOpen(DrawerLayout drawerOpen) {

    }

    @Override
    public void setBackground(String Localpath) {
        if (Utils.hasFile())
            Picasso.with(MainActivity.this)
                    .load(new File(Localpath))
                    .placeholder(R.mipmap.weath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)//不使用内存缓存否则还会被认为是同一张不更新图片
                    .into(mainBackground);
    }

    @Override
    public void saveDataAndUpdateUi(SearchWeather_Bean searchWeatherBean) {
        if (getWeatherFilter(searchWeatherBean))
            ApiDao.saveWeather(searchWeatherBean);
        hand.postDelayed(() -> hand.obtainMessage(UPDATE_HEADER_WEATHCER, getLocalWeather())
                .sendToTarget(), 500);
    }

    private List<WeatherDao> getLocalWeather() {
        return DataSupport.order("id asc")
                .find(WeatherDao.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setPushContent(String drawerContent) {
        SharedPreferences share = MainActivity.this.getSharedPreferences(getResources().getString(R.string.drawer_content_file), MODE_PRIVATE);
        if (drawerContent != null) {//是否有推送内容
            share.edit().putString(getResources().getString(R.string.push_content), drawerContent).apply();
            honeyTxt.setText(drawerContent + "");
        } else {
            honeyTxt.setText(share.getString(getResources().getString(R.string.push_content), "I love you my Lover"));
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
    public void onRefresh() {
        getRefreshRresult();
    }

    private void getRefreshRresult() {
        if (!httpUtils.hasNetwork(mContext)) {
            Widget_Utils.showSnackbar(linearLayout, "亲爱的,要打开你的网络哦.么么哒");
            List<WeatherDao> bean = getLocalWeather();
            hand.postDelayed(() -> hand.obtainMessage(UPDATE_HEADER_WEATHCER, bean != null ? bean : new ArrayList<WeatherDao>())
                    .sendToTarget(), 500);
        } else {
            //开启地图定位 MapLocationListener回调方法上传位置和查询天气
            locationClient.startLocation();
        }
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
            boolean isSave = Utils.fileChannelCopy(new File(new Commutils().getPath(mContext, data.getData())), new File(ConfigTb.Photo_Path));
            if (isSave) {
                setBackground(ConfigTb.Photo_Path);
            } else {
                Widget_Utils.showSnackbar(linearLayout, "设置失败");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());//获取PID
//            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
        locationClient.onDestroy();
        locationClient = null;
        locationOption = null;
    }

    public class MapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (isOne) {
                        uploadLocation(amapLocation.getAddress());
                        isOne = false;
                    }
                    getWeatherResult(amapLocation.getLongitude() + "", amapLocation.getLatitude() + "");

                    //定位成功回调信息，设置相关消息
//                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                    amapLocation.getAccuracy();//获取精度信息
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = new Date(amapLocation.getTime());
//                    df.format(date);//定位时间
//                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    amapLocation.getCountry();//国家信息
//                    amapLocation.getProvince();//省信息
//                    amapLocation.getCity();//城市信息
//                    amapLocation.getDistrict();//城区信息
//                    amapLocation.getStreet();//街道信息
//                    amapLocation.getStreetNum();//街道门牌号信息
//                    amapLocation.getCityCode();//城市编码
//                    amapLocation.getAdCode();//地区编码


                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    LogUtils.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    }
}
