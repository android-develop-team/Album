package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.gallery.core.action.GalleryImageLoader
import com.gallery.core.ext.GalleryImageView
import com.gallery.core.ext.uri
import com.gallery.scan.ScanEntity
import com.squareup.picasso.Picasso

/**
 * by y on 19/08/2017.
 */

class SimplePicassoGalleryImageLoader : GalleryImageLoader {

    override fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.GalleryImageView()
        Picasso.get()
                .load(galleryEntity.uri())
                .centerCrop()
                .resize(width, height)
                .into(albumImageView)
        return albumImageView
    }

    override fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.GalleryImageView()
        Picasso.get()
                .load(finderEntity.uri())
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }

    override fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.GalleryImageView()
        Picasso.get()
                .load(galleryEntity.uri())
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }
}
