package com.life.me.app;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        SpeechUtility.createUtility(Myapplication.this, SpeechConstant.APPID + "=5608ae61");//初始化讯飞
        LitePalApplication.initialize(Myapplication.this);//初始化litepal
    }
}
