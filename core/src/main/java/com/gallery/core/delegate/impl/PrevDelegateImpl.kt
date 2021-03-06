package com.gallery.core.delegate.impl

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.PrevArgs.Companion.configOrDefault
import com.gallery.core.PrevArgs.Companion.prevArgs
import com.gallery.core.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.PrevArgs.Companion.putPrevArgs
import com.gallery.core.R
import com.gallery.core.ScanArgs
import com.gallery.core.ScanArgs.Companion.putScanArgs
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.adapter.PrevAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.isFileExistsExpand
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.core.extensions.toScanEntity
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.ScanCore.Companion.scanCore
import com.gallery.scan.extensions.isScanNoNeExpand
import com.gallery.scan.extensions.multipleScanExpand
import com.gallery.scan.impl.ScanImpl
import com.gallery.scan.impl.file.FileScanArgs
import com.gallery.scan.impl.file.FileScanEntity
import com.gallery.scan.impl.file.fileExpand
import com.gallery.scan.types.ScanType.SCAN_ALL
import com.gallery.scan.types.ScanType.SCAN_NONE

/**
 * 预览代理
 */
class PrevDelegateImpl(
        /**
         * [Fragment]
         * 承载容器
         * 使用容器获取需要的[ViewPager2]
         * [Fragment]中必须存在 [R.id.gallery_prev_viewpager2] [R.id.gallery_prev_checkbox] 两个id的View
         */
        private val fragment: Fragment,
        /**
         * [IGalleryPrevCallback]
         * 预览回调
         */
        private val galleryPrevCallback: IGalleryPrevCallback,
        /**
         * [IGalleryImageLoader]
         * 图片加载框架
         */
        private val galleryImageLoader: IGalleryImageLoader,
) : IPrevDelegate {

    private val pageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            galleryPrevCallback.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            galleryPrevCallback.onPageScrollStateChanged(state)
        }

        override fun onPageSelected(position: Int) {
            galleryPrevCallback.onPageSelected(position)
            checkBox.isSelected = isCheckBox(position)
        }
    }

    private val viewPager2: ViewPager2 = fragment.view?.findViewById(R.id.gallery_prev_viewpager2) as ViewPager2
    private val checkBox: View = fragment.view?.findViewById(R.id.gallery_prev_checkbox) as View
    private val prevAdapter: PrevAdapter = PrevAdapter { entity, container -> galleryImageLoader.onDisplayGalleryPrev(entity, container) }
    private val prevArgs: PrevArgs = fragment.arguments.orEmptyExpand().prevArgsOrDefault
    private val galleryBundle: GalleryBundle = prevArgs.configOrDefault

    override val allItem: ArrayList<ScanEntity>
        get() = prevAdapter.allItem
    override val selectEntities: ArrayList<ScanEntity>
        get() = prevAdapter.currentSelectList
    override val currentPosition: Int
        get() = viewPager2.currentItem

    /** 保存当前position和选中的文件数据 */
    override fun onSaveInstanceState(outState: Bundle) {
        PrevArgs.newSaveInstance(currentPosition, selectEntities).putPrevArgs(outState)
    }

    /**
     * 如果parentId是[SCAN_NONE]的话，就是不扫描，直接把传入的 selectList
     * 作为全部数据展示
     * 否则从数据库获取数据，从数据库获取数据时会判断 scanAlone 是否是 [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]
     * 如果是，则使用 [GalleryBundle.scanType]作为参数，否则使用 scanAlone
     * 如果预览页想扫描专门的类型，则使用 scanAlone，这个时候传入[SCAN_ALL]即可
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //https://github.com/7449/Album/issues/4
        //新增对单独扫描的支持，获取scanAlone和parentId
        val scanAlone = prevArgs.scanAlone
        val parentId: Long = prevArgs.parentId
        if (parentId.isScanNoNeExpand()) {
            updateEntity(savedInstanceState, prevArgs.selectList)
        } else {
            //https://issuetracker.google.com/issues/127692541
            //这个问题已经在ViewPager2上修复
            val scanFileArgs = FileScanArgs(
                    if (scanAlone == MediaStore.Files.FileColumns.MEDIA_TYPE_NONE) galleryBundle.scanType.map { it.toString() }.toTypedArray() else arrayOf(scanAlone.toString()),
                    galleryBundle.scanSortField,
                    galleryBundle.scanSort
            )
            ScanImpl<FileScanEntity>(fragment.scanCore(
                    factory = ScanEntityFactory.fileExpand(),
                    args = scanFileArgs
            )).registerScanResource {
                updateEntity(savedInstanceState, it.multipleValue.toScanEntity())
            }.scanMultiple(parentId.multipleScanExpand())
        }
    }

    override fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>) {
        val prevArgs: PrevArgs = savedInstanceState?.prevArgs ?: prevArgs
        prevAdapter.addAll(arrayList)
        prevAdapter.addSelectAll(prevArgs.selectList)
        prevAdapter.updateEntity()
        galleryPrevCallback.onPrevCreated(fragment, galleryBundle, savedInstanceState)
        viewPager2.adapter = prevAdapter
        viewPager2.registerOnPageChangeCallback(pageChangeCallback)
        setCurrentItem(prevArgs.position)
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener { checkBoxClick(checkBox) }
        checkBox.isSelected = isCheckBox(currentPosition)
    }

    override fun checkBoxClick(checkBox: View) {
        if (!currentItem.uri.isFileExistsExpand(fragment.requireActivity())) {
            if (prevAdapter.containsSelect(currentItem)) {
                prevAdapter.removeSelectEntity(currentItem)
            }
            checkBox.isSelected = false
            currentItem.isSelected = false
            galleryPrevCallback.onClickCheckBoxFileNotExist(fragment.requireActivity(), galleryBundle, currentItem)
            return
        }
        if (!prevAdapter.containsSelect(currentItem) && selectEntities.size >= galleryBundle.multipleMaxCount) {
            galleryPrevCallback.onClickCheckBoxMaxCount(fragment.requireActivity(), galleryBundle, currentItem)
            return
        }
        if (currentItem.isSelected) {
            prevAdapter.removeSelectEntity(currentItem)
            currentItem.isSelected = false
            checkBox.isSelected = false
        } else {
            prevAdapter.addSelectEntity(currentItem)
            currentItem.isSelected = true
            checkBox.isSelected = true
        }
        galleryPrevCallback.onChangedCheckBox()
    }

    override fun isCheckBox(position: Int): Boolean {
        return prevAdapter.isCheck(position)
    }

    override fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(position, smoothScroll)
    }

    override fun notifyItemChanged(position: Int) {
        prevAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        prevAdapter.notifyDataSetChanged()
    }

    override fun resultBundle(isRefresh: Boolean): Bundle {
        return ScanArgs.newResultInstance(selectEntities, isRefresh).putScanArgs()
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}