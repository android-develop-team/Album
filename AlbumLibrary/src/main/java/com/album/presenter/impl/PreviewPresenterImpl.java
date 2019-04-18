package com.album.presenter.impl;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.presenter.PreviewPresenter;
import com.album.ui.view.PreviewView;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.scan.AlbumScanUtils;
import com.album.util.scan.VideoScanUtils;
import com.album.util.task.AlbumTask;
import com.album.util.task.AlbumTaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public class PreviewPresenterImpl implements PreviewPresenter, ScanCallBack {
    private final PreviewView previewView;
    private final ScanView scanView;

    public PreviewPresenterImpl(PreviewView previewView, boolean isVideo) {
        this.previewView = previewView;
        ContentResolver contentResolver = previewView.getPreviewActivity().getContentResolver();
        scanView = isVideo ? VideoScanUtils.get(contentResolver) : AlbumScanUtils.get(contentResolver);
    }

    @Override
    public void scan(final String bucketId, final int page, final int count) {
        previewView.getPreviewActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewView.showProgress();
            }
        });
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.start(PreviewPresenterImpl.this, bucketId, page, count);
            }
        });
    }

    @Override
    public void mergeEntity(List<AlbumEntity> albumEntityList, ArrayList<AlbumEntity> selectAlbumEntityList) {
        if (albumEntityList == null || selectAlbumEntityList == null) {
            return;
        }
        for (AlbumEntity albumEntity : selectAlbumEntityList) {
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
    public void scanSuccess(final ArrayList<AlbumEntity> albumEntityList, ArrayList<FinderEntity> list) {
        previewView.getPreviewActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewView.hideProgress();
                previewView.scanSuccess(albumEntityList);
            }
        });
    }

    @Override
    public void resultSuccess(AlbumEntity albumEntity, ArrayList<FinderEntity> finderEntityList) {

    }

}
