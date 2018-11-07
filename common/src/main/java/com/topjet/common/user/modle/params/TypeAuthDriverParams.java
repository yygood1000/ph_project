package com.topjet.common.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 司机身份认证请求参数
 */

public class TypeAuthDriverParams {
    private String driver_license_img = null;//驾驶证
    private String driver_operation_img = null;//运营证
    private String version = null;//数据版本
    private String driver_license_img_key = null;//司机驾驶证key
    private String driver_license_img_url = null;//司机驾驶证url
    private String driver_operation_img_key = null;//司机营运证key
    private String driver_operation_img_url = null;//司机营运证url
    private String user_auth_status = null;//审核状态 0、未认证 1、认证中 2、认证失败 3、认证通过
    private String user_auth_remark = null;//审核备注

    public String getDriver_license_img() {
        return driver_license_img;
    }

    public void setDriver_license_img(String driver_license_img) {
        this.driver_license_img = driver_license_img;
    }

    public String getDriver_operation_img() {
        return driver_operation_img;
    }

    public void setDriver_operation_img(String driver_operation_img) {
        this.driver_operation_img = driver_operation_img;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDriver_license_img_key() {
        return driver_license_img_key;
    }

    public void setDriver_license_img_key(String driver_license_img_key) {
        this.driver_license_img_key = driver_license_img_key;
    }

    public String getDriver_license_img_url() {
        return driver_license_img_url;
    }

    public void setDriver_license_img_url(String driver_license_img_url) {
        this.driver_license_img_url = driver_license_img_url;
    }

    public String getDriver_operation_img_key() {
        return driver_operation_img_key;
    }

    public void setDriver_operation_img_key(String driver_operation_img_key) {
        this.driver_operation_img_key = driver_operation_img_key;
    }

    public String getDriver_operation_img_url() {
        return driver_operation_img_url;
    }

    public void setDriver_operation_img_url(String driver_operation_img_url) {
        this.driver_operation_img_url = driver_operation_img_url;
    }

    public String getUser_auth_status() {
        return user_auth_status;
    }

    public void setUser_auth_status(String user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public String getUser_auth_remark() {
        return user_auth_remark;
    }

    public void setUser_auth_remark(String user_auth_remark) {
        this.user_auth_remark = user_auth_remark;
    }
}