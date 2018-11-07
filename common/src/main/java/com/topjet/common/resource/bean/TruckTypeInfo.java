package com.topjet.common.resource.bean;

/**
 * 车型
 * Created by tsj004 on 2016/12/3.
 */
public class TruckTypeInfo extends TypeAndLengthDad implements Cloneable{

    private String code;
    private String icon;

    public TruckTypeInfo(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Object clone() {
        TruckTypeInfo o = null;
        try {
            o = (TruckTypeInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
