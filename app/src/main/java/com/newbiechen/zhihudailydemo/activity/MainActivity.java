package com.newbiechen.zhihudailydemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.StringUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.adapter.ThemeMenuAdapter;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.ThemeEntity;
import com.newbiechen.zhihudailydemo.entity.UserInfo;
import com.newbiechen.zhihudailydemo.fragment.HomePageFragment;
import com.newbiechen.zhihudailydemo.fragment.PersonInfoFragment;
import com.newbiechen.zhihudailydemo.fragment.ThemeBriefFragment;
import com.newbiechen.zhihudailydemo.utils.SharedPresManager;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.umeng.socialize.UMShareAPI;

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

    private TextView mTvUserName;
    private ImageView mIvIcon;

    private UserInfo mUserInfo;
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

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {
        //
        mUserInfo = ZhiHuApplication.sUserInfo;
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
        setUpHeader(header);
        mLvSlide.addHeaderView(header,null,false);
        mThemeMenuAdapter = new ThemeMenuAdapter(this);
        mLvSlide.setAdapter(mThemeMenuAdapter);
    }

    private void setUpHeader(View view){
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.header_linear_userinfo);
        mIvIcon = (ImageView) view.findViewById(R.id.header_iv_icon);
        mTvUserName = (TextView) view.findViewById(R.id.header_tv_name);

        initUserInfo();
        //设置登陆监听
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是登录，还是获取个人信息
                if (mUserInfo.isOauthQQ == false && mUserInfo.isOauthWeiBo == false){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_frame,
                                    PersonInfoFragment.newInstance(),PersonInfoFragment.TAG)
                            .addToBackStack(PersonInfoFragment.TAG)
                            .commit();
                }
            }
        });
    }

    private void initUserInfo(){
        mTvUserName.setText(mUserInfo.userName);
        if (mUserInfo.isOauthWeiBo == false && mUserInfo.isOauthQQ == false){
            mIvIcon.setImageResource(R.mipmap.menu_avatar);
        }
        else {
            //获取网络中的图片
            ImageLoader.getInstance(this)
                    .bindImageFromUrl(mUserInfo.userImageUrl,mIvIcon);
        }
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
    protected void onResume() {
        super.onResume();
        initUserInfo();
    }

    @Override
    protected void initClick() {
        mLvSlide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭侧滑栏
                mDrawerLayout.closeDrawer(Gravity.LEFT);

                if (position == 1){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_frame,
                                    mHomePageFragment,HomePageFragment.TAG)
                            .commit();
                }
                else if (position != 0){
                    ThemeEntity entity = mThemeMenuAdapter.getItem(position-1);
                    getSupportActionBar().setTitle(entity.getName());
                    //切换Fragment
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_frame, ThemeBriefFragment.
                                    newInstance(entity.getId(),entity.getName()),ThemeBriefFragment.TAG)
                            .commit();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {
            super.onBackPressed();
        }

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
    protected void onDestroy() {
        super.onDestroy();
        //存储UserInfo
        UserInfo.saveUserInfo(this,mUserInfo);
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
