package com.topjet.common.common.modle.event;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.resource.bean.EntiretyInfo;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;

import java.util.List;

/**
 * Created by tsj-004 on 2017/9/3.
 * <p>
 * 车型车长 选中实体类
 */

public class TruckTypeLengthSelectedData extends BaseExtra {
    private EntiretyInfo is_carpool;//0 : 不可拼车、整车 1: 可拼车
    private List<TruckLengthInfo> lengthList = null;
    private List<TruckTypeInfo> typeList = null;

    public TruckTypeLengthSelectedData(String tag) {
        setTag(tag);
    }

    public TruckTypeLengthSelectedData() {
    }

    public EntiretyInfo getIs_carpool() {
        return is_carpool;
    }

    public void setIs_carpool(EntiretyInfo is_carpool) {
        this.is_carpool = is_carpool;
    }

    public List<TruckLengthInfo> getLengthList() {
        return lengthList;
    }

    public void setLengthList(List<TruckLengthInfo> lengthList) {
        this.lengthList = lengthList;
    }

    public List<TruckTypeInfo> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TruckTypeInfo> typeList) {
        this.typeList = typeList;
    }

    public String getSingleTruckTypeId() {
        return getTypeList().get(0).getId();
    }

    public String getSingleTruckLengthId() {
        return getLengthList().get(0).getId();
    }
}
