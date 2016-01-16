package com.life.me;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androipathview.PathView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

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
    String drawerContent;

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
                Intent i = new Intent(Wel_Activity.this, MainActivity.class);
                //把推送的内容带过去
                i.putExtra(mContext.getResources().getString(R.string.push_content), drawerContent);
                startActivity(i);
                //push推的  style.xml中添加<item name="android:windowIsTranslucent">true</item>因为有这个属性会变成进入的activity push覆盖上来的效果
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
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
