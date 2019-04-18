package com.album.ui.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.album.Album;
import com.album.AlbumConstant;
import com.album.R;
import com.album.entity.FinderEntity;
import com.album.ui.adapter.ListPopupWindowAdapter;
import com.album.annotation.PermissionsType;
import com.album.ui.fragment.AlbumFragment;
import com.album.ui.view.AlbumMethodActivityView;
import com.album.util.AlbumTool;

import java.util.List;
import java.util.Objects;

/**
 * by y on 14/08/2017.
 */

public class AlbumActivity extends AlbumBaseActivity
        implements View.OnClickListener, AlbumMethodActivityView, AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private AppCompatTextView preview;
    private AppCompatTextView select;
    private AlbumFragment albumFragment;
    private AppCompatTextView finderTv;
    private ListPopupWindow listPopupWindow;
    private RelativeLayout albumBottomView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (albumFragment != null) {
            albumFragment.disconnectMediaScanner();
            albumFragment = null;
        }
        if (listPopupWindow != null) {
            listPopupWindow = null;
        }
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        if (albumFragment != null) {
            albumFragment = null;
        }
        initFragment();
        initBottomView();
        initListPopupWindow();
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.album_toolbar);
        preview = findViewById(R.id.album_tv_preview);
        select = findViewById(R.id.album_tv_select);
        finderTv = findViewById(R.id.album_tv_finder_all);
        albumBottomView = findViewById(R.id.album_bottom_view);
        listPopupWindow = new ListPopupWindow(this);
        preview.setOnClickListener(this);
        select.setOnClickListener(this);
        finderTv.setOnClickListener(this);
        preview.setVisibility(albumConfig.isRadio() ? View.GONE : View.VISIBLE);
        select.setVisibility(albumConfig.isRadio() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(AlbumFragment.class.getSimpleName());
        if (fragment != null) {
            albumFragment = (AlbumFragment) fragment;
        } else {
            albumFragment = AlbumFragment.newInstance();
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.album_frame, albumFragment, AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void initBottomView() {
        finderTv.setText(TextUtils.isEmpty(finderName) ? getString(R.string.album_all) : finderName);
        albumBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomViewBackground()));
        finderTv.setTextSize(albumConfig.getAlbumBottomFinderTextSize());
        finderTv.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomFinderTextColor()));
        finderTv.setCompoundDrawables(null, null, AlbumTool.getDrawable(this, albumConfig.getAlbumBottomFinderTextCompoundDrawable(), albumConfig.getAlbumBottomFinderTextDrawableColor()), null);
        if (albumConfig.getAlbumBottomFinderTextBackground() != -1) {
            finderTv.setBackgroundResource(albumConfig.getAlbumBottomFinderTextBackground());
        }

        preview.setText(albumConfig.getAlbumBottomPreViewText());
        preview.setTextSize(albumConfig.getAlbumBottomPreViewTextSize());
        preview.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomPreViewTextColor()));
        if (albumConfig.getAlbumBottomPreviewTextBackground() != -1) {
            preview.setBackgroundResource(albumConfig.getAlbumBottomPreviewTextBackground());
        }

        select.setText(albumConfig.getAlbumBottomSelectText());
        select.setTextSize(albumConfig.getAlbumBottomSelectTextSize());
        select.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomSelectTextColor()));
        if (albumConfig.getAlbumBottomSelectTextBackground() != -1) {
            select.setBackgroundResource(albumConfig.getAlbumBottomSelectTextBackground());
        }
    }

    @Override
    public void initListPopupWindow() {
        listPopupWindow.setAnchorView(finderTv);
        listPopupWindow.setWidth(albumConfig.getAlbumListPopupWidth());
        listPopupWindow.setHorizontalOffset(albumConfig.getAlbumListPopupHorizontalOffset());
        listPopupWindow.setVerticalOffset(albumConfig.getAlbumListPopupVerticalOffset());
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(this);
    }


    @Override
    protected void initTitle() {
        toolbar.setTitle(albumConfig.getAlbumToolbarText());
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarTextColor()));
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumToolbarIcon());
        Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumToolbarIconColor()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(drawable);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarBackground()));
        if (AlbumTool.hasL()) {
            toolbar.setElevation(albumConfig.getAlbumToolbarElevation());
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album.getInstance().getAlbumListener().onAlbumActivityFinish();
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    protected void permissionsDenied(@PermissionsType int type) {
        Album.getInstance().getAlbumListener().onAlbumPermissionsDenied(type);
        if (albumConfig.isPermissionsDeniedFinish()) {
            finish();
        }
    }

    @Override
    protected void permissionsGranted(int type) {
        switch (type) {
            case AlbumConstant.TYPE_PERMISSIONS_ALBUM:
                albumFragment.onScanAlbum(null, false, false);
                break;
            case AlbumConstant.TYPE_PERMISSIONS_CAMERA:
                albumFragment.openCamera();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (albumFragment == null) {
            Album.getInstance().getAlbumListener().onAlbumFragmentNull();
            return;
        }
        int i = v.getId();
        if (i == R.id.album_tv_preview) {
            albumFragment.multiplePreview();
        } else if (i == R.id.album_tv_select) {
            albumFragment.multipleSelect();
        } else if (i == R.id.album_tv_finder_all) {
            List<FinderEntity> finderEntity = albumFragment.getFinderEntity();
            if (finderEntity != null && !finderEntity.isEmpty()) {
                listPopupWindow.setAdapter(new ListPopupWindowAdapter(finderEntity));
                listPopupWindow.show();
                ListView listView = listPopupWindow.getListView();
                if (listView != null) {
                    listView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumListPopupItemBackground()));
                }
                return;
            }
            Album.getInstance().getAlbumListener().onAlbumFinderNull();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = listPopupWindow.getListView();
        if (listView == null) {
            return;
        }
        ListPopupWindowAdapter adapter = (ListPopupWindowAdapter) listView.getAdapter();
        FinderEntity finder = adapter.getFinder(position);
        if (finder == null) {
            return;
        }
        finderName = finder.getDirName();
        finderTv.setText(finder.getDirName());
        albumFragment.onScanAlbum(finder.getBucketId(), true, false);
        listPopupWindow.dismiss();
    }

    @Override
    public void onBackPressed() {
        Album.getInstance().getAlbumListener().onAlbumActivityBackPressed();
        super.onBackPressed();
    }
}
