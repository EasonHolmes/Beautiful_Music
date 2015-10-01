package com.life.me.entity;

import com.life.me.dao.WeatherDao;

import java.util.List;

/**
 * Created by cuiyang on 15/9/24.
 */
public class SearchWeatherBean {

    /**
     * error : 0
     * status : success
     * date : 2015-09-24
     * results : [{"currentCity":"北京","pm25":"141","index":[{"title":"穿衣","zs":"舒适","tipt":"穿衣指数","des":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},{"title":"洗车","zs":"不宜","tipt":"洗车指数","des":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},{"title":"旅游","zs":"适宜","tipt":"旅游指数","des":"天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},{"title":"感冒","zs":"少发","tipt":"感冒指数","des":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"},{"title":"运动","zs":"较适宜","tipt":"运动指数","des":"阴天，较适宜进行各种户内外运动。"},{"title":"紫外线强度","zs":"最弱","tipt":"紫外线强度指数","des":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}],"weather_data":[{"date":"周四 09月24日 (实时：21℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/leizhenyu.png","weather":"阴转雷阵雨","wind":"微风","temperature":"24 ~ 16℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"微风","temperature":"25 ~ 13℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"25 ~ 15℃"},{"date":"周日","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"多云转阴","wind":"微风","temperature":"26 ~ 16℃"}]}]
     */

    private int error;
    private String status;
    private String date;
    private List<ResultsEntity> results;

    public void setError(int error) {
        this.error = error;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public  class ResultsEntity {
        /**
         * currentCity : 北京
         * pm25 : 141
         * index : [{"title":"穿衣","zs":"舒适","tipt":"穿衣指数","des":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},{"title":"洗车","zs":"不宜","tipt":"洗车指数","des":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},{"title":"旅游","zs":"适宜","tipt":"旅游指数","des":"天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},{"title":"感冒","zs":"少发","tipt":"感冒指数","des":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"},{"title":"运动","zs":"较适宜","tipt":"运动指数","des":"阴天，较适宜进行各种户内外运动。"},{"title":"紫外线强度","zs":"最弱","tipt":"紫外线强度指数","des":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}]
         * weather_data : [{"date":"周四 09月24日 (实时：21℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/leizhenyu.png","weather":"阴转雷阵雨","wind":"微风","temperature":"24 ~ 16℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/qing.png","weather":"晴","wind":"微风","temperature":"25 ~ 13℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/qing.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/duoyun.png","weather":"晴转多云","wind":"微风","temperature":"25 ~ 15℃"},{"date":"周日","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/duoyun.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"多云转阴","wind":"微风","temperature":"26 ~ 16℃"}]
         */
        private String currentCity;
        private String pm25;
                private List<IndexEntity> index;
        private List<WeatherDao> weather_data;

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public void setIndex(List<IndexEntity> index) {
            this.index = index;
        }

        public void setWeather_data(List<WeatherDao> weather_data) {
            this.weather_data = weather_data;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public String getPm25() {
            return pm25;
        }

        public List<IndexEntity> getIndex() {
            return index;
        }

        public List<WeatherDao> getWeather_data() {
            return weather_data;
        }

        public class IndexEntity {
            /**
             * title : 穿衣
             * zs : 舒适
             * tipt : 穿衣指数
             * des : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
             */

            private String title;
            private String zs;
            private String tipt;
            private String des;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setZs(String zs) {
                this.zs = zs;
            }

            public void setTipt(String tipt) {
                this.tipt = tipt;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getTitle() {
                return title;
            }

            public String getZs() {
                return zs;
            }

            public String getTipt() {
                return tipt;
            }

            public String getDes() {
                return des;
            }
        }
    }
}
