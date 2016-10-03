package com.newbiechen.androidlib.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by PC on 2016/10/2.
 */
public class FileUtils {

    //禁止实例化
    private FileUtils(){
    }

    public static File getCachePath(Context context,String fileName){
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.
                equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable()){
            //获取刘路径
            cachePath = context.getExternalCacheDir().getPath();
        }
        else {
            cachePath = context.getCacheDir().getPath();
        }
        //设置文件路径
        cachePath = cachePath + File.separator + fileName;
        //判断文件路径是否存在，不存在则创建文件路径
        File cacheDir = new File(cachePath);
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        return cacheDir;
    }


    /**
     * 获取SD卡的剩余空间
     * 消除警告：StatFs类的新方法 只在Api-18才能使用，现在是Api-17所以只能使用旧方法
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return freeBlocks * blockSize; //单位byte
    }

    //SD卡总容量
    public static long getSDAllSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        //return (allBlocks * blockSize)/1024/1024; //单位MB
    }
}
