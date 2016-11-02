package com.newbiechen.zhihudailydemo;

import android.app.Application;

import com.newbiechen.androidlib.utils.MetricsUtils;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.ToastUtils;

import org.litepal.LitePalApplication;

/**
 * Created by PC on 2016/10/1.
 */
public class ZhiHuApplication extends LitePalApplication {
    public static boolean isNightMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MetricsUtils.init(this);
        ToastUtils.init(this);
        //初始化LitePal
        LitePalApplication.initialize(this);

        //判断当前的模式
        isNightMode = SharedPreferenceUtils.getData(this,"NightMode",false);
    }
}
