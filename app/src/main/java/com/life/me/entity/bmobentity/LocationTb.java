package com.life.me.entity.bmobentity;

import cn.bmob.v3.BmobObject;

/**
 * Created by cuiyang on 15/9/22.
 */
public class LocationTb extends BmobObject{
    private String locName;
    private TokenTb deviceId;


    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public TokenTb getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(TokenTb deviceId) {
        this.deviceId = deviceId;
    }
}
