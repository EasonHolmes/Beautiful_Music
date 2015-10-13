package com.life.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.life.me.view.SystemBarTintManager;


public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar(String title, boolean or) {
        //设置整个ToolBar  继承自BaseActivity
        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.statc_color);
        }
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(title);//设置左上角标题的，默认是APP的名字
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(or);
        getSupportActionBar().setDisplayHomeAsUpEnabled(or);
        if (!or) {
            mDrawerlayout = (DrawerLayout) findViewById(R.id.mDrawerlayout);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, R.string.open, R.string.close);
            mDrawerToggle.syncState();
            mDrawerlayout.setDrawerListener(mDrawerToggle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();//activityOPtionICs不能用super要不失效
    }
}
