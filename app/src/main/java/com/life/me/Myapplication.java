package com.life.me;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.LocationClient;
import com.github.mmin18.layoutcast.LayoutCast;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.life.me.mutils.SingleRequestQueue;
import com.life.me.presenter.Main_presenter;

import org.litepal.LitePalApplication;

import java.util.HashSet;
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
    //    public MyLocationListener mMyLocationListener;
    public Vibrator mVibrator;
    private Main_presenter presenter;

    public static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
        presenter = new Main_presenter();
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        SpeechUtility.createUtility(Myapplication.this, SpeechConstant.APPID + "=5608ae61");//初始化讯飞
        LitePalApplication.initialize(Myapplication.this);//初始化litepal
        initJpush();//初始化极光
        initImageLoader();
    }

    private void initImageLoader() {
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

    private void initJpush() {
        JPushInterface.init(this);            // 初始化 JPush
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = presenter.getToken(Myapplication.this).substring(0, 13);
        JPushInterface.setAliasAndTags(this, id, pushTags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    Log.e(getClass().getName(), "ok successful==");
                }
            }
        });
    }
}
