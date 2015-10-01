package com.life.me.mutils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * Created by cuiyang on 15/9/30.
 */
public class SingleGson {

    private static Gson Gsons;

    private SingleGson() {
        Gsons = new Gson();
    }

    public static synchronized Gson getRequestQueue() {
        if (Gsons == null) {
            new SingleGson();
        }
        return Gsons;
    }
}
