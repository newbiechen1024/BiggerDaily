package com.newbiechen.zhihudailydemo;

import android.app.Application;

import com.newbiechen.androidlib.utils.MetricsUtils;
import com.newbiechen.androidlib.utils.SharedPreferenceUtils;
import com.newbiechen.androidlib.utils.ToastUtils;
import com.newbiechen.zhihudailydemo.entity.UserInfo;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.litepal.LitePalApplication;

/**
 * Created by PC on 2016/10/1.
 */
public class ZhiHuApplication extends LitePalApplication {

    public static boolean isNightMode = false;
    public static UserInfo sUserInfo;
    //初始化各平台的AppKey
    {
        PlatformConfig.setSinaWeibo("3350831799","300a52d8f0858175d218c09058377c3e");
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        //QQSSO授权
        PlatformConfig.setQQZone("1105731889","sseur5zIAcnKGfiE");
        //支付宝授权
        PlatformConfig.setAlipay("2016111002706971");
        //还缺一个微信授权
        PlatformConfig.setWeixin("wxc75944ead17f318a","4dcd0a1fdafd997b9ca3bc1d13c8336f");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MetricsUtils.init(this);
        ToastUtils.init(this);
        //初始化LitePal
        LitePalApplication.initialize(this);

        //判断当前的模式
        isNightMode = SharedPreferenceUtils.getData(this,"NightMode",false);

        //初始化友盟组件
        UMShareAPI.get(this);

        //初始化个人信息
        sUserInfo = UserInfo.readUserInfo(this);
    }
}
