package cn.qssq666.audio.voiceutil.utils;

import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luozheng on 16/1/27.
 * getExternalStorageDirectory
 */
public class MediaDirectoryUtils {

    public static File getTempCacheWavFileName() {
        if (mediaManagerProvider != null && mediaManagerProvider.getTempCacheWavFileName() != null) {
            return mediaManagerProvider.getTempCacheWavFileName();
        }
        return getTempCacheFileName(".wav");
    }

    public static File getTempCachePcmFileName() {
        if (mediaManagerProvider != null && mediaManagerProvider.getTempCachePcmFileName() != null) {
            return mediaManagerProvider.getTempCachePcmFileName();
        }
        return getTempCacheFileName(".pcm");
    }

    public static File getTempMp3FileName() {
        if (mediaManagerProvider != null && mediaManagerProvider.getTempMp3FileName() != null) {
            return mediaManagerProvider.getTempMp3FileName();
        }
        File tempCacheFileName = getTempCacheFileName(".mp3");

        return tempCacheFileName;
    }

    public static File getTempAACFileName() {
        if (mediaManagerProvider != null && mediaManagerProvider.getTempAACFileName() != null) {
            return mediaManagerProvider.getTempAACFileName();
        }
        return getTempCacheFileName(".aac");
    }

    public static File getTempAmrFileName() {
        if (mediaManagerProvider != null && mediaManagerProvider.getTempAmrFileName() != null) {
            return mediaManagerProvider.getTempAmrFileName();
        }
        return getTempCacheFileName(".amr");
    }

    private static File getTempCacheFileName(String pex) {
        File file = new File(getCachePath(), productSimpleFileName(pex));
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private static String getAmrSimpleFileName() {

        return productSimpleFileName(".amr");
    }

    private static String getWavSimpleFileName() {
        return productSimpleFileName(".wav");
    }


    @NonNull
    public static File getCachePath() {

        if (mediaManagerProvider != null && mediaManagerProvider.getCachePath() != null) {
            return mediaManagerProvider.getCachePath();
        }

        return getAppPath(cacheStr);
    }


    private static String productSimpleFileName(String postfix) {


        if (mediaManagerProvider != null && mediaManagerProvider.productFileName(postfix) != null) {
            return mediaManagerProvider.productFileName(postfix);
        }
        Date date = new Date(System.currentTimeMillis()); //2016-01-28 12:02:28  14位年月日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + MediaDirectoryUtils.getRandom(3) + postfix;//(2017 02 23 17 38 23 1
//        return sdf.format(date) + MediaUtils.getRandom(1) + postfix;//(2017 02 23 17 38 23 1
    }


    public static int getRandom(int n) {
        int ans = 0;
        while (Math.log10(ans) + 1 < n)
            ans = (int) (Math.random() * Math.pow(10, n));
        return ans;
    }

    public static File getAppPath(String path) {
/*        Log.d(TAG, "系统空间:" + FileUtils.readSystemAvailableBlocks() + "k," + Formatter.formatFileSize(AppContext.getInstance(), FileUtils.getAvailableInternalMemorySize()));
        Log.d(TAG, "磁盘空间:" + FileUtils.readSDCardAvailableBlocks() + "k,," + Formatter.formatFileSize(AppContext.getInstance(), FileUtils.getAvailableExternalMemorySize()));*/
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory, ROOT_FOLDER);
        File f = new File(file, path);
        if(!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    public final static String ROOT_FOLDER = "voice-recorder";
    private static String cacheStr = "cache";

    public interface MediaManagerProvider {

        File getTempCacheWavFileName();

        File getTempAmrFileName();

        File getTempMp3FileName();

        File getTempAACFileName();

        File getTempCachePcmFileName();

        /**
         * 缓存根目录
         *
         * @return
         */
        File getCachePath();

        String productFileName(String postfix);
    }

    public static void setMediaManagerProvider(MediaManagerProvider mediaManagerProvider) {
        MediaDirectoryUtils.mediaManagerProvider = mediaManagerProvider;
    }

    static MediaManagerProvider mediaManagerProvider;
}
