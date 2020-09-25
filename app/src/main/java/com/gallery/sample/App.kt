package com.gallery.sample

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler())
    }

    class UncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(thread: Thread, ex: Throwable) {
            val result: Writer = StringWriter()
            val printWriter = PrintWriter(result)
            ex.printStackTrace(printWriter)
            Log.e("error:", result.toString())
        }
    }
}