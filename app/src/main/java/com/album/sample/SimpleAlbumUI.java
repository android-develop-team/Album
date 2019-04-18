package com.album.sample;

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
import android.widget.Toast;

import com.album.AlbumConstant;
import com.album.entity.FinderEntity;
import com.album.ui.activity.AlbumBaseActivity;
import com.album.ui.adapter.ListPopupWindowAdapter;
import com.album.annotation.PermissionsType;
import com.album.ui.fragment.AlbumFragment;

import java.util.List;
import java.util.Objects;

/**
 * by y on 22/08/2017.
 */

public class SimpleAlbumUI extends AlbumBaseActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private AlbumFragment albumFragment;
    private ListPopupWindow listPopupWindow;
    private AppCompatTextView finerName;

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
        initListPopupWindow();
    }

    private void initListPopupWindow() {
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAnchorView(finerName);
        listPopupWindow.setWidth(albumConfig.getAlbumListPopupWidth());
        listPopupWindow.setHorizontalOffset(albumConfig.getAlbumListPopupHorizontalOffset());
        listPopupWindow.setVerticalOffset(albumConfig.getAlbumListPopupVerticalOffset());
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(this);
    }

    private void initFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(AlbumFragment.class.getSimpleName());
        if (fragment != null) {
            albumFragment = (AlbumFragment) fragment;
        } else {
            albumFragment = AlbumFragment.newInstance();
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.sample_album_frame, albumFragment, AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.sample_toolbar);
        finerName = findViewById(R.id.sample_finder_name);
        finerName.setText(TextUtils.isEmpty(finderName) ? getString(R.string.album_all) : finderName);
        finerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FinderEntity> finderEntity = albumFragment.getFinderEntity();
                if (finderEntity != null && !finderEntity.isEmpty()) {
                    listPopupWindow.setAdapter(new ListPopupWindowAdapter(finderEntity));
                    listPopupWindow.show();
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        toolbar.setTitle("sample UI");
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumToolbarIcon());
        Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumToolbarIconColor()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarTextColor()));
        toolbar.setNavigationIcon(drawable);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_album;
    }

    @Override
    protected void permissionsDenied(@PermissionsType int type) {
        Toast.makeText(this, "permissions error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void permissionsGranted(@PermissionsType int type) {
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
        finerName.setText(finder.getDirName());
        albumFragment.onScanAlbum(finder.getBucketId(), true, false);
        listPopupWindow.dismiss();
    }

}
