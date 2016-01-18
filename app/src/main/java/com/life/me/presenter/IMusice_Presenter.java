package com.life.me.presenter;

/**
 * Created by cuiyang on 16/1/16.
 */
public interface IMusice_Presenter extends IPresenter {


    /**
     * 关键字查找歌曲列表
     */
    void getMusicListBySearch(String name);

    /**
     * 获取音乐文件
     */
    void getMusicRing(int resId);
}
