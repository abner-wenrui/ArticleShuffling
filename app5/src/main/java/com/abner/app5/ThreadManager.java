package com.abner.app5;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Project   com.abner.app5
 *
 * @Author Abner
 * Time   2016/11/12.20:38
 */

public class ThreadManager {

    private static ThreadPool mThreadPool;


    public static ThreadPool getThreadPull() {
        if (mThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPool == null)
                    mThreadPool = new ThreadPool(10, 5, 1000);
            }
        }
        return mThreadPool;
    }

    //线程池
    public static class ThreadPool {
        private ThreadPoolExecutor executor;
        private int maximumPoolSize;
        long keepAliveTime;
        int corePoolSize;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;

        }

        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,     //核心线程数，最大线程数
                        keepAliveTime, java.util.concurrent.TimeUnit.SECONDS,               //线程休眠时间，时间单位
                        new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(),  //线程队列，线程工厂
                        new ThreadPoolExecutor.AbortPolicy());
            }
            //执行一个Runnable对象
            executor.execute(r);
        }
    }
}
