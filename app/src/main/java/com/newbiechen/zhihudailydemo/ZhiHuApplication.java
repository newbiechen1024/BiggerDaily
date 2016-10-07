package com.newbiechen.zhihudailydemo;

import android.app.Application;

import com.newbiechen.androidlib.utils.MetricsUtils;
import com.newbiechen.androidlib.utils.ToastUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

/**
 * Created by PC on 2016/10/1.
 */
public class ZhiHuApplication extends LitePalApplication {
    private static RefWatcher sRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        MetricsUtils.init(this);
        ToastUtils.init(this);
        //初始化LitePal
        LitePalApplication.initialize(this);

    }

    public static RefWatcher getRefWatcher(){
        return getRefWatcher();
    }
}
