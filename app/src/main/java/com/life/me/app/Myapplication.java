package com.life.me.app;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

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
    }
}
