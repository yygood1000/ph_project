package com.foamtrace.photopicker.intent;

import java.io.Serializable;

/**
 * Created by yy
 * <p/>
 * 申请退款/重新申请退款  图片选择实体类
 */
public class SelectPhotoBean implements Serializable {
    private String path;
    private String key;
    private boolean isOldPhoto;// 是否是老图片，即之前上传过，重新申请时从服务端下载的图片。

    public SelectPhotoBean() {
    }

    public SelectPhotoBean(String path, String key, boolean isOldPhoto) {
        this.path = path;
        this.key = key;
        this.isOldPhoto = isOldPhoto;
    }

    public String getPath() {
        return path;
    }

    public String getKey() {
        return null == key | "".equals(key) ? "" : key;
    }


    public boolean isOldPhoto() {
        return isOldPhoto;
    }

}
