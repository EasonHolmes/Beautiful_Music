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


    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5608ae61");//初始化讯飞
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        LitePalApplication.initialize(Myapplication.this);//初始化litepal
        initJpush();
    }
    private void initJpush() {
        Set<String> pushTags = new HashSet<String>();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = DeviceInfo.getInstance(this).getToken();
        JPushInterface.setAliasAndTags(this, id, pushTags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    LogUtils.e(getClass().getName(), "ok successfuljpush==");
                }
            }
        });
    }
}
