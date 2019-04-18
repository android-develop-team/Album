package com.album.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.listener.AlbumImageLoader;
import com.album.R;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.annotation.FrescoType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * by y on 15/08/2017.
 */

public class SimpleGlideAlbumImageLoader implements AlbumImageLoader {

    private RequestOptions requestOptions;

    public SimpleGlideAlbumImageLoader() {
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop();
    }

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumEntity albumEntity) {
        Glide
                .with(view.getContext())
                .load(albumEntity.getPath())
                .apply(requestOptions.override(width, height))
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderEntity finderEntity) {
        Glide
                .with(view.getContext())
                .load(finderEntity.getThumbnailsPath()).apply(requestOptions)
                .into(view);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumEntity albumEntity) {
        Glide
                .with(view.getContext())
                .load(albumEntity.getPath()).apply(requestOptions)
                .into(view);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }

}
