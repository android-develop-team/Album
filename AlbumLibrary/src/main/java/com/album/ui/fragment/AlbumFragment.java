package com.album.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.annotation.AlbumResultType;
import com.album.entity.AlbumEntity;
import com.album.entity.FinderEntity;
import com.album.listener.AlbumCameraListener;
import com.album.listener.AlbumVideoListener;
import com.album.presenter.impl.AlbumPresenterImpl;
import com.album.ui.activity.PreviewActivity;
import com.album.ui.adapter.AlbumAdapter;
import com.album.ui.view.AlbumMethodFragmentView;
import com.album.ui.view.AlbumView;
import com.album.ui.widget.LoadMoreRecyclerView;
import com.album.ui.widget.SimpleGridDivider;
import com.album.util.AlbumTool;
import com.album.util.FileUtils;
import com.album.util.PermissionUtils;
import com.album.util.scanner.SingleMediaScanner;
import com.album.util.scanner.SingleScannerListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * by y on 14/08/2017.
 */

public class AlbumFragment extends Fragment implements
        AlbumView,
        AlbumMethodFragmentView,
        AlbumAdapter.OnItemClickListener,
        SingleScannerListener, LoadMoreRecyclerView.LoadMoreListener {

    private Activity albumActivity;

    private LoadMoreRecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlbumAdapter albumAdapter;
    private AppCompatImageView emptyView;
    private AlbumPresenterImpl albumPresenter;

    private Uri uCropImagePath = null;
    private Uri imagePath = null;
    private SingleMediaScanner singleMediaScanner = null;
    private ArrayList<FinderEntity> finderEntityList = null;
    private ArrayList<AlbumEntity> multipleAlbumEntity = null;
    private AlbumConfig albumConfig = null;
    private String bucketId = null;
    private int page = 0;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        bucketId = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID);
        multipleAlbumEntity = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT);
        imagePath = savedInstanceState.getParcelable(AlbumConstant.TYPE_ALBUM_STATE_URI_PATH);
        uCropImagePath = savedInstanceState.getParcelable(AlbumConstant.TYPE_ALBUM_STATE_CROP_URI_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        View inflate = inflater.inflate(R.layout.fragment_album, container, false);
        inflate.findViewById(R.id.album_content_view).setBackgroundColor(ContextCompat.getColor(inflate.getContext(), albumConfig.getAlbumContentViewBackground()));
        recyclerView = inflate.findViewById(R.id.album_recyclerView);
        progressBar = inflate.findViewById(R.id.album_progress);
        emptyView = inflate.findViewById(R.id.album_empty);
        albumActivity = getActivity();
        albumPresenter = new AlbumPresenterImpl(this, albumConfig.isVideo());

        Drawable drawable = ContextCompat.getDrawable(albumActivity, albumConfig.getAlbumContentEmptyDrawable());
        Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(albumActivity, albumConfig.getAlbumContentEmptyDrawableColor()), PorterDuff.Mode.SRC_ATOP);
        emptyView.setImageDrawable(drawable);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Album.getInstance().getEmptyClickListener() != null) {
                    if (Album.getInstance().getEmptyClickListener().click(v)) {
                        openCamera();
                    }
                }
            }
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        finderEntityList = new ArrayList<>();
        initRecyclerView();
        onScanAlbum(bucketId, false, false);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(albumActivity, albumConfig.getSpanCount()));
        recyclerView.setLoadingListener(this);
        recyclerView.addItemDecoration(new SimpleGridDivider(albumConfig.getDividerWidth()));
        albumAdapter = new AlbumAdapter(new ArrayList<AlbumEntity>(), AlbumTool.getImageViewWidth(albumActivity, albumConfig.getSpanCount()));
        albumAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(albumAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_CANCELED:
                switch (requestCode) {
                    case AlbumConstant.TYPE_PREVIEW_CODE:
                        onResultPreview(data.getExtras());
                        break;
                    case UCrop.REQUEST_CROP:
                        Album.getInstance().getAlbumListener().onAlbumFragmentCropCanceled();
                        break;
                    case AlbumConstant.ITEM_CAMERA:
                        Album.getInstance().getAlbumListener().onAlbumFragmentCameraCanceled();
                        break;
                }
                break;
            case UCrop.RESULT_ERROR:
                Album.getInstance().getAlbumListener().onAlbumFragmentUCropError(UCrop.getError(data));
                albumActivity.finish();
                break;
            case Activity.RESULT_OK:
                switch (requestCode) {
                    case AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE:
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            String customizePath = extras.getString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY);
                            if (!TextUtils.isEmpty(customizePath)) {
                                imagePath = Uri.fromFile(new File(customizePath));
                                refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA);
                                if (albumConfig.isCameraCrop()) {
                                    openUCrop(imagePath.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getUCropPath(), albumConfig.isVideo())));
                                }
                            }
                        }
                        break;
                    case AlbumConstant.ITEM_CAMERA:
                        refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA);
                        if (albumConfig.isCameraCrop()) {
                            openUCrop(imagePath.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getUCropPath(), albumConfig.isVideo())));
                        }
                        break;
                    case UCrop.REQUEST_CROP:
                        Album.getInstance().getAlbumListener().onAlbumUCropResources(FileUtils.getScannerFile(uCropImagePath.getPath()));
                        refreshMedia(AlbumConstant.TYPE_RESULT_CROP);
                        albumActivity.finish();
                        break;
                    case AlbumConstant.TYPE_PREVIEW_CODE:
                        onResultPreview(data.getExtras());
                        break;
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disconnectMediaScanner();
    }

    @Override
    public void showProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void scanSuccess(ArrayList<AlbumEntity> albumEntityList) {
        if (emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(bucketId) && !albumConfig.isHideCamera() && page == 0 && albumEntityList != null && !albumEntityList.isEmpty()) {
            albumEntityList.add(0, new AlbumEntity(null, null, AlbumConstant.CAMERA, 0, false));
        }
        albumAdapter.addAll(albumEntityList);
        if (page == 0 && !albumConfig.isRadio()) {
            ArrayList<AlbumEntity> selectEntity = Album.getInstance().getAlbumEntityList();
            if (selectEntity != null && !selectEntity.isEmpty() && albumEntityList != null && !albumEntityList.isEmpty()) {
                albumPresenter.firstMergeEntity(albumEntityList, selectEntity);
                albumAdapter.setMultiplePreviewList(selectEntity);
            }
        }
        ++page;
    }

    @Override
    public void scanFinder(ArrayList<FinderEntity> list) {
        finderEntityList.clear();
        finderEntityList.addAll(list);
    }

    @Override
    public ArrayList<AlbumEntity> getSelectEntity() {
        if (multipleAlbumEntity != null) {
            albumAdapter.setMultiplePreviewList(multipleAlbumEntity);
        }
        return albumAdapter.getMultiplePreviewList();
    }

    @Override
    public Activity getAlbumActivity() {
        return albumActivity;
    }

    @Override
    public void onAlbumNoMore() {
        if (page == 0) {
            emptyView.setVisibility(View.VISIBLE);
            Album.getInstance().getAlbumListener().onAlbumEmpty();
        }
        Album.getInstance().getAlbumListener().onAlbumNoMore();
    }

    @Override
    public void resultSuccess(AlbumEntity albumEntity) {
        if (albumEntity == null) {
            Album.getInstance().getAlbumListener().onAlbumResultCameraError();
        } else {
            albumAdapter.getAlbumList().add(1, albumEntity);
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position, AlbumEntity albumEntity) {
        if (position == 0 && TextUtils.equals(albumEntity.getPath(), AlbumConstant.CAMERA)) {
            if (PermissionUtils.camera(albumActivity)) {
                openCamera();
            }
            return;
        }
        if (!FileUtils.isFile(albumEntity.getPath())) {
            Album.getInstance().getAlbumListener().onAlbumFragmentFileNull();
            return;
        }
        if (albumConfig.isVideo()) {
            AlbumVideoListener albumVideoListener = Album.getInstance().getAlbumVideoListener();
            if (albumVideoListener != null) {
                albumVideoListener.startVideo(this);
                return;
            }
            try {
                Intent openVideo = new Intent(Intent.ACTION_VIEW);
                openVideo.setDataAndType(Uri.parse(albumEntity.getPath()), AlbumConstant.VIDEO_PLAY_TYPE);
                startActivity(openVideo);
            } catch (Exception e) {
                Album.getInstance().getAlbumListener().onVideoPlayError();
            }
            return;
        }
        if (albumConfig.isRadio()) {
            if (albumConfig.isCrop()) {
                openUCrop(albumEntity.getPath(), uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getUCropPath(), albumConfig.isVideo())));
            } else {
                List<AlbumEntity> list = new ArrayList<>();
                list.add(albumEntity);
                Album.getInstance().getAlbumListener().onAlbumResources(list);
                albumActivity.finish();
            }
            return;
        }
        Bundle bundle = new Bundle();
        ArrayList<AlbumEntity> multiplePreviewList = albumAdapter.getMultiplePreviewList();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, multiplePreviewList);
        bundle.putInt(AlbumConstant.PREVIEW_POSITION_KEY, albumConfig.isHideCamera() ? position + 1 : position);
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, bucketId);
        startActivityForResult(new Intent(albumActivity, PreviewActivity.class).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE);
    }

    @Override
    public void onScanStart() {
    }

    @Override
    public void onScanCompleted(@AlbumResultType int type) {
        if (type == AlbumConstant.TYPE_RESULT_CROP) {
            return;
        }
        onScanAlbum(bucketId, false, true);
    }


    @Override
    public void onScanAlbum(final String bucketId, boolean isFinder, boolean result) {
        if (isFinder && albumAdapter != null) {
            page = 0;
            albumAdapter.removeAll();
        }
        this.bucketId = bucketId;
        if (PermissionUtils.storage(albumActivity)) {
            if (result && !albumAdapter.getAlbumList().isEmpty()) {
                albumPresenter.resultScan(imagePath.getPath());
                return;
            }
            albumPresenter.scan(bucketId, page, albumConfig.getCount());
        }
    }

    @Override
    public void disconnectMediaScanner() {
        if (singleMediaScanner != null) {
            singleMediaScanner.disconnect();
            singleMediaScanner = null;
        }
    }

    @Override
    public void openCamera() {
        AlbumCameraListener albumCameraListener = Album.getInstance().getAlbumCameraListener();
        if (albumCameraListener != null) {
            albumCameraListener.startCamera(this);
            return;
        }
        imagePath = Uri.fromFile(FileUtils.getCameraFile(albumActivity, albumConfig.getCameraPath(), albumConfig.isVideo()));
        int i = AlbumTool.openCamera(this, imagePath, albumConfig.isVideo());
        if (i == 1) {
            Album.getInstance().getAlbumListener().onAlbumOpenCameraError();
        }
    }

    @Override
    public void openUCrop(String path, Uri uri) {
        UCrop.of(Uri.fromFile(new File(path)), uri)
                .withOptions(Album.getInstance().getOptions())
                .start(albumActivity, this);
    }


    @Override
    public void refreshMedia(@AlbumResultType int type) {
        disconnectMediaScanner();
        singleMediaScanner = new SingleMediaScanner(albumActivity,
                FileUtils.getScannerFile(type == AlbumConstant.TYPE_RESULT_CAMERA ?
                        imagePath.getPath() :
                        uCropImagePath.getPath()),
                AlbumFragment.this, type);
    }

    @Override
    public List<FinderEntity> getFinderEntity() {
        return finderEntityList;
    }

    @Override
    public void multiplePreview() {
        ArrayList<AlbumEntity> albumEntityList = albumAdapter.getMultiplePreviewList();
        if (albumEntityList == null || albumEntityList.isEmpty()) {
            Album.getInstance().getAlbumListener().onAlbumBottomPreviewNull();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, albumEntityList);
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, AlbumConstant.PREVIEW_BUTTON_KEY);
        startActivityForResult(new Intent(albumActivity, PreviewActivity.class).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE);
    }

    @Override
    public void multipleSelect() {
        List<AlbumEntity> albumEntityList = albumAdapter.getMultiplePreviewList();
        if (albumEntityList == null || albumEntityList.isEmpty()) {
            Album.getInstance().getAlbumListener().onAlbumBottomSelectNull();
            return;
        }
        Album.getInstance().getAlbumListener().onAlbumResources(albumEntityList);
        albumActivity.finish();
    }

    @Override
    public void onResultPreview(Bundle bundle) {
        ArrayList<AlbumEntity> previewAlbumEntity = bundle.getParcelableArrayList(AlbumConstant.PREVIEW_KEY);
        boolean isRefreshUI = bundle.getBoolean(AlbumConstant.PREVIEW_REFRESH_UI, true);
        boolean isFinish = bundle.getBoolean(AlbumConstant.PREVIEW_FINISH, false);
        if (isFinish) {
            albumActivity.finish();
            return;
        }
        if (previewAlbumEntity == null) {
            return;
        }
        multipleAlbumEntity = previewAlbumEntity;
        if (!isRefreshUI) {
            return;
        }
        albumPresenter.mergeEntity(albumAdapter.getAlbumList(), previewAlbumEntity);
        albumAdapter.setMultiplePreviewList(previewAlbumEntity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT, albumAdapter.getMultiplePreviewList());
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID, bucketId);
        outState.putParcelable(AlbumConstant.TYPE_ALBUM_STATE_URI_PATH, imagePath);
        outState.putParcelable(AlbumConstant.TYPE_ALBUM_STATE_CROP_URI_PATH, uCropImagePath);
    }


    @Override
    public void onLoadMore() {
        if (PermissionUtils.storage(albumActivity) && !albumPresenter.isScan()) {
            albumPresenter.scan(bucketId, page, albumConfig.getCount());
        }
    }
}
