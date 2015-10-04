package com.life.me.entity;

/**
 * Created by cuiyang on 15/10/1.
 */
public class MusicImgBean {


    /**
     * code : 1
     * data : {"singerPic":"http://pic.ttpod.cn/bb61f91e9721ab6d42cb24e5aadef025.jpg"}
     */

    private int code;
    private DataEntity data;
    /**
     * msg : PARAMS ERROR
     */

    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public class DataEntity {
        /**
         * singerPic : http://pic.ttpod.cn/bb61f91e9721ab6d42cb24e5aadef025.jpg
         */

        private String singerPic;

        public void setSingerPic(String singerPic) {
            this.singerPic = singerPic;
        }

        public String getSingerPic() {
            return singerPic;
        }
    }
}
