package com.topjet.shipper.user.model.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: zhulunjun
 * time:    2017/9/28
 * describe: 跳转添加联系人
 */

public class AddContactExtra extends BaseExtra {

    private boolean isAddOrEdit;
    private int resource; //1为常用联系人列表进来，2为选择常用联系人列表进来
    private String linkmanId;

    public static final int LIST = 1;
    public static final int SELECT = 2;

    public AddContactExtra(boolean isAddOrEdit, int resource) {
        this.isAddOrEdit = isAddOrEdit;
        this.resource = resource;
    }

    public AddContactExtra(boolean isAddOrEdit, int resource, String linkmanId) {
        this.isAddOrEdit = isAddOrEdit;
        this.resource = resource;
        this.linkmanId = linkmanId;
    }

    public boolean isAddOrEdit() {
        return isAddOrEdit;
    }

    public void setAddOrEdit(boolean addOrEdit) {
        isAddOrEdit = addOrEdit;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getLinkmanId() {
        return linkmanId;
    }

    public void setLinkmanId(String linkmanId) {
        this.linkmanId = linkmanId;
    }
}
