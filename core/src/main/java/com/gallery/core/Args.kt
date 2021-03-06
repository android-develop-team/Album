package com.gallery.core

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Size
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.types.ScanType
import com.gallery.scan.types.Sort
import kotlinx.parcelize.Parcelize

/**
 * 1.
 *  调用[IPrevDelegate.resultBundle]获取需要的参数
 *  其中
 *  [parentId]默认参数 [ScanType.SCAN_ALL]
 *  [fileUri]默认参数 [Uri.EMPTY]
 *  [isRefresh]是否需要合并数据并刷新
 *  [selectList]选中的数据
 *  如果需要获取数据可通过[ScanArgs.scanArgs]获取里面的[selectList]
 *
 * 2.
 *  在横竖屏切换时[IScanDelegate.onSaveInstanceState]获取需要的参数
 *  其中
 *  [parentId]当前的parentId
 *  [fileUri]拍照的Uri
 *  [isRefresh]默认参数false
 *  [selectList]当前选中的数据
 */
@Parcelize
data class ScanArgs(
        val parentId: Long,
        val fileUri: Uri,
        val isRefresh: Boolean,
        val selectList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "scanArgs"

        fun newSaveInstance(parentId: Long, fileUri: Uri, selectList: ArrayList<ScanEntity>): ScanArgs {
            return ScanArgs(parentId, fileUri, false, selectList)
        }

        fun newResultInstance(selectList: ArrayList<ScanEntity>, isRefresh: Boolean): ScanArgs {
            return ScanArgs(ScanType.SCAN_ALL, Uri.EMPTY, isRefresh, selectList)
        }

        fun ScanArgs.putScanArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.scanArgs
            get() = getParcelable<ScanArgs>(Key)
    }
}

@Parcelize
data class PrevArgs(
        /**
         * 当前文件的parentId,用于扫描数据库获取对应的数 如果是预览进入的话可传[ScanType.SCAN_NONE]
         */
        val parentId: Long,
        /**
         * 当前选中的数据,如果是预览进入则认为所有数据==[selectList]
         */
        val selectList: ArrayList<ScanEntity>,
        /**
         * [GalleryBundle] 参数
         */
        val config: GalleryBundle?,
        /**
         * position 需要跳转的位置
         */
        val position: Int,
        /**
         * [scanAlone] 是否是单独扫描某些类型的数据
         * 如果[parentId] == [ScanType.SCAN_NONE] 则认为点击的是预览而不是item,则未选中数据和选中数据应该一致
         * 如果不是，则判断[scanAlone] == [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]] ，
         * 如果不是，则使用 [scanAlone],如果是，则扫描 [GalleryBundle.scanType]类型的数据
         * 如果使用自定义 scanType,则parentId传 [ScanType.SCAN_ALL] 比较合适
         */
        val scanAlone: Int,
) : Parcelable {
    companion object {

        private const val Key = "prevArgs"

        fun newSaveInstance(position: Int, selectList: ArrayList<ScanEntity>): PrevArgs {
            return PrevArgs(ScanType.SCAN_ALL, selectList, null, position, MediaStore.Files.FileColumns.MEDIA_TYPE_NONE)
        }

        fun PrevArgs.putPrevArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.prevArgs
            get() = getParcelable<PrevArgs>(Key)

        val Bundle.prevArgsOrDefault
            get() = prevArgs
                    ?: PrevArgs(ScanType.SCAN_ALL, arrayListOf(), GalleryBundle(), 0, MediaStore.Files.FileColumns.MEDIA_TYPE_NONE)

        val PrevArgs.configOrDefault
            get() = config ?: GalleryBundle()
    }
}

@Parcelize
data class GalleryBundle(
        /**
         * 默认选中的数据
         */
        val selectEntities: ArrayList<ScanEntity> = arrayListOf(),
        /**
         * 扫描类型
         * 根据[MediaStore.Files.FileColumns.MEDIA_TYPE]搜索
         */
        @Suppress("ArrayInDataClass")
        @Size(min = 1)
        val scanType: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
        /**
         * 排序方式
         * [Sort.DESC]
         * [Sort.ASC]
         */
        val scanSort: String = Sort.DESC,
        val scanSortField: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
        /**
         * 隐藏相机
         */
        val hideCamera: Boolean = false,
        /**
         * 是否单选
         */
        val radio: Boolean = false,
        /**
         * 是否裁剪
         * 只在[radio]true的情况下生效
         */
        val crop: Boolean = true,
        /**
         * 拍照之后是否立即裁剪
         */
        val cameraCrop: Boolean = false,
        /**
         * 多选最多数
         */
        val multipleMaxCount: Int = 9,
        /**
         * 用于配置加载图片的 Width Height
         */
        val spanCount: Int = 4,
        /**
         * 文件输出路径
         * [Environment.DIRECTORY_DCIM]
         * [Environment.DIRECTORY_PICTURES]
         */
        val relativePath: String = Environment.DIRECTORY_DCIM,
        /**
         * 拍照路径
         * 支持在Android10版本以下
         */
        val cameraPath: String? = null,
        /**
         * 摄像或拍照文件名称
         */
        val cameraName: String = System.currentTimeMillis().toString(),
        /**
         * 摄像或拍照文件名称后缀
         * cameraName.cameraNameSuffix
         * sample: photo.jpg
         */
        val cameraNameSuffix: String = "jpg",
        /**
         * 裁剪路径
         * 只在未拦截自定义裁剪生效,或使用者自定义裁剪使用
         */
        val cropPath: String? = null,
        /**
         * 裁剪文件名称
         */
        val cropName: String = System.currentTimeMillis().toString(),
        /**
         * 裁剪文件后缀
         */
        val cropNameSuffix: String = "jpg",
        /**
         * 根目录名称,有的会出现0所以设置下
         */
        val sdName: String = "根目录",
        /**
         * 全部图片
         */
        val allName: String = "全部",
        /**
         * 相机提示文字
         */
        val cameraText: String = "",
        /**
         * 相机提示文字大小
         */
        val cameraTextSize: Float = 16F,
        /**
         * 相机图片
         */
        @DrawableRes
        val cameraDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 选择框
         */
        @DrawableRes
        val checkBoxDrawable: Int = R.drawable.selector_gallery_item_check,
        /**
         * 空数据占位图片
         */
        @DrawableRes
        val photoEmptyDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 相机提示文字颜色
         */
        @ColorInt
        val cameraTextColor: Int = Color.WHITE,
        /**
         * 相机图片背景色
         */
        @ColorInt
        val cameraDrawableColor: Int = Color.parseColor("#FF02A5D2"),
        /**
         * 相机背景色
         */
        @ColorInt
        val cameraBackgroundColor: Int = Color.parseColor("#FFB0C9C9"),
) : Parcelable {
    companion object {
        private const val Key = "galleryBundle"

        fun GalleryBundle.putGalleryArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.galleryBundle
            get() = getParcelable<GalleryBundle>(Key)

        val Bundle.galleryBundleOrDefault
            get() = galleryBundle ?: GalleryBundle()
    }
}