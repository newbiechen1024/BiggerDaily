package com.newbiechen.androidlib.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by PC on 2016/10/1.
 *
 */
public final class IOUtils {
    private static final String TAG = "IOUtils";

    //禁止被实例化
    private IOUtils(){
    }


    /**
     * 获取输入流的数据，并转化成String
     * @param is
     * @return
     */
    public static String input2Str(InputStream is) throws IOException{
        if (is == null){
            return "";
        }

        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String str = null;
        while ((str = br.readLine()) != null){
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static void closeStream(Closeable closeable){
        if (closeable == null){
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"流关闭出错");
        }
    }
}
