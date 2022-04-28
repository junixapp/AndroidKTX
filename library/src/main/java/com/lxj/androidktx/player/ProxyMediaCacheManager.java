package com.lxj.androidktx.player;

import android.content.Context;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.StorageUtils;
import com.lxj.androidktx.AndroidKTX;
import com.lxj.androidktx.livedata.StateLiveData;
import com.lxj.androidktx.util.DirManager;

import java.io.File;

public class ProxyMediaCacheManager {

    private static HttpProxyCacheServer sharedProxy;

    private ProxyMediaCacheManager() {
    }

    public static HttpProxyCacheServer getProxy() {
        return sharedProxy == null ? (sharedProxy = newProxy()) : sharedProxy;
    }

    private static StateLiveData<CacheInfo> cacheState = new StateLiveData<>();
    private static HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer server = new HttpProxyCacheServer.Builder(AndroidKTX.context)
                .maxCacheSize(512 * 1024 * 1024)       // 512MB for cache
                //缓存路径，不设置默认在sd_card/Android/data/[app_package_name]/cache中
                .cacheDirectory(new File(DirManager.INSTANCE.getCacheDir() + "/.media-cache"))
                .build();
        return server;
    }

    /**
     * 删除所有缓存文件
     * @return 返回缓存是否删除成功
     */
    public static boolean clearAllCache() {
        getProxy();
        return StorageUtils.deleteFiles(sharedProxy.getCacheRoot());
    }

    /**
     * 删除url对应默认缓存文件
     * @return 返回缓存是否删除成功
     */
    public static boolean clearDefaultCache(String url) {
        getProxy();
        File pathTmp = sharedProxy.getTempCacheFile(url);
        File path = sharedProxy.getCacheFile(url);
        return StorageUtils.deleteFile(pathTmp.getAbsolutePath()) &&
                StorageUtils.deleteFile(path.getAbsolutePath());

    }

}