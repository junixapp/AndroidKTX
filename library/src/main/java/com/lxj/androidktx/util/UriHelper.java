package com.lxj.androidktx.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import com.lxj.androidktx.AndroidKTX;
import java.util.List;


public class UriHelper {

    public static void setIntentDataAndType(Intent intent,
                                            String type,
                                            Uri uri,
                                            boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(uri, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(uri, type);
        }
    }


    public static void setIntentData(Intent intent,
                                     Uri uri,
                                     boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(uri);
        }
    }

    /**
     * 申请uri的操作权限
     * @param intent
     * @param uri
     * @param writeAble
     */
    public static void grantPermissions(Intent intent, Uri uri, boolean writeAble) {

        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = AndroidKTX.context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            AndroidKTX.context.grantUriPermission(packageName, uri, flag);
        }
    }

}
