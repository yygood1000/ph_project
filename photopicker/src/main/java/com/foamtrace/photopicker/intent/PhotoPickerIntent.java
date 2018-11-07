package com.foamtrace.photopicker.intent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.foamtrace.photopicker.ImageConfig;
import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class PhotoPickerIntent extends Intent implements  Serializable{

    public PhotoPickerIntent(Context packageContext) {
        super(packageContext, PhotoPickerActivity.class);
    }

    public void setShowCarema(boolean bool) {
        this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, bool);
    }

    public void setMaxTotal(int total) {
        this.putExtra(PhotoPickerActivity.EXTRA_SELECT_COUNT, total);
    }

    /**
     * 选择
     */
    public void setSelectModel(SelectModel model) {
        this.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, Integer.parseInt(model.toString()));
    }

    /**
     * 已选择的照片地址
     */
    public void setSelectedPaths(ArrayList<ImageModel> imagePaths) {
        this.putExtra(PhotoPickerActivity.EXTRA_DEFAULT_SELECTED_LIST, imagePaths);
    }

    /**
     * 显示相册图片的属性
     */
    public void setImageConfig(ImageConfig config) {
        this.putExtra(PhotoPickerActivity.EXTRA_IMAGE_CONFIG, config);
    }

    /**
     * 是否是司机
     */
    public void setIsDriver(boolean isDriver) {
        this.putExtra(PhotoPickerActivity.EXTRA_IS_DRIVER, isDriver);
    }
}
