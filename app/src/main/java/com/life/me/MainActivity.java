package com.life.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.life.me.dao.ApiDao;
import com.life.me.dao.WeatherDao;
import com.life.me.entity.CacheBean;
import com.life.me.entity.SearchWeather_Bean;
import com.life.me.entity.ConfigTb;
import com.life.me.http.ApiClient;
import com.life.me.mutils.Commutils;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.Utils;
import com.life.me.mutils.Widget_Utils;
import com.life.me.mutils.imageloder.ImageLoader;
import com.life.me.presenter.Main_presenter;
import com.romainpiel.shimmer.Shimmer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Main_presenter implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private Context mContext;
    private final int UPDATE_IMG = 0;
    private final int GET_FORM_GALLERY = 1;
    private final int REFRESH = 2;
    private HttpUtils httpUtils;
    private Subscription subscription;


    private Handler hand = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_IMG:
                    refreshLayout.setRefreshing(false);
                    updateUI((List<WeatherDao>) msg.obj);
                    break;
                case REFRESH:
                    refreshLayout.setRefreshing(false);
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onViewCreated() {
        mContext = this;

        /**
         * 因为picasso加载总会闪一下所以按鸿杨大神的写法写了一个
         */
        if (Utils.hasFile())
            ImageLoader.getInstance().loadImage(ConfigTb.Photo_Path, mainBackground, false);

        new Shimmer().setDuration(1500).start(honeyTxt);

        httpUtils = HttpUtils.getSingleton();

        init();

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        String drawerContent = getIntent().getStringExtra(getResources().getString(R.string.push_content));//推送传过来的内容

        setPushContent(drawerContent);

        drawerView.setOnClickListener(this);//设置一个空的键听要不侧边栏会透过去影响后面的手势
        imgMusic.setOnClickListener(this);
        refreshLayout.setColorSchemeResources(R.color.toolbar_background2);
        refreshLayout.setOnRefreshListener(this);

        if (!httpUtils.hasNetwork(mContext)) {
            Widget_Utils.showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
            updateDataAndUiByDatabase();
        } else {
            getWeatherResult();
        }
    }

    protected void getWeatherResult() {
        if (TextUtils.isEmpty(CacheBean.Longitude) || TextUtils.isEmpty(CacheBean.Latitude)) {
            Widget_Utils.showSnacker(linearLayout, getResources().getString(R.string.not_have_location));
            return;
        }

        Action1 action1 = searchWeatherBean -> saveDataAndUpdateUi((SearchWeather_Bean) searchWeatherBean);

        Action1<Throwable> onError = error -> Widget_Utils.showSnacker(linearLayout, "哎哟喂,没有拿到数据");

        subscription = ApiClient.SERVICE_rx.getWeather(ConfigTb.getWeatherResult + CacheBean.Longitude + "," + CacheBean.Latitude + ConfigTb.baidu_mcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, onError);
    }


    private void saveDataAndUpdateUi(SearchWeather_Bean searchWeatherBean) {
        if (getWeatherFilter(searchWeatherBean)) {
            ApiDao.saveWeather(searchWeatherBean);
            updateDataAndUiByDatabase();
        } else {
            updateDataAndUiByDatabase();
        }
    }

    private void updateDataAndUiByDatabase() {
        hand.postDelayed(() -> hand.obtainMessage(UPDATE_IMG, DataSupport.order("id asc")
                .find(WeatherDao.class))
                .sendToTarget(), 500);
    }

    private void setBackground() {
        if (Utils.hasFile())
            Picasso.with(MainActivity.this)
                    .load(new File(ConfigTb.Photo_Path))
                    .placeholder(R.mipmap.weath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)//不使用内存缓存否则还会被认为是同一张不更新图片
                    .into(mainBackground);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_music:
                startActivity(new Intent(mContext, Music_Activity.class));
                overridePendingTransition(R.anim.small_2_big, R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (!httpUtils.hasNetwork(mContext)) {
            Widget_Utils.showDialog(mContext, "亲爱的,要打开你的网络哦.么么哒");
            hand.obtainMessage(REFRESH).sendToTarget();
        } else {
            getWeatherResult();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(mContext, Setting_Activity.class));
                break;
            case R.id.background:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_FORM_GALLERY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FORM_GALLERY && data != null) {

            boolean isSave = Utils.fileChannelCopy(new File(new Commutils().getPath(mContext, data.getData())), new File(ConfigTb.Photo_Path));
            if (isSave) {
                setBackground();
            } else {
                Snackbar.make(linearLayout, "设置失败", Snackbar.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            android.os.Process.killProcess(android.os.Process.myPid());//获取PID
            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
