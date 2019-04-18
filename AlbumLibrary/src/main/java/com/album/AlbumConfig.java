package com.album;

/**
 * by y on 14/08/2017.
 */

public class AlbumConfig {

    private boolean hideCamera = false;
    private boolean isRadio = false;
    private boolean isCrop = true;
    private boolean cameraCrop = false;
    private boolean previewFinishRefresh = false;
    private boolean previewBackRefresh = false;
    private boolean isPermissionsDeniedFinish = false;
    private boolean isFrescoImageLoader = false;
    private boolean isVideo = false;
    private int multipleMaxCount = 9;
    private String cameraPath = null;
    private String uCropPath = null;
    private int count = 500;
    private String sdName = "根目录";
    private boolean filterImg = false;

    //////////////  album toolbar //////
    private int albumStatusBarColor = R.color.colorAlbumStatusBarColorDay;
    private int albumToolbarBackground = R.color.colorAlbumToolbarBackgroundDay;
    private int albumToolbarIcon = R.drawable.ic_action_back_day;
    private int albumToolbarIconColor = R.color.colorAlbumToolbarIconColorDay;
    private int albumToolbarTextColor = R.color.colorAlbumToolbarTextColorDay;
    private int albumToolbarText = R.string.album_name;
    private float albumToolbarElevation = 6f;
    ////////////// album bottom view //////
    private int albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundDay;
    private int albumBottomFinderTextSize = 16;
    private int albumBottomFinderTextBackground = -1;
    private int albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorDay;
    private int albumBottomFinderTextCompoundDrawable = R.drawable.ic_action_album_finder_day;
    private int albumBottomFinderTextDrawableColor = R.color.colorAlbumBottomFinderTextDrawableColorDay;
    private int albumBottomPreViewText = R.string.album_preview;
    private int albumBottomPreViewTextSize = 16;
    private int albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorDay;
    private int albumBottomPreviewTextBackground = -1;
    private int albumBottomSelectText = R.string.album_select;
    private int albumBottomSelectTextSize = 16;
    private int albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorDay;
    private int albumBottomSelectTextBackground = -1;
    ///////////// album list popup window /////
    private int albumListPopupWidth = 600;
    private int albumListPopupHorizontalOffset = 0;
    private int albumListPopupVerticalOffset = 0;
    private int albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundDay;
    private int albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorDay;
    //////////// album content view ////////
    private int spanCount = 3;
    private int dividerWidth = 10;
    private int albumContentViewCameraTips = R.string.album_image_camera_tv_tips;
    private int albumContentViewCameraTipsSize = 18;
    private int albumContentViewCameraTipsColor = R.color.colorAlbumContentViewTipsColorDay;
    private int albumContentViewCameraBackgroundColor = R.color.colorAlbumContentViewBackgroundColorColorDay;
    private int albumContentViewBackground = R.color.colorAlbumContentViewBackgroundDay;
    private int albumContentViewCameraDrawable = R.drawable.ic_camera_alt_black_24dp;
    private int albumContentViewCameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorDay;
    private int albumContentItemCheckBoxDrawable = R.drawable.selector_album_item_check;
    private int albumContentEmptyDrawable = R.drawable.ic_camera_alt_black_24dp;
    private int albumContentEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorDay;
    /////////// album preview ////////
    private int albumPreviewTitle = R.string.preview_title;
    private int albumPreviewBackground = R.color.colorAlbumPreviewBackgroundDay;
    private int albumPreviewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundDay;
    private int albumPreviewBottomOkText = R.string.preview_select;
    private int albumPreviewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorDay;
    private int albumPreviewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorDay;
    private int albumPreviewBottomOkTextSize = 16;
    private int albumPreviewBottomCountTextSize = 16;

    public AlbumConfig() {
    }

    public AlbumConfig(int type) {
        switch (type) {
            case AlbumConstant.TYPE_NIGHT:
                albumStatusBarColor = R.color.colorAlbumStatusBarColorNight;
                albumToolbarBackground = R.color.colorAlbumToolbarBackgroundNight;
                albumToolbarTextColor = R.color.colorAlbumToolbarTextColorNight;
                albumToolbarIconColor = R.color.colorAlbumToolbarIconColorNight;

                albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundNight;
                albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorNight;
                albumBottomFinderTextDrawableColor = R.color.colorAlbumBottomFinderTextDrawableColorNight;
                albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorNight;
                albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorNight;

                albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorNight;
                albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundNight;

                albumContentViewBackground = R.color.colorAlbumContentViewBackgroundNight;
                albumContentViewCameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorNight;
                albumContentViewCameraTipsColor = R.color.colorAlbumContentViewTipsColorNight;
                albumContentViewCameraBackgroundColor = R.color.colorAlbumContentViewBackgroundColorColorNight;
                albumContentEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorNight;

                albumPreviewBackground = R.color.colorAlbumPreviewBackgroundNight;
                albumPreviewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundNight;
                albumPreviewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorNight;
                albumPreviewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorNight;
                break;
            default:
                throw new RuntimeException("type");
        }
    }

    public boolean isHideCamera() {
        return hideCamera;
    }

    public AlbumConfig setHideCamera(boolean hideCamera) {
        this.hideCamera = hideCamera;
        return this;
    }

    public int getAlbumToolbarBackground() {
        return albumToolbarBackground;
    }

    public AlbumConfig setAlbumToolbarBackground(int albumToolbarBackground) {
        this.albumToolbarBackground = albumToolbarBackground;
        return this;
    }

    public int getAlbumToolbarIcon() {
        return albumToolbarIcon;
    }

    public AlbumConfig setAlbumToolbarIcon(int albumToolbarIcon) {
        this.albumToolbarIcon = albumToolbarIcon;
        return this;
    }

    public int getAlbumStatusBarColor() {
        return albumStatusBarColor;
    }

    public AlbumConfig setAlbumStatusBarColor(int albumStatusBarColor) {
        this.albumStatusBarColor = albumStatusBarColor;
        return this;
    }

    public int getAlbumToolbarTextColor() {
        return albumToolbarTextColor;
    }

    public AlbumConfig setAlbumToolbarTextColor(int albumToolbarTextColor) {
        this.albumToolbarTextColor = albumToolbarTextColor;
        return this;
    }

    public int getAlbumToolbarText() {
        return albumToolbarText;
    }

    public AlbumConfig setAlbumToolbarText(int albumToolbarText) {
        this.albumToolbarText = albumToolbarText;
        return this;
    }

    public float getAlbumToolbarElevation() {
        return albumToolbarElevation;
    }

    public AlbumConfig setAlbumToolbarElevation(float albumToolbarElevation) {
        this.albumToolbarElevation = albumToolbarElevation;
        return this;
    }

    public int getAlbumBottomViewBackground() {
        return albumBottomViewBackground;
    }

    public AlbumConfig setAlbumBottomViewBackground(int albumBottomViewBackground) {
        this.albumBottomViewBackground = albumBottomViewBackground;
        return this;
    }

    public int getAlbumBottomFinderTextSize() {
        return albumBottomFinderTextSize;
    }

    public AlbumConfig setAlbumBottomFinderTextSize(int albumBottomFinderTextSize) {
        this.albumBottomFinderTextSize = albumBottomFinderTextSize;
        return this;
    }

    public int getAlbumBottomFinderTextColor() {
        return albumBottomFinderTextColor;
    }

    public AlbumConfig setAlbumBottomFinderTextColor(int albumBottomFinderTextColor) {
        this.albumBottomFinderTextColor = albumBottomFinderTextColor;
        return this;
    }

    public int getAlbumBottomPreViewText() {
        return albumBottomPreViewText;
    }

    public AlbumConfig setAlbumBottomPreViewText(int albumBottomPreViewText) {
        this.albumBottomPreViewText = albumBottomPreViewText;
        return this;
    }

    public int getAlbumBottomPreViewTextSize() {
        return albumBottomPreViewTextSize;
    }

    public AlbumConfig setAlbumBottomPreViewTextSize(int albumBottomPreViewTextSize) {
        this.albumBottomPreViewTextSize = albumBottomPreViewTextSize;
        return this;
    }

    public int getAlbumBottomPreViewTextColor() {
        return albumBottomPreViewTextColor;
    }

    public AlbumConfig setAlbumBottomPreViewTextColor(int albumBottomPreViewTextColor) {
        this.albumBottomPreViewTextColor = albumBottomPreViewTextColor;
        return this;
    }

    public int getAlbumBottomSelectText() {
        return albumBottomSelectText;
    }

    public AlbumConfig setAlbumBottomSelectText(int albumBottomSelectText) {
        this.albumBottomSelectText = albumBottomSelectText;
        return this;
    }

    public int getAlbumBottomSelectTextSize() {
        return albumBottomSelectTextSize;
    }

    public AlbumConfig setAlbumBottomSelectTextSize(int albumBottomSelectTextSize) {
        this.albumBottomSelectTextSize = albumBottomSelectTextSize;
        return this;
    }

    public int getAlbumBottomSelectTextColor() {
        return albumBottomSelectTextColor;
    }

    public AlbumConfig setAlbumBottomSelectTextColor(int albumBottomSelectTextColor) {
        this.albumBottomSelectTextColor = albumBottomSelectTextColor;
        return this;
    }

    public int getAlbumListPopupWidth() {
        return albumListPopupWidth;
    }

    public AlbumConfig setAlbumListPopupWidth(int albumListPopupWidth) {
        this.albumListPopupWidth = albumListPopupWidth;
        return this;
    }

    public int getAlbumListPopupHorizontalOffset() {
        return albumListPopupHorizontalOffset;
    }

    public AlbumConfig setAlbumListPopupHorizontalOffset(int albumListPopupHorizontalOffset) {
        this.albumListPopupHorizontalOffset = albumListPopupHorizontalOffset;
        return this;
    }

    public int getAlbumListPopupVerticalOffset() {
        return albumListPopupVerticalOffset;
    }

    public AlbumConfig setAlbumListPopupVerticalOffset(int albumListPopupVerticalOffset) {
        this.albumListPopupVerticalOffset = albumListPopupVerticalOffset;
        return this;
    }

    public int getAlbumBottomFinderTextCompoundDrawable() {
        return albumBottomFinderTextCompoundDrawable;
    }

    public AlbumConfig setAlbumBottomFinderTextCompoundDrawable(int albumBottomFinderTextCompoundDrawable) {
        this.albumBottomFinderTextCompoundDrawable = albumBottomFinderTextCompoundDrawable;
        return this;
    }

    public int getAlbumListPopupItemBackground() {
        return albumListPopupItemBackground;
    }

    public AlbumConfig setAlbumListPopupItemBackground(int albumListPopupItemBackground) {
        this.albumListPopupItemBackground = albumListPopupItemBackground;
        return this;
    }

    public int getAlbumListPopupItemTextColor() {
        return albumListPopupItemTextColor;
    }

    public AlbumConfig setAlbumListPopupItemTextColor(int albumListPopupItemTextColor) {
        this.albumListPopupItemTextColor = albumListPopupItemTextColor;
        return this;
    }

    public int getAlbumToolbarIconColor() {
        return albumToolbarIconColor;
    }

    public AlbumConfig setAlbumToolbarIconColor(int albumToolbarIconColor) {
        this.albumToolbarIconColor = albumToolbarIconColor;
        return this;
    }

    public int getAlbumBottomFinderTextDrawableColor() {
        return albumBottomFinderTextDrawableColor;
    }

    public AlbumConfig setAlbumBottomFinderTextDrawableColor(int albumBottomFinderTextDrawableColor) {
        this.albumBottomFinderTextDrawableColor = albumBottomFinderTextDrawableColor;
        return this;
    }

    public boolean isRadio() {
        return isRadio;
    }

    public AlbumConfig setRadio(boolean radio) {
        isRadio = radio;
        return this;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public AlbumConfig setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public int getAlbumContentViewBackground() {
        return albumContentViewBackground;
    }

    public AlbumConfig setAlbumContentViewBackground(int albumContentViewBackground) {
        this.albumContentViewBackground = albumContentViewBackground;
        return this;
    }

    public int getAlbumContentViewCameraDrawable() {
        return albumContentViewCameraDrawable;
    }

    public AlbumConfig setAlbumContentViewCameraDrawable(int albumContentViewCameraDrawable) {
        this.albumContentViewCameraDrawable = albumContentViewCameraDrawable;
        return this;
    }

    public int getAlbumContentViewCameraDrawableColor() {
        return albumContentViewCameraDrawableColor;
    }

    public AlbumConfig setAlbumContentViewCameraDrawableColor(int albumContentViewCameraDrawableColor) {
        this.albumContentViewCameraDrawableColor = albumContentViewCameraDrawableColor;
        return this;
    }

    public int getAlbumPreviewTitle() {
        return albumPreviewTitle;
    }

    public AlbumConfig setAlbumPreviewTitle(int albumPreviewTitle) {
        this.albumPreviewTitle = albumPreviewTitle;
        return this;
    }

    public int getAlbumPreviewBackground() {
        return albumPreviewBackground;
    }

    public AlbumConfig setAlbumPreviewBackground(int albumPreviewBackground) {
        this.albumPreviewBackground = albumPreviewBackground;
        return this;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public AlbumConfig setCrop(boolean crop) {
        isCrop = crop;
        return this;
    }

    public int getAlbumContentItemCheckBoxDrawable() {
        return albumContentItemCheckBoxDrawable;
    }

    public AlbumConfig setAlbumContentItemCheckBoxDrawable(int albumContentItemCheckBoxDrawable) {
        this.albumContentItemCheckBoxDrawable = albumContentItemCheckBoxDrawable;
        return this;
    }

    public int getMultipleMaxCount() {
        return multipleMaxCount;
    }

    public AlbumConfig setMultipleMaxCount(int multipleMaxCount) {
        this.multipleMaxCount = multipleMaxCount;
        return this;
    }

    public int getAlbumPreviewBottomViewBackground() {
        return albumPreviewBottomViewBackground;
    }

    public AlbumConfig setAlbumPreviewBottomViewBackground(int albumPreviewBottomViewBackground) {
        this.albumPreviewBottomViewBackground = albumPreviewBottomViewBackground;
        return this;
    }

    public int getAlbumPreviewBottomOkText() {
        return albumPreviewBottomOkText;
    }

    public AlbumConfig setAlbumPreviewBottomOkText(int albumPreviewBottomOkText) {
        this.albumPreviewBottomOkText = albumPreviewBottomOkText;
        return this;
    }

    public int getAlbumPreviewBottomOkTextColor() {
        return albumPreviewBottomOkTextColor;
    }

    public AlbumConfig setAlbumPreviewBottomOkTextColor(int albumPreviewBottomOkTextColor) {
        this.albumPreviewBottomOkTextColor = albumPreviewBottomOkTextColor;
        return this;
    }

    public int getAlbumPreviewBottomOkTextSize() {
        return albumPreviewBottomOkTextSize;
    }

    public AlbumConfig setAlbumPreviewBottomOkTextSize(int albumPreviewBottomOkTextSize) {
        this.albumPreviewBottomOkTextSize = albumPreviewBottomOkTextSize;
        return this;
    }


    public boolean isPreviewBackRefresh() {
        return previewBackRefresh;
    }

    public AlbumConfig setPreviewBackRefresh(boolean previewBackRefresh) {
        this.previewBackRefresh = previewBackRefresh;
        return this;
    }

    public boolean isPreviewFinishRefresh() {
        return previewFinishRefresh;
    }

    public AlbumConfig setPreviewFinishRefresh(boolean previewFinishRefresh) {
        this.previewFinishRefresh = previewFinishRefresh;
        return this;
    }

    public boolean isCameraCrop() {
        return cameraCrop;
    }

    public AlbumConfig setCameraCrop(boolean cameraCrop) {
        this.cameraCrop = cameraCrop;
        return this;
    }


    public String getCameraPath() {
        return cameraPath;
    }

    public AlbumConfig setCameraPath(String cameraPath) {
        this.cameraPath = cameraPath;
        return this;
    }

    public String getUCropPath() {
        return uCropPath;
    }

    public AlbumConfig setUCropPath(String uCropPath) {
        this.uCropPath = uCropPath;
        return this;
    }

    public boolean isPermissionsDeniedFinish() {
        return isPermissionsDeniedFinish;
    }

    public AlbumConfig setPermissionsDeniedFinish(boolean permissionsDeniedFinish) {
        this.isPermissionsDeniedFinish = permissionsDeniedFinish;
        return this;
    }

    public int getAlbumContentViewCameraTips() {
        return albumContentViewCameraTips;
    }

    public AlbumConfig setAlbumContentViewCameraTips(int albumContentViewCameraTips) {
        this.albumContentViewCameraTips = albumContentViewCameraTips;
        return this;
    }

    public int getAlbumContentViewCameraTipsSize() {
        return albumContentViewCameraTipsSize;
    }

    public AlbumConfig setAlbumContentViewCameraTipsSize(int albumContentViewCameraTipsSize) {
        this.albumContentViewCameraTipsSize = albumContentViewCameraTipsSize;
        return this;
    }

    public int getAlbumContentViewCameraTipsColor() {
        return albumContentViewCameraTipsColor;
    }

    public AlbumConfig setAlbumContentViewCameraTipsColor(int albumContentViewCameraTipsColor) {
        this.albumContentViewCameraTipsColor = albumContentViewCameraTipsColor;
        return this;
    }

    public int getAlbumContentViewCameraBackgroundColor() {
        return albumContentViewCameraBackgroundColor;
    }

    public AlbumConfig setAlbumContentViewCameraBackgroundColor(int albumContentViewCameraBackgroundColor) {
        this.albumContentViewCameraBackgroundColor = albumContentViewCameraBackgroundColor;
        return this;
    }

    public boolean isFrescoImageLoader() {
        return isFrescoImageLoader;
    }

    public AlbumConfig setFrescoImageLoader(boolean frescoImageLoader) {
        isFrescoImageLoader = frescoImageLoader;
        return this;
    }

    public int getAlbumBottomFinderTextBackground() {
        return albumBottomFinderTextBackground;
    }

    public AlbumConfig setAlbumBottomFinderTextBackground(int albumBottomFinderTextBackground) {
        this.albumBottomFinderTextBackground = albumBottomFinderTextBackground;
        return this;
    }

    public int getAlbumBottomPreviewTextBackground() {
        return albumBottomPreviewTextBackground;
    }

    public AlbumConfig setAlbumBottomPreviewTextBackground(int albumBottomPreviewTextBackground) {
        this.albumBottomPreviewTextBackground = albumBottomPreviewTextBackground;
        return this;
    }

    public int getAlbumBottomSelectTextBackground() {
        return albumBottomSelectTextBackground;
    }

    public AlbumConfig setAlbumBottomSelectTextBackground(int albumBottomSelectTextBackground) {
        this.albumBottomSelectTextBackground = albumBottomSelectTextBackground;
        return this;
    }

    public int getDividerWidth() {
        return dividerWidth;
    }

    public AlbumConfig setDividerWidth(int dividerWidth) {
        this.dividerWidth = dividerWidth;
        return this;
    }

    public int getAlbumPreviewBottomCountTextColor() {
        return albumPreviewBottomCountTextColor;
    }

    public AlbumConfig setAlbumPreviewBottomCountTextColor(int albumPreviewBottomCountTextColor) {
        this.albumPreviewBottomCountTextColor = albumPreviewBottomCountTextColor;
        return this;
    }

    public int getAlbumPreviewBottomCountTextSize() {
        return albumPreviewBottomCountTextSize;
    }

    public AlbumConfig setAlbumPreviewBottomCountTextSize(int albumPreviewBottomCountTextSize) {
        this.albumPreviewBottomCountTextSize = albumPreviewBottomCountTextSize;
        return this;
    }

    public int getCount() {
        return count;
    }

    public AlbumConfig setCount(int count) {
        this.count = count;
        return this;
    }

    public int getAlbumContentEmptyDrawable() {
        return albumContentEmptyDrawable;
    }

    public AlbumConfig setAlbumContentEmptyDrawable(int albumContentEmptyDrawable) {
        this.albumContentEmptyDrawable = albumContentEmptyDrawable;
        return this;
    }

    public int getAlbumContentEmptyDrawableColor() {
        return albumContentEmptyDrawableColor;
    }

    public AlbumConfig setAlbumContentEmptyDrawableColor(int albumContentEmptyDrawableColor) {
        this.albumContentEmptyDrawableColor = albumContentEmptyDrawableColor;
        return this;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public AlbumConfig setVideo(boolean video) {
        isVideo = video;
        return this;
    }

    public String getSdName() {
        return sdName;
    }

    public AlbumConfig setSdName(String sdName) {
        this.sdName = sdName;
        return this;
    }

    public boolean isFilterImg() {
        return filterImg;
    }

    public AlbumConfig setFilterImg(boolean filterImg) {
        this.filterImg = filterImg;
        return this;
    }
}
