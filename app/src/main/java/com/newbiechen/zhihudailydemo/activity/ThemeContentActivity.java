package com.newbiechen.zhihudailydemo.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newbiechen.androidlib.base.BaseActivity;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.androidlib.utils.ToastUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.StoryContentEntity;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.newbiechen.zhihudailydemo.widget.CommentActionProvider;
import com.newbiechen.zhihudailydemo.widget.PraiseActionProvider;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * Created by PC on 2016/10/12.
 */

public class ThemeContentActivity extends AppBaseActivity {
    private static final String TAG = "ThemeContentActivity";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    private WebView mWebView;
    private ImageView mIvTitle;
    private AppBarLayout mAppBarLayout;
    private MenuItem mFavoriteMenu;


    private CommentActionProvider mApComment;
    private PraiseActionProvider mApPraise;

    private boolean isCollected = true;
    private String mUrlPath;
    private String mTitle;
    private String mImageUrl;

    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_theme_content);

        //设置添加参数
        mWebView = getViewById(R.id.theme_content_webview);
        mIvTitle = getViewById(R.id.theme_content_iv_icon);
        mAppBarLayout = getViewById(R.id.theme_content_abl_title);
        initData();
    }

    private void initData(){
        mUrlPath = URLManager.STORY_CONTENT+getIntent().getIntExtra(EXTRA_URL,0);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        //不显示Title
        getSupportActionBar().setTitle("");
        //设置WebView
        setUpWebView();
        //加载数据
        loadDataFromUrl();
    }

    private void setUpWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings()
                .setAppCacheEnabled(true);
    }

    private void loadDataFromUrl(){
        //从网络中提取
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                Gson gson = new Gson();
                StoryContentEntity entity = gson.fromJson(data,StoryContentEntity.class);
                showContent(entity);
                entity.save();
                if (entity.getImages() != null){
                    String imgUrl = entity.getImages().get(0);
                    mIvTitle.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance(ThemeContentActivity.this)
                            .bindImageFromUrl(imgUrl,mIvTitle);
                    mImageUrl = imgUrl;
                }
            }
        };
        mRemoteService.loadData(mUrlPath,callback);
    }

    private void showContent(StoryContentEntity entity){
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + entity.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //改变收藏按钮的颜色
        if (hasFocus && isCollected){
            mFavoriteMenu.setIcon(R.mipmap.collected);
        }
        mApComment.setCommentCount(10);
        mApPraise.setPraiseCount(20);
        mApPraise.setIsSelect(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //设置菜单
        getMenuInflater().inflate(R.menu.menu_theme_content,menu);
        //获取收藏控件
        mFavoriteMenu = menu.findItem(R.id.theme_content_favorite);
        //获取评论按钮
        MenuItem commentMenu = menu.findItem(R.id.theme_content_comment);
        mApComment = (CommentActionProvider) MenuItemCompat.getActionProvider(commentMenu);
        //获取点赞按钮
        MenuItem praiseMenu = menu.findItem(R.id.theme_content_praise);
        mApPraise = (PraiseActionProvider) MenuItemCompat.getActionProvider(praiseMenu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.theme_content_favorite:
                if (isCollected){
                    //取消收藏,并在无网络环境下无法点击
                    mFavoriteMenu.setIcon(R.mipmap.collect);
                    isCollected = false;
                }
                else {
                    mFavoriteMenu.setIcon(R.mipmap.collected);
                    //设置收藏
                    isCollected = true;
                }
                break;
            case R.id.theme_content_share:
                openShareController();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void openShareController(){
        //创建分享时候的图片
        UMImage shareImg;
        if(mImageUrl != null){
            shareImg = new UMImage(this, mImageUrl);
        }
        else {
            shareImg = new UMImage(this, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        }

        //创建分享面板
        new ShareAction(this).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.ALIPAY,SHARE_MEDIA.EMAIL,SHARE_MEDIA.SMS,SHARE_MEDIA.QZONE,SHARE_MEDIA.MORE)
                .withTitle("装逼日报")
                .withText(mTitle+"---本文来自装逼日报")
                .withMedia(shareImg)
                .withTargetUrl(mUrlPath)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.makeText("分享成功", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.makeText("分享失败", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastUtils.makeText("取消分享",Toast.LENGTH_SHORT);
                    }
                }).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //提供授权的Activity页面必须使用
        mUMShareAPI.onActivityResult(requestCode,resultCode,data);
    }
}
