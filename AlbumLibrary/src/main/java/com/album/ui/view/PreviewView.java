package com.album.ui.view;

import android.app.Activity;

import com.album.entity.AlbumEntity;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 */

public interface PreviewView {

    void scanSuccess(ArrayList<AlbumEntity> albumEntityList);

    Activity getPreviewActivity();

    void hideProgress();

    void showProgress();

}
