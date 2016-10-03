package com.newbiechen.androidlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.disklrucache.DiskLruCache;
import com.newbiechen.androidlib.net.RemoteService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;

import okhttp3.Response;

/**
 * Created by PC on 2016/10/2.
 * 图片加载框架
 * 步骤：
 * 1、创建该类的单例模式和外部调用的方法
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    //二级缓存的名字
    private static final String CACHE_FILE_NAME = "bitmap";
    //50MB
    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024;

    private static final int DISK_INDEX = 0;
    //线程池的创建
    private static final ExecutorService THREAD_POOL = new DefaultThreadPool();

    private static ImageLoader sImageLoader;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private LruCache<String,Bitmap> mLruCache;
    private DiskLruCache mDiskCache;
    private Context mContext;

    private boolean isDiskCacheExist = true;

    private ImageLoader(Context context){
        mContext = context;
        setUpLruCache();
        setUpDiskCache();
    }

    private void setUpLruCache(){
        //将最大内存数转换成kb （之后所有放入LruCache的数据都要除以1024）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //取最大内存数的1/8
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //意思是：每行的字节数除以行数就是该Bitmap的大小，并除以1024转化成kb
                return value.getRowBytes()*value.getHeight() / 1024;
            }
        };
    }

    private void setUpDiskCache(){
        try {
            //获取二级缓存的存储目录
            File cacheDir = FileUtils.getCachePath(mContext,CACHE_FILE_NAME);
            //判断当前磁盘是否可以创建二级缓存
            if (FileUtils.getSDFreeSize() >= DISK_CACHE_SIZE){
                mDiskCache = DiskLruCache.open(cacheDir,1,1,DISK_CACHE_SIZE);
            }
            else {
                ToastUtils.makeText("创建缓存目录失败，请清空磁盘", Toast.LENGTH_LONG);
                isDiskCacheExist = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"创建缓存文件夹失败");
        }
    }

    private Bitmap getBitmapFromLruCache(String key){
        return mLruCache.get(key);
    }

    /**
     * 从网络中提取数据，并写入缓存
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromUrl(String url){
        String key = str2HexStr(url);
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            //同步获取数据
            Response response = RemoteService.getInstance(mContext).loadData(url,null,null);
            if (response.isSuccessful()){
                //获取数据流(不直接获取String是因为，数据太大，不合适)
                is = response.body().byteStream();
                if (isDiskCacheExist){
                    //将数据缓存到二级缓存中
                    saveBitmap2DiskCache(key,is);
                }
                else {
                    bitmap = BitmapFactory.decodeStream(is);
                }
            }
            else {
                Log.d(TAG,"获取数据失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"网络加载失败");
        } finally {
            IOUtils.closeStream(is);
        }
        if (isDiskCacheExist){
            bitmap = getBitmapFromDiskCache(url);
        }
        return bitmap;
    }



    private Bitmap loadBitmap(String urlPath){
        //将网址转换成对应的haxString
        String key  = str2HexStr(urlPath);
        //首先从一级缓存中查找是否存在图片
        Bitmap bitmap = getBitmapFromLruCache(key);

        //查看二级缓存是否存在图
        if (bitmap == null) {
            bitmap = getBitmapFromDiskCache(urlPath);
        }

        //从网络中获取图片
        if (bitmap == null){
            bitmap = loadBitmapFromUrl(urlPath);
        }
        return bitmap;
    }

    /**
     * 存储数据到一级缓存
     * @param key
     * @param bitmap
     */
    private void saveBitmap2LruCache(String key,Bitmap bitmap){
        if(bitmap != null){
            mLruCache.put(key,bitmap);
        }
    }

    /**
     * 存储数据到二级缓存
     * @param key
     * @param
     */
    private void saveBitmap2DiskCache(String key,InputStream input){
        if (!isDiskCacheExist && input != null){
            return;
        }
        OutputStream output = null;
        try {
            DiskLruCache.Editor editor = mDiskCache.edit(key);
            if (editor != null){
                output = editor.newOutputStream(DISK_INDEX);

                int data = 0;
                while ((data = input.read()) != -1){
                    output.write(data);
                }

                editor.commit();
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(output);
        }
    }


    /**
     * 将key用MD5加密
     */
    private String str2HexStr(String key){
        String cacheKey = null;
        try {
            //进行MD5加密
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = byteToHashString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    //将获取到byte值转换成String
    private String byteToHashString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<bytes.length; ++i){
            String hash = Integer.toHexString(0xFF & bytes[i]);
            if (hash.length() == 1){
                sb.append('0');
            }
            sb.append(hash);
        }
        return sb.toString();
    }

    /**
     * 当图片加载完成的回调。
     */
    public interface Callback {
        void onImageLoad(Bitmap bitmap);
    }
    /*****************************公共的方法*************************************/

    /**
     * 单例模式
     * @return
     */
    public static ImageLoader getInstance(Context context){
        synchronized (ImageLoader.class){
            if (sImageLoader == null){
                sImageLoader = new ImageLoader(context);
            }
        }
        return sImageLoader;
    }

    /**
     * 从二级缓存中提取数据
     * @param urlPath ：url对应的hexString作为key
     * @return bitmap ：返回图片
     */
    public Bitmap getBitmapFromDiskCache(String urlPath){
        //转换成hexStr
        String key = str2HexStr(urlPath);
        //首先判断DiskCache是否存在
        if(!isDiskCacheExist){
            return null;
        }
        //提取DiskCache中的数据
        DiskLruCache.Snapshot snapshot = null;
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            //获取key对应的文件的句柄
            snapshot = mDiskCache.get(key);
            //如果句柄存在，说明文件存在。补句柄不存在说明没有该数据
            if (snapshot != null){
                fis = (FileInputStream)snapshot.getInputStream(DISK_INDEX);
                FileDescriptor fd = fis.getFD();
                //压缩获取Bitmap
                bitmap = ImageResize.compressBitmapFromFD(fd);
                //将压缩完成后的Bitmap添加到一级缓存中
                if (bitmap != null){
                    saveBitmap2LruCache(key,bitmap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            if (snapshot != null){
                snapshot.close();
            }
            IOUtils.closeStream(fis);
        }
        return bitmap;
    }


    /**
     * 直接加载图片数据，不经过缓存查询
     * @param urlPath
     * @param callback
     */
    public void loadImageFromUrl(final String urlPath, final Callback callback){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //直接加载数据
                Bitmap bitmap = loadBitmapFromUrl(urlPath);
                if (callback != null){
                    callback.onImageLoad(bitmap);
                }
            }
        };
        THREAD_POOL.execute(runnable);
    }

    /**
     * 从网络中加载图片，并将图片显示到指定的ImageView中
     * @param url           网址
     * @param imageView     显示图片的控件
     */
    public void bindImageFromUrl(final String url, final ImageView imageView){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                final Bitmap bitmap = loadBitmap(url);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null){
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        };
        THREAD_POOL.execute(runnable);
    }
}
