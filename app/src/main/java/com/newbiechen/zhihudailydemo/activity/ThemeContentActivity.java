package com.newbiechen.zhihudailydemo.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.newbiechen.androidlib.base.BaseActivity;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.androidlib.utils.ImageLoader;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.StoryContentEntity;
import com.newbiechen.zhihudailydemo.utils.URLManager;
import com.newbiechen.zhihudailydemo.widget.CommentActionProvider;
import com.newbiechen.zhihudailydemo.widget.PraiseActionProvider;

/**
 * Created by PC on 2016/10/12.
 */

public class ThemeContentActivity extends AppBaseActivity {
    private static final String TAG = "ThemeContentActivity";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_IMG = "img";

    private WebView mWebView;
    private ImageView mIvTitle;
    private MenuItem mFavoriteMenu;

    private CommentActionProvider mApComment;
    private PraiseActionProvider mApPraise;

    private boolean isCollected = true;

    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_theme_content);

        //设置添加参数
        mWebView = getViewById(R.id.theme_content_webview);
        mIvTitle = getViewById(R.id.theme_content_iv_icon);
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
        String url = URLManager.STORY_CONTENT+getIntent().getIntExtra(EXTRA_URL,0);
        RemoteService.RemoteCallback callback = new RemoteService.RemoteCallback() {
            @Override
            public void onResponse(String data) {
                Gson gson = new Gson();
                StoryContentEntity entity = gson.fromJson(data,StoryContentEntity.class);
                showContent(entity);
                entity.save();
                String imgUrl = entity.getImages().get(0);
                if (imgUrl != null){
                    ImageLoader.getInstance(ThemeContentActivity.this)
                            .bindImageFromUrl(imgUrl,mIvTitle);
                }

            }
        };
        mRemoteService.loadData(url,callback);
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
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
