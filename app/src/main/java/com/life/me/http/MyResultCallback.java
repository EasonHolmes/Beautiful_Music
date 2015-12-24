package com.life.me.http;

import android.util.Log;

import com.squareup.okhttp.Request;

/**
 * Created by cuiyang on 15/12/24.
 */
public abstract class MyResultCallback<T> extends OkHttpClientManager.ResultCallback<T> {

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
        Log.e(getClass().getName(), "loading...");
    }

    @Override
    public void onAfter() {
        super.onAfter();
        Log.e(getClass().getName(), "Sample-okHttp");
    }
}
