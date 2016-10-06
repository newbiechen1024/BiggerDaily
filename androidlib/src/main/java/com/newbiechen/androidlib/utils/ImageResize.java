package com.newbiechen.androidlib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by PC on 2016/10/2.
 * 压缩图片
 * 原理：
 * 1、获取当前图片的宽高，判断是否符合具体要求
 * 2、如果不符合要求下修改图片的Sample(最好是2的倍数)
 * 3、再判断是否符合要求，如果符合要求则，返回具体图片
 */
public class ImageResize {

    //默认压缩的宽度(屏幕的宽度)
    private static final int DEFAULT_COMPRESS_WIDTH = MetricsUtils.getScreenWidth();
    //默认压缩的高度(屏幕的高度)
    private static final int DEFAULT_COMPRESS_HEIGHT = MetricsUtils.getScreenHeight();

    private ImageResize(){
    }

    /**
     * 通过FileDescriptor缩放图片
     * @param fd          文件流
     * @param reqWidth    图片最大宽度
     * @param reqHeight   图片最大高度
     * @return
     */
    public static Bitmap compressBitmapFromFD(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (fd != null){
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd,null,options);
            options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
            options.inJustDecodeBounds = false;
        }
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    /**
     * 获取缩放的值
     * @param options  Bitmap的配置信息
     * @param reqWidth  缩放的宽度
     * @param reqHeight 缩放的高度
     * @return  缩放的倍数
     */
    private static int calculateSampleSize(BitmapFactory.Options options,
                                           int reqWidth, int reqHeight){
        int sampleSize = 1;
        int width = options.outWidth;
        int height = options.outHeight;

        if (width/2 > reqWidth || height/2 > reqHeight){
            sampleSize = 2;
            while (width/sampleSize > reqWidth ||
                    height/sampleSize > reqHeight){
                //一般使用2的倍数来进行缩放
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }
}
