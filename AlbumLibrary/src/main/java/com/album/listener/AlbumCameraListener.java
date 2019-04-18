package com.album.listener;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * by y on 28/08/2017.
 * <p>
 * customize camera ui
 */

public interface AlbumCameraListener {

    /**
     * 自定义相机
     *
     * @param fragment {@link com.album.ui.fragment.AlbumFragment}
     */
    void startCamera(@NonNull Fragment fragment);
}
