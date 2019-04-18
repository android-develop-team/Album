package com.album;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.album.entity.AlbumEntity;
import com.album.listener.AlbumCameraListener;
import com.album.listener.AlbumImageLoader;
import com.album.listener.AlbumListener;
import com.album.listener.AlbumVideoListener;
import com.album.listener.OnEmptyClickListener;
import com.album.ui.activity.AlbumActivity;
import com.album.ui.widget.SimpleAlbumListener;
import com.album.ui.widget.SimpleGlideAlbumImageLoader;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class Album {

    private AlbumConfig config = null;
    private AlbumImageLoader albumImageLoader = null;
    private UCrop.Options options = null;
    private AlbumListener albumListener = null;
    private AlbumCameraListener albumCameraListener = null;
    private AlbumVideoListener albumVideoListener = null;
    private OnEmptyClickListener emptyClickListener = null;
    private ArrayList<AlbumEntity> albumEntityList = null;
    private Class<?> albumClass = null;

    public static Album getInstance() {
        return AlbumHolder.ALBUM;
    }

    public AlbumImageLoader getAlbumImageLoader() {
        return albumImageLoader;
    }

    public Album setAlbumImageLoader(AlbumImageLoader albumImageLoader) {
        this.albumImageLoader = albumImageLoader;
        return this;
    }

    public UCrop.Options getOptions() {
        return options;
    }

    public Album setOptions(@Nullable UCrop.Options options) {
        this.options = options;
        return this;
    }

    public AlbumConfig getConfig() {
        return config;
    }

    public Album setConfig(@Nullable AlbumConfig config) {
        this.config = config;
        return this;
    }

    public AlbumListener getAlbumListener() {
        return albumListener;
    }

    public Album setAlbumListener(@Nullable AlbumListener albumListener) {
        this.albumListener = albumListener;
        return this;
    }

    public ArrayList<AlbumEntity> getAlbumEntityList() {
        return albumEntityList;
    }

    public Album setAlbumEntityList(@Nullable ArrayList<AlbumEntity> albumEntityList) {
        this.albumEntityList = albumEntityList;
        return this;
    }

    public Class<?> getAlbumClass() {
        return albumClass;
    }

    public Album setAlbumClass(@Nullable Class<?> albumClass) {
        this.albumClass = albumClass;
        return this;
    }

    public OnEmptyClickListener getEmptyClickListener() {
        return emptyClickListener;
    }

    public Album setEmptyClickListener(OnEmptyClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
        return this;
    }

    public AlbumCameraListener getAlbumCameraListener() {
        return albumCameraListener;
    }

    public Album setAlbumCameraListener(@Nullable AlbumCameraListener albumCameraListener) {
        this.albumCameraListener = albumCameraListener;
        return this;
    }

    public AlbumVideoListener getAlbumVideoListener() {
        return albumVideoListener;
    }

    public void setAlbumVideoListener(@Nullable AlbumVideoListener albumVideoListener) {
        this.albumVideoListener = albumVideoListener;
    }

    private static final class AlbumHolder {
        private static final Album ALBUM = new Album();
    }

    public void start(@NonNull Context context) {
        if (albumClass == null) {
            albumClass = AlbumActivity.class;
        }
        if (config == null) {
            config = new AlbumConfig();
        }
        if (albumImageLoader == null) {
            albumImageLoader = new SimpleGlideAlbumImageLoader();
        }
        if (albumListener == null) {
            albumListener = new SimpleAlbumListener();
        }
        Intent intent = new Intent(context, albumClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
