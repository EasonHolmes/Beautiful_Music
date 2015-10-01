package com.life.me.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.life.me.Myapplication;
import com.life.me.entity.CacheBean;
import com.life.me.entity.bmobentity.LocationTb;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.mutils.DeviceInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by cuiyang on 15/9/22.
 */
public class Wel_presenter {
    /**
     * 如果有token也就是打开过应用就使用
     * 如果没有就生成上传
     */
    public String getToken(Context mContext) {
        SharedPreferences share = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokens = share.getString("token", "");
        if (tokens.equals("") || tokens.length() < 1) {
            String tem = DeviceInfo.getInstance(mContext).makeDeviceIdForToken();
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

    /**
     * 上传地理位置
     */
    public void upLocation(Myapplication myapplication) {
        BmobQuery<TokenTb> query = new BmobQuery<TokenTb>();
        query.addWhereEqualTo("deviceId", getToken(myapplication.getApplicationContext()));
        query.findObjects(myapplication.getApplicationContext(), new FindListener<TokenTb>() {
            @Override
            public void onSuccess(List<TokenTb> object) {
                LocationTb location = new LocationTb();
                location.setLocName(CacheBean.addr);
                if (object != null && object.size() > 0) {
                    location.setDeviceId((TokenTb) object.get(0));
                }
                location.save(myapplication.getApplicationContext(), null);
            }
            @Override
            public void onError(int code, String msg) {
            }
        });
    }
}
