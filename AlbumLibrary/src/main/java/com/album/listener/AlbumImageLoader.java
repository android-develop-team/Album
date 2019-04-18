package com.album.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.annotation.FrescoType;

/**
 * by y on 15/08/2017.
 */

public interface AlbumImageLoader {

    void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumEntity albumEntity);

    void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderEntity finderEntity);

    void displayPreview(@NonNull ImageView view, @NonNull AlbumEntity albumEntity);

    ImageView frescoView(@NonNull Context context, @FrescoType int type);
}
