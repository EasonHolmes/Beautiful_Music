package com.life.me.mutils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * ScreenUtils
 * Created by hanj on 14-9-25.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;

    public static int getScreenW(Activity mActivity){
        if (screenW == 0){
            initScreen(mActivity);
        }
        return screenW;
    }

    public static int getScreenH(Activity mActivity){
        if (screenH == 0){
            initScreen(mActivity);
        }
        return screenH;
    }

    private static void initScreen(Activity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
