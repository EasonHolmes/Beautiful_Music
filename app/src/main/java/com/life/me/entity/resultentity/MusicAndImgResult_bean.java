package com.life.me.entity.resultentity;

import java.util.List;

/**
 * Created by cuiyang on 16/1/16.
 */
public class MusicAndImgResult_bean {


    /**
     * resCode : 0
     * resId : 1101741005
     * resName : 王妃
     * singger : 萧敬腾
     * album : 王妃
     * fullListUrl : http://res2.imuapp.cn/resource/duomi/128/resource/6/077/9dbc5101e5733d3d4d1e49246b936077.mp3?sid=18930513825&serviceid=100&nettype=wifi&resid=1101741005&Mode=online&softid=250572082
     * pics : [{"picUrl":"http://4galbum.ctmus.cn/scale/album/4/ca/big_ma_74ca_100001029287.jpg?param=500y500"}]
     * resInfo : 成功
     */

    private String resCode;
    private int resId;
    private String resName;
    private String singger;
    private String album;
    private String fullListUrl;
    private String resInfo;

    /**
     * picUrl : http://4galbum.ctmus.cn/scale/album/4/ca/big_ma_74ca_100001029287.jpg?param=500y500
     */

    private List<PicsEntity> pics;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getSingger() {
        return singger;
    }

    public void setSingger(String singger) {
        this.singger = singger;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFullListUrl() {
        return fullListUrl;
    }

    public void setFullListUrl(String fullListUrl) {
        this.fullListUrl = fullListUrl;
    }

    public String getResInfo() {
        return resInfo;
    }

    public void setResInfo(String resInfo) {
        this.resInfo = resInfo;
    }

    public List<PicsEntity> getPics() {
        return pics;
    }

    public void setPics(List<PicsEntity> pics) {
        this.pics = pics;
    }

    public class PicsEntity {
        private String picUrl;

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }
    }
}
