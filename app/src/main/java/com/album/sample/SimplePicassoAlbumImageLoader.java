package com.album.sample;

import android.content.ContentUris;
import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.annotation.FrescoType;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.listener.AlbumImageLoader;
import com.squareup.picasso.Picasso;

/**
 * by y on 19/08/2017.
 */

public class SimplePicassoAlbumImageLoader implements AlbumImageLoader {

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumEntity albumEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.getId()))
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderEntity finderEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, finderEntity.getThumbnailsId()))
                .resize(50, 50)
                .into(view);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumEntity albumEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.getId()))
                .resize(50, 50)
                .into(view);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }
}
