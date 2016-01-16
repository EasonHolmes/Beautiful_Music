package com.life.me.view;

import com.life.me.entity.XunFei_Speak_Bean;

/**
 * Created by cuiyang on 16/1/16.
 */
public interface MusicView {
    /**
     * 讯飞语音的结果回调
     */
    void SpliceRecogingByXunFei(String SpliceStr);

    void getMusicResult(String musicUrl, String singerName, String songName);

    void getMusiceImage(String imgUrl);

    void errorResult(String error);
}
