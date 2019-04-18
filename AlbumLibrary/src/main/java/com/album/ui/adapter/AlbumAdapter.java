package com.album.ui.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.entity.AlbumEntity;
import com.album.ui.widget.AlbumImageView;
import com.album.util.FileUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * by y on 14/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    private ArrayList<AlbumEntity> albumList;
    private ArrayList<AlbumEntity> multiplePreviewList = null;
    private OnItemClickListener onItemClickListener = null;
    private final AlbumConfig albumConfig;
    private final int display;
    private final FrameLayout.LayoutParams layoutParams;

    public AlbumAdapter(ArrayList<AlbumEntity> list, int display) {
        this.albumList = list;
        this.display = display;
        albumConfig = Album.getInstance().getConfig();
        layoutParams = new FrameLayout.LayoutParams(display, display);
        if (!albumConfig.isRadio()) {
            multiplePreviewList = new ArrayList<>();
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null && albumList != null) {
                    onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition(), albumList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumAdapter.ViewHolder holder, int position) {

        if (albumList == null) {
            return;
        }
        final AlbumEntity albumEntity = albumList.get(position);
        if (albumEntity == null) {
            return;
        }
        String path = albumEntity.getPath();
        if (TextUtils.equals(String.valueOf(path), AlbumConstant.CAMERA)) {
            holder.camera();
        } else {
            holder.cameraRootView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            ImageView imageView;
            if (albumConfig.isFrescoImageLoader()) {
                imageView = Album.getInstance().getAlbumImageLoader().frescoView(holder.imageView.getContext(), AlbumConstant.TYPE_FRESCO_ALBUM);
            } else {
                imageView = new AlbumImageView(holder.imageView.getContext());
            }
            imageView.setLayoutParams(layoutParams);
            holder.imageView.addView(imageView);
            Album.getInstance().getAlbumImageLoader().displayAlbum(imageView, display, display, albumEntity);
            if (!albumConfig.isRadio()) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(albumEntity.isCheck());
                holder.checkBox.setBackgroundResource(albumConfig.getAlbumContentItemCheckBoxDrawable());
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FileUtils.isFile(albumEntity.getPath())) {
                            holder.checkBox.setChecked(false);
                            Album.getInstance().getAlbumListener().onAlbumCheckBoxFileNull();
                            return;
                        }
                        if (!multiplePreviewList.contains(albumEntity) && multiplePreviewList.size() >= albumConfig.getMultipleMaxCount()) {
                            holder.checkBox.setChecked(false);
                            Album.getInstance().getAlbumListener().onAlbumMaxCount();
                            return;
                        }
                        if (!albumEntity.isCheck()) {
                            albumEntity.setCheck(true);
                            multiplePreviewList.add(albumEntity);
                        } else {
                            multiplePreviewList.remove(albumEntity);
                            albumEntity.setCheck(false);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return albumList == null ? 0 : albumList.size();
    }

    public void addAll(ArrayList<AlbumEntity> list) {
        if (albumList == null) {
            albumList = new ArrayList<>();
        }
        if (list != null && !list.isEmpty()) {
            albumList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public ArrayList<AlbumEntity> getAlbumList() {
        return albumList;
    }

    public ArrayList<AlbumEntity> getMultiplePreviewList() {
        return multiplePreviewList;
    }

    public void setMultiplePreviewList(ArrayList<AlbumEntity> multiplePreviewList) {
        this.multiplePreviewList = multiplePreviewList;
        notifyDataSetChanged();
    }

    public void removeAll() {
        if (albumList != null) {
            albumList.clear();
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, AlbumEntity albumEntity);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout imageView;
        private final AppCompatCheckBox checkBox;
        private final AppCompatImageView imageCamera;
        private final AppCompatTextView cameraTips;
        private final LinearLayout cameraRootView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_image);
            checkBox = itemView.findViewById(R.id.album_check_box);
            imageCamera = itemView.findViewById(R.id.album_image_camera);
            cameraTips = itemView.findViewById(R.id.album_image_camera_tv);
            cameraRootView = itemView.findViewById(R.id.album_camera_root_view);
        }

        void camera() {
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), albumConfig.getAlbumContentViewCameraDrawable());
            Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraDrawableColor()), PorterDuff.Mode.SRC_ATOP);
            cameraTips.setText(albumConfig.getAlbumContentViewCameraTips());
            cameraTips.setTextSize(albumConfig.getAlbumContentViewCameraTipsSize());
            cameraTips.setTextColor(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraTipsColor()));
            cameraRootView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraBackgroundColor()));
            imageCamera.setImageDrawable(drawable);
            cameraRootView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }
    }
}
