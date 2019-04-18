package com.album.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * by y on 14/08/2017.
 */

public class AlbumEntity implements Parcelable {

    public static final Creator<AlbumEntity> CREATOR = new Creator<AlbumEntity>() {
        @Override
        public AlbumEntity createFromParcel(Parcel in) {
            return new AlbumEntity(in);
        }

        @Override
        public AlbumEntity[] newArray(int size) {
            return new AlbumEntity[size];
        }
    };
    private String dirPath;
    private String dirName;
    private String path;
    private long id;
    private boolean isCheck;

    public AlbumEntity(String dirPath, String dirName, String path, long id, boolean isCheck) {
        this.dirPath = dirPath;
        this.dirName = dirName;
        this.path = path;
        this.id = id;
        this.isCheck = isCheck;
    }

    private AlbumEntity(Parcel in) {
        dirPath = in.readString();
        dirName = in.readString();
        path = in.readString();
        id = in.readLong();
        isCheck = in.readByte() != 0;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumEntity that = (AlbumEntity) o;

        if (id != that.id) return false;
        if (isCheck == that.isCheck)
            if (dirPath != null ? dirPath.equals(that.dirPath) : that.dirPath == null)
                if (dirName != null ? dirName.equals(that.dirName) : that.dirName == null)
                    return path != null ? path.equals(that.path) : that.path == null;
        return false;

    }

    @Override
    public int hashCode() {
        int result = dirPath != null ? dirPath.hashCode() : 0;
        result = 31 * result + (dirName != null ? dirName.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (isCheck ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dirPath);
        dest.writeString(dirName);
        dest.writeString(path);
        dest.writeLong(id);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }
}
