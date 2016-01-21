package com.life.me.mutils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;


import com.life.me.entity.bmobentity.TokenTb;

import java.io.File;
import java.util.UUID;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by cuiyang on 15-4-28.
 */
public class DeviceInfo {

    private Context mContext;
    private static DeviceInfo info;

    private DeviceInfo(Context mContext) {
        this.mContext = mContext;
    }

    public static DeviceInfo getInstance(Context mContext) {
        if (info == null) info = new DeviceInfo(mContext);
        return info;
    }

    /**
     * 如果有token也就是打开过应用就使用
     * 如果没有就生成上传
     */
    public String getToken() {
        SharedPreferences share = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokens = share.getString("token", "");
        if (TextUtils.isEmpty(tokens)) {
            String device = getUniquePsuedoID();
            share.edit().putString("token", device).apply();
            TokenTb tb = new TokenTb();
            tb.setDeviceId(device);
            tb.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int i, String s) {
                    LogUtils.e(getClass().getName(), "iiii==_" + i + "ss" + s);
                }
            });
            return device;
        } else {
            return tokens;
        }
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        return m_szDevIDShort;
    }
}