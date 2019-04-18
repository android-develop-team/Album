package com.album.util.task;

import android.os.HandlerThread;

/**
 * by y on 21/08/2017.
 */

public class AlbumTask implements AlbumTaskCallBack {

    private static final String TASK_NAME = "Album";
    private HandlerThread handlerThread;

    public static AlbumTask get() {
        return AlbumTaskHolder.ALBUM_TASK;
    }

    @Override
    public void start(final Call call) {
        quit();
        handlerThread = new HandlerThread(TASK_NAME) {
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                call.start();
            }
        };
        handlerThread.start();
    }

    @Override
    public void quit() {
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }

    private static final class AlbumTaskHolder {
        private static final AlbumTask ALBUM_TASK = new AlbumTask();
    }
}
