package com.gallery.core.ext

import com.gallery.core.callback.*
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment

val GalleryBaseFragment.galleryImageLoaderExpand: IGalleryImageLoader
    get() = when {
        parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
        activity is IGalleryImageLoader -> activity as IGalleryImageLoader
        else -> object : IGalleryImageLoader {}
    }

val ScanFragment.galleryInterceptorExpand: IGalleryInterceptor
    get() = when {
        parentFragment is IGalleryInterceptor -> parentFragment as IGalleryInterceptor
        activity is IGalleryInterceptor -> activity as IGalleryInterceptor
        else -> object : IGalleryInterceptor {}
    }

val ScanFragment.galleryCallbackExpand: IGalleryCallback
    get() = when {
        parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
        activity is IGalleryCallback -> activity as IGalleryCallback
        else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
    }

val PrevFragment.galleryPrevInterceptorExpand: IGalleryPrevInterceptor
    get() = when {
        parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
        activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
        else -> object : IGalleryPrevInterceptor {}
    }

val PrevFragment.galleryPrevCallbackExpand: IGalleryPrevCallback
    get() = when {
        parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
        activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
        else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
    }