package com.topjet.common.user.modle.response;

/**
 * Created by tsj-004 on 2017/11/9.
 * 修改头像返回信息
 */

public class GetAvatarInfoResponse {
    private String icon_image_key = null;//头像key
    private String icon_image_url = null;//头像utl
    private String is_solve = null;// 审核状态 0、未认证 1、认证中 2、认证失败 3、认证通过
    private String is_update = null;//能否进行修改  0:不能修改 1:可以修改


    public String getIcon_image_key() {
        return icon_image_key;
    }

    public void setIcon_image_key(String icon_image_key) {
        this.icon_image_key = icon_image_key;
    }

    public String getIcon_image_url() {
        return icon_image_url;
    }

    public void setIcon_image_url(String icon_image_url) {
        this.icon_image_url = icon_image_url;
    }

    public String getIs_solve() {
        return is_solve;
    }

    public void setIs_solve(String is_solve) {
        this.is_solve = is_solve;
    }

    public String getIs_update() {
        return is_update;
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }
}
