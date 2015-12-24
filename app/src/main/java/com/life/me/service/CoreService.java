//package com.life.me.service;//package com.cui.library.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//
//import java.io.File;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by cuiyang on 15/5/14.
// */
//public class CoreService extends Service {
//    private int TimerNumber = 10;
//
//    @Override
//    public void onCreate() {
//        Timer timer = new Timer();
//        sendTimer sendTimer = new sendTimer();
//        timer.schedule(sendTimer, 1000 * 5, 1000 * 5);//处理计时器的方法,延迟多久开始执行,多久重复一遍  每5秒检测一次
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {//onStartCommand==Onstart方法
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public void onRebind(Intent intent) {
//        super.onRebind(intent);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new MyBinder();
//    }
//
//    // 要实现Activity与服务间的通信，需要创建一个Binder的子类，然后在onBind方法中返回MyBinder的实例
//    class MyBinder extends Binder {
//        public void test(String str) {
//            System.out.println("Str=====" + str);
//        }
//    }
//
//    class sendTimer extends TimerTask {
//
//        @Override
//        public void run() {
//
//        }
//    }
//}
