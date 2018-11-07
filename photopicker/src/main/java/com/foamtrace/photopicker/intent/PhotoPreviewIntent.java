package com.foamtrace.photopicker.intent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 预览照片
 * Created by foamtrace on 2015/8/25.
 */
@SuppressLint("ParcelCreator")
public class PhotoPreviewIntent extends Intent {

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
    }

//    /**
//     * 照片地址
//     *
//     * @param paths
//     */
//    public void setPhotoPaths(ArrayList<String> paths) {
//        this.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, paths);
//    }

    /**
     * 照片地址
     */
    public void setPhotoPaths(ArrayList<ImageModel> selectPhotoBeans) {
        this.putExtra(PhotoPreviewActivity.EXTRA_PHOTOS, (Serializable) selectPhotoBeans);
    }

    /**
     * 当前照片的下标
     */
    public void setCurrentItem(int currentItem) {
        this.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
    }

    /**
     * 当前预览的是否是已选择照片
     */
    public void setIsPreviewSelectedImages(boolean isPreviewSelectedImages) {
        this.putExtra(PhotoPreviewActivity.EXTRA_IS_PREVIEW_SELECTED_IAMGE, isPreviewSelectedImages);
    }
}
