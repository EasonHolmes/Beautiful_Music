package com.life.me.entity.resultentity;

import java.util.List;

/**
 * Created by cuiyang on 15/12/23.
 */
public class Contains_keyWord_bean {
    /**
     * resId : 988277
     * resName : Are You Ready(最美和声)
     * resType : 5
     * resDesc : Are You Ready(最美和声)<br>林惠敏+杨丹(萧敬腾学员)<br>
     * parentId : 3
     * randomId : 0
     * moduleKey : null
     * moduleVersion : 0
     * actionList : null
     * listenCount : 277
     * faviorCount : 0
     * commentCount : 0
     * shareCount : 0
     * picture : null
     * resPic : null
     * iconUrl : null
     * flag : {"crFlag":1,"downFlag":0,"crListenFlag":1,"listenFlag":1,"priceFlag":0,"listenPriceFlag":0,"favoriteFlag":0,"zlListenFlag":0,"mvFlag":0,"hqFlag":0,"overdueFlag":0,"type":0,"hifiFlag":0,"surpassFlag":0,"sqFlag":0,"advertiseFlag":0}
     * resScore : 3
     * singer : 林惠敏+杨丹(萧敬腾学员)
     * album : null
     * playTime : 130
     * invalidate : null
     * contentId : 2742051
     * lyric : null
     * albumId : 0
     * singerId : 0
     * limit : 0
     * contentIdBlank : false
     */

    private List<ResListEntity> resList;


    public void setResList(List<ResListEntity> resList) {
        this.resList = resList;
    }

    public List<ResListEntity> getResList() {
        return resList;
    }

    public class ResListEntity {
        private int resId;
        private String resName;
        private int resType;
        private String resDesc;
        private int parentId;
        private int randomId;
        private Object moduleKey;
        private String moduleVersion;
        private Object actionList;
        private int listenCount;
        private int faviorCount;
        private int commentCount;
        private int shareCount;
        private Object picture;
        private Object resPic;
        private Object iconUrl;
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
        private int resScore;
        private String singer;
        private Object album;
        private int playTime;
        private Object invalidate;
        private int contentId;
        private Object lyric;
        private int albumId;
        private int singerId;
        private int limit;
        private boolean contentIdBlank;

        public void setResId(int resId) {
            this.resId = resId;
        }

        public void setResName(String resName) {
            this.resName = resName;
        }

        public void setResType(int resType) {
            this.resType = resType;
        }

        public void setResDesc(String resDesc) {
            this.resDesc = resDesc;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public void setRandomId(int randomId) {
            this.randomId = randomId;
        }

        public void setModuleKey(Object moduleKey) {
            this.moduleKey = moduleKey;
        }

        public void setModuleVersion(String moduleVersion) {
            this.moduleVersion = moduleVersion;
        }

        public void setActionList(Object actionList) {
            this.actionList = actionList;
        }

        public void setListenCount(int listenCount) {
            this.listenCount = listenCount;
        }

        public void setFaviorCount(int faviorCount) {
            this.faviorCount = faviorCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public void setPicture(Object picture) {
            this.picture = picture;
        }

        public void setResPic(Object resPic) {
            this.resPic = resPic;
        }

        public void setIconUrl(Object iconUrl) {
            this.iconUrl = iconUrl;
        }

        public void setFlag(FlagEntity flag) {
            this.flag = flag;
        }

        public void setResScore(int resScore) {
            this.resScore = resScore;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public void setAlbum(Object album) {
            this.album = album;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public void setInvalidate(Object invalidate) {
            this.invalidate = invalidate;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public void setLyric(Object lyric) {
            this.lyric = lyric;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public void setSingerId(int singerId) {
            this.singerId = singerId;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setContentIdBlank(boolean contentIdBlank) {
            this.contentIdBlank = contentIdBlank;
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

        public String getResDesc() {
            return resDesc;
        }

        public int getParentId() {
            return parentId;
        }

        public int getRandomId() {
            return randomId;
        }

        public Object getModuleKey() {
            return moduleKey;
        }

        public String getModuleVersion() {
            return moduleVersion;
        }

        public Object getActionList() {
            return actionList;
        }

        public int getListenCount() {
            return listenCount;
        }

        public int getFaviorCount() {
            return faviorCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public Object getPicture() {
            return picture;
        }

        public Object getResPic() {
            return resPic;
        }

        public Object getIconUrl() {
            return iconUrl;
        }

        public FlagEntity getFlag() {
            return flag;
        }

        public int getResScore() {
            return resScore;
        }

        public String getSinger() {
            return singer;
        }

        public Object getAlbum() {
            return album;
        }

        public int getPlayTime() {
            return playTime;
        }

        public Object getInvalidate() {
            return invalidate;
        }

        public int getContentId() {
            return contentId;
        }

        public Object getLyric() {
            return lyric;
        }

        public int getAlbumId() {
            return albumId;
        }

        public int getSingerId() {
            return singerId;
        }

        public int getLimit() {
            return limit;
        }

        public boolean isContentIdBlank() {
            return contentIdBlank;
        }

        public class FlagEntity {
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
    }
}
