package com.gallery.core.callback

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.gallery.core.GalleryBundle
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.kotlin.x.CameraStatus
import com.kotlin.x.PermissionCode
import com.kotlin.x.toast

/**
 *
 */
interface IGalleryCallback {

    /**
     * 单选状态下,点击[Adapter]返回的那条数据
     * [ScanFragment.onPhotoItemClick]
     */
    fun onGalleryResource(context: Context, scanEntity: ScanEntity)

    /**
     * 点击CheckBox时该文件已经被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PhotoViewHolder.photo]
     */
    fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        "文件已被删除".toast(context)
    }

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount(context: Context, scanEntity: ScanEntity) {
        "不能再选择啦".toast(context)
    }

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这个方法优先级高于单选和视频播放，裁剪等功能
     * [ScanFragment.onPhotoItemClick]
     */
    fun onClickItemFileNotExist(context: Context, scanEntity: ScanEntity) {
        "文件已被删除".toast(context)
    }

    /**
     * 点击CheckBox时会触发
     * [PhotoViewHolder.photo]
     */
    fun onChangedCheckBox(isSelect: Boolean, scanEntity: ScanEntity) {}

    /**
     * 横竖屏切换时触发
     * [ScanFragment.onActivityCreated]
     */
    fun onChangedScreen(selectCount: Int) {}

    /**
     * 刷新预览页数据之后触发
     * [ScanFragment.onResultPreview]
     */
    fun onChangedPrevCount(selectCount: Int) {}

    /**
     * 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法
     * 可以跳转到预览页
     * [ScanFragment.onPhotoItemClick]
     */
    fun onPhotoItemClick(context: Context, scanEntity: ScanEntity, position: Int, parentId: Long)

    /**
     * 每次扫描之后数据非空触犯
     * [ScanFragment.scanSuccess]
     */
    fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {}

    /**
     * 拍照or裁剪返回
     */
    fun onScanResultSuccess(context: Context, scanEntity: ScanEntity) {}

    /**
     * 在[IGalleryInterceptor.onGalleryFragmentResult]为false的情况下会触发
     * 取消拍照
     */
    fun onCameraCanceled(context: Context) {
        "取消拍照".toast(context)
    }

    /**
     * 拍照之后获取数据失败
     */
    fun onCameraResultError(context: Context) {
        "拍照异常".toast(context)
    }

    /**
     * 打开相机返回的状态
     * [CameraStatus.SUCCESS] 成功
     * [CameraStatus.ERROR] 失败
     * [CameraStatus.PERMISSION] 权限被拒
     */
    fun onCameraOpenStatus(context: Context, status: CameraStatus) {
        when (status) {
            CameraStatus.ERROR -> "相机打开失败".toast(context)
            CameraStatus.SUCCESS -> "拍照".toast(context)
            CameraStatus.PERMISSION -> "缺少权限".toast(context)
        }
    }

    /**
     * 没有扫描到任何数据
     */
    fun onScanSuccessEmpty(context: Context) {
        "没有扫描到数据".toast(context)
    }

    /**
     * 视频播放异常
     */
    fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity) {
        "视频播放异常".toast(context)
    }

    /**
     * 权限被拒
     * [PermissionCode.READ]
     * [PermissionCode.WRITE]
     */
    fun onPermissionsDenied(context: Context, type: PermissionCode) {
        "获取权限异常".toast(context)
    }
}