package com.life.me.presenter.iml;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.life.me.R;
import com.life.me.adapter.PopView_Adapter;
import com.life.me.app.BaseActivity;
import com.life.me.entity.postentity.Post_Get_Ring;
import com.life.me.entity.postentity.Post_Get_Search;
import com.life.me.entity.resultentity.Contains_keyWord_bean;
import com.life.me.http.ApiClient;
import com.life.me.mutils.BulerTransformation;
import com.life.me.mutils.LogUtils;
import com.life.me.presenter.IMusice_Presenter;
import com.life.me.widget.ProgressWheel;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cuiyang on 15/10/1.
 */
public abstract class Music_Presenter extends BaseActivity implements IMusice_Presenter {

    @InjectView(R.id.img_background)
    protected ImageView imgBackground;
    @InjectView(R.id.img_musics)
    protected ImageView imgMusics;
    @InjectView(R.id.root_layout)
    protected LinearLayout rootLayout;
    @InjectView(R.id.progress_seek)
    protected AppCompatSeekBar musicProgress;
    @InjectView(R.id.buler_img)
    protected ImageView bulerImg;
    @InjectView(R.id.Music_title)
    protected TextView MusicTitle;
    @InjectView(R.id.progress_wheel)
    protected ProgressWheel progressWheel;
    @InjectView(R.id.pop_list)
    protected RecyclerView popList;
    protected SearchView searchView;

    protected final int SHOW_DIALOG = 1;
    protected final int DISS_DIALOG = 2;
    protected final int SET_TITLE = 3;

    protected Context mContext;
    protected BulerTransformation bulerTransformation;

    protected XunFei_Recogning recogin;
    protected Music_Player_Presenter myPlayer;//播放控制器

    protected PopView_Adapter adapter;
    protected List<Contains_keyWord_bean.ResListEntity> contains_list = new ArrayList<Contains_keyWord_bean.ResListEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_act);
        ButterKnife.inject(this);
        mContext = this;
        super.initToolbar(getResources().getString(R.string.music_act_title), true);
        bulerTransformation = new BulerTransformation(mContext);
        onViewCreated(savedInstanceState);
    }

    @Override
    public void getMusicListBySearch(String name) {
        ApiClient.SERVICE_rx.getContains_key_MusicName(new Post_Get_Search(name))
                .subscribeOn(Schedulers.io())
                .filter(contains_keyWord_bean1 -> contains_keyWord_bean1.getResList() != null && contains_keyWord_bean1.getResList().size() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contains_keyWord_bean -> {
                    contains_list.clear();
                    contains_list.addAll(contains_keyWord_bean.getResList());
                    adapter.notifyDataSetChanged();
                }, error -> LogUtils.e(getClass().getName(), "musicList_error===" + error.getMessage()));
    }

    @Override
    public void getMusicRing(int resId) {
        ApiClient.SERVICE_rx.getMusicResult(new Post_Get_Ring(resId))
                .subscribeOn(Schedulers.newThread())
                .filter(musicAndImgResult_bean1 -> musicAndImgResult_bean1 != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicAndImgResult_bean -> {
                    progressWheel.setVisibility(View.VISIBLE);
                    popList.setVisibility(View.GONE);
                    MusicTitle.setText(musicAndImgResult_bean.getSingger() + musicAndImgResult_bean.getResName());
                    myPlayer.playUrl(musicAndImgResult_bean.getFullListUrl());
                    myPlayer.mediaPlayer.getDuration();
                    progressWheel.setVisibility(View.GONE);
                },error -> LogUtils.e(getClass().getName(), "getRing_error===" + error.getMessage()));
    }
}