package com.fuerte.riskcontrol.threadpool;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Nevio
 * on 2022/2/8
 */
public class ExecutorManager {


    private static ExecutorManager instance;
    // 核心线程的数量
    private static final int CORE_POOL_SIZE = 3;
    // 最大线程数量
    private static int MAX_POOL_SIZE = 6;
    // 存活时间
    private static long KEEP_ALIVE_TIME = 3;
    private static TimeUnit timeUnit = TimeUnit.MINUTES;
    private ThreadPoolExecutor poolExecutor;

    private ExecutorManager() {
    }

    public static ExecutorManager getInstance() {
        if (instance == null) {
            synchronized (ExecutorManager.class) {
                if (instance == null) {
                    instance = new ExecutorManager();
                }
            }
        }
        return instance;
    }

    public Executor getExecutor() {
        if (poolExecutor == null) {
            ThreadFactory threadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);
            poolExecutor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAX_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    timeUnit,
                    new LinkedBlockingDeque<>(),
                    threadFactory,
                    new ThreadPoolExecutor.AbortPolicy());
        }
        return poolExecutor;
    }


}
