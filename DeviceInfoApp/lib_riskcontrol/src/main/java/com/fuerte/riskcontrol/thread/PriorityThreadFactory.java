package com.risk.riskcontrol.thread;

import android.os.Process;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;


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
