package com.newbiechen.zhihudailydemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newbiechen.androidlib.utils.ToastUtils;
import com.newbiechen.zhihudailydemo.R;
import com.newbiechen.zhihudailydemo.ZhiHuApplication;
import com.newbiechen.zhihudailydemo.base.AppBaseActivity;
import com.newbiechen.zhihudailydemo.entity.UserInfo;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by PC on 2016/11/10.
 */

public class LoginActivity extends AppBaseActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";

    private Button mBtnWBLogin;
    private Button mBtnQQLogin;

    private SHARE_MEDIA mPlatform;
    private UserInfo mUserInfo;
    @Override
    protected void onCreateContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

        mBtnWBLogin = getViewById(R.id.login_btn_wb);
        mBtnQQLogin = getViewById(R.id.login_btn_qq);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        mUserInfo = ZhiHuApplication.sUserInfo;
    }

    @Override
    protected void initClick() {
        mBtnWBLogin.setOnClickListener(this);
        mBtnQQLogin.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_wb:
                mPlatform = SHARE_MEDIA.SINA;
                mUMShareAPI.getPlatformInfo(this,mPlatform,mUmAuthListener);
                break;
            case R.id.login_btn_qq:
                mPlatform = SHARE_MEDIA.QQ;
                ToastUtils.makeText("暂未开放",Toast.LENGTH_SHORT);
                break;
        }
/*        mUMShareAPI.getPlatformInfo(this,mPlatform,mUmAuthListener);*/
    }

    private UMAuthListener mUmAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            mUserInfo.isOauthWeiBo = true;
            mUserInfo.userName = map.get("screen_name");
            mUserInfo.userImageUrl = map.get("profile_image_url");
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.makeText("登陆失败", Toast.LENGTH_SHORT);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.makeText("取消登陆", Toast.LENGTH_SHORT);
        }
    };
}
