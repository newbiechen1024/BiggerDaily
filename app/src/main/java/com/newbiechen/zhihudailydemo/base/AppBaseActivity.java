package com.newbiechen.zhihudailydemo.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.newbiechen.androidlib.base.BaseActivity;
import com.newbiechen.androidlib.net.RemoteService;
import com.newbiechen.zhihudailydemo.R;

/**
 * Created by PC on 2016/10/3.
 */
public abstract class AppBaseActivity extends BaseActivity {
    protected Toolbar mToolbar;
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        //初始化View
        onCreateContentView(savedInstanceState);
        //初始化Widget
        setUpToolBar();
    }

    private void setUpToolBar(){
        //获取Toolbar
        mToolbar = getViewById(R.id.toolbar);
        //标题的颜色的白色
        mToolbar.setTitleTextColor(Color.WHITE);
        //与Activity关联
        setSupportActionBar(mToolbar);
        //允许显示左上角的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract void onCreateContentView(Bundle savedInstanceState);
}
