# Album
android album

Chinese : [wiki](https://github.com/7449/Album/wiki)


## Screenshot


#### multiple, radio, preview, crop, sample ui,customize camera

![](https://github.com/7449/Album/blob/master/screenshot/album_multiple.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_radio.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_preview.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_crop.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_sample_ui.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_customize_camera.png)


## sample



#### filter damaged pictures

        new AlbumConfig().setFilterImg(false)

#### Manifests.xml

        <activity
            android:name="com.yalantis.ucrop.UCropActivity"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.AlbumActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.PreviewActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
            
#### gradle

     api 'com.ydevelop:album:0.0.4'
     api "com.android.support:recyclerview-v7:$supportLibraryVersion"
     api "com.github.bumptech.glide:glide:$glideVersion"
     
  
  If you use the built-in frame, please rely on glide
  
  
    glide
    
#### sampleDemo

        Album
                .getInstance()
                .setAlbumEntitys(new ArrayList<AlbumEntity>())
                .setOptions(new UCrop.Options())
                .setAlbumImageLoader(new SimpleAlbumImageLoader())
                .setConfig(new AlbumConfig())
                .setAlbumListener(new SimpleAlbumListener())
                .start(this);
              
## customize camera

> picture folder can not exist `.nomedia`, otherwise the picture is not scanned

[CustomizeCamera](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/camera)

         Album
                .getInstance()
                .setAlbumCameraListener(new AlbumCameraListener() {
                      @Override
                      public void startCamera(@NonNull Fragment fragment) {
                      
                           FragmentActivity activity = fragment.getActivity();
                           Intent intent = new Intent(activity, SimpleCameraActivity.class);
                           // AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE
                           fragment.startActivityForResult(intent, AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE);
                      }
                })


    FileUtils.finishCamera(SimpleCameraActivity.this, cameraFile.getPath());
    
    
    public static void finishCamera(Activity activity, String path) {
        Bundle bundle = new Bundle();
        
        //AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path
        bundle.putString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        activity.setResult(RESULT_OK, intent);
        activity.finish();
    }
                
## ImageLoader

> Fresco

    Album
         .getInstance()
         .setConfig(new AlbumConfig().setFrescoImageLoader(true)  // notification Album is using Fresco
         .start(this);



[SimpleFrescoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleFrescoAlbumImageLoader.java)

[SimpleGlide4xAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleGlide4xAlbumImageLoader.java)

[SimpleImageLoaderAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleImageLoaderAlbumImageLoader.java)

[SimplePicassoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimplePicassoAlbumImageLoader.java)


    public class SimpleImageLoader implements AlbumImageLoader {
    
        @Override
        public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumEntity albumEntity) {
            
        }
    
        @Override
        public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderEntity finderEntity) {
    
        }
    
        @Override
        public void displayPreview(@NonNull ImageView view, @NonNull AlbumEntity albumEntity) {
    
        }
    
    
        // fresco DraweeView
        // other  null
        @Nullable
        @Override
        public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
            return null;
        }
    
    }


## UI

see: [SimpleAlbumUI](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleAlbumUI.java)


## Listener

see: [SimpleAlbumListener](https://github.com/7449/Album/blob/master/AlbumLibrary/src/main/java/com/album/ui/widget/SimpleAlbumListener.java)

     public class AlbumListener implements AlbumListener {
    
            private Context context;
            private List<AlbumEntity> list = null;
    
            void toast(String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
    
            MainAlbumListener(Context context, ArrayList<AlbumEntity> list) {
                this.context = context.getApplicationContext();
                this.list = list;
            }
    
            @Override
            public void onAlbumActivityFinish() {
                toast("album activity finish");
            }
    
            @Override
            public void onAlbumPermissionsDenied(@PermissionsType int type) {
                toast("permissions error");
            }
    
            @Override
            public void onAlbumFragmentNull() {
                toast("album fragment null");
            }
    
            @Override
            public void onAlbumPreviewFileNull() {
                toast("preview image has been deleted");
            }
    
            @Override
            public void onAlbumFinderNull() {
                toast("folder directory is empty");
            }
    
            @Override
            public void onAlbumBottomPreviewNull() {
                toast("preview no image");
            }
    
            @Override
            public void onAlbumBottomSelectNull() {
                toast("select no image");
            }
    
            @Override
            public void onAlbumFragmentFileNull() {
                toast("album image has been deleted");
            }
    
            @Override
            public void onAlbumPreviewSelectNull() {
                toast("PreviewActivity,  preview no image");
            }
    
            @Override
            public void onAlbumCheckBoxFileNull() {
                toast("check box  image has been deleted");
            }
    
            @Override
            public void onAlbumFragmentCropCanceled() {
                toast("cancel crop");
            }
    
            @Override
            public void onAlbumFragmentCameraCanceled() {
                toast("cancel camera");
            }
    
            @Override
            public void onAlbumFragmentUCropError(@Nullable Throwable data) {
                toast("crop error:" + data.toString());
            }
    
            @Override
            public void onAlbumResources(@NonNull List<AlbumEntity> list) {
                
            }
    
            @Override
            public void onAlbumUCropResources(@Nullable File scannerFile) {
                toast("crop file:" + scannerFile);
            }
    
            @Override
            public void onAlbumMaxCount() {
                toast("select max count");
            }
    
            @Override
            public void onAlbumActivityBackPressed() {
                toast("AlbumActivity Back");
            }
    
            @Override
            public void onAlbumOpenCameraError() {
                toast("camera error");
            }
            
            @Override
            public void onAlbumEmpty() {
                 toast("no image");
            }
            
            @Override
            public void onAlbumNoMore() {
                 toast("image no more");
            }
            
            @Override
            public void onAlbumResultCameraError() {
                 toast("result camera error");
            }
        }

## TestPhone

* onePlus3T           7.1.1
* huawei ale-cl00        4.4.4
* meizu mx5         5.1
* galaxy S8+         7.0
* hanzhong       5.1
* xiaomi note        6.0.1
* redmi note4 6.0
* oppo R7c      4.4.4
* Lenovo K30-T  4.4.4

## ProGuard

    -dontwarn com.album.**
    -keep class  com.album.** { *;}
    -keep interface  com.album.** { *;}
    
#### ucrop

    -dontwarn com.yalantis.ucrop**
    -keep class com.yalantis.ucrop** { *; }
    -keep interface com.yalantis.ucrop** { *; }
    
 
## Thanks

[TouchImageView](https://github.com/MikeOrtiz/TouchImageView)

[cameraview](https://github.com/google/cameraview)
    
    
## MediaScannerConnection Memory leak

 * https://issuetracker.google.com/issues/37046656
 * https://github.com/square/leakcanary/issues/26


## LICENSE

    Mozilla Public License 2.0




