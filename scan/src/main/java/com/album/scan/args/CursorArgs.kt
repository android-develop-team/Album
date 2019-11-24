package com.album.scan.args

import android.net.Uri
import android.provider.MediaStore

internal object CursorArgs {

    /**
     * Scan Uri
     */
    val FILE_URI: Uri = MediaStore.Files.getContentUri("external")

    /**
     * 图片信息字段
     */
    val ALL_COLUMNS = arrayOf(
            Columns.DATA,
            Columns.ID,
            Columns.SIZE,
            Columns.DURATION,
            Columns.PARENT,
            Columns.MIME_TYPE,
            Columns.DISPLAY_NAME,
            Columns.ORIENTATION,
            Columns.BUCKET_ID,
            Columns.BUCKET_DISPLAY_NAME,
            Columns.MEDIA_TYPE,
            Columns.WIDTH,
            Columns.HEIGHT,
            Columns.DATE_MODIFIED
    )

    /**
     * 排序条件
     */
    const val ORDER_BY = Columns.DATE_MODIFIED + " DESC"

    /**
     * 图片信息条件
     */
    const val ALL_SELECTION = Columns.SIZE + " > 0 AND " + Columns.MEDIA_TYPE + "=? or " + Columns.MEDIA_TYPE + "=? "

    /**
     * 图片信息条件
     */
    fun getParentSelection(parent: Long) = Columns.PARENT + "=" + parent + " and (" + ALL_SELECTION + ")"

    /**
     * 图片信息条件
     */
    fun getPathSelection(path: String) = Columns.DATA + "=\"" + path + "\" and (" + ALL_SELECTION + ")"

    /**
     * 扫描条件
     */
    fun getSelectionArgs(scanType: Int): Array<String> = when (scanType) {
        ScanConst.VIDEO -> arrayOf(Columns.VIDEO)
        ScanConst.IMAGE -> arrayOf(Columns.IMAGE)
        ScanConst.MIX -> arrayOf(Columns.IMAGE, Columns.VIDEO)
        else -> throw  KotlinNullPointerException()
    }
}