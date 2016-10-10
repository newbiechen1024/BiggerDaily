package com.newbiechen.zhihudailydemo.activity;

import android.os.Bundle;
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

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by PC on 2016/10/4.
 */
public class StoryContentActivity extends AppBaseActivity {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    private WebView mWebView;
    private ImageView mIvTitle;
    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_story_content);
        mWebView = getViewById(R.id.content_webview);
        mIvTitle = getViewById(R.id.content_iv_title);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpWebView();
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        //加载数据
        loadData();
    }

    private void setUpWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
    }

    private void loadData(){
        if (!loadDataFromDb()){
            loadDataFromUrl();
        }
    }

    private boolean loadDataFromDb(){
        List<StoryContentEntity> entities = DataSupport.where("content_id = "+getIntent().getIntExtra(EXTRA_URL,0))
                .find(StoryContentEntity.class);
        int data = getIntent().getIntExtra(EXTRA_URL,0);
        int size = entities.size();
        //从数据库中提取
        if (entities != null && entities.size() > 0){
            StoryContentEntity entity = entities.get(0);
            showContent(entity);
            return true;
        }
        return false;
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
            }
        };
        mRemoteService.loadData(url,callback);
    }

    private void showContent(StoryContentEntity entity){
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + entity.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

        ImageLoader.getInstance(StoryContentActivity.this)
                .bindImageFromUrl(entity.getImage(),mIvTitle);
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
