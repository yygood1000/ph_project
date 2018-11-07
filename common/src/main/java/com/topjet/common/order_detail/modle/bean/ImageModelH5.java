package com.topjet.common.order_detail.modle.bean;

/**
 * Created by yy on 2017/10/12.
 * <p>
 * 退款 H5交互图片实体类
 */

public class ImageModelH5 {
    private String encodedImage = "";
    private String path = "";

    public ImageModelH5(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public ImageModelH5(String path, String encodedImage) {
        this.encodedImage = encodedImage;
        this.path = path;
    }
}
