package com.topjet.common.resource.bean;

/**
 * 车长
 * Created by tsj004 on 2016/12/3.
 */
public class TruckLengthInfo extends TypeAndLengthDad implements Cloneable{
    private String length;

    public TruckLengthInfo(){

    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }


    public Object clone() {
        TruckLengthInfo o = null;
        try {
            o = (TruckLengthInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
