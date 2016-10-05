package com.newbiechen.androidlib.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by PC on 2016/10/2.
 * 为ImageLoader服务的线程池
 */
class ImageThreadPool extends ThreadPoolExecutor {
    //CPU的数量
    private static final int CPU_SIZE = Runtime.getRuntime().availableProcessors();
    //核心线程数
    private static final int CORE_THREAD_SIZE = CPU_SIZE + 2;
    //最大线程数
    private static final int MAX_THREAD_SIZE = CORE_THREAD_SIZE * 2;
    //非核心线程保留时间  3s
    private static final int ALIVE_TIME = 3000;
    //队列最大存储数
    private static final int QUEUE_MAX_SIZE = 128;
    //队列
    private static final BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(QUEUE_MAX_SIZE);

    private static final ThreadFactory FACTORY = new ThreadFactory() {
        AtomicInteger number = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r,"线程"+number.getAndIncrement());
            return thread;
        }
    };

    public ImageThreadPool() {
        super(CORE_THREAD_SIZE, MAX_THREAD_SIZE, ALIVE_TIME, TimeUnit.MILLISECONDS, QUEUE, FACTORY);
    }
}
