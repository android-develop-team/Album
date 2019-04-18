package com.album.util.scan;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.album.Album;
import com.album.AlbumConstant;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.FileUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */
public class AlbumScanUtils implements ScanView {

    private final ContentResolver contentResolver;
    private static final String ALL_ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? ";
    private static final String FINDER_ALBUM_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? %s and  (" + ALL_ALBUM_SELECTION + " ) ";
    private static final String[] ALBUM_NO_BUCKET_ID_SELECTION_ARGS = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
    private static final String[] ALBUM_COUNT_PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.SIZE,
    };
    private static final String[] ALBUM_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
    };
    private static final String[] ALBUM_FINDER_PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
    };

    private AlbumScanUtils(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static AlbumScanUtils get(ContentResolver contentResolver) {
        if (contentResolver == null) {
            throw new NullPointerException("contentResolver == null");
        }
        return new AlbumScanUtils(contentResolver);
    }

    @Override
    public void start(ScanCallBack scanCallBack, String bucketId, int page, int count) {
        ArrayList<AlbumEntity> albumEntityList = new ArrayList<>();
        ArrayList<FinderEntity> finderEntityList = new ArrayList<>();
        Cursor cursor = getCursor(bucketId, page, count);
        if (cursor != null) {
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                MediaStore.Images.Media.DATA + "= ? ",
                new String[]{path},
                MediaStore.Images.Media.DATE_MODIFIED);
        if (query != null) {
            int dataColumnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = query.getColumnIndex(MediaStore.Images.Media._ID);
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
        long size = cursor.getLong(sizeColumnIndex);
        if (FileUtils.getPathFile(path) != null) {
            if (Album.getInstance().getConfig().isFilterImg() && size <= 0) {
                return;
            }
            albumEntityList.add(new AlbumEntity(null, null, path, id, false));
        }
    }

    @Override
    public void cursorFinder(ArrayList<FinderEntity> finderEntityList) {
        ArrayMap<String, FinderEntity> finderEntityMap = new ArrayMap<>();
        Cursor finderCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_FINDER_PROJECTION, ALL_ALBUM_SELECTION, ALBUM_NO_BUCKET_ID_SELECTION_ARGS,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (finderCursor != null) {
            int bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int sizeColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            while (finderCursor.moveToNext()) {
                String bucketId = finderCursor.getString(bucketIdColumnIndex);
                String finderName = TextUtils.equals(finderCursor.getString(finderNameColumnIndex), "0") ? Album.getInstance().getConfig().getSdName() : finderCursor.getString(finderNameColumnIndex);
                String finderPath = finderCursor.getString(finderPathColumnIndex);
                long size = finderCursor.getLong(sizeColumnIndex);
                long id = finderCursor.getLong(idColumnIndex);
                FinderEntity finderEntity = finderEntityMap.get(finderName);
                if (finderEntity == null && FileUtils.getPathFile(finderPath) != null) {
                    if (Album.getInstance().getConfig().isFilterImg() && size <= 0) {
                        continue;
                    }
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
        String selection = Album.getInstance().getConfig().isFilterImg() ? String.format(FINDER_ALBUM_SELECTION, " and _size > 0") : String.format(FINDER_ALBUM_SELECTION, "");
        Cursor query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_COUNT_PROJECTION,
                selection,
                getSelectionArgs(bucketId),
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }

    @Override
    public Cursor getCursor(String bucketId, int page, int count) {
        String sortOrder = count == -1 ? MediaStore.Images.Media.DATE_MODIFIED + " desc" : MediaStore.Images.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count;
        String selection = TextUtils.isEmpty(bucketId) ? ALL_ALBUM_SELECTION : Album.getInstance().getConfig().isFilterImg() ? String.format(FINDER_ALBUM_SELECTION, " and _size > 0") : String.format(FINDER_ALBUM_SELECTION, "");
        String[] args = TextUtils.isEmpty(bucketId) ? ALBUM_NO_BUCKET_ID_SELECTION_ARGS : getSelectionArgs(bucketId);
        return contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                selection,
                args,
                sortOrder);
    }

    @Override
    public String[] getSelectionArgs(String bucketId) {
        return new String[]{bucketId, "image/jpeg", "image/png", "image/jpg", "image/gif"};
    }


}



