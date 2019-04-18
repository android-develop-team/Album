package com.album.presenter;

import com.album.entity.AlbumEntity;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public interface AlbumPresenter {
    void scan(String bucketId, int page, int count);

    void mergeEntity(ArrayList<AlbumEntity> albumList, ArrayList<AlbumEntity> multiplePreviewList);

    void firstMergeEntity(ArrayList<AlbumEntity> albumEntityList, ArrayList<AlbumEntity> selectEntity);

    boolean isScan();

    void resultScan(String path);
}
