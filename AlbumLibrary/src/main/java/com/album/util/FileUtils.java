package com.album.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.album.AlbumConstant;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * by y on 11/08/2017.
 */

@SuppressWarnings("ALL")
public class FileUtils {
    private FileUtils() {
    }

    public static boolean isFile(String path) {
        return getPathFile(path) != null;
    }

    public static File getPathFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return null;
        }
        return parentFile;
    }

    public static File getScannerFile(String path) {
        return new File(path);
    }

    public static void finishCamera(Activity activity, String path) {
        Bundle bundle = new Bundle();
        bundle.putString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        activity.setResult(RESULT_OK, intent);
        activity.finish();
    }

    public static File getCameraFile(Context context, String path, boolean video) {
        String cachePath = null;
        if (TextUtils.isEmpty(path)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                cachePath = Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM;
            } else {
                File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir != null) {
                    cachePath = externalCacheDir.getPath();
                }
            }
        } else {
            cachePath = path;
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
        }
        return new File(cachePath, System.currentTimeMillis() + (video ? ".mp4" : ".jpg"));
    }
}
