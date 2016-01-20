package com.life.me.mutils;

import android.Manifest;
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
            String tem = makeDeviceIdForToken();
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

    public String makeDeviceIdForToken() {
        String handSetInfo =
                "手机型号:" + android.os.Build.MODEL +
                        "系统版本:" + android.os.Build.VERSION.RELEASE + getMacInfo(false);
        return handSetInfo;


//        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();//cdma SIM卡唯一编号
//        //第一开机时才会有
//        androidId = "" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String uniqueId = deviceUuid.toString();
//        if (tmDevice.length() > 1) {
//            return "DeviceId:" + tmDevice + "-1";
//        }
//        if (tmSerial.length() > 1) {
//            return "DeviceId:" + tmSerial + "-2";
//        }
//        if (androidId.length() > 1) {
//            return "DeviceId:" + androidId + "-3";
//        }
//        if (uniqueId.length() > 1) {
//            return "DeviceId:" + uniqueId + "-4";
//        }
//        return null;
    }

    //获取手机wifi mac信息
    public String getMacInfo(boolean isOnly) {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        if (isOnly) {
            return ssid;
        }

        return "mac:" + maxText
                + "ip:" + ipText
                + "ssid :" + ssid
                ;
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }
}