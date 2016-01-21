package com.life.me.app;

import android.app.Application;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Created by cuiyang on 15/9/22.
 */
public class Myapplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(Myapplication.this, "03d70b2e98eee0a88cf31f0423409771");//初始化bmob
        LitePalApplication.initialize(Myapplication.this);//初始化litepal
    }
}
