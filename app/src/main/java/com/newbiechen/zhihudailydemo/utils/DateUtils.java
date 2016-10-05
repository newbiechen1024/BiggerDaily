package com.newbiechen.zhihudailydemo.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by PC on 2016/10/4.
 */
public class DateUtils {

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;//获取当前月份
        int day = calendar.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        return year+""+month+""+day;
    }

    public static String parseDateStr(String dateStr){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String relDate = "";
        try {
            Date date = dateFormat.parse(dateStr);
            if (date != null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                relDate = (month+1)+"月"+day+"日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relDate;
    }
}
