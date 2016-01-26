package com.life.me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.LogUtils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.presenter.iml.Music_Presenter;
import com.life.me.presenter.iml.XunFei_Recogning;
import com.life.me.view.MusicView;

/**
 * Created by cuiyang on 15/9/28.
 */
public class Music_Activity extends Music_Presenter implements MusicView,
        View.OnClickListener, SearchView.OnQueryTextListener {

    private final int REQUEST_MIC_CODE = 10;
    private int currentPosition = 0;

    private final Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    recogin.showDialog();
                    break;
                case DISS_DIALOG:
                    recogin.dissDialog();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        recogin = new XunFei_Recogning(Music_Activity.this, this);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        adapter.setOnItemClickListener((position, v) -> {
            getMusicRing(contains_list.get(position).getResId());
            currentPosition = position;
        });
    }

    @Override
    public void SpliceRecogingByXunFei(String SpliceStr) {
        LogUtils.e(getClass().getName(), "search_content==" + SpliceStr);
        getMusicListBySearch(SpliceStr);
    }

    @Override
    public void errorResult(String error) {
        progressWheel.setVisibility(View.GONE);
        Snackbar.make(rootLayout, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void playerPause() {
        if (myPlayer.mediaPlayer.isPlaying()) {
            myPlayer.mediaPlayer.pause();
            img_pause.setImageResource(R.mipmap.img_play);
        } else {
            myPlayer.mediaPlayer.start();
            img_pause.setImageResource(R.mipmap.img_pause);
        }
    }

    /**
     * 进度改变SeekBar.OnSeekBarChangeListener
     */
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress < 2) {
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
                checkPermission();
                break;
            case R.id.img_pause:
                playerPause();
                break;
            case R.id.img_next:
                getMusicRing(contains_list.get(nextMusic_position()).getResId());
                break;
            case R.id.img_last:
                getMusicRing(contains_list.get(lastMusic_position()).getResId());
                break;
        }

    }

    private int nextMusic_position() {
        //到列表最后一个再下一曲就第一首
        int p = currentPosition + 1 >= contains_list.size() ? 0 : currentPosition + 1;
        currentPosition = p;
        return currentPosition;
    }

    private int lastMusic_position() {
        //到列表第一首就列表最后一首
        int p = currentPosition - 1 < 0 ? contains_list.size() - 1 : currentPosition - 1;
        currentPosition = p;
        return currentPosition;
    }


    private void pleaseSpeak() {
        if (!HttpUtils.getSingleton().hasNetwork(mContext)) {
            Widget_Utils.showSnackbar(popList, "亲爱的,要打开你的网络哦.么么哒");
        } else {
            hand.obtainMessage(SHOW_DIALOG).sendToTarget();
        }
        myPlayer.mediaPlayer.pause();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO},
                    REQUEST_MIC_CODE);
            return;
        }
        pleaseSpeak();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_MIC_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pleaseSpeak();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("请在设置中开启录音权限");
                dialog.setNegativeButton("确定", null);
                dialog.show();
            }
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
