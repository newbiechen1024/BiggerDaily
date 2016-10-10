package com.newbiechen.zhihudailydemo.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.newbiechen.androidlib.base.BaseActivity;


import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.net.RemoteService.*;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.StringUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.entity.SplashImageEntity;
import com.newbiechen.zhihudailydemo.utils.SharedPresManager;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.newbiechen.zhihudailydemo.widget.IconView;

/**
 * Created by PC on 2016/10/1.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    private RelativeLayout mRlShowContent;
    private IconView mIvShowLogo;
    private ImageView mIvShowPic;

    private ImageLoader mImageLoader;
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        //界面的处理
        hideStatusBar();
        hideNavigationBar();
        setContentView(R.layout.activity_splash);

        //获取控件
        mRlShowContent = getViewById(R.id.splash_rl_show_content);
        mIvShowLogo = getViewById(R.id.splash_fl_logo_anim);
        mIvShowPic = getViewById(R.id.splash_iv_show_img);
    }

    /**
     * 隐藏状态栏
     */
    private void hideStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //原理应该是Window默认LayoutParams在状态栏下
            this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    /**
     * 隐藏虚拟键盘
     */
    private void hideNavigationBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        mImageLoader = ImageLoader.getInstance(this);
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setUpSloganAnim();
    }

    /**
     * Activity显示时候的动画效果
     */
    private void setUpSloganAnim(){
        //设置展现标语的动画
        Animation sloganShowAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down_to_up);
        sloganShowAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                setUpLogoAnim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //开启动画
        mRlShowContent.startAnimation(sloganShowAnim);
    }

    /**
     * 设置Logo显示的动画
     */
    private void setUpLogoAnim(){
        //展现Logo动画
        mIvShowLogo.startAnimation();
        //Logo动画结束的监听
        mIvShowLogo.setOnIconAnimFinishListener(new IconView.OnIconAnimFinishListener() {
            @Override
            public void onAnimFinish() {
                showSplashImg();
            }
        });
    }

    /**
     * 设置图片显示的动画
     */
    private void showSplashImg(){
        //获取图片的最新地址,并加载到缓存
        RemoteCallback callback = new RemoteCallback(){
            @Override
            public void onResponse(String data) {
                Gson gson = new Gson();
                //判断获取的data是否为json数据(移动的随意行会拦截数据，返回随意行网页)
                //教训：当用到json数据的时候，首先需要判断string是否为json格式
                if (StringUtils.isGoodJson(data)){
                    SplashImageEntity entity = gson.fromJson(data,SplashImageEntity.class);
                    String splashUrlPath = entity.getImg();
                    //存储图片路径
                    SharedPreferenceUtils.saveData(SplashActivity.this,
                                    SharedPresManager.PRES_SPLASH_IMG_URL,splashUrlPath);
                    //加载图片到缓存
                    mImageLoader.loadImageFromUrl(splashUrlPath);
                }
            }
        };
        //远程调用
        RemoteService.getInstance(this).loadData(URLManager.SPLASH_IMG_PATH,callback);
        //加载图片
        setUpImgAnim();
    }

    /**
     * 设置图片显示的动画
     */
    private void setUpImgAnim(){
        //从缓存中获取地址
        String urlPath = SharedPreferenceUtils.
                getData(SplashActivity.this,
                        SharedPresManager.PRES_SPLASH_IMG_URL,"");
        //首先判断缓存中是否存在图片数据
        Bitmap bitmap = mImageLoader.getBitmapFromDiskCache(urlPath);
        if (bitmap != null){
            mIvShowPic.setImageBitmap(bitmap);
        }
        else {
            mIvShowPic.setImageResource(R.mipmap.splash_pic);
        }

        //添加逐渐显示效果，过渡
        Animation alphaAnim = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.alpha_hide_to_show);
        mIvShowPic.startAnimation(alphaAnim);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //跳转到MainActivity
                SplashActivity.this.startActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

