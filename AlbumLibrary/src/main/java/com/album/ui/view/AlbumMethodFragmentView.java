package com.album.ui.view;

import android.net.Uri;
import android.os.Bundle;

import com.album.entity.FinderEntity;
import com.album.annotation.AlbumResultType;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public interface AlbumMethodFragmentView {

    void initRecyclerView();

    void disconnectMediaScanner();

    void onScanAlbum(String bucketId, boolean isFinder, boolean result);

    void openCamera();

    void openUCrop(String path, Uri uri);

    void refreshMedia(@AlbumResultType int type);

    List<FinderEntity> getFinderEntity();

    void multiplePreview();

    void multipleSelect();

    void onResultPreview(Bundle bundle);
}
