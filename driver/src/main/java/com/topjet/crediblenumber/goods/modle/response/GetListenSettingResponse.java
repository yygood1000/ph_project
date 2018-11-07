package com.topjet.crediblenumber.goods.modle.response;

import com.topjet.common.base.model.BaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/9/8.
 * 获取听单设置  的返回参数
 */

public class GetListenSettingResponse extends BaseEvent{
    private ListenSettingObject depart = new ListenSettingObject();         //  出发地设置
    private ListenSettingObject optional = new ListenSettingObject();       //	  自选-目的地设置
    private List<ListenSettingObject> common = new ArrayList<>();         //  常跑-目的地设置
    private boolean needLocation = true;            // 是否需要定位

    private boolean isSuccess;
    private String errorCode;

    public ListenSettingObject getDepart() {
        return depart;
    }

    public void setDepart(ListenSettingObject depart) {
        this.depart = depart;
    }

    public ListenSettingObject getOptional() {
        return optional;
    }

    public void setOptional(ListenSettingObject optional) {
        this.optional = optional;
    }

    public List<ListenSettingObject> getCommon() {
        return common;
    }

    public void setCommon(List<ListenSettingObject> common) {
        this.common = common;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isNeedLocation() {
        return needLocation;
    }

    public void setNeedLocation(boolean needLocation) {
        this.needLocation = needLocation;
    }
}
