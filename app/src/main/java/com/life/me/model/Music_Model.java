package com.life.me.model;


/**
 * Created by cuiyang on 15/9/30.
 */
public interface Music_Model {
    /**
     * @param musicUrl   音频地址
     * @param singerName 歌手名和歌名用找对应图片的。天天动听的url地址是随机给的图片
     */
    void getMusic(String musicUrl, String singerName, String songName);

    void getMusicImg(String imgUrl);

    void error(String error);
}
