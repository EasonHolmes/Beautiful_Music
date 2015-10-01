package com.life.me;

import android.app.Application;
import android.app.Service;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.v4.util.LruCache;
import android.text.Annotation;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.github.mmin18.layoutcast.LayoutCast;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.life.me.entity.CacheBean;
import com.life.me.mutils.DeviceInfo;
import com.life.me.mutils.SingleRequestQueue;
import com.life.me.presenter.Wel_presenter;

import org.litepal.LitePalApplication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by cuiyang on 15/9/22.
 */
public class Myapplication extends Application {
    private Set<String> pushTags = new HashSet<String>();//tag相当于分组。可以把某一些用户设置同样的tag就能某一组发了,一般在程序里动态获取服务器某些信息后添加


    //baiduLocation servers
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public Vibrator mVibrator;
    private Wel_presenter presenter;

    public static ImageLoader imageLoader;
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
        presenter = new Wel_presenter();
        Bmob.initialize(this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5608ae61");
        LitePalApplication.initialize(this);//初始化litepal
        initJpush();//初始化极光
        initBaidu();//初始化百度并上传位置
        initImageLoader();

    }
    private void initImageLoader(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize);
        imageLoader = new ImageLoader(SingleRequestQueue.getRequestQueue(this), new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }
            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                lruCache.put(s, bitmap);
            }
        });
    }

    private void initBaidu() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    private void initJpush() {
        JPushInterface.init(this);            // 初始化 JPush
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = presenter.getToken(Myapplication.this);
        JPushInterface.setAliasAndTags(this, id, pushTags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    //Log.e(TAG,"ok successful==");
                }
            }
        });
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            CacheBean.addr = location.getAddrStr();
            CacheBean.Latitude = String.valueOf(location.getLatitude());
            CacheBean.Longitude = String.valueOf(location.getLongitude());
            List<Poi> list1 = location.getPoiList();// POI信息
            if (list1 != null) {
                CacheBean.addr = CacheBean.addr + list1.get(0).getName();
            }
            Log.e(getClass().getName(), "location====" + CacheBean.addr + "Latitude====" +
                    CacheBean.Latitude + "Longitude====" + CacheBean.Longitude);
            mLocationClient.stop();
            /**
             * 上传位置
             * */
            presenter.upLocation(Myapplication.this);
        }
    }

}
