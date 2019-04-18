package com.album.presenter;


import com.album.entity.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public interface PreviewPresenter {
    void scan(String bucketId, int page, int count);

    void mergeEntity(List<AlbumEntity> albumEntityList, ArrayList<AlbumEntity> selectAlbumEntityList);

}
