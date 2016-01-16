package com.life.me;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.life.me.adapter.PopView_Adapter;
import com.life.me.entity.Contains_keyWord_bean;
import com.life.me.entity.Post_Get_Search;
import com.life.me.entity.XunFei_Speak_Bean;
import com.life.me.http.ApiClient;
import com.life.me.model.Music_Model;
import com.life.me.mutils.BulerTransformation;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.ScreenUtils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.presenter.iml.Music_Player_Presenter;
import com.life.me.presenter.iml.Music_Presenter;
import com.life.me.presenter.iml.XunFei_Recogning;
import com.life.me.widget.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cuiyang on 15/9/28.
 */
public class Music_Activity extends Music_Presenter implements Observer, Music_Model,
        View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


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

    SearchView searchView;
    @InjectView(R.id.pop_list)
    RecyclerView popList;
    private XunFei_Recogning recogin;//被观察者
    private Music_Player_Presenter myPlayer;//播放控制器
    private Context mContext;
    private PopView_Adapter adapter;
    private List<String> contains_list = new ArrayList<String>();

    private final int SHOW_DIALOG = 1;
    private final int DISS_DIALOG = 2;
    private final int SET_TITLE = 3;
    private boolean isOne = true;

    private BulerTransformation bulerTransformation;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_act);
        ButterKnife.inject(this);
        mContext = this;
        bulerTransformation = new BulerTransformation(mContext);
        initView();
    }

    private void initView() {
        super.initToolbar(getResources().getString(R.string.music_act_title), true);

        adapter = new PopView_Adapter(Music_Activity.this, contains_list);
        popList.setLayoutManager(new LinearLayoutManager(mContext));
        popList.setAdapter(adapter);
        popList.setBackground(getResources().getDrawable(R.drawable.list_pop_background));
        popList.getBackground().setAlpha(100);
        adapter.setOnItemClickListener(new PopView_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });

        myPlayer = new Music_Player_Presenter(musicProgress);

        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());

        imgMusics.setOnClickListener(this);

        recogin = new XunFei_Recogning();

        recogin.addObserver(this);

    }

    /**
     * 讯飞语音的结果回调
     */
    @Override
    public void update(Observable observable, Object data) {
        XunFei_Speak_Bean speakBean = ((XunFei_Speak_Bean) data);
        StringBuilder sb = new StringBuilder();
        List<XunFei_Speak_Bean.WsEntity> entity = speakBean.getWs();
        for (int i = 0; i < entity.size(); i++) {//拼接讯飞的结果
            sb.append(entity.get(i).getCw().get(0).getW());
        }
        Log.e(getClass().getName(), "search_content==" + sb.toString());
        Snackbar.make(rootLayout, sb.toString(), Snackbar.LENGTH_SHORT).show();
//        presenter.getMusic_Result(sb.toString(), mContext, this);
    }

    @Override
    public void getMusic(String musicUrl, String singerName, String songName) {
        new Thread(() -> {
            myPlayer.playUrl(musicUrl);
        }).start();
        title = singerName + ":" + songName;
        hand.obtainMessage(SET_TITLE, title).sendToTarget();
//        presenter.getMusic_Img(mContext, singerName + songName, this);
    }

    @Override
    public void getMusicImg(String imgUrl) {
        Picasso.with(this).load(imgUrl).into(imgBackground);
        Picasso.with(this).load(imgUrl).transform(bulerTransformation).into(bulerImg);
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
        searchView.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    //搜索框搜索
    @Override
    public boolean onQueryTextSubmit(String query) {
        progressWheel.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            popList.setVisibility(View.VISIBLE);
            Post_Get_Search get = new Post_Get_Search();
            get.setKey(newText);
            ApiClient.SERVICE_rx.getContains_key_MusicName(get)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contains_keyWord_bean -> setQueryChangeData(contains_keyWord_bean));
        } else {
            popList.setVisibility(View.GONE);
        }
        return false;
    }

    private void setQueryChangeData(Contains_keyWord_bean contains_keyWord_bean) {
        contains_list.clear();
        for (Contains_keyWord_bean.ResListEntity cr : contains_keyWord_bean.getResList()) {
            contains_list.add(cr.getResName() + " " + cr.getSinger());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_musics:
                if (!HttpUtils.getSingleton().hasNetwork(mContext)) {
                    Widget_Utils.showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
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
