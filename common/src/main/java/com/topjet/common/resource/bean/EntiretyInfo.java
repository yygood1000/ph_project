package com.topjet.common.resource.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;

/**
 * 整车或拼车
 * Created by tsj004 on 2017年9月3日 20:51:25
 */
public class EntiretyInfo  extends BaseExtra {
    private String displayName;
    private boolean isSelected = false; // 是否选中
    private int id;

    public EntiretyInfo(){

    }

    public EntiretyInfo(String displayName, boolean isSelected, int id){
        this.displayName = displayName;
        this.isSelected = isSelected;
        this.id = id;
    }

    public EntiretyInfo(int displayNameResourceId, boolean isSelected, int id){
        this.displayName = ResourceUtils.getString(displayNameResourceId);
        this.isSelected = isSelected;
        this.id = id;
    }

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
