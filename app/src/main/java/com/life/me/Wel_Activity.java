package com.life.me;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.eftimoff.androipathview.PathView;
import com.life.me.entity.CacheBean;
import com.life.me.presenter.Main_presenter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cuiyang on 15/9/22.
 */
public class Wel_Activity extends AppCompatActivity implements PathView.AnimatorBuilder.ListenerEnd {

    @InjectView(R.id.pathView)
    PathView pathView;
    @InjectView(R.id.wel_txt)
    TextView welTxt;
    private Context mContext;


    //BaiduLocation Servers
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    public Vibrator mVibrator;

    private LocationClient mLocationClient;

    String drawerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wel_act);
        ButterKnife.inject(this);
        mContext = this;

        mLocationClient = new LocationClient(mContext);//初始化百度client
        initLocation(mContext, mLocationClient, new MyLocationListener());//开启地图定位 上传位置和查询天气
        initView();
    }

    private void initView() {
        drawerContent = getIntent().getStringExtra(getResources().getString(R.string.push_content));//拿推送传过来的内容
        welAnimation();//欢迎动画
    }

    private void welAnimation() {
        final Path path = makeConvexArrow(50, 100);
        pathView.setPath(path);
        pathView.setFillAfter(true);
        pathView.useNaturalColors();
        //设置播放
        pathView.getPathAnimator().
                delay(100).
                duration(2000).
                interpolator(new AccelerateDecelerateInterpolator()).listenerEnd(this).start();
    }

    private Path makeConvexArrow(float length, float height) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(length / 4f, 0.0f);
        path.lineTo(length, height / 2.0f);
        path.lineTo(length / 4f, height);
        path.lineTo(0.0f, height);
        path.lineTo(length * 3f / 4f, height / 2f);
        path.lineTo(0.0f, 0.0f);
        path.close();
        return path;
    }

    @Override
    public void onAnimationEnd() {
        welTxt.setVisibility(View.VISIBLE);
        ObjectAnimator wel_animation = ObjectAnimator.ofFloat(welTxt, "alpha", 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(wel_animation);
        animSet.setDuration(1200);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent i = new Intent(Wel_Activity.this, MainActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.push_content), drawerContent);//把推送的内容带过去
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//push推的          style.xml中添加<item name="android:windowIsTranslucent">true</item>因为有这个属性会变成进入的activity push覆盖上来的效果
                finish();
            }
        });
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            CacheBean.addr = location.getAddrStr();
            CacheBean.Latitude = String.valueOf(location.getLatitude());
            CacheBean.Longitude = String.valueOf(location.getLongitude());
            List<Poi> list1 = location.getPoiList();// POI信息
            if (list1 != null) {
                CacheBean.addr = CacheBean.addr + list1.get(0).getName();
            }
            Log.e(getClass().getName(), "location====" + CacheBean.addr + "Latitude====" +
                    CacheBean.Latitude + "Longitude====" + CacheBean.Longitude);
            mLocationClient.stop();
            /**
             * 上传位置
             * */
            new Main_presenter().upLocation(mContext);
        }
    }

    /**
     * 初始化定位信息
     *
     * @param mContext
     * @param mLocationClient
     * @param listener
     */
    public void initLocation(Context mContext, LocationClient mLocationClient, BDLocationListener listener) {
        mLocationClient.registerLocationListener(listener);
        mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);

        mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }
}
