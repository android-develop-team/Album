package com.album.core.ui.adapter.vh

import android.graphics.PorterDuff
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.album.core.AlbumBundle
import com.album.core.R

class CameraViewHolder(itemView: View, private val albumBundle: AlbumBundle) : RecyclerView.ViewHolder(itemView) {

    private val container: LinearLayout = itemView.findViewById(R.id.album_camera_root_view)
    private val cameraIv: AppCompatImageView = itemView.findViewById(R.id.albumImageCamera)
    private val cameraTv: AppCompatTextView = itemView.findViewById(R.id.albumImageCameraTv)

    fun camera() {
        val drawable = ContextCompat.getDrawable(itemView.context, albumBundle.cameraDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(itemView.context, albumBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
        cameraTv.setText(albumBundle.cameraText)
        cameraTv.textSize = albumBundle.cameraTextSize
        cameraTv.setTextColor(ContextCompat.getColor(itemView.context, albumBundle.cameraTextColor))
        container.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.cameraBackgroundColor))
        cameraIv.setImageDrawable(drawable)
    }
}