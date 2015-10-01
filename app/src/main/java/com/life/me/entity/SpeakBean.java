package com.life.me.entity;

import java.util.List;

/**
 * Created by cuiyang on 15/9/30.
 */
public class SpeakBean {


    /**
     * sn : 1
     * ls : false
     * bg : 0
     * ed : 0
     * ws : [{"bg":0,"cw":[{"sc":-5,"w":"我"}]},{"bg":0,"cw":[{"sc":-50.13,"w":"想"}]},{"bg":0,"cw":[{"sc":-54.93,"w":"吃"}]},{"bg":0,"cw":[{"sc":-90.71,"w":"炸鸡"}]}]
     */

    private int sn;
    private boolean ls;
    private int bg;
    private int ed;
    private List<WsEntity> ws;

    public void setSn(int sn) {
        this.sn = sn;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public void setWs(List<WsEntity> ws) {
        this.ws = ws;
    }

    public int getSn() {
        return sn;
    }

    public boolean getLs() {
        return ls;
    }

    public int getBg() {
        return bg;
    }

    public int getEd() {
        return ed;
    }

    public List<WsEntity> getWs() {
        return ws;
    }

    public  class WsEntity {
        /**
         * bg : 0
         * cw : [{"sc":-5,"w":"我"}]
         */

        private int bg;
        private List<CwEntity> cw;

        public void setBg(int bg) {
            this.bg = bg;
        }

        public void setCw(List<CwEntity> cw) {
            this.cw = cw;
        }

        public int getBg() {
            return bg;
        }

        public List<CwEntity> getCw() {
            return cw;
        }

        public  class CwEntity {
            /**
             * sc : -5.0
             * w : 我
             */

            private double sc;
            private String w;

            public void setSc(double sc) {
                this.sc = sc;
            }

            public void setW(String w) {
                this.w = w;
            }

            public double getSc() {
                return sc;
            }

            public String getW() {
                return w;
            }
        }
    }
}
