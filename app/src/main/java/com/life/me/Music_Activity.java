package com.life.me;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.life.me.entity.SpeakBean;
import com.life.me.model.Music_Model;
import com.life.me.mutils.SingleRequestQueue;
import com.life.me.presenter.Music_Player_Presenter;
import com.life.me.presenter.Music_Presenter;
import com.life.me.presenter.Music_Recogning;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cuiyang on 15/9/28.
 */
public class Music_Activity extends BaseActivity implements Observer, Music_Model, View.OnClickListener {


    @InjectView(R.id.img_background)
    ImageView imgBackground;
    @InjectView(R.id.img_musics)
    ImageView imgMusics;
    @InjectView(R.id.root_layout)
    LinearLayout rootLayout;
    @InjectView(R.id.progress_seek)
    SeekBarCompat musicProgress;
    @InjectView(R.id.buler_img)
    ImageView bulerImg;
    @InjectView(R.id.Music_title)
    TextView MusicTitle;
    private Music_Recogning recogin;//被观察者
    private Music_Player_Presenter myPlayer;//播放控制器
    private Music_Presenter presenter;//网络控制器
    private Context mContext;

    private final int BULER = 0;
    private String title;
    private final Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BULER:
                    bulerImg.setImageBitmap(presenter.blur((Bitmap) msg.obj,mContext));
                    if (title != null) MusicTitle.setText(title + "");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_act);
        ButterKnife.inject(this);
        super.initToolbar("Music", true);
        mContext = this;
        initView();
    }

    private void initView() {
        myPlayer = new Music_Player_Presenter(musicProgress);
        recogin = new Music_Recogning(mContext);
        presenter = new Music_Presenter();
        recogin.addObserver(this);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        imgMusics.setOnClickListener(this);
    }

    /**
     * 讯飞语音的结果回调
     */
    @Override
    public void update(Observable observable, Object data) {
        SpeakBean speakBean = ((SpeakBean) data);
        StringBuffer sb = new StringBuffer();
        List<SpeakBean.WsEntity> entity = speakBean.getWs();
        for (int i = 0; i < entity.size(); i++) {//拼接讯飞的结果
            sb.append(entity.get(i).getCw().get(0).getW());
        }
        Log.e(getClass().getName(), "xunfei===" + sb.toString());
        presenter.getMusic_Result(sb.toString(), mContext, this);
    }

    @Override
    public void getMusic(String musicUrl, String singerName, String songName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myPlayer.playUrl(musicUrl);
            }
        }).start();
        title = singerName + "：" + songName;
        presenter.getMusic_Img(mContext, singerName + songName, this);
    }

    @Override
    public void getMusicImg(String imgUrl) {
        ImageRequest imgRequest = new ImageRequest(imgUrl,
                bitmap1 -> hand.obtainMessage(BULER,bitmap1).sendToTarget(),
                0, 0, Bitmap.Config.ARGB_8888, null);
        SingleRequestQueue.getRequestQueue(mContext).add(imgRequest);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgBackground, R.mipmap.img_center_fu, R.mipmap.img_center_fu);// 加载的控件，默认未加载前的图片，加载失败的图片
        Myapplication.imageLoader.get(imgUrl, listener);
    }

    // 进度改变
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_musics:
                recogin.showDialog();
                myPlayer.mediaPlayer.pause();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消讯飞连接和被观察者
        recogin.unConncetion();
        recogin.deleteObserver(Music_Activity.this);
        if (myPlayer != null) {
            myPlayer.stop();
            myPlayer = null;
        }
        recogin = null;
    }


}
