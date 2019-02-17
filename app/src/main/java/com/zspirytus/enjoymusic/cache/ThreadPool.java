package com.zspirytus.enjoymusic.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static final int THREAD_POOL_SIZE = 20;
    private static ThreadPool INSTANCE = new ThreadPool();

    private ExecutorService mExecutorService;

    private ThreadPool() {
    }

    public static void execute(Runnable r) {
        ExecutorService executorService = INSTANCE.mExecutorService;
        if (executorService == null) {
            executorService = INSTANCE.mExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        }
        executorService.execute(r);
    }
}
