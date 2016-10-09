package com.newbiechen.zhihudailydemo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.adapter.ThemeMenuAdapter;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.ThemeEntity;
import com.newbiechen.zhihudailydemo.fragment.HomePageFragment;

import junit.framework.Test;

import java.util.List;

public class MainActivity extends AppBaseActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ListView mLvSlide;
    private ThemeMenuAdapter mThemeMenuAdapter;
    private Fragment mCurrentFragment;
    private HomePageFragment mHomePageFragment;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        mDrawerLayout = getViewById(R.id.main_drawer);
        mLvSlide = getViewById(R.id.main_nav_slide);
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {

        //设置标题
        getSupportActionBar().setTitle(R.string.homepage);
        //让DrawLayout与Toolbar关联
        setUpToggle();
        setUpListView();
        //初始化Fragment
        initFragment(savedInstanceState);
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

    private void setUpListView(){
        //添加头部
        View header = LayoutInflater.from(this)
                .inflate(R.layout.slide_header,null,false);
        mLvSlide.addHeaderView(header);
        mThemeMenuAdapter = new ThemeMenuAdapter(this);
        mLvSlide.setAdapter(mThemeMenuAdapter);
    }

    @Override
    protected void initClick() {
        mLvSlide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
        //添加首页的菜单选项
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initFragment(Bundle savedInstanceState){
        mFragmentManager = getSupportFragmentManager();
        //创建Fragment
        if (savedInstanceState != null){
            mHomePageFragment = (HomePageFragment) mFragmentManager.
                    findFragmentByTag(HomePageFragment.TAG);
        }
        else {
            mHomePageFragment = new HomePageFragment();
            //添加到FrameLayout中
            addFragment(mHomePageFragment);
        }


        //最后展现HomePageFragment
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.show(mHomePageFragment);
        ft.commit();
        mCurrentFragment = mHomePageFragment;
    }

    private void addFragment(Fragment fragment){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.main_frame,fragment, HomePageFragment.TAG);
        ft.hide(fragment);
        ft.commit();
    }

    private void switchFragment(Fragment fragment){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mCurrentFragment != fragment){
            ft.hide(mCurrentFragment);
            ft.show(fragment);
        }
    }
}
