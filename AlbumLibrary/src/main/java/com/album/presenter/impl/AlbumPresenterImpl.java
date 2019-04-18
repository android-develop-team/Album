package com.album.presenter.impl;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.presenter.AlbumPresenter;
import com.album.ui.view.AlbumView;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.scan.AlbumScanUtils;
import com.album.util.scan.VideoScanUtils;
import com.album.util.task.AlbumTask;
import com.album.util.task.AlbumTaskCallBack;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class AlbumPresenterImpl implements AlbumPresenter, ScanCallBack {
    private final AlbumView albumView;
    private boolean isScan = false;
    private final ScanView scanView;

    public AlbumPresenterImpl(AlbumView albumView, boolean isVideo) {
        this.albumView = albumView;
        ContentResolver contentResolver = albumView.getAlbumActivity().getContentResolver();
        scanView = isVideo ? VideoScanUtils.get(contentResolver) : AlbumScanUtils.get(contentResolver);
    }

    @Override
    public void scan(final String bucketId, final int page, final int count) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isScan = true;
                if (page == 0) {
                    albumView.showProgress();
                }
            }
        });
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.start(AlbumPresenterImpl.this, bucketId, page, count);
            }
        });
    }

    @Override
    public void mergeEntity(ArrayList<AlbumEntity> albumList, ArrayList<AlbumEntity> multiplePreviewList) {
        if (albumList == null || multiplePreviewList == null) {
            return;
        }
        for (AlbumEntity albumEntity : albumList) {
            albumEntity.setCheck(false);
        }
        for (AlbumEntity albumEntity : multiplePreviewList) {
            String path = albumEntity.getPath();
            for (AlbumEntity allAlbumEntity : albumList) {
                String allEntityPath = allAlbumEntity.getPath();
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.setCheck(albumEntity.isCheck());
                }
            }
        }
    }

    @Override
    public void firstMergeEntity(ArrayList<AlbumEntity> albumEntityList, ArrayList<AlbumEntity> selectEntity) {
        for (AlbumEntity albumEntity : albumEntityList) {
            albumEntity.setCheck(false);
        }
        for (AlbumEntity albumEntity : selectEntity) {
            albumEntity.setCheck(true);
        }
        for (AlbumEntity albumEntity : selectEntity) {
            String path = albumEntity.getPath();
            for (AlbumEntity allAlbumEntity : albumEntityList) {
                String allEntityPath = allAlbumEntity.getPath();
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.setCheck(albumEntity.isCheck());
                }
            }
        }

    }

    @Override
    public boolean isScan() {
        return isScan;
    }

    @Override
    public void resultScan(final String path) {
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.resultScan(AlbumPresenterImpl.this, path);
            }
        });
    }

    @Override
    public void scanSuccess(final ArrayList<AlbumEntity> albumEntityList, final ArrayList<FinderEntity> list) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isScan = false;
                albumView.hideProgress();
                if (albumEntityList != null && albumEntityList.isEmpty()) {
                    albumView.onAlbumNoMore();
                } else {
                    mergeEntity(albumEntityList, albumView.getSelectEntity());
                    albumView.scanSuccess(albumEntityList);
                    if (list != null && !list.isEmpty()) {
                        albumView.scanFinder(list);
                    }
                }
            }
        });
    }

    @Override
    public void resultSuccess(final AlbumEntity albumEntity, final ArrayList<FinderEntity> finderEntityList) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                albumView.resultSuccess(albumEntity);
                if (finderEntityList != null && !finderEntityList.isEmpty()) {
                    albumView.scanFinder(finderEntityList);
                }
            }
        });
    }
}
