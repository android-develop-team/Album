package com.album.util.scanner;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.album.annotation.AlbumResultType;

import java.io.File;

/**
 * by y on 14/08/2017.
 * <p>
 * <p>
 * https://issuetracker.google.com/issues/37046656
 * <p>
 * https://github.com/square/leakcanary/issues/26
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection connection;
    private final File file;
    private final SingleScannerListener listener;
    private final int type;

    public SingleMediaScanner(Context context, File file, SingleScannerListener listener, @AlbumResultType int type) {
        this.file = file;
        this.type = type;
        this.connection = new MediaScannerConnection(context.getApplicationContext(), this);
        this.connection.connect();
        this.listener = listener;
        this.listener.onScanStart();
    }

    @Override
    public void onMediaScannerConnected() {
        connection.scanFile(file.getAbsolutePath(), null);
    }

    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        disconnect();
        if (listener != null) {
            listener.onScanCompleted(type);
        }
    }
}
