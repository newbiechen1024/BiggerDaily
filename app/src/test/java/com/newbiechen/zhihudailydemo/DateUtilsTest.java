package com.newbiechen.zhihudailydemo;

import com.newbiechen.zhihudailydemo.utils.DateUtils;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.Date;

/**
 * Created by PC on 2016/10/4.
 */

public class DateUtilsTest extends TestCase{

    public void testDateUtils(){
        String data = DateUtils.parseDateStr("20131011");
        Assert.assertNotNull(data);
        System.out.print(data);
    }
}
