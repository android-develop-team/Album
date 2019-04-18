package com.album.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.listener.AlbumImageLoader;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.annotation.FrescoType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * by y on 20/08/2017.
 */

public class SimpleImageLoaderAlbumImageLoader implements AlbumImageLoader {

    private DisplayImageOptions displayImageOptions;

    public SimpleImageLoaderAlbumImageLoader() {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
    }

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumEntity albumEntity) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.getPath(), view, displayImageOptions, null);
    }


    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderEntity finderEntity) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + finderEntity.getThumbnailsPath(), new ImageViewAware(view), displayImageOptions, null, null, null);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumEntity albumEntity) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.getPath(), new ImageViewAware(view), displayImageOptions, null, null, null);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }
}
