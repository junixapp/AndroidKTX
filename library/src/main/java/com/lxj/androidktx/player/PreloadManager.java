package com.lxj.androidktx.player;

import com.blankj.utilcode.util.LogUtils;
import com.danikula.videocache.HttpProxyCacheServer;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 预加载工具，使用AndroidVideoCache实现
 */
public class PreloadManager {

    private static PreloadManager sPreloadManager;

    /**
     * 单线程池，按照添加顺序依次执行{@link PreloadTask}
     */
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    /**
     * 保存正在预加载的{@link PreloadTask}
     */
    private LinkedHashMap<String, PreloadTask> mPreloadTasks = new LinkedHashMap<>();

    /**
     * 标识是否需要预加载
     */
    private boolean mIsStartPreload = true;

    private HttpProxyCacheServer mHttpProxyCacheServer;

    /**
     * 预加载的大小，每个视频预加载1M，这个参数可根据实际情况调整
     */
    public static int PRELOAD_LENGTH = 1024 * 1024;

    private PreloadManager() {
        mHttpProxyCacheServer = ProxyMediaCacheManager.getProxy();
    }

    public static PreloadManager get() {
        if (sPreloadManager == null) {
            synchronized (PreloadManager.class) {
                if (sPreloadManager == null) {
                    sPreloadManager = new PreloadManager();
                }
            }
        }
        return sPreloadManager;
    }

    public static void setPreloadLength(int length){
        PRELOAD_LENGTH = length;
    }

    /**
     * 开始预加载
     *
     * @param rawUrl 原始视频地址
     */
    public void addPreloadTask(String rawUrl) {
        if (isPreloaded(rawUrl)) {
            LogUtils.d("已经预加载过："+rawUrl);
            return;
        }
        PreloadTask task = new PreloadTask();
        task.mRawUrl = rawUrl;
        task.mCacheServer = mHttpProxyCacheServer;
        LogUtils.d("addPreloadTask: " + rawUrl);
        mPreloadTasks.put(rawUrl, task);

        if (mIsStartPreload) {
            //开始预加载
            task.executeOn(mExecutorService);
        }
    }

    /**
     * 判断该播放地址是否已经预加载
     */
    private boolean isPreloaded(String rawUrl) {
        //先判断是否有缓存文件，如果已经存在缓存文件，并且其大小大于1KB，则表示已经预加载完成了
        File cacheFile = mHttpProxyCacheServer.getCacheFile(rawUrl);
        if (cacheFile.exists()) {
            if (cacheFile.length() >= 1024) {
                return true;
            } else {
                //这种情况一般是缓存出错，把缓存删掉，重新缓存
                cacheFile.delete();
                return false;
            }
        }
        //再判断是否有临时缓存文件，如果已经存在临时缓存文件，并且临时缓存文件超过了预加载大小，则表示已经预加载完成了
        File tempCacheFile = mHttpProxyCacheServer.getTempCacheFile(rawUrl);
        if (tempCacheFile.exists()) {
            return tempCacheFile.length() >= PRELOAD_LENGTH;
        }

        return false;
    }

    /**
     * 暂停预加载
     *
     */
    public void pausePreload(String url) {
        LogUtils.d("pausePreload：" + url );
        mIsStartPreload = false;
        PreloadTask preloadTask = mPreloadTasks.get(url);
        if(preloadTask!=null) preloadTask.cancel();
    }

    /**
     * 恢复预加载
     *
     */
    public void resumePreload(String url) {
        LogUtils.d("resumePreload：" + url);
        mIsStartPreload = true;
        PreloadTask preloadTask = mPreloadTasks.get(url);
        if(preloadTask!=null) preloadTask.executeOn(mExecutorService);
    }

    /**
     * 通过原始地址取消预加载
     *
     * @param rawUrl 原始地址
     */
    public void removePreloadTask(String rawUrl) {
        PreloadTask task = mPreloadTasks.get(rawUrl);
        if (task != null) {
            task.cancel();
            mPreloadTasks.remove(rawUrl);
        }
    }

    /**
     * 取消所有的预加载
     */
    public void removeAllPreloadTask() {
        Iterator<Map.Entry<String, PreloadTask>> iterator = mPreloadTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PreloadTask> next = iterator.next();
            PreloadTask task = next.getValue();
            task.cancel();
            iterator.remove();
        }
    }

    /**
     * 获取播放地址
     */
    public String getPlayUrl(String rawUrl) {
        PreloadTask task = mPreloadTasks.get(rawUrl);
        if (task != null) {
            task.cancel();
        }
        if (isPreloaded(rawUrl)) {
            return mHttpProxyCacheServer.getProxyUrl(rawUrl);
        } else {
            return rawUrl;
        }
    }
}
