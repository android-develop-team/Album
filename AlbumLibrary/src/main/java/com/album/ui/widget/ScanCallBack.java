package com.album.ui.widget;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;

import java.util.ArrayList;

/**
 * by y on 04/09/2017.
 */

public interface ScanCallBack {
    void scanSuccess(ArrayList<AlbumEntity> albumEntityList, ArrayList<FinderEntity> list);

    void resultSuccess(AlbumEntity albumEntity, ArrayList<FinderEntity> finderEntityList);
}
