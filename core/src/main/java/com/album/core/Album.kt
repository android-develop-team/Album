package com.album.core

import android.view.View
import com.album.core.action.AlbumImageLoader
import com.album.core.action.OnAlbumListener
import com.album.scan.ScanEntity
import com.album.core.ui.base.AlbumBaseFragment
import com.yalantis.ucrop.UCrop

class Album private constructor() {

    /**
     * UCrop setting
     */
    var options: UCrop.Options? = null

    /**
     * 图片加载框架
     */
    var albumImageLoader: AlbumImageLoader? = null

    /**
     * 回调
     */
    var albumListener: OnAlbumListener? = null

    /**
     * 自定义相机
     */
    var customCameraListener: ((fragment: AlbumBaseFragment) -> Unit)? = null

    /**
     * 占位符自定义点击
     */
    var emptyClickListener: ((view: View) -> Unit)? = null

    /**
     * 默认初始化选中数据
     */
    var selectList: ArrayList<ScanEntity>? = null

    companion object {

        val instance by lazy { Album() }

        fun destroy() = instance.apply {
            options = null
            albumImageLoader = null
            albumListener = null
            customCameraListener = null
            emptyClickListener = null
            selectList = null
        }
    }
}