package com.album.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.annotation.PermissionsType;
import com.album.util.AlbumTool;
import com.album.util.task.AlbumTask;

/**
 * by y on 14/08/2017.
 */

public abstract class AlbumBaseActivity extends AppCompatActivity {

    protected AlbumConfig albumConfig = null;
    protected String finderName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        AlbumTool.setStatusBarColor(ContextCompat.getColor(this, albumConfig.getAlbumStatusBarColor()), getWindow());
        if (savedInstanceState != null) {
            finderName = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME);
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initTitle();
        initCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AlbumConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_PERMISSIONS_ALBUM);
                } else {
                    permissionsGranted(AlbumConstant.TYPE_PERMISSIONS_ALBUM);
                }
                break;
            case AlbumConstant.CAMERA_REQUEST_CODE:
                if (grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_PERMISSIONS_CAMERA);
                } else {
                    permissionsGranted(AlbumConstant.TYPE_PERMISSIONS_CAMERA);
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME, finderName);
    }

    protected abstract void initCreate(@Nullable Bundle savedInstanceState);

    protected abstract void initView();


    protected abstract void initTitle();

    @LayoutRes
    protected abstract int getLayoutId();


    protected abstract void permissionsDenied(@PermissionsType int type);

    protected abstract void permissionsGranted(@PermissionsType int type);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlbumTask.get().quit();
    }
}
