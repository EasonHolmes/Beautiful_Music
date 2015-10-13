package com.life.me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.life.me.entity.ConfigTb;
import com.life.me.entity.SpeakBean;
import com.life.me.model.Music_Model;
import com.life.me.mutils.Commutils;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleRequestQueue;
import com.life.me.mutils.Utils;
import com.life.me.presenter.Music_Player_Presenter;
import com.life.me.presenter.Music_Presenter;
import com.life.me.presenter.Music_Recogning;
import com.life.me.view.ProgressWheel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.schedulers.NewThreadScheduler;

/**
 * Created by cuiyang on 15/9/28.
 */
public class Music_Activity extends BaseActivity implements Observer, Music_Model,
        View.OnClickListener, SearchView.OnQueryTextListener {


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
    @InjectView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    private Music_Recogning recogin;//被观察者
    private Music_Player_Presenter myPlayer;//播放控制器
    private Music_Presenter presenter;//网络控制器
    private Context mContext;

    private final int BULER = 0;
    private final int SHOW_DIALOG = 1;
    private final int DISS_DIALOG = 2;
    private final int SET_TITLE = 3;
    private boolean isOne = true;

    private String title;
    private final Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BULER:
                    bulerImg.setImageBitmap(presenter.blur((Bitmap) msg.obj, mContext));
                    break;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_act);
        ButterKnife.inject(this);
        super.initToolbar(getResources().getString(R.string.music_act_title), true);
        mContext = this;
        initView();
    }

    private void initView() {
        myPlayer = new Music_Player_Presenter(musicProgress);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        imgMusics.setOnClickListener(this);
        presenter = new Music_Presenter();

        recogin = new Music_Recogning();
        recogin.addObserver(this);

    }

    /**
     * 讯飞语音的结果回调
     */
    @Override
    public void update(Observable observable, Object data) {
        SpeakBean speakBean = ((SpeakBean) data);
        StringBuilder sb = new StringBuilder();
        List<SpeakBean.WsEntity> entity = speakBean.getWs();
        for (int i = 0; i < entity.size(); i++) {//拼接讯飞的结果
            sb.append(entity.get(i).getCw().get(0).getW());
        }
        Log.e(getClass().getName(), "search_content==" + sb.toString());
        Snackbar.make(rootLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
        presenter.getMusic_Result(sb.toString(), mContext, this);
    }

    @Override
    public void getMusic(String musicUrl, String singerName, String songName) {
        new Thread(() -> {
            myPlayer.playUrl(musicUrl);
        }).start();
        title = singerName + ":" + songName;
        hand.obtainMessage(SET_TITLE, title).sendToTarget();
        presenter.getMusic_Img(mContext, singerName + songName, this);
    }

    @Override
    public void getMusicImg(String imgUrl) {
        progressWheel.setVisibility(View.GONE);
        hand.obtainMessage(DISS_DIALOG).sendToTarget();
        ImageRequest imgRequest = new ImageRequest(imgUrl,
                bitmap1 -> hand.obtainMessage(BULER, bitmap1).sendToTarget(),
                0, 0, Bitmap.Config.ARGB_8888, null);
        SingleRequestQueue.getRequestQueue(mContext).add(imgRequest);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgBackground, R.mipmap.music_mic, R.mipmap.music_mic);// 加载的控件，默认未加载前的图片，加载失败的图片
        Myapplication.imageLoader.get(imgUrl, listener);
    }

    @Override
    public void error(String error) {//错误信息
        progressWheel.setVisibility(View.GONE);
        Snackbar.make(rootLayout, error, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 进度改变
     */
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    //搜索框搜索
    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.getMusic_Result(query, mContext, Music_Activity.this);
        progressWheel.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_musics:
                if (!HttpUtils.getSingleton().hasNetwork(mContext)) {
                    HttpUtils.getSingleton().showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
                    return;
                }
                if (isOne) {
                    recogin.initRecogn(mContext);
                    myPlayer.mediaPlayer.pause();
                    isOne = false;
                } else {
                    recogin.setParam(mContext);
                    hand.obtainMessage(SHOW_DIALOG).sendToTarget();
                    myPlayer.mediaPlayer.pause();
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recogin != null) {
            //取消讯飞连接和被观察者
            recogin.unConncetion();
            recogin.deleteObserver(Music_Activity.this);
            recogin = null;
        }
        if (myPlayer != null) {
            myPlayer.stop();
            myPlayer = null;
        }
    }
}
