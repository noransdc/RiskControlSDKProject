package com.fuerte.riskcontrol.threadpool;

import android.os.Process;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单线程线程池
 * @author Nevio
 * on 2019/4/17
 **/
public class SingleThreadPool {

    private static SingleThreadPool instance;
    // 核心线程的数量
    private final int CORE_POOL_SIZE = 1;
    // 最大线程数量
    private final int MAX_POOL_SIZE = 1;
    // 存活时间
    private final long KEEP_ALIVE_TIME = 0L;
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    private ThreadPoolExecutor poolExecutor;

    private SingleThreadPool() {}

    public static SingleThreadPool getInstance() {
        if (instance == null) {
            synchronized (SingleThreadPool.class) {
                if (instance == null) {
                    instance = new SingleThreadPool();
                }
            }
        }
        return instance;
    }

    public void init() {
        ThreadFactory threadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);
        poolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            timeUnit,
            new LinkedBlockingDeque<>(),
            threadFactory,
            new ThreadPoolExecutor.AbortPolicy());
        poolExecutor.allowCoreThreadTimeOut(false);
    }

    public void execute(Runnable runnable) {
        if (poolExecutor == null) {
            return;
        }
        poolExecutor.execute(runnable);
    }

    public Future submit(Runnable runnable) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.submit(runnable);
    }

    public Future submit(Callable callable) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.submit(callable);
    }

    public Future submit(FutureTask futureTask) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.submit(futureTask);
    }

    public void remove(Runnable runnable) {
        if (poolExecutor == null) {
            return;
        }
        poolExecutor.remove(runnable);
    }

}
