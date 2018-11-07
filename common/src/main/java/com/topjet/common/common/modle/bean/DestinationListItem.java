package com.topjet.common.common.modle.bean;

/**
 * Created by yy
 * <p>
 * 司机 目的地弹窗 列表实体类
 * 因为要在内存中存储，所以写在common中
 */
public class DestinationListItem {
    public static String IS_SELF = "is_self";// 自选目的地
    public static String IS_NOT_SELF = "is_not_self";// 非自选目的地

    private String id;
    private String destinationName;
    private boolean isSelected;
    private String isSelectedBySelf;// 是否是自选目的地

    public DestinationListItem()
    {

    }

    public DestinationListItem(String id, String destinationName, boolean isSelected, String isSelectedBySelf) {
        this.id = id;
        this.destinationName = destinationName;
        this.isSelected = isSelected;
        this.isSelectedBySelf = isSelectedBySelf;
    }

    public DestinationListItem(String id, String destinationName, boolean isSelected) {
        this(id, destinationName, isSelected, "0");
    }


    public String getId() {
        return id;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsSelectedBySelf() {
        return isSelectedBySelf.equals(IS_SELF);
    }

    public void setIsSelectedBySelf(String isSelectedBySelf) {
        this.isSelectedBySelf = isSelectedBySelf;
    }

    @Override
    public String toString() {
        return "DestinationListItem{" +
                "id='" + id + '\'' +
                ", destinationName='" + destinationName + '\'' +
                ", isSelected=" + isSelected +
                ", isSelectedBySelf='" + isSelectedBySelf + '\'' +
                '}';
    }
}
