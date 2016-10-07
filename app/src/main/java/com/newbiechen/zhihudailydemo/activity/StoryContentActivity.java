package com.newbiechen.zhihudailydemo.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.newbiechen.androidlib.base.BaseActivity;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;

/**
 * Created by PC on 2016/10/4.
 */
public class StoryContentActivity extends AppBaseActivity {
    public static final String EXTRA_URL = "url";
    private WebView mWebView;
    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_story_content);

        mWebView = getViewById(R.id.story_content_webview);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {

    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void processLogin(Bundle savedInstanceState) {

    }
}
