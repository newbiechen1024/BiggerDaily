package com.newbiechen.androidlib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by PC on 2016/10/2.
 */
public class MetricsUtils {
    private static DisplayMetrics sMetrics;

    private MetricsUtils(Context context){
    }

    public static void init(Context context){
        synchronized (MetricsUtils.class){
            if (sMetrics == null){
                sMetrics = context.getResources().getDisplayMetrics();
            }
        }
    }

    public static int getScreenWidth(){
        return sMetrics.widthPixels;
    }

    public static int getScreenHeight(){
        return sMetrics.heightPixels;
    }

    public static float dp2px(int dp){
        return sMetrics.density * dp +0.5f;
    }

    public static float px2dp(int px){
        return px/sMetrics.density + 0.5f;
    }
}




