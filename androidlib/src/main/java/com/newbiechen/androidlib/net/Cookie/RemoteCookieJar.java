package com.newbiechen.androidlib.net.Cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by PC on 2016/10/2.
 */
public class RemoteCookieJar implements CookieJar {
    private PersistentCookieStore mCookieStore;

    public RemoteCookieJar(Context context) {
        mCookieStore = new PersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies){
            mCookieStore.add(url,cookie);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return mCookieStore.get(url);
    }
}
