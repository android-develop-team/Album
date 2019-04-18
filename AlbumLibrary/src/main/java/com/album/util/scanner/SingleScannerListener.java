package com.album.util.scanner;

import com.album.annotation.AlbumResultType;

public interface SingleScannerListener {
    @SuppressWarnings("EmptyMethod")
    void onScanStart();

    void onScanCompleted(@AlbumResultType int type);
}