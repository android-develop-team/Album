package com.gallery.sample.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.compat.GalleryConfig
import com.gallery.compat.GalleryUiBundle
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.extensions.cropNameExpand
import com.gallery.core.extensions.hasQExpand
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.core.extensions.queryDataExpand
import com.yalantis.ucrop.UCrop
import java.io.File

open class UCropImpl(private val galleryUiBundle: GalleryUiBundle) : ICrop {

    override fun onCropResult(delegate: IScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    onCropSuccess(delegate, galleryBundle, uri)
                }
            }
        }
    }

    override fun openCrop(delegate: IScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        return UCrop.of(inputUri, cropOutPutUri2(delegate.activityNotNull, galleryBundle))
                .withOptions(UCrop.Options().apply { optionBundle.putAll(galleryUiBundle.args.getBundle(GalleryConfig.CROP_ARGS).orEmptyExpand()) })
                .getIntent(delegate.activityNotNull)
    }

    private fun onCropSuccess(delegate: IScanDelegate, galleryBundle: GalleryBundle, uri: Uri) {
        val currentUri: Uri = if (!hasQExpand()) {
            uri
        } else {
            val contentUri = delegate.activityNotNull.copyImageExpand(uri, galleryBundle.cropNameExpand).orEmptyExpand()
            val filePath: String? = delegate.activityNotNull.contentResolver.queryDataExpand(contentUri)
            if (filePath.isNullOrEmpty()) {
                uri
            } else {
                File(uri.path.orEmpty()).delete()
                Uri.fromFile(File(filePath))
            }
        }
        delegate.onScanResult(currentUri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(GalleryConfig.GALLERY_RESULT_CROP, currentUri)
        intent.putExtras(bundle)
        delegate.activityNotNull.setResult(GalleryConfig.RESULT_CODE_CROP, intent)
        delegate.activityNotNull.finish()
    }
}