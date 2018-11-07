package com.topjet.shipper.deliver.modle.bean;

import com.topjet.common.resource.bean.OptionItem;

/**
 * Created by tsj-004 on 2017/8/26.
 * 货物类型和包装方式的封装类
 */

public class TypeAndPackingItem {
    private OptionItem item = null;
    private boolean isFromUser = false;     // 是否源自用户输入
    private boolean isSelected = false;     // 是否选中

    public TypeAndPackingItem(OptionItem item, boolean isFromUser) {
        this.item = item;
        this.isFromUser = isFromUser;
    }

    public OptionItem getItem() {
        return item;
    }

    public void setItem(OptionItem item) {
        this.item = item;
    }

    public boolean isFromUser() {
        return isFromUser;
    }

    public void setFromUser(boolean fromUser) {
        isFromUser = fromUser;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
