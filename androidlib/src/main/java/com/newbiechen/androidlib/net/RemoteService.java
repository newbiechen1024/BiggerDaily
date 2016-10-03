package com.newbiechen.androidlib.net;

import android.content.Context;

import com.newbiechen.androidlib.net.Cookie.RemoteCookieJar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PC on 2016/10/2.
 * 网络交互工具
 * 1、创建和配置OkHttp
 * 2、设置Cookie缓存
 */
public class RemoteService {
    //超时的时间
    private static final int TIME_OUT = 10000;

    private static RemoteService sRemoteService;
    private OkHttpClient mClient;
    private Context mContext;

    private RemoteService(Context context){
        mContext = context;
        setUpClient();
    }


    private void setUpClient(){
        mClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT,TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .cookieJar(new RemoteCookieJar(mContext))
                .build();
    }

    /**
     * get请求的时候拼接明文参数
     * @return
     */
    private static String spliceUrl(String urlPath,HashMap<String,String> params){
        StringBuilder sb = new StringBuilder();
        sb.append(urlPath);
        if (params != null){
            int i=0;
            for(Map.Entry<String,String> param : params.entrySet()){
                if (i == 0){
                    sb.append("?");
                }
                else {
                    sb.append("&");
                }
                sb.append(param.getKey()+"="+param.getValue());
                i += 1;
            }
        }
        return sb.toString();
    }

    /***********************公共方法*****************************************/
    public static RemoteService getInstance(Context context){
        synchronized (RemoteService.class){
            if (sRemoteService == null){
                sRemoteService = new RemoteService(context);
            }
        }
        return sRemoteService;
    }

    /**
     * 同步加载、只接受GET请求
     */

    public Response loadData (String urlPath,
                                     HashMap<String,String> headers,
                                     HashMap<String,String> params) throws IOException{
        //设置Request请求
        Request.Builder builder = new Request.Builder();
        if (headers != null){
            for (Map.Entry<String,String> header : headers.entrySet()){
                builder.addHeader(header.getKey(),header.getValue());
            }
        }
        String url = spliceUrl(urlPath,params);
        builder.url(HttpUrl.parse(url));
        Call call = mClient.newCall(builder.build());
        return call.execute();
    }
}
