package com.life.me.app;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.life.me.mutils.DeviceInfo;
import com.life.me.mutils.LogUtils;

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
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5608ae61");//初始化讯飞
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        LitePalApplication.initialize(Myapplication.this);//初始化litepal

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);            // 初始化 JPush
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = DeviceInfo.getInstance(this).getToken().substring(0, 13);
        JPushInterface.setAliasAndTags(this, id, pushTags,new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    LogUtils.e(getClass().getName(), "ok successfuljpush==");
                }
            }
        });

    }
}
