package com.newbiechen.androidlib.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.newbiechen.androidlib.R;
import com.newbiechen.androidlib.net.Cookie.RemoteCookieJar;
import com.newbiechen.androidlib.utils.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
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
    private static final String TAG = "RemoteService";

    //超时的时间
    private static final int TIME_OUT = 10000;

    private static RemoteService sRemoteService;
    //在主线程中
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
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


    private Request setUpGetRequest(String urlPath, HashMap<String,String> headers,
                                    HashMap<String,String> params){
        //设置Request请求
        Request.Builder builder = new Request.Builder();
        if (headers != null){
            for (Map.Entry<String,String> header : headers.entrySet()){
                builder.addHeader(header.getKey(),header.getValue());
            }
        }
        String url = spliceUrl(urlPath,params);
        builder.url(HttpUrl.parse(url));
        return builder.build();
    }

    /**
     * 封装了Callback回调，将回调设置在主线程
     */

    public static abstract class RemoteCallback implements Callback{
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onRemoteFailure();
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            Runnable runnable = null;
            final String data = response.body().string();
            if (response.isSuccessful()){
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        onResponse(data);
                    }
                };
            }
            else {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        onResponseFailure(response);
                    }
                };
            }
            mHandler.post(runnable);
        }

        public void onRemoteFailure(){
            ToastUtils.makeText("网络连接错误", Toast.LENGTH_LONG);
        }

        public void onResponseFailure(Response response){
            ToastUtils.makeText("网络请求错误code = "+response.code()+",请重试",
                    Toast.LENGTH_LONG);
        }
        public abstract void onResponse(String data);
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
        Request request = setUpGetRequest(urlPath,headers,params);
        Call call = mClient.newCall(request);
        return call.execute();
    }



    public void loadData(String urlPath,Callback callback){
        loadData(urlPath,null,null,callback);
    }
    /**
     * 异步加载
     * @param urlPath
     * @param headers
     * @param params
     * @param callback
     */
    public void loadData(String urlPath, HashMap<String,String> headers,
                         HashMap<String,String> params, @NonNull Callback callback){
        Request request = setUpGetRequest(urlPath,headers,params);
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }
}
