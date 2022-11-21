package com.device.deviceinfosdk.rxjava;

import android.os.Process;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;


/**
 * 优先级线程工厂
 * @author Nevio
 * on 2019/5/22
 **/
public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;

    PriorityThreadFactory(int threadPriority) {
        mThreadPriority = threadPriority;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        Runnable wrapperRunnable = () -> {
            try {
                Process.setThreadPriority(mThreadPriority);
            } catch (Exception e) {
                e.printStackTrace();
            }
            runnable.run();
        };
        return new Thread(wrapperRunnable);
    }

}
