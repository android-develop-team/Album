package com.album.util.task;

/**
 * by y on 21/08/2017.
 */

public interface AlbumTaskCallBack {
    void start(Call call);

    void quit();

    interface Call {
        void start();
    }
}
