package com.gallery.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryPrev
import com.gallery.ui.page.GalleryActivity

class Gallery(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val galleryLauncher: ActivityResultLauncher<Intent>,
        private val galleryBundle: GalleryBundle = GalleryBundle(),
        private val galleryUiBundle: GalleryUiBundle = GalleryUiBundle(),
        private val galleryOption: Bundle = Bundle.EMPTY,
        private val galleryPrevOption: Bundle = Bundle.EMPTY,
        private val clz: Class<*> = GalleryActivity::class.java) {

    private val fragmentActivity: FragmentActivity

    init {
        when {
            fragment != null -> {
                fragmentActivity = fragment.requireActivity()
                startFragment()
            }
            activity != null -> {
                fragmentActivity = activity
                startActivity()
            }
            else -> throw NullPointerException("fragment and activity == null")
        }
    }

    private fun launchIntent(): Intent {
        return Intent(fragmentActivity, clz).apply {
            putExtras(Bundle().apply {
                putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
                putParcelable(UIResult.UI_CONFIG, galleryUiBundle)
                putBundle(IGallery.GALLERY_START_BUNDLE, galleryOption)
                putBundle(IGalleryPrev.PREV_START_BUNDLE, galleryPrevOption)
            })
        }
    }

    private fun startActivity() {
        galleryLauncher.launch(launchIntent())
    }

    private fun startFragment() {
        galleryLauncher.launch(launchIntent())
    }
}