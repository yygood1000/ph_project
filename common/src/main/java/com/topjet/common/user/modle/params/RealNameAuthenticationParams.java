package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 实名认证请求参数
 */

public class RealNameAuthenticationParams {
    private String use_status_icon_image = null;//头像
    private String use_status_idcard_image = null;//身份证
    private String version = null;//数据版本
    private String use_status = null;//实名认证状态
    private String use_status_remark = null;//审核备注
    private String use_status_icon_image_key = null;//头像key
    private String use_status_icon_image_url = null;//头像url
    private String use_status_idcard_image_key = null;//身份证key
    private String use_status_idcard_image_url = null;//身份证URl

    public String getUse_status_icon_image() {
        return use_status_icon_image;
    }

    public void setUse_status_icon_image(String use_status_icon_image) {
        this.use_status_icon_image = use_status_icon_image;
    }

    public String getUse_status_icon_image_key() {
        return use_status_icon_image_key;
    }

    public void setUse_status_icon_image_key(String use_status_icon_image_key) {
        this.use_status_icon_image_key = use_status_icon_image_key;
    }

    public String getUse_status_idcard_image() {
        return use_status_idcard_image;
    }

    public void setUse_status_idcard_image(String use_status_idcard_image) {
        this.use_status_idcard_image = use_status_idcard_image;
    }

    public String getUse_status_idcard_image_key() {
        return use_status_idcard_image_key;
    }

    public void setUse_status_idcard_image_key(String use_status_idcard_image_key) {
        this.use_status_idcard_image_key = use_status_idcard_image_key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUse_status() {
        return use_status;
    }

    public void setUse_status(String use_status) {
        this.use_status = use_status;
    }

    public String getUse_status_remark() {
        return use_status_remark;
    }

    public void setUse_status_remark(String use_status_remark) {
        this.use_status_remark = use_status_remark;
    }

    public String getUse_status_icon_image_url() {
        return use_status_icon_image_url;
    }

    public void setUse_status_icon_image_url(String use_status_icon_image_url) {
        this.use_status_icon_image_url = use_status_icon_image_url;
    }

    public String getUse_status_idcard_image_url() {
        return use_status_idcard_image_url;
    }

    public void setUse_status_idcard_image_url(String use_status_idcard_image_url) {
        this.use_status_idcard_image_url = use_status_idcard_image_url;
    }
}