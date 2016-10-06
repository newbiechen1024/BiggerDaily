package com.newbiechen.zhihudailydemo.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.newbiechen.androidlib.base.BaseActivity;


import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.net.RemoteService.*;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.entity.SplashImageEntity;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.newbiechen.zhihudailydemo.widget.IconView;

/**
 * Created by PC on 2016/10/1.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    private static final String SPLASH_IMG = "splash_img";
    private static final String NO_NETWORK = "NoNetWork";
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
    protected void processLogin(Bundle savedInstanceState) {
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
        RemoteCallback callback = new RemoteCallback(){
            @Override
            public void onRemoteFailure() {
                String urlPath = SharedPreferenceUtils.
                        getData(SplashActivity.this,SPLASH_IMG,NO_NETWORK);
                setUpImgAnim(urlPath);
            }

            @Override
            public void onResponse(String data) {
                Gson gson = new Gson();
                SplashImageEntity entity = gson.fromJson(data,SplashImageEntity.class);
                SharedPreferenceUtils.
                        saveData(SplashActivity.this,SPLASH_IMG,entity.getImg());
                setUpImgAnim(entity.getImg());
            }
        };
        //远程调用
        RemoteService.getInstance(this).loadData(URLManager.SPLASH_IMG_PATH,callback);
    }

    /**
     * 设置图片显示的动画
     * @param urlPath
     */
    private void setUpImgAnim(String urlPath){
        //首先判断缓存中是否存在图片数据
        Bitmap bitmap = mImageLoader.getBitmapFromDiskCache(urlPath);
        if (bitmap != null){
            mIvShowPic.setImageBitmap(bitmap);
        }
        else {
            mIvShowPic.setImageResource(R.mipmap.splash_pic);
            //第一次加载没网环境下，不加载
            if (urlPath != NO_NETWORK){
                //并加载图片数据
                mImageLoader.loadImageFromUrl(urlPath);
            }
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

