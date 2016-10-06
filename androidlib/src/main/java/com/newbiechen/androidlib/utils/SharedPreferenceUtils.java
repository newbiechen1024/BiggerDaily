package com.newbiechen.androidlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PC on 2016/10/6.
 */

public class SharedPreferenceUtils {

    private static final String FILE_NAME = "default_name";

    public static void saveData(Context context,String key, String value){
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getData(Context context,String key,String defValue){
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).
                getString(key,defValue);
    }
}
