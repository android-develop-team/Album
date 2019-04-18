package com.album.ui.view;

import android.database.Cursor;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 * <p>
 * <p>
 *
 */


public interface ScanView {

    void start(ScanCallBack scanCallBack, String bucketId, int page, int count);

    void resultScan(ScanCallBack scanCallBack, String path);

    void scanCursor(ArrayList<AlbumEntity> albumEntityList, int dataColumnIndex, int idColumnIndex, int sizeColumnIndex, Cursor cursor);

    void cursorFinder(ArrayList<FinderEntity> finderEntityList);

    int cursorCount(String bucketId);

    Cursor getCursor(String bucketId, int page, int count);

    String[] getSelectionArgs(String bucketId);

}
