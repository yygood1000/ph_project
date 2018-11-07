package com.topjet.common.common.modle.response;

/**
 * 版本升级接口返回参数类
 * Created by tsj004 on 2017/8/12.
 */

public class AppUpgradeResponse {
    public static final String FORCED = "1";
    public static final String FORCED_NO = "0";
    private String is_forced = "0"; // 是否强制更新 0：否 1：是
    private String download_address;
    private String description;
    private String system_version;
    private String is_update; // 0: 没有更新 1:有更新
    private String version;

    public String getIsForced() {
        return is_forced;
    }
    public void setIsForced(String is_forced) {
        this.is_forced = is_forced;
    }

    public String getDownloadAddress() {
        return download_address;
    }
    public void setDownloadAddress(String download_address) {
        this.download_address = download_address;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemVersion() {
        return system_version;
    }
    public void setSystemVersion(String system_version) {
        this.system_version = system_version;
    }

    public String getIsUpdate() {
        return is_update;
    }
    public void setIsUpdate(String is_update) {
        this.is_update = is_update;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
