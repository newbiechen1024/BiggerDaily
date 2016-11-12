package com.newbiechen.zhihudailydemo;

import android.content.Context;
import android.provider.Settings;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.newbiechen.zhihudailydemo.entity.UserInfo;

/**
 * Created by PC on 2016/11/11.
 */

public class UserInfoTest extends AndroidTestCase{
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = new MockContext();
    }

    public void testContext(){
        assertNotNull(mContext);
        UserInfo userInfo = UserInfo.readUserInfo(mContext);
        System.out.print(userInfo.loginMethod);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
