package com.album.ui.view;

import android.app.Activity;

import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public interface AlbumView {

    void showProgress();

    void hideProgress();

    void scanSuccess(ArrayList<AlbumEntity> albumEntityList);

    void scanFinder(ArrayList<FinderEntity> list);

    ArrayList<AlbumEntity> getSelectEntity();

    Activity getAlbumActivity();

    void onAlbumNoMore();

    void resultSuccess(AlbumEntity albumEntity);
}
