package com.album.entity;

/**
 * by y on 14/08/2017.
 */

public class FinderEntity {
    private String dirName;
    private String thumbnailsPath;
    private long thumbnailsId;
    private String bucketId;
    private int count;

    public FinderEntity(String dirName, String thumbnailsPath, long thumbnailsId, String bucketId, int count) {
        this.dirName = dirName;
        this.thumbnailsPath = thumbnailsPath;
        this.thumbnailsId = thumbnailsId;
        this.bucketId = bucketId;
        this.count = count;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }

    public long getThumbnailsId() {
        return thumbnailsId;
    }

    public void setThumbnailsId(long thumbnailsId) {
        this.thumbnailsId = thumbnailsId;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
