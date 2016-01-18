package com.life.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.life.me.adapter.PopView_Adapter;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.presenter.iml.Music_Player_Presenter;
import com.life.me.presenter.iml.Music_Presenter;
import com.life.me.presenter.iml.XunFei_Recogning;
import com.life.me.view.MusicView;
import com.squareup.picasso.Picasso;

/**
 * Created by cuiyang on 15/9/28.
 */
public class Music_Activity extends Music_Presenter implements MusicView,
        View.OnClickListener, SearchView.OnQueryTextListener {

    private String title;


    private final Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    recogin.showDialog();
                    break;
                case DISS_DIALOG:
                    recogin.dissDialog();
                    break;
                case SET_TITLE:
                    if (title != null) MusicTitle.setText(title + "");
                    break;
            }
            return false;
        }
    });

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        myPlayer = new Music_Player_Presenter(musicProgress);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        imgMusics.setOnClickListener(this);
        recogin = new XunFei_Recogning(this, this);

        adapter = new PopView_Adapter(Music_Activity.this, contains_list);
        popList.setLayoutManager(new LinearLayoutManager(mContext));
        popList.setAdapter(adapter);
        popList.setBackground(getResources().getDrawable(R.drawable.list_pop_background));
        popList.getBackground().setAlpha(100);
        adapter.setOnItemClickListener(new PopView_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                getMusicRing(contains_list.get(position).getResId());
            }
        });
    }

    @Override
    public void SpliceRecogingByXunFei(String SpliceStr) {
        Log.e(getClass().getName(), "search_content==" + SpliceStr);
        getMusicListBySearch(SpliceStr);
    }

    @Override
    public void getMusiceImage(String imgUrl) {
        Picasso.with(this).load(imgUrl).into(imgBackground);
        Picasso.with(this).load(imgUrl).transform(bulerTransformation).into(bulerImg);
    }

    @Override
    public void errorResult(String error) {
        progressWheel.setVisibility(View.GONE);
        Snackbar.make(rootLayout, error, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 进度改变SeekBar.OnSeekBarChangeListener
     */
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            /**
             * 等歌开始放了的时候再让他消失
             */
            if (progress < 2) {//小于2就只进一次了不会一直changed就更新
                progressWheel.setVisibility(View.GONE);
                hand.obtainMessage(DISS_DIALOG).sendToTarget();
            }
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * myPlayer.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            myPlayer.mediaPlayer.seekTo(progress);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            popList.setVisibility(View.VISIBLE);
            getMusicListBySearch(newText);
        } else {
            popList.setVisibility(View.GONE);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_musics:
                if (!HttpUtils.getSingleton().hasNetwork(mContext))
                    Widget_Utils.showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
                else
                    hand.obtainMessage(SHOW_DIALOG).sendToTarget();
                myPlayer.mediaPlayer.pause();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recogin != null) {
            recogin.unConncetion();
            recogin = null;
        }
        if (myPlayer != null) {
            myPlayer.stop();
            myPlayer = null;
        }
    }
}
