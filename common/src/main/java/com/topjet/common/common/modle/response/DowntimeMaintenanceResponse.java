package com.topjet.common.common.modle.response;

/**
 * 停机维护查询接口返回参数类
 * Created by yy
 */

public class DowntimeMaintenanceResponse {

    private String valid;
    private String title;
    private String text;

    public String getValid() {
        return valid;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "DowntimeMaintenanceResponse{" +
                "valid='" + valid + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
