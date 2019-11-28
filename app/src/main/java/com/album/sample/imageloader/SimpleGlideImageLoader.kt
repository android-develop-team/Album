package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.gallery.core.action.AlbumImageLoader
import com.gallery.core.ext.AlbumImageView
import com.gallery.core.ext.uri
import com.album.sample.R
import com.gallery.scan.ScanEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SimpleGlideImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.uri()).apply(requestOptions.override(width, height)).into(imageView)
        return imageView
    }

    override fun displayAlbumThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(imageView)
        return imageView
    }

    override fun displayAlbumPreview(albumEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.uri()).apply(requestOptions.override(albumEntity.width, albumEntity.height)).into(imageView)
        return imageView
    }
}
