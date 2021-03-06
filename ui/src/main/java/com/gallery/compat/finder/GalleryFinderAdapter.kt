package com.gallery.compat.finder

import android.view.View
import android.widget.FrameLayout
import com.gallery.core.entity.ScanEntity

interface GalleryFinderAdapter {

    interface AdapterFinderListener {
        /** 文件夹图片加载 */
        fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout)

        /** item点击 */
        fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity)
    }

    /** adapter 初始化 */
    fun finderInit() {}

    /** 更新文件夹数据 */
    fun finderUpdate(finderList: ArrayList<ScanEntity>)

    /** 显示Adapter */
    fun show()

    /** 隐藏Adapter  */
    fun hide()

}
