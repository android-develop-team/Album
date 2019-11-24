package com.album.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

class SingleMediaScanner(context: Context, private val path: String, private val type: Int, private val listener: SingleScannerListener) : MediaScannerConnection.MediaScannerConnectionClient {

    interface SingleScannerListener {
        fun onScanStart()

        fun onScanCompleted(type: Int, path: String)
    }

    private val connection: MediaScannerConnection = MediaScannerConnection(context.applicationContext, this)

    init {
        connection.connect()
        listener.onScanStart()
    }

    override fun onMediaScannerConnected() {
        connection.scanFile(path, null)
    }

    fun disconnect() {
        connection.disconnect()
    }

    override fun onScanCompleted(path: String, uri: Uri?) {
        disconnect()
        listener.onScanCompleted(type, path)
    }
}