package com.life.me.http;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by cuiyang on 15/12/22.
 */
public class OkHttpClientSingle {


    private static OkHttpClient ok;

    private OkHttpClientSingle() {
        ok = new OkHttpClient();
        ok.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                Log.e(getClass().getName(), "response===" + response.body().string());
                //Response{protocol=http/1.1, code=200, message=OK, url=http://www.baidu.com/search/error.html}
//                Log.e(getClass().getName(), "response===" + response.toString());
                return response;
            }
        });
    }

    public static synchronized OkHttpClient getHttpClientSingle() {
        if (ok == null) {
            new OkHttpClientSingle();
        }
        return ok;
    }

}
