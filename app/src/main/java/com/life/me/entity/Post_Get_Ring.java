package com.life.me.entity;

/**
 * Created by cuiyang on 15/12/24.
 */
public class Post_Get_Ring {


    /**
     * sid : 249253307
     * networkType : wifi
     * format : json
     * resId : 988269
     * kind : 4
     * protocolCode : 2.0
     * timestamp : 20151224113414
     * sign : a8b698adeed64d726cc173ccbd64eca2
     * parentId : 3
     * sessionId : A1358D18F39DD03ACAF270EC6EFB783E
     * appChannelCode : 10000071
     * appVerCode : V7.002.002.696ctch1
     * method : get_ring
     * imsi : 204043431728399
     * bitRate : 0
     * parentPath : 400
     * resType : 5
     */

    private int sid = 249253307;
    private String networkType = "wifi";
    private String format ="json" ;
    private int resId;
    private int kind = 4;
    private String protocolCode = "2.0";
    private String timestamp = "20151224113414";
    private String sign = "a8b698adeed64d726cc173ccbd64eca2";
    private int parentId = 3;
    private String sessionId = "A1358D18F39DD03ACAF270EC6EFB783E";
    private String appChannelCode = "10000071";
    private String appVerCode = "V7.002.002.696ctch1";
    private String method = "get_ring";
    private String imsi = "204043431728399";
    private int bitRate= 0;
    private String parentPath = "400";
    private int resType = 5;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
