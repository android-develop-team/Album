package com.album.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.album.Album;
import com.album.AlbumConstant;
import com.album.R;
import com.album.entity.AlbumEntity;
import com.album.presenter.PreviewPresenter;
import com.album.presenter.impl.PreviewPresenterImpl;
import com.album.ui.adapter.PreviewAdapter;
import com.album.annotation.PermissionsType;
import com.album.ui.view.PreviewMethodActivityView;
import com.album.ui.view.PreviewView;
import com.album.util.AlbumTool;
import com.album.util.FileUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * by y on 15/08/2017.
 */

public class PreviewActivity extends AlbumBaseActivity implements View.OnClickListener, PreviewView, PreviewMethodActivityView {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private PreviewAdapter adapter;
    private AppCompatCheckBox appCompatCheckBox;
    private AppCompatTextView previewCount;
    private ProgressBar progressBar;


    private PreviewPresenter previewPresenter;
    private ArrayList<AlbumEntity> albumEntityList;
    private ArrayList<AlbumEntity> selectAlbumEntityList;
    private int selectPosition;
    private String bucketId = null;
    private boolean isPreview;


    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        previewPresenter = new PreviewPresenterImpl(this, albumConfig.isVideo());
        albumEntityList = new ArrayList<>();
        initBundle();
        isPreview = TextUtils.equals(bucketId, AlbumConstant.PREVIEW_BUTTON_KEY);
        if (savedInstanceState != null) {
            ArrayList<AlbumEntity> select = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT);
            ArrayList<AlbumEntity> allImageEntity = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT_ALL);
            selectAlbumEntityList = select;
            selectPosition = savedInstanceState.getInt(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT_POSITION);
            if (isPreview) {
                albumEntityList = allImageEntity;
            }
        }
        init();
        setCount(selectAlbumEntityList.size(), albumConfig.getMultipleMaxCount());
    }

    @Override
    public void initBundle() {
        Bundle extras = getIntent().getExtras();
        selectAlbumEntityList = Objects.requireNonNull(extras).getParcelableArrayList(AlbumConstant.PREVIEW_KEY);
        selectPosition = extras.getInt(AlbumConstant.PREVIEW_POSITION_KEY, 0);
        bucketId = extras.getString(AlbumConstant.PREVIEW_BUCKET_ID);
    }

    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefreshAlbumUI(albumConfig.isPreviewFinishRefresh(), false);
            }
        });
        if (!isPreview) {
            previewPresenter.scan(bucketId, -1, -1);
            return;

        }
        if (albumEntityList.isEmpty()) {
            albumEntityList.addAll(selectAlbumEntityList);
        }
        initViewPager(albumEntityList);
    }

    @Override
    public void initViewPager(final ArrayList<AlbumEntity> albumEntityList) {
        adapter = new PreviewAdapter(albumEntityList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(TextUtils.isEmpty(bucketId) ? selectPosition - 1 : selectPosition);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (!FileUtils.isFile(adapter.getAlbumPath(position))) {
                    Album.getInstance().getAlbumListener().onAlbumPreviewFileNull();
                }
                appCompatCheckBox.setChecked(albumEntityList.get(position).isCheck());
                selectPosition = TextUtils.isEmpty(bucketId) ? position + 1 : position;
                setTitles(position + 1, albumEntityList.size());
            }
        });
        setTitles(viewPager.getCurrentItem() + 1, albumEntityList.size());
        appCompatCheckBox.setChecked(albumEntityList.get(viewPager.getCurrentItem()).isCheck());
    }


    @Override
    protected void initTitle() {
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarTextColor()));
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumToolbarIcon());
        Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumToolbarIconColor()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(drawable);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarBackground()));
        if (AlbumTool.hasL()) {
            toolbar.setElevation(albumConfig.getAlbumToolbarElevation());
        }
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.preview_toolbar);
        viewPager = findViewById(R.id.preview_viewPager);
        appCompatCheckBox = findViewById(R.id.preview_check_box);
        previewCount = findViewById(R.id.preview_tv_preview_count);
        progressBar = findViewById(R.id.preview_progress);
        LinearLayout rootView = findViewById(R.id.preview_root_view);
        RelativeLayout previewBottomView = findViewById(R.id.preview_bottom_view);
        AppCompatTextView previewOk = findViewById(R.id.preview_bottom_view_tv_select);
        appCompatCheckBox.setBackgroundResource(albumConfig.getAlbumContentItemCheckBoxDrawable());
        previewBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBottomViewBackground()));
        previewOk.setText(albumConfig.getAlbumPreviewBottomOkText());
        previewOk.setTextSize(albumConfig.getAlbumPreviewBottomOkTextSize());
        previewOk.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBottomOkTextColor()));
        previewCount.setTextSize(albumConfig.getAlbumPreviewBottomCountTextSize());
        previewCount.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBottomCountTextColor()));
        rootView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBackground()));
        appCompatCheckBox.setOnClickListener(this);
        previewOk.setOnClickListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void permissionsDenied(@PermissionsType int type) {
        Album.getInstance().getAlbumListener().onAlbumPermissionsDenied(type);
        finish();
    }

    @Override
    protected void permissionsGranted(@PermissionsType int type) {
        switch (type) {
            case AlbumConstant.TYPE_PERMISSIONS_ALBUM:
                init();
                break;
            case AlbumConstant.TYPE_PERMISSIONS_CAMERA:
                break;
        }
    }

    @Override
    public void setTitles(int page, int imageSize) {
        toolbar.setTitle(getString(albumConfig.getAlbumPreviewTitle()) + "(" + page + "/" + imageSize + ")");
    }

    @Override
    public void setCount(int count, int size) {
        previewCount.setText(String.format("%s / %s", String.valueOf(count), String.valueOf(size)));
    }

    @Override
    public void checkBoxClick() {
        AlbumEntity albumEntity = adapter.getAlbumEntity(viewPager.getCurrentItem());
        if (!selectAlbumEntityList.contains(albumEntity) && selectAlbumEntityList.size() >= albumConfig.getMultipleMaxCount()) {
            appCompatCheckBox.setChecked(false);
            Album.getInstance().getAlbumListener().onAlbumMaxCount();
            return;
        }
        if (albumEntity.isCheck()) {
            selectAlbumEntityList.remove(albumEntity);
            albumEntity.setCheck(false);
        } else {
            albumEntity.setCheck(true);
            selectAlbumEntityList.add(albumEntity);
        }
        setCount(selectAlbumEntityList.size(), albumConfig.getMultipleMaxCount());
    }

    @Override
    public void isRefreshAlbumUI(boolean isRefresh, boolean isFinish) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, selectAlbumEntityList);
        bundle.putBoolean(AlbumConstant.PREVIEW_REFRESH_UI, isRefresh);
        bundle.putBoolean(AlbumConstant.PREVIEW_FINISH, isFinish);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.preview_check_box) {
            checkBoxClick();
        } else if (i == R.id.preview_bottom_view_tv_select) {
            if (selectAlbumEntityList == null || selectAlbumEntityList.isEmpty()) {
                Album.getInstance().getAlbumListener().onAlbumPreviewSelectNull();
                return;
            }
            Album.getInstance().getAlbumListener().onAlbumResources(selectAlbumEntityList);
            isRefreshAlbumUI(false, true);
        }
    }

    @Override
    public void onBackPressed() {
        isRefreshAlbumUI(albumConfig.isPreviewBackRefresh(), false);
        super.onBackPressed();
    }

    @Override
    public void scanSuccess(ArrayList<AlbumEntity> albumEntityList) {
        previewPresenter.mergeEntity(albumEntityList, selectAlbumEntityList);
        initViewPager(albumEntityList);
    }

    @Override
    public Activity getPreviewActivity() {
        return this;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT, selectAlbumEntityList);
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT_ALL, isPreview ? albumEntityList : null);
        outState.putInt(AlbumConstant.TYPE_ALBUM_PREVIEW_STATE_SELECT_POSITION, selectPosition);
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

}
