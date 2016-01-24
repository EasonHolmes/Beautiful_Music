package com.life.me.view;


/**
 * Created by cuiyang on 16/1/16.
 */
public interface MusicView {
    /**
     * 讯飞语音的结果回调
     */
    void SpliceRecogingByXunFei(String SpliceStr);

    void errorResult(String error);

    void playerPause();


}
