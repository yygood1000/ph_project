package com.topjet.shipper.user.model.bean;

/**
 * 获取常用联系人信息时提交的参数类
 * Created by tsj004 on 2017/8/28.
 */

public class GetUsualContactInfoParams {
    public GetUsualContactInfoParams(String linkman_id) {
        this.linkman_id = linkman_id;
    }

    private String linkman_id;

    public String getLinkManID() {
        return linkman_id;
    }

    public void setLinkManID(String linkman_id) {
        this.linkman_id = linkman_id;
    }
}
