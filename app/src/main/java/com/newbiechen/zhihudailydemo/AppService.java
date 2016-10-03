package com.newbiechen.zhihudailydemo;

import android.app.Application;

import com.newbiechen.androidlib.utils.MetricsUtils;
import com.newbiechen.androidlib.utils.ToastUtils;

/**
 * Created by PC on 2016/10/1.
 */
public class AppService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MetricsUtils.init(this);
        ToastUtils.init(this);
    }
}
