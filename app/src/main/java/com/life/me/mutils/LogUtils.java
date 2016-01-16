package com.life.me.mutils;


import android.util.Log;

import com.life.me.entity.ConfigTb;

/**
 * Created by cuiyang on 16/1/15.
 */
public class LogUtils {

    public static void println(String msg) {
        if (ConfigTb.DEBUG) {
            Log.i("LogUtils", msg);
        }
    }

    public static void printStackTrace(Exception e) {
        if (ConfigTb.DEBUG) {
            e.printStackTrace();
        }
    }

    public static void printStackTrace(Error e) {
        if (ConfigTb.DEBUG) {
            e.printStackTrace();
        }
    }

    public static void v(String tag, String msg) {
        if (ConfigTb.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (ConfigTb.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (ConfigTb.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ConfigTb.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable thr) {
        if (ConfigTb.DEBUG) {
            Log.e(tag, msg, thr);
        }
    }
}
