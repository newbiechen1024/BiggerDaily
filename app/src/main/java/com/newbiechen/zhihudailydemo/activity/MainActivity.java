package com.newbiechen.zhihudailydemo.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;

public class MainActivity extends AppBaseActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavSlide;
    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mDrawerLayout = getViewById(R.id.main_drawer);
        mNavSlide = getViewById(R.id.main_nav_slide);
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {
        //设置标题
        getSupportActionBar().setTitle(R.string.homepage);
        //让DrawLayout与Toolbar关联
        setUpToggle();
        //设置侧滑栏的内容
        setUpSlideContent();
    }

    /**
     * 关联DrawerLayout与Toolbar
     */
    private void setUpToggle(){
        //创建关联器
        mToggle = new ActionBarDrawerToggle(
                this,mDrawerLayout,mToolbar,R.string.open_drawer,R.string.close_drawer
        );
        //设置监听
        mDrawerLayout.addDrawerListener(mToggle);
        //设置指示器
        mToggle.syncState();
    }

    private void setUpSlideContent(){
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogin(Bundle savedInstanceState) {
    }

    /**
     * Toggle的官方写法
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    /**
     * 当资源配置改变的时候调用
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加菜单选项
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
