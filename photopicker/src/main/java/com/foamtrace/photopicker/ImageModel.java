package com.foamtrace.photopicker;

import java.io.Serializable;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class ImageModel implements Serializable {
    public String path = "";
    private String key = "";
    public String name = "";
    public long time;
    private boolean isOldPhoto = false;// 是否是老图片，即之前上传过，重新申请时从服务端下载的图片。

    public ImageModel() {
    }

    public ImageModel(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;

    }

    public ImageModel(String path, String key, boolean isOldPhoto) {
        this.path = path;
        this.key = key;
        this.isOldPhoto = isOldPhoto;
    }

    public ImageModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isOldPhoto() {
        return isOldPhoto;
    }

    public void setOldPhoto(boolean oldPhoto) {
        isOldPhoto = oldPhoto;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageModel other = (ImageModel) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "path='" + path + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", isOldPhoto=" + isOldPhoto +
                '}';
    }
}
