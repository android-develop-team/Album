package com.album.ui.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.album.listener.AlbumListener;
import com.album.entity.AlbumEntity;

import java.io.File;
import java.util.List;

/**
 * by y on 18/08/2017.
 */

public class SimpleAlbumListener implements AlbumListener {

    @Override
    public void onAlbumActivityFinish() {

    }

    @Override
    public void onAlbumPermissionsDenied(int type) {

    }

    @Override
    public void onAlbumFragmentNull() {

    }

    @Override
    public void onAlbumPreviewFileNull() {

    }

    @Override
    public void onAlbumFinderNull() {

    }

    @Override
    public void onAlbumBottomPreviewNull() {

    }

    @Override
    public void onAlbumBottomSelectNull() {

    }

    @Override
    public void onAlbumFragmentFileNull() {

    }

    @Override
    public void onAlbumPreviewSelectNull() {

    }

    @Override
    public void onAlbumCheckBoxFileNull() {

    }

    @Override
    public void onAlbumFragmentCropCanceled() {

    }

    @Override
    public void onAlbumFragmentCameraCanceled() {

    }

    @Override
    public void onAlbumFragmentUCropError(@Nullable Throwable data) {

    }

    @Override
    public void onAlbumResources(@NonNull List<AlbumEntity> list) {

    }

    @Override
    public void onAlbumUCropResources(@Nullable File scannerFile) {

    }

    @Override
    public void onAlbumMaxCount() {

    }

    @Override
    public void onAlbumActivityBackPressed() {

    }

    @Override
    public void onAlbumOpenCameraError() {

    }

    @Override
    public void onAlbumEmpty() {

    }

    @Override
    public void onAlbumNoMore() {

    }

    @Override
    public void onAlbumResultCameraError() {

    }

    @Override
    public void onVideoPlayError() {

    }
}
