package com.newbiechen.androidlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.newbiechen.androidlib.net.RemoteService;

/**
 * Created by PC on 2016/9/8.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected RemoteService mRemoteService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateView(savedInstanceState);
        mRemoteService = RemoteService.getInstance(this);
        initWidget(savedInstanceState);
        initClick();
        processLogic(savedInstanceState);
    }

    /************************需要继承的抽象类************************************/
    /**
     * 初始化View
     */
    protected abstract void onCreateView(Bundle savedInstanceState);

    /**
     * 初始化零件
     */
    protected abstract void initWidget(Bundle savedInstanceState);
    /**
     * 初始化点击事件
     */
    protected abstract void initClick();
    /**
     * 逻辑使用区
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**************************公共类*******************************************/
    public <VT> VT getViewById(int id){
        return (VT) findViewById(id);
    }

    public void startActivity(Class<? extends AppCompatActivity> activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }
}
