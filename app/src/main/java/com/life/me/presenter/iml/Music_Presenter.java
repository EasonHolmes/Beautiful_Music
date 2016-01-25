package com.life.me.presenter.iml;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
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
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cuiyang on 15/10/1.
 */
public abstract class Music_Presenter extends BaseActivity implements IMusice_Presenter, View.OnClickListener {

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
    @InjectView(R.id.img_pause)
    protected ImageView img_pause;
    @InjectView(R.id.img_last)
    protected ImageView img_last;
    @InjectView(R.id.img_next)
    protected ImageView img_next;
    protected SearchView searchView;

    protected final int SHOW_DIALOG = 1;
    protected final int DISS_DIALOG = 2;


    protected Context mContext;
    protected BulerTransformation bulerTransformation;

    protected XunFei_Recogning recogin;
    protected Music_Player_Helper myPlayer;//播放控制器

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

        myPlayer = new Music_Player_Helper(musicProgress);

        adapter = new PopView_Adapter(Music_Presenter.this, contains_list);
        popList.setLayoutManager(new LinearLayoutManager(mContext));
        popList.setAdapter(adapter);
        popList.setBackground(getResources().getDrawable(R.drawable.list_pop_background));
        popList.getBackground().setAlpha(100);

        setOnclickListener(imgMusics, img_pause, img_last, img_next);
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
                    popList.setVisibility(View.VISIBLE);
                }, error -> LogUtils.e(getClass().getName(), "musicList_error===" + error.getMessage()));
    }

    @Override
    public void getMusicRing(int resId) {
        ApiClient.SERVICE_rx.getMusicResult(new Post_Get_Ring(resId))
                .subscribeOn(Schedulers.io())
                .filter(musicAndImgResult_bean1 -> musicAndImgResult_bean1 != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicAndImgResult_bean -> {

                    progressWheel.setVisibility(View.VISIBLE);
                    popList.setVisibility(View.GONE);
                    MusicTitle.setText(musicAndImgResult_bean.getSingger() + " : " + musicAndImgResult_bean.getResName());
                    setAlbum(musicAndImgResult_bean.getPics().get(0).getPicUrl());
                    startPlayer(musicAndImgResult_bean.getFullListUrl());

                }, error -> LogUtils.e(getClass().getName(), "getRing_error===" + error.getMessage()));
    }

    private void startPlayer(String playUrl) {
        Observable.just(playUrl)
                .filter(s -> s != null)
                .observeOn(Schedulers.io())
                .subscribe(myPlayer::startPlayUrl, error -> LogUtils.e(getClass().getName(), "player_error" + error.getMessage()));
    }

    private void setAlbum(String imgUrl) {
        Picasso.with(this).load(imgUrl).placeholder(R.mipmap.music_mic).into(imgBackground);
        Picasso.with(this).load(imgUrl).placeholder(R.mipmap.music_mic).transform(bulerTransformation).into(bulerImg);
    }

    @Override
    public void setOnclickListener(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }
}