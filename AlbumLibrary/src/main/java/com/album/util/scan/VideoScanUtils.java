package com.album.util.scan;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.album.AlbumConstant;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.FileUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 01/09/2017.
 */

public class VideoScanUtils implements ScanView {
    private final ContentResolver contentResolver;
    private static final String FINDER_VIDEO_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? ";
    private static final String[] VIDEO_COUNT_PROJECTION = new String[]{MediaStore.Video.Media.BUCKET_ID};
    private static final String[] VIDEO_PROJECTION = new String[]{
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
    };
    private static final String[] VIDEO_FINDER_PROJECTION = new String[]{
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA
    };


    private VideoScanUtils(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static VideoScanUtils get(ContentResolver contentResolver) {
        if (contentResolver == null) {
            throw new NullPointerException("contentResolver == null");
        }
        return new VideoScanUtils(contentResolver);
    }

    @Override
    public void start(ScanCallBack scanCallBack, String bucketId, int page, int count) {
        ArrayList<AlbumEntity> albumEntityList = new ArrayList<>();
        ArrayList<FinderEntity> finderEntityList = new ArrayList<>();
        Cursor cursor = getCursor(bucketId, page, count);
        if (cursor != null) {
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            int sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            while (cursor.moveToNext()) {
                scanCursor(albumEntityList, dataColumnIndex, idColumnIndex, sizeColumnIndex, cursor);
            }
            cursor.close();
            cursorFinder(finderEntityList);
            scanCallBack.scanSuccess(albumEntityList, finderEntityList);
        }
    }

    @Override
    public void resultScan(ScanCallBack scanCallBack, String path) {
        AlbumEntity albumEntity = null;
        ArrayList<FinderEntity> finderEntityList = new ArrayList<>();
        Cursor query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                MediaStore.Video.Media.DATA + "= ? ",
                new String[]{path},
                MediaStore.Video.Media.DATE_MODIFIED);
        if (query != null) {
            int dataColumnIndex = query.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = query.getColumnIndex(MediaStore.Video.Media._ID);
            while (query.moveToNext()) {
                String resultPath = query.getString(dataColumnIndex);
                long id = query.getLong(idColumnIndex);
                if (FileUtils.getPathFile(resultPath) != null) {
                    albumEntity = new AlbumEntity(null, null, resultPath, id, false);
                }
            }
            cursorFinder(finderEntityList);
            query.close();
        }
        scanCallBack.resultSuccess(albumEntity, finderEntityList);
    }

    @Override
    public void scanCursor(ArrayList<AlbumEntity> albumEntityList, int dataColumnIndex, int idColumnIndex, int sizeColumnIndex, Cursor cursor) {
        String path = cursor.getString(dataColumnIndex);
        long id = cursor.getLong(idColumnIndex);
        if (FileUtils.getPathFile(path) != null) {
            albumEntityList.add(new AlbumEntity(null, null, path, id, false));
        }
    }

    @Override
    public void cursorFinder(ArrayList<FinderEntity> finderEntityList) {
        ArrayMap<String, FinderEntity> finderEntityMap = new ArrayMap<>();
        Cursor finderCursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_FINDER_PROJECTION, null, null,
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (finderCursor != null) {
            int bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);
            int finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            int finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media._ID);
            while (finderCursor.moveToNext()) {
                String bucketId = finderCursor.getString(bucketIdColumnIndex);
                String finderName = finderCursor.getString(finderNameColumnIndex);
                String finderPath = finderCursor.getString(finderPathColumnIndex);
                long id = finderCursor.getLong(idColumnIndex);
                FinderEntity finderEntity = finderEntityMap.get(finderName);
                if (finderEntity == null && FileUtils.getPathFile(finderPath) != null) {
                    finderEntityMap.put(finderName, new FinderEntity(finderName, finderPath, id, bucketId, cursorCount(bucketId)));
                }
            }
            finderCursor.close();
        }
        if (finderEntityMap.isEmpty()) {
            return;
        }
        FinderEntity finderEntity = new FinderEntity(AlbumConstant.ALL_ALBUM_NAME, null, 0, null, 0);
        int count = 0;
        for (Map.Entry<String, FinderEntity> entry : finderEntityMap.entrySet()) {
            finderEntityList.add(entry.getValue());
            count += entry.getValue().getCount();
        }
        finderEntity.setCount(count);
        if (finderEntityList.size() > 0 && finderEntityList.get(0) != null) {
            finderEntity.setThumbnailsPath(finderEntityList.get(0).getThumbnailsPath());
            finderEntity.setThumbnailsId(finderEntityList.get(0).getThumbnailsId());
        }
        finderEntityList.add(0, finderEntity);
        finderEntityMap.clear();
    }

    @Override
    public int cursorCount(String bucketId) {
        Cursor query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_COUNT_PROJECTION,
                FINDER_VIDEO_SELECTION,
                new String[]{bucketId},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }

    @Override
    public Cursor getCursor(String bucketId, int page, int count) {
        String sortOrder = count == -1 ? MediaStore.Video.Media.DATE_MODIFIED + " desc" : MediaStore.Video.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count;
        String selection = TextUtils.isEmpty(bucketId) ? null : FINDER_VIDEO_SELECTION;
        String[] args = TextUtils.isEmpty(bucketId) ? null : new String[]{bucketId};
        return contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                selection,
                args,
                sortOrder);
    }

    @Override
    public String[] getSelectionArgs(String bucketId) {
        return null;
    }
}
