package com.album.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.annotation.PermissionsType;
import com.album.entity.AlbumEntity;
import com.album.listener.AlbumCameraListener;
import com.album.listener.AlbumListener;
import com.album.listener.OnEmptyClickListener;
import com.album.sample.camera.SimpleCameraActivity;
import com.album.util.AlbumTool;
import com.album.util.FileUtils;
import com.album.util.PermissionUtils;
import com.album.util.scanner.SingleMediaScanner;
import com.album.util.scanner.SingleScannerListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private UCrop.Options dayOptions;
    private UCrop.Options nightOptions;
    private ArrayList<AlbumEntity> list;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_day_album).setOnClickListener(this);
        findViewById(R.id.btn_night_album).setOnClickListener(this);
        findViewById(R.id.btn_open_camera).setOnClickListener(this);
        findViewById(R.id.btn_sample_ui).setOnClickListener(this);
        findViewById(R.id.btn_customize_camera).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);

        dayOptions = new UCrop.Options();
        dayOptions.setToolbarTitle("DayTheme");
        dayOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundDay));
        dayOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorDay));
        dayOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundDay));

        nightOptions = new UCrop.Options();
        nightOptions.setToolbarTitle("NightTheme");
        nightOptions.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarTextColorNight));
        nightOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight));
        nightOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight));
        nightOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorNight));
        list = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day_album:
                Album
                        .getInstance()
//                        .setAlbumImageLoader(new SimpleFrescoAlbumImageLoader())
                        .setAlbumListener(new MainAlbumListener(this, list))
                        .setAlbumEntityList(list)
                        .setOptions(dayOptions)
                        .setEmptyClickListener(new OnEmptyClickListener() {
                            @Override
                            public boolean click(View view) {
                                return true;
                            }
                        })
                        .setAlbumCameraListener(null)
                        .setAlbumClass(null)
                        .setConfig(new AlbumConfig()
                                .setCameraCrop(false)
                                .setHideCamera(true)
                                .setPermissionsDeniedFinish(false)
                                .setPreviewFinishRefresh(true)
                                .setAlbumBottomFinderTextBackground(R.drawable.selector_btn)
//                                .setAlbumBottomPreviewTextBackground(R.drawable.selector_btn)
                                .setAlbumBottomSelectTextBackground(R.drawable.selector_btn)
                                .setAlbumContentItemCheckBoxDrawable(R.drawable.simple_selector_album_item_check)
//                                .setFrescoImageLoader(true)  // 通知 Album 图片加载框架使用的是 Fresco
                                .setPreviewBackRefresh(true))
                        .start(this);
                break;
            case R.id.btn_night_album:
                Album
                        .getInstance()
                        .setAlbumListener(new MainAlbumListener(this, null))
                        .setOptions(nightOptions)
                        .setAlbumCameraListener(null)
                        .setAlbumClass(null)
                        .setEmptyClickListener(new OnEmptyClickListener() {
                            @Override
                            public boolean click(View view) {
                                return true;
                            }
                        })
                        .setConfig(new AlbumConfig(AlbumConstant.TYPE_NIGHT)
                                .setRadio(true)
                                .setHideCamera(true)
                                .setCrop(true).setCameraPath(Environment.getExternalStorageDirectory().getPath() + "/" + "DCIM/Album")
                                .setUCropPath(Environment.getExternalStorageDirectory().getPath() + "/" + "DCIM" + "/" + "uCrop")
                                .setPermissionsDeniedFinish(true)
                                .setPreviewFinishRefresh(true)
                                .setPreviewBackRefresh(true)
                                .setCameraCrop(true))
                        .start(this);
                break;

            case R.id.btn_sample_ui:

                Album
                        .getInstance()
                        .setAlbumListener(new MainAlbumListener(this, list))
                        .setAlbumClass(SimpleAlbumUI.class)
                        .setAlbumCameraListener(null)
                        .setEmptyClickListener(null)
                        .setConfig(new AlbumConfig()
                                .setCameraCrop(false)
                                .setAlbumPreviewBackground(R.color.colorAlbumPreviewBackgroundNight)
                                .setAlbumToolbarBackground(R.color.colorPrimary)
                                .setAlbumStatusBarColor(R.color.colorPrimary)
                                .setPreviewBackRefresh(true))
                        .start(this);

                break;

            case R.id.btn_open_camera:
                // fragment  activity 直接传递 this ， 内部会自己处理
                int i = AlbumTool.openCamera(this, imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null, false)), false);

                // -1 没有权限
                // 0 打开成功
                // 1 打开相机失败，估计设备没有安装相关软件

                break;

            case R.id.btn_customize_camera:

                Album
                        .getInstance()
                        .setAlbumListener(new MainAlbumListener(this, list))
                        .setAlbumEntityList(list)
                        .setOptions(dayOptions)
                        .setAlbumCameraListener(new AlbumCameraListener() {
                            @Override
                            public void startCamera(@NonNull Fragment fragment) {
                                if (PermissionUtils.storage(fragment.getActivity()) && PermissionUtils.camera(fragment.getActivity())) {
                                    FragmentActivity activity = fragment.getActivity();
                                    Toast.makeText(activity, "camera", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity, SimpleCameraActivity.class);
                                    fragment.startActivityForResult(intent, AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE);
                                }
                            }
                        })
                        .setEmptyClickListener(new OnEmptyClickListener() {
                            @Override
                            public boolean click(View view) {
                                return true;
                            }
                        })
                        .setAlbumClass(null)
                        .setConfig(new AlbumConfig()
                                .setCameraCrop(false)
                                .setPermissionsDeniedFinish(false)
                                .setPreviewFinishRefresh(true)
                                .setAlbumBottomFinderTextBackground(R.drawable.selector_btn)
//                                .setAlbumBottomPreviewTextBackground(R.drawable.selector_btn)
                                .setAlbumBottomSelectTextBackground(R.drawable.selector_btn)
                                .setAlbumContentItemCheckBoxDrawable(R.drawable.simple_selector_album_item_check)
//                                .setFrescoImageLoader(true)  // 通知 Album 图片加载框架使用的是 Fresco
                                .setPreviewBackRefresh(true))
                        .start(this);

                break;

            case R.id.btn_video:
                Album
                        .getInstance()
                        .setAlbumListener(new MainAlbumListener(this, list))
                        .setAlbumCameraListener(null)
                        .setEmptyClickListener(new OnEmptyClickListener() {
                            @Override
                            public boolean click(View view) {
                                return true;
                            }
                        })
                        .setConfig(
                                new AlbumConfig()
                                        .setVideo(true)
                                        .setAlbumToolbarText(R.string.album_video_title)
                                        .setAlbumContentViewCameraTips(R.string.video_tips)
                                        .setPreviewBackRefresh(true))
                        .start(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
        } else if (resultCode == UCrop.RESULT_ERROR) {
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AlbumConstant.ITEM_CAMERA:
                    new SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.getPath()),
                            new SingleScannerListener() {
                                @Override
                                public void onScanStart() {

                                }

                                @Override
                                public void onScanCompleted(int type) {

                                }
                            }, AlbumConstant.TYPE_RESULT_CAMERA);
                    UCrop.of(Uri.fromFile(new File(imagePath.getPath())), imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null, false)))
                            .withOptions(new UCrop.Options())
                            .start(this);
                    break;
                case UCrop.REQUEST_CROP:
                    new SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.getPath()),
                            new SingleScannerListener() {
                                @Override
                                public void onScanStart() {

                                }

                                @Override
                                public void onScanCompleted(int type) {

                                }
                            }, AlbumConstant.TYPE_RESULT_CROP);
                    Toast.makeText(getApplicationContext(), imagePath.getPath(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * @see com.album.ui.widget.SimpleAlbumListener
     */
    private static class MainAlbumListener implements AlbumListener {

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
            Log.i("ALBUM", data.getMessage());
            toast("crop error:" + data);
        }

        @Override
        public void onAlbumResources(@NonNull List<AlbumEntity> list) {
            toast("select count :" + list.size());
            if (this.list != null) {
                this.list.clear();
                this.list.addAll(list);
            }
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
            toast("album no more");
        }

        @Override
        public void onAlbumResultCameraError() {
            toast("result error");
        }

        @Override
        public void onVideoPlayError() {
            toast("play video error : checked video app");
        }
    }

}
