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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.StringUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.adapter.ThemeMenuAdapter;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.ThemeEntity;
import com.newbiechen.zhihudailydemo.fragment.HomePageFragment;
import com.newbiechen.zhihudailydemo.fragment.ThemeBriefFragment;
import com.newbiechen.zhihudailydemo.utils.SharedPresManager;
import com.newbiechen.zhihudailydemo.utils.URLManager;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
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
        //设置模式
        if(ZhiHuApplication.isNightMode){
            setTheme(R.style.NightMode);
        }
        else {
            setTheme(R.style.LightMode);
        }

        setContentView(R.layout.activity_main);

        mDrawerLayout = getViewById(R.id.main_drawer);
        mLvSlide = getViewById(R.id.main_lv_slide);
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {

        //设置标题
        getSupportActionBar().setTitle(R.string.homepage);
        //让DrawLayout与Toolbar关联
        setUpToggle();
        setUpSlideLayout();
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

    /**
     * 设置侧滑栏
     */
    private void setUpSlideLayout(){
        //添加头部
        View header = LayoutInflater.from(this)
                .inflate(R.layout.slide_header,mLvSlide,false);
        mLvSlide.addHeaderView(header,null,false);
        mThemeMenuAdapter = new ThemeMenuAdapter(this);
        mLvSlide.setAdapter(mThemeMenuAdapter);
    }

    private void loadSlideData(){
        //首先从SharedPreference中获取
        String data = SharedPreferenceUtils.getData(this,SharedPresManager.
                PRES_SLIDE_THEME_TYPE,"");
        if (!data.equals("")){
            addData2Slide(data);
        }

        //然后再从网络中获取(原因是，在弱网环境下，会展现不了侧滑菜单，所以需要从文档中获取数据。)
        //网络获取数据，是保证数据是最新的
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                if (!StringUtils.isGoodJson(data)){
                    return;
                }
                addData2Slide(data);
                //存储到SharedPreference，因为只需要利用一次就可以了，不需要存储到数据库
                SharedPreferenceUtils.saveData(MainActivity.this,
                        SharedPresManager.PRES_SLIDE_THEME_TYPE,data);
            }
        };
        mRemoteService.loadData(URLManager.THEME_TYPE,callback);
    }

    private void addData2Slide(String data){

        Gson gson = new Gson();
        JSONArray jsonArray = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("others");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<ThemeEntity> themeEntities = gson.fromJson(jsonArray.toString(),
                new TypeToken<List<ThemeEntity>>(){}.getType());
        //加入到Adapter里面去
        mThemeMenuAdapter.refreshItems(themeEntities);
    }

    @Override
    protected void initClick() {
        mLvSlide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_frame,
                                    mHomePageFragment,HomePageFragment.TAG)
                            .commit();
                }
                else if (position != 0){
                    ThemeEntity entity = mThemeMenuAdapter.getItem(position-1);
                    //切换Fragment
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_frame, ThemeBriefFragment.
                                    newInstance(entity.getId()),ThemeBriefFragment.TAG)
                            .commit();
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        loadSlideData();
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

        MenuItem changeMode = menu.findItem(R.id.main_night_theme);
        if (ZhiHuApplication.isNightMode){
            changeMode.setTitle(R.string.light_mode);
        }
        else {
            changeMode.setTitle(R.string.night_mode);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_night_theme:
                if (ZhiHuApplication.isNightMode){
                    //切换为日间模式
                    SharedPreferenceUtils.saveData(this,"NightMode",false);
                    ZhiHuApplication.isNightMode = false;
                    item.setTitle(R.string.light_mode);
                    setTheme(R.style.LightMode);
                }
                else {
                    //切换为夜间模式
                    SharedPreferenceUtils.saveData(this,"NightMode",true);
                    ZhiHuApplication.isNightMode = true;
                    item.setTitle(R.string.night_mode);
                    setTheme(R.style.NightMode);
                }
                this.recreate();
                break;
        }
        return true;
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
            mFragmentManager.beginTransaction()
                    .replace(R.id.main_frame,mHomePageFragment)
                    .commit();
            //添加到FrameLayout中
/*            addFragment(mHomePageFragment);*/
        }


/*        //最后展现HomePageFragment
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.show(mHomePageFragment);
        ft.commit();
        mCurrentFragment = mHomePageFragment;*/
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
