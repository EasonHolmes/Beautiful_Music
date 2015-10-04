package com.life.me.presenter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.life.me.R;

import java.util.Timer;
import java.util.TimerTask;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

/**
 * Created by cuiyang on 15/9/30.
 */
public class Music_Player_Presenter implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public MediaPlayer mediaPlayer; // 媒体播放器
    private SeekBarCompat seekBar; // 拖动条
    private Timer mTimer = new Timer(); // 计时器


    private final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int position = mediaPlayer.getCurrentPosition();//当前位置
            int duration = mediaPlayer.getDuration();//持续时间
            if (duration > 0) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                long pos = seekBar.getMax() * position / duration;
                seekBar.setProgress((int) pos);
            }
            return false;
        }
    });

    // 初始化播放器
    public Music_Player_Presenter(SeekBarCompat seekBar) {
        this.seekBar = seekBar;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 计时器每一秒更新一次进度条
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                mediaPlayer在播放并且seebar没有被按下
                if (mediaPlayer != null && mediaPlayer.isPlaying() && !seekBar.isPressed()) {
                    handler.sendEmptyMessage(0); //发送消息
                }
            }
        };
        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000);
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();//复位
            mediaPlayer.setDataSource(url);//设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (Exception e) {
            Log.e(getClass().getName(), "playerror==" + e.getMessage());
        }
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();//释放
            mediaPlayer = null;
        }
    }

    // 播放准备OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    // 播放完成OnCompletelistener
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
        //循环播放
        mp.start();
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);//设置二级缓冲显示位置。
//        int currentProgress = seekBar.getMax()
//                * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
//        Log.i(currentProgress + "% play播放进度", percent + " buffer-缓冲进度");
    }
}
