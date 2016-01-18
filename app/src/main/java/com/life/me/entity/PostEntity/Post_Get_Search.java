package com.life.me.entity.postentity;

/**
 * Created by cuiyang on 15/12/24.
 */
public class Post_Get_Search {


    /**
     * sid : 249253307
     * networkType : wifi
     * format : json
     * type : 1
     * protocolCode : 2.0
     * pageNum : 1
     * timestamp : 20151224113330
     * sign : f1d8975deb297ecd49fb571ae69d745f
     * sessionId : A1358D18F39DD03ACAF270EC6EFB783E
     * appChannelCode : 10000071
     * responseType : Ring
     * appVerCode : V7.002.002.696ctch1
     * maxRows : 20
     * method : search
     * imsi : 204043431728399
     * parentPath : 400
     * key : 林惠敏(萧敬腾学员)
     */

    private int sid = 249253307;
    private String networkType = "wifi";
    private String format = "json";
    private int type = 1;
    private String protocolCode = "2.0";
    private int pageNum = 1;
    private String timestamp = "20151224113330";
    private String sign = "f1d8975deb297ecd49fb571ae69d745f";
    private String sessionId = "A1358D18F39DD03ACAF270EC6EFB783E";
    private String appChannelCode = "10000071";
    private String responseType = "Ring";
    private String appVerCode = "V7.002.002.696ctch1";
    private int maxRows = 20;
    private String method = "search";
    private String imsi = "204043431728399";
    private String parentPath = "400";
    public String key;

    public  Post_Get_Search(String key){
        this.key = key;
    }
}
