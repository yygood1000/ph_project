package com.topjet.common.resource.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/9/3.
 */

public class TypeAndLengthDad  extends BaseExtra {
    private String displayName;
    private String id;
    private boolean isSelected = false; // 是否选中

    public String getDisplayName() {
        if(StringUtils.isBlank(displayName)){
            return "";
        }else{
            return displayName;
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        if(StringUtils.isNotBlank(id)){
            return id;
        }else{
            return "";
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
