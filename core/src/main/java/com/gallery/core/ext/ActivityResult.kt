package com.gallery.core.ext

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.camera.CameraStatus
import com.gallery.scan.ScanType

fun Context.checkSelfPermissionExpand(name: String) =
        ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED

fun Context.checkCameraPermissionExpand() =
        checkSelfPermissionExpand(Manifest.permission.CAMERA)

fun Context.checkWritePermissionExpand() =
        checkSelfPermissionExpand(Manifest.permission.WRITE_EXTERNAL_STORAGE)

fun Fragment.checkCameraPermissionExpand() =
        requireContext().checkSelfPermissionExpand(Manifest.permission.CAMERA)

fun Fragment.checkWritePermissionExpand() =
        requireContext().checkSelfPermissionExpand(Manifest.permission.WRITE_EXTERNAL_STORAGE)

fun Fragment.requestCameraResultLauncherExpand(cancel: () -> Unit, ok: () -> Unit): ActivityResultLauncher<CameraUri> =
        registerForActivityResult(CameraResultContract()) {
            when (it) {
                Activity.RESULT_CANCELED -> cancel.invoke()
                Activity.RESULT_OK -> ok.invoke()
            }
        }

fun Fragment.requestPermissionResultLauncherExpand(action: (isGranted: Boolean) -> Unit): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            action.invoke(it)
        }

fun Fragment.checkPermissionAndRequestWriteExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkWritePermissionExpand()) {
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        false
    } else {
        true
    }
}

fun Fragment.checkPermissionAndRequestCameraExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkCameraPermissionExpand()) {
        launcher.launch(Manifest.permission.CAMERA)
        false
    } else {
        true
    }
}

fun Fragment.openCameraExpand(uri: CameraUri, action: (uri: CameraUri) -> Unit): CameraStatus {
    val intent: Intent = if (uri.type == ScanType.VIDEO) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(requireActivity().packageManager) == null) {
        return CameraStatus.ERROR
    }
    action.invoke(uri)
    return CameraStatus.SUCCESS
}
