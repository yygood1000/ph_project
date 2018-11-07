package com.topjet.common.widget.bottomlayout.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * creator: zhulunjun
 * time:    2018/3/6
 * describe: 使用线程池来管理下载线程，避免资源浪费
 */

public class DownLoadImageManager {
    /**
     * 单线程列队执行
     */
    private static ExecutorService singleExecutor = null;

    /**
     * 执行单线程列队执行
     */
    public static void runOnQueue(Runnable runnable) {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.submit(runnable);
    }

}
