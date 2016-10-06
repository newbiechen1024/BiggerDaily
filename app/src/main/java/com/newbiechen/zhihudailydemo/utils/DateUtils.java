package com.newbiechen.zhihudailydemo.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by PC on 2016/10/4.
 * date几号的格式是1而不是01
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
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int datOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                relDate = (month+1)+"月"+dayOfMonth+"日"+"  星期"+changeNum2Chinese(datOfWeek);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relDate;
    }

    private static String changeNum2Chinese(int num){
        String data = "";
        switch (num){
            case 1:
                data = "日";
                break;
            case 2:
                data = "一";
                break;
            case 3:
                data = "二";
                break;
            case 4:
                data = "三";
                break;
            case 5:
                data = "四";
                break;
            case 6:
                data = "五";
                break;
            case 7:
                data = "六";
                break;
        }
        return data;
    }
}
