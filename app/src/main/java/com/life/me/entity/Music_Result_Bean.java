package com.life.me.entity;

import java.util.List;

/**
 * Created by cuiyang on 15/9/30.
 */
public class Music_Result_Bean {


    /**
     * resCode : 0
     * parentPath : 400-5_988269
     * resId : 988269
     * resName : 萧敬腾女汉子逆袭神曲(最美和声)
     * resType : 5
     * describe : 萧敬腾女汉子逆袭神曲(最美和声)<br>林惠敏(萧敬腾学员)<br>
     * flag : {"crFlag":1,"downFlag":0,"crListenFlag":1,"listenFlag":1,"priceFlag":0,"listenPriceFlag":0,"favoriteFlag":0,"zlListenFlag":0,"mvFlag":0,"hqFlag":0,"overdueFlag":0,"type":0,"hifiFlag":0,"surpassFlag":0,"sqFlag":0,"advertiseFlag":0}
     * crPrice : 2元
     * dlPrice : 0元
     * lyric : null
     * ringNum : 0
     * rings : null
     * singger : 林惠敏(萧敬腾学员)
     * songer : 萧敬腾女汉子逆袭神曲(最美和声)
     * album : null
     * crUrl : null
     * crPlayTime : 0
     * fullListUrl : http://dl.118100.cn:9495/res/1253/mp3/00/12/02/1253001202030800.mp3?type=cl&filesize=2157505&fileId=29534521&sid=00000000000&serviceid=100&nettype=wifi&resid=988269&Mode=online&softid=249253307
     * fullListenBit : 128
     * fullListenPlayTime : 131
     * fullDownloadUrl : null
     * fullDownloadBit : 0
     * fullDownloadPlayTime : 0
     * dingNum : 0
     * caiNum : 0
     * picNum : 1
     * limitTime : 0
     * finishTime : 0
     * pics : [{"picUrl":"http://dl.118100.cn/data/images/20120313/538337/6635091.png"}]
     * playInfoUrl :
     * messages : null
     * mvFlag : 0
     * memberPrice : 10
     * popupMode : 0
     * bigPic :
     * contentId : 2742053
     * listenHq : 0
     * isSubscribeHQ : 0
     * singerId : 0
     * singerPic : null
     * albumId : 0
     * albumPic : null
     * resInfo : 成功
     */

    private String resCode;
    private String parentPath;
    private int resId;
    private String resName;
    private int resType;
    private String describe;
    /**
     * crFlag : 1
     * downFlag : 0
     * crListenFlag : 1
     * listenFlag : 1
     * priceFlag : 0
     * listenPriceFlag : 0
     * favoriteFlag : 0
     * zlListenFlag : 0
     * mvFlag : 0
     * hqFlag : 0
     * overdueFlag : 0
     * type : 0
     * hifiFlag : 0
     * surpassFlag : 0
     * sqFlag : 0
     * advertiseFlag : 0
     */

    private FlagEntity flag;
    private String crPrice;
    private String dlPrice;
    private Object lyric;
    private int ringNum;
    private Object rings;
    private String singger;
    private String songer;
    private Object album;
    private Object crUrl;
    private int crPlayTime;
    private String fullListUrl;
    private int fullListenBit;
    private int fullListenPlayTime;
    private Object fullDownloadUrl;
    private int fullDownloadBit;
    private int fullDownloadPlayTime;
    private int dingNum;
    private int caiNum;
    private int picNum;
    private int limitTime;
    private int finishTime;
    private String playInfoUrl;
    private Object messages;
    private int mvFlag;
    private int memberPrice;
    private int popupMode;
    private String bigPic;
    private int contentId;
    private int listenHq;
    private int isSubscribeHQ;
    private int singerId;
    private Object singerPic;
    private int albumId;
    private Object albumPic;
    private String resInfo;
    /**
     * picUrl : http://dl.118100.cn/data/images/20120313/538337/6635091.png
     */

    private List<PicsEntity> pics;

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setFlag(FlagEntity flag) {
        this.flag = flag;
    }

    public void setCrPrice(String crPrice) {
        this.crPrice = crPrice;
    }

    public void setDlPrice(String dlPrice) {
        this.dlPrice = dlPrice;
    }

    public void setLyric(Object lyric) {
        this.lyric = lyric;
    }

    public void setRingNum(int ringNum) {
        this.ringNum = ringNum;
    }

    public void setRings(Object rings) {
        this.rings = rings;
    }

    public void setSingger(String singger) {
        this.singger = singger;
    }

    public void setSonger(String songer) {
        this.songer = songer;
    }

    public void setAlbum(Object album) {
        this.album = album;
    }

    public void setCrUrl(Object crUrl) {
        this.crUrl = crUrl;
    }

    public void setCrPlayTime(int crPlayTime) {
        this.crPlayTime = crPlayTime;
    }

    public void setFullListUrl(String fullListUrl) {
        this.fullListUrl = fullListUrl;
    }

    public void setFullListenBit(int fullListenBit) {
        this.fullListenBit = fullListenBit;
    }

    public void setFullListenPlayTime(int fullListenPlayTime) {
        this.fullListenPlayTime = fullListenPlayTime;
    }

    public void setFullDownloadUrl(Object fullDownloadUrl) {
        this.fullDownloadUrl = fullDownloadUrl;
    }

    public void setFullDownloadBit(int fullDownloadBit) {
        this.fullDownloadBit = fullDownloadBit;
    }

    public void setFullDownloadPlayTime(int fullDownloadPlayTime) {
        this.fullDownloadPlayTime = fullDownloadPlayTime;
    }

    public void setDingNum(int dingNum) {
        this.dingNum = dingNum;
    }

    public void setCaiNum(int caiNum) {
        this.caiNum = caiNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public void setPlayInfoUrl(String playInfoUrl) {
        this.playInfoUrl = playInfoUrl;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }

    public void setMvFlag(int mvFlag) {
        this.mvFlag = mvFlag;
    }

    public void setMemberPrice(int memberPrice) {
        this.memberPrice = memberPrice;
    }

    public void setPopupMode(int popupMode) {
        this.popupMode = popupMode;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setListenHq(int listenHq) {
        this.listenHq = listenHq;
    }

    public void setIsSubscribeHQ(int isSubscribeHQ) {
        this.isSubscribeHQ = isSubscribeHQ;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public void setSingerPic(Object singerPic) {
        this.singerPic = singerPic;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setAlbumPic(Object albumPic) {
        this.albumPic = albumPic;
    }

    public void setResInfo(String resInfo) {
        this.resInfo = resInfo;
    }

    public void setPics(List<PicsEntity> pics) {
        this.pics = pics;
    }

    public String getResCode() {
        return resCode;
    }

    public String getParentPath() {
        return parentPath;
    }

    public int getResId() {
        return resId;
    }

    public String getResName() {
        return resName;
    }

    public int getResType() {
        return resType;
    }

    public String getDescribe() {
        return describe;
    }

    public FlagEntity getFlag() {
        return flag;
    }

    public String getCrPrice() {
        return crPrice;
    }

    public String getDlPrice() {
        return dlPrice;
    }

    public Object getLyric() {
        return lyric;
    }

    public int getRingNum() {
        return ringNum;
    }

    public Object getRings() {
        return rings;
    }

    public String getSingger() {
        return singger;
    }

    public String getSonger() {
        return songer;
    }

    public Object getAlbum() {
        return album;
    }

    public Object getCrUrl() {
        return crUrl;
    }

    public int getCrPlayTime() {
        return crPlayTime;
    }

    public String getFullListUrl() {
        return fullListUrl;
    }

    public int getFullListenBit() {
        return fullListenBit;
    }

    public int getFullListenPlayTime() {
        return fullListenPlayTime;
    }

    public Object getFullDownloadUrl() {
        return fullDownloadUrl;
    }

    public int getFullDownloadBit() {
        return fullDownloadBit;
    }

    public int getFullDownloadPlayTime() {
        return fullDownloadPlayTime;
    }

    public int getDingNum() {
        return dingNum;
    }

    public int getCaiNum() {
        return caiNum;
    }

    public int getPicNum() {
        return picNum;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public String getPlayInfoUrl() {
        return playInfoUrl;
    }

    public Object getMessages() {
        return messages;
    }

    public int getMvFlag() {
        return mvFlag;
    }

    public int getMemberPrice() {
        return memberPrice;
    }

    public int getPopupMode() {
        return popupMode;
    }

    public String getBigPic() {
        return bigPic;
    }

    public int getContentId() {
        return contentId;
    }

    public int getListenHq() {
        return listenHq;
    }

    public int getIsSubscribeHQ() {
        return isSubscribeHQ;
    }

    public int getSingerId() {
        return singerId;
    }

    public Object getSingerPic() {
        return singerPic;
    }

    public int getAlbumId() {
        return albumId;
    }

    public Object getAlbumPic() {
        return albumPic;
    }

    public String getResInfo() {
        return resInfo;
    }

    public List<PicsEntity> getPics() {
        return pics;
    }

    public static class FlagEntity {
        private int crFlag;
        private int downFlag;
        private int crListenFlag;
        private int listenFlag;
        private int priceFlag;
        private int listenPriceFlag;
        private int favoriteFlag;
        private int zlListenFlag;
        private int mvFlag;
        private int hqFlag;
        private int overdueFlag;
        private int type;
        private int hifiFlag;
        private int surpassFlag;
        private int sqFlag;
        private int advertiseFlag;

        public void setCrFlag(int crFlag) {
            this.crFlag = crFlag;
        }

        public void setDownFlag(int downFlag) {
            this.downFlag = downFlag;
        }

        public void setCrListenFlag(int crListenFlag) {
            this.crListenFlag = crListenFlag;
        }

        public void setListenFlag(int listenFlag) {
            this.listenFlag = listenFlag;
        }

        public void setPriceFlag(int priceFlag) {
            this.priceFlag = priceFlag;
        }

        public void setListenPriceFlag(int listenPriceFlag) {
            this.listenPriceFlag = listenPriceFlag;
        }

        public void setFavoriteFlag(int favoriteFlag) {
            this.favoriteFlag = favoriteFlag;
        }

        public void setZlListenFlag(int zlListenFlag) {
            this.zlListenFlag = zlListenFlag;
        }

        public void setMvFlag(int mvFlag) {
            this.mvFlag = mvFlag;
        }

        public void setHqFlag(int hqFlag) {
            this.hqFlag = hqFlag;
        }

        public void setOverdueFlag(int overdueFlag) {
            this.overdueFlag = overdueFlag;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setHifiFlag(int hifiFlag) {
            this.hifiFlag = hifiFlag;
        }

        public void setSurpassFlag(int surpassFlag) {
            this.surpassFlag = surpassFlag;
        }

        public void setSqFlag(int sqFlag) {
            this.sqFlag = sqFlag;
        }

        public void setAdvertiseFlag(int advertiseFlag) {
            this.advertiseFlag = advertiseFlag;
        }

        public int getCrFlag() {
            return crFlag;
        }

        public int getDownFlag() {
            return downFlag;
        }

        public int getCrListenFlag() {
            return crListenFlag;
        }

        public int getListenFlag() {
            return listenFlag;
        }

        public int getPriceFlag() {
            return priceFlag;
        }

        public int getListenPriceFlag() {
            return listenPriceFlag;
        }

        public int getFavoriteFlag() {
            return favoriteFlag;
        }

        public int getZlListenFlag() {
            return zlListenFlag;
        }

        public int getMvFlag() {
            return mvFlag;
        }

        public int getHqFlag() {
            return hqFlag;
        }

        public int getOverdueFlag() {
            return overdueFlag;
        }

        public int getType() {
            return type;
        }

        public int getHifiFlag() {
            return hifiFlag;
        }

        public int getSurpassFlag() {
            return surpassFlag;
        }

        public int getSqFlag() {
            return sqFlag;
        }

        public int getAdvertiseFlag() {
            return advertiseFlag;
        }
    }

    public static class PicsEntity {
        private String picUrl;

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }
    }
}
