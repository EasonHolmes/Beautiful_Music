package com.life.me.mutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


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

    public String makeDeviceIdForToken() {
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();//cdma SIM卡唯一编号
        //第一开机时才会有
        androidId = "" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        if (tmDevice.length() > 1) {
            return "DeviceId:" + tmDevice + "-1";
        }
        if (tmSerial.length() > 1) {
            return "DeviceId:" + tmSerial + "-2";
        }
        if (androidId.length() > 1) {
            return "DeviceId:" + androidId + "-3";
        }
        if (uniqueId.length() > 1) {
            return "DeviceId:" + uniqueId + "-4";
        }
        return null;
    }
}