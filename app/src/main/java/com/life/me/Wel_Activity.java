package com.life.me;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androipathview.PathView;
import com.life.me.entity.bmobentity.TokenTb;
import com.life.me.mutils.DeviceInfo;
import com.life.me.mutils.Widget_Utils;
import com.life.me.presenter.iml.Main_presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by cuiyang on 15/9/22.
 */
public class Wel_Activity extends AppCompatActivity implements PathView.AnimatorBuilder.ListenerEnd {

    @InjectView(R.id.pathView)
    PathView pathView;
    @InjectView(R.id.wel_txt)
    TextView welTxt;
    private Context mContext;


    private final int ALPHA_ANIMATION_DEFAULT = 1200;
    private final int PATH_ANIMATION_DEFAULT = 2000;
    private final int PATH_DELAY_DEFAULT = 100;
    private final int PREAD_PHONE_STATEREQUEST_CODE = 10;
    String drawerContent;

    private Set<String> pushTags = new HashSet<String>();//tag相当于分组。可以把某一些用户设置同样的tag就能某一组发了,一般在程序里动态获取服务器某些信息后添加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wel_act);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mContext = this;
        initView();
    }

    private void initView() {
        drawerContent = getIntent().getStringExtra(getResources().getString(R.string.push_content));//拿推送传过来的内容
        welAnimation();
    }

    private void welAnimation() {
        final Path path = makeConvexArrow(50, 100);
        pathView.setPath(path);
        pathView.setFillAfter(true);
        pathView.useNaturalColors();
        //设置播放
        pathView.getPathAnimator().
                delay(PATH_DELAY_DEFAULT).
                duration(PATH_ANIMATION_DEFAULT).
                interpolator(new DecelerateInterpolator(1f)).listenerEnd(this).start();
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
        animSet.setDuration(ALPHA_ANIMATION_DEFAULT);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                checkPermission();
                initJpush();
            }
        });
    }

    private void initJpush() {
        JPushInterface.init(this);            // 初始化 JPush
        pushTags.add("1");//添加tag分组
        pushTags.add("2");
        //根据token设置设备别名
        String id = DeviceInfo.getInstance(this).getToken().substring(0, 13);
        JPushInterface.setAliasAndTags(this, id, pushTags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    Log.e(getClass().getName(), "ok successful==");
                }
            }
        });
        startMainActivity();
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    private void checkPermission() {
//        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE},
//                    PREAD_PHONE_STATEREQUEST_CODE);
//            return;
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        doNext(requestCode, grantResults);
//    }
//
//    private void doNext(int requestCode, int[] grantResults) {
//        if (requestCode == PREAD_PHONE_STATEREQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initJpush();
//            } else {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//                dialog.setMessage("由于定位需要,请在设置给予权限,我们不会泄漏任何用户个人资料");
//                dialog.setNegativeButton("确定",null);
//                dialog.show();
//            }
//        }
//    }

    private void startMainActivity() {
        Intent i = new Intent(Wel_Activity.this, MainActivity.class);
        //把推送的内容带过去
        i.putExtra(mContext.getResources().getString(R.string.push_content), drawerContent);
        startActivity(i);
        //push推的  style.xml中添加<item name="android:windowIsTranslucent">true</item>因为有这个属性会变成进入的activity push覆盖上来的效果
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(Wel_Activity.this);//是做用户统计的
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(Wel_Activity.this);//是做用户统计的
    }
}
