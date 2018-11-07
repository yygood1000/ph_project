package com.topjet.shipper.deliver.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj-004 on 2017/9/5.
 * 其他信息
 * 发货用到
 */

public class OtherInfoExtra extends BaseExtra {
    private String type;
    private String pack;
    private String load;
    private String remark;
    private boolean refre = true; // 默认勾选
    private String photo;
    private String photoKey;    // 图片key
    private boolean isAssigned = false; // 是否是定向订单

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getRefre() {
        return refre;
    }

    public void setRefre(boolean refre) {
        this.refre = refre;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoKey() {
        return photoKey;
    }

    public void setPhotoKey(String photoKey) {
        this.photoKey = photoKey;
    }

    public boolean isRefre() {
        return refre;
    }

    @Override
    public String toString() {
        return "OtherInfoExtra{" +
                "type='" + type + '\'' +
                ", pack='" + pack + '\'' +
                ", load='" + load + '\'' +
                ", remark='" + remark + '\'' +
                ", refre=" + refre +
                ", photo='" + photo + '\'' +
                ", photoKey='" + photoKey + '\'' +
                '}';
    }
}
