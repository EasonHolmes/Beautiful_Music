package com.life.me.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.mutils.DeviceInfo;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        SpeechUtility.createUtility(Myapplication.this, SpeechConstant.APPID + "=5608ae61");//初始化讯飞
        LitePalApplication.initialize(Myapplication.this);//初始化litepal
//        initJpush();//初始化极光
    }


    private void initJpush() {
        JPushInterface.init(this);            // 初始化 JPush
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = getToken(Myapplication.this).substring(0, 13);
        JPushInterface.setAliasAndTags(this, id, pushTags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    Log.e(getClass().getName(), "ok successful==");
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

}
