package com.newbiechen.androidlib.utils;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Created by PC on 2016/10/8.
 */

public final class StringUtils {
    private static final String TAG = "StringUtils";

    /**
     * 判断String是否是json格式
     * @param json
     * @return
     */
    public static boolean isGoodJson(String json){
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            Log.w(TAG,"bad json");
            return false;
        }
    }
}
